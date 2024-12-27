import java.util.List;

class ProcessResult {
    private boolean accepted;
    private List<String> path;
    private String message;

    public ProcessResult(boolean accepted, List<String> path, String message) {
        this.accepted = accepted;
        this.path = path;
        this.message = message;
    }

    public void printResult() {
        System.out.println("Result: " + message);
        System.out.println("Path taken: " + String.join(" -> ", path));
    }
}

