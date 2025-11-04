package TODOList;

/**
 * A task in the TODO List
 */
public class Task {
    String description;
    boolean isComplete;

    /**
     * Creates a new task with a description and completed status
     *
     * @param description
     * @param isComplete
     */
    Task(String description, boolean isComplete) {
        this.description = description;
        this.isComplete = isComplete;
    }

    /**
     * It returns the string representation of a task
     *
     * @return task description
     */
    @Override
    public String toString() {
        return description;
    }
}