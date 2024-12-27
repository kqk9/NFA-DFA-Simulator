import java.io.IOException;
import java.util.Scanner;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Automaton automaton = AutomataSimulator.createAutomatonFromFile("file.txt");
        while (true) {
            System.out.println("Enter an input string to test (or 'exit' to quit):");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            ProcessResult result = automaton.processInput(input);
            result.printResult();
        }
    }
}
