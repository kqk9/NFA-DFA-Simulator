import java.util.*;
import java.io.*;
import java.nio.file.*;
public class AutomataSimulator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String EPSILON = "e";
    private static final AutomataTableGenerator tableGenerator = new AutomataTableGenerator();
    static Automaton createAutomatonFromFile(String filename) throws IOException {
        Automaton automaton = new Automaton();
        Map<String, State> stateMap = new HashMap<>();

        List<String> lines = Files.readAllLines(Paths.get(filename));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String headerLine = lines.get(0);
        String[] headerTokens = headerLine.trim().split("\\s+");
        Set<String> alphabet = new LinkedHashSet<>();
        for (int i = 1; i < headerTokens.length; i++) {
            alphabet.add(headerTokens[i]);
        }
        List<String> stateNames = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                String[] tokens = line.split("\\s+");
                stateNames.add(tokens[0]);
            }
        }
        String initialState = getInitialState(stateNames);
        Set<String> acceptingStates = getAcceptingStates();

        for (String stateName : stateNames) {
            State state = new State(stateName, acceptingStates.contains(stateName));
            stateMap.put(stateName, state);
            automaton.addState(state);
        }
        automaton.setInitialState(stateMap.get(initialState));
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            List<String> tokens = parseTableRow(line);
            String currentState = tokens.get(0);
            int symbolIndex = 0;
            for (String symbol : alphabet) {
                symbolIndex++;
                if (tokens.size() <= symbolIndex) continue;
                String transitionStr = tokens.get(symbolIndex).trim();
                if (transitionStr.equals("-")) continue;
                String[] targetStates = transitionStr.split(",");
                for (String targetState : targetStates) {
                    targetState = targetState.trim();
                    if (stateMap.containsKey(targetState)) {
                        if (symbol.equals(EPSILON)) {
                            stateMap.get(currentState).addEpsilonTransition(stateMap.get(targetState));
                        } else {
                            stateMap.get(currentState).addTransition(symbol.charAt(0), stateMap.get(targetState));
                        }
                    } else {
                        System.out.println("Warning: Invalid state '" + targetState + "' ignored.");
                    }
                }
            }
        }
        printCurrentTransitionTable(stateNames, alphabet, stateMap);
        return automaton;
    }

    private static List<String> parseTableRow(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inToken = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isWhitespace(c)) {
                if (inToken) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                    inToken = false;
                }
            } else {
                currentToken.append(c);
                inToken = true;
            }
        }
        if (inToken) {
            tokens.add(currentToken.toString());
        }
        return tokens;
    }

    private static Set<String> getAcceptingStates() {
        System.out.println("Enter accepting states (comma-separated):");
        Set<String> acceptingStates = new HashSet<>(Arrays.asList(scanner.nextLine().trim().split(",")));
        return acceptingStates;
    }

    private static String getInitialState(List<String> stateNames) {
        while (true) {
            System.out.println("Enter the initial state:");
            String initialState = scanner.nextLine().trim();
            if (stateNames.contains(initialState)) {
                return initialState;
            }
            System.out.println("Invalid state name. Please enter a valid state.");
        }
    }

    private static void printCurrentTransitionTable(List<String> states, Set<String> alphabet,
                                                    Map<String, State> stateMap) {
        System.out.println("Transition Table:");
        String table = tableGenerator.generateTransitionTable(states, alphabet, stateMap);
        System.out.print(table);
    }}
