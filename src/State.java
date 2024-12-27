import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class State {
    private String name;
    private boolean isAccepting;
    private Map<Character, Set<State>> transitions;
    private Set<State> epsilonTransitions;

    public State(String name, boolean isAccepting) {
        this.name = name;
        this.isAccepting = isAccepting;
        this.transitions = new HashMap<>();
        this.epsilonTransitions = new HashSet<>();
    }

    public void addTransition(char symbol, State targetState) {
        transitions.computeIfAbsent(symbol, k -> new HashSet<>()).add(targetState);
    }

    public void addEpsilonTransition(State targetState) {
        epsilonTransitions.add(targetState);
    }

    public Set<State> getNextStates(char symbol) {
        return transitions.getOrDefault(symbol, new HashSet<>());
    }

    public Set<State> getEpsilonClosure() {
        Set<State> closure = new HashSet<>();
        computeEpsilonClosure(closure);
        return closure;
    }

    private void computeEpsilonClosure(Set<State> closure) {
        if (!closure.contains(this)) {
            closure.add(this);
            for (State nextState : epsilonTransitions) {
                nextState.computeEpsilonClosure(closure);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isAccepting() {
        return isAccepting;
    }
}

