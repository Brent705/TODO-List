package TODOList;

public class Task {
    String description;
    boolean isComplete;

    Task(String description, boolean isComplete) {
        this.description = description;
        this.isComplete = isComplete;
    }

    @Override
    public String toString() {
        return description;

    }
}