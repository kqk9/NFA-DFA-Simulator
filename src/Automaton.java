import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
class Automaton {
    private State initialState;
    private Set<State> states;
    public Automaton() {
        this.states = new HashSet<>();
    }
    public void addState(State state) {
        states.add(state);
    }
    public void setInitialState(State state) {
        if (states.contains(state)) {
            initialState = state;
        }
    }
    public ProcessResult processInput(String input) {
        Set<State> currentStates = initialState.getEpsilonClosure();
        List<String> path = new ArrayList<>();
        path.add(getStateSetString(currentStates));

        for (char symbol : input.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State current : currentStates) {
                Set<State> symbolTransitions = current.getNextStates(symbol);
                for (State symbolState : symbolTransitions) {
                    nextStates.addAll(symbolState.getEpsilonClosure());
                }
            }
            if (nextStates.isEmpty()) {
                return new ProcessResult(false, path, "Rejected: No valid transition for symbol '" + symbol + "'");
            }
            currentStates = nextStates;
            path.add(getStateSetString(currentStates));
        }
        boolean accepted = currentStates.stream().anyMatch(State::isAccepting);
        String message = accepted ? "Accepted" : "Rejected: Did not end in accepting state";
        return new ProcessResult(accepted, path, message);
    }
    private String getStateSetString(Set<State> states) {
        return states.stream()
                .map(State::getName)
                .sorted()
                .reduce("", (a, b) -> a.isEmpty() ? b : a + "," + b);
    }
}

