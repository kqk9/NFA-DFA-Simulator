import java.util.*;

public class AutomataTableGenerator {
    private final TableGenerator tableGenerator;

    public AutomataTableGenerator() {
        this.tableGenerator = new TableGenerator();
    }
    public String generateTransitionTable(List<String> states, Set<String> alphabet,
                                          Map<String, State> stateMap) {
        List<String> headers = new ArrayList<>();
        headers.add("State");
        headers.addAll(alphabet);

        List<List<String>> rows = new ArrayList<>();

        for (String stateName : states) {
            List<String> row = new ArrayList<>();
            row.add(stateName);
            State state = stateMap.get(stateName);

            for (String symbol : alphabet) {
                Set<State> transitions;
                if (symbol.equals("ε")) {
                    transitions = state.getEpsilonClosure();
                    transitions.remove(state);
                } else {
                    transitions = state.getNextStates(symbol.charAt(0));
                }

                String transitionStr = transitions.stream()
                        .map(State::getName)
                        .sorted()
                        .reduce((a, b) -> a + "," + b)
                        .orElse("-");
                row.add(transitionStr);
            }
            rows.add(row);
        }

        return tableGenerator.generateTable(headers, rows);
    }
}

