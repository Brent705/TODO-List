package TODOList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Application class for my TODO List app
 */
public class App {
    private final String intro = "Welcome to my Java TODO List App\n" +
            "-----------------------------------\n";

    private final String menu = "\n-----------------------------------\n" +
            "What would you like to do?\n" +
            "a - add task\n" +
            "c - complete task\n" +
            "r - remove completed task\n" +
            "q - quit\n" +
            "-----------------------------------\n" +
            "Enter choice: ";

    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Scanner scanner;
    private final String tasksFile;

    /**
     * The default constructor
     */
    public App() {
        this("tasks.txt", new Scanner(System.in));
    }

    /**
     * Constructor that allows a tasks file and a Scanner
     *
     * @param tasksFile
     * @param scanner
     */
    public App(String tasksFile, Scanner scanner) {
        this.tasksFile = tasksFile;
        this.scanner = scanner;
    }

    /**
     * The run method that runs a while loop which displays the menu
     */
    public void run() {
        System.out.println(intro);
        loadTasks();
        boolean done = false;
        while (!done) {
            listTasks();
            System.out.print(menu);
            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "a":
                    addTask();
                    break;
                case "c":
                    completeTask();
                    break;
                case "r":
                    removeTask();
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    saveTasks();
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice, please try again");
            }
        }
    }

    /**
     * Displays all the tasks
     */
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Nothing to do...");
            return;
        }

        System.out.println("Your Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isComplete ? "[X] " : "[ ] ";
            System.out.println((i + 1) + ". " + status + task);
        }
    }

    /**
     * Prompts the user to add a task
     * Saves the task to the file
     */
    public void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        boolean isComplete = false;
        tasks.add(new Task(description, isComplete));
        saveTasks();
    }

    /**
     * Prompts the user to mark a task as complete
     * Updates the list and saves the change to the file
     */
    public void completeTask() {
        listTasks();
        System.out.print("Which task would you like to complete?: ");
        int index;
        try {
            index = scanner.nextInt();
        } catch (java.util.InputMismatchException ime) {
            System.out.println("Invalid input, please enter a number.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (index <= 0 || index > tasks.size()) {
            System.out.println("Invalid index!");
            return;
        }

        Task task = tasks.get(index - 1);
        task.isComplete = true;
        System.out.println("Task completed! Task: " + task);
        saveTasks();
    }

    /**
     * Displays a list of completed tasks
     * The user can select one of the completed tasks to remove
     * It removes the selected tasks and updates the list
     * It also saves to the file
     */
    public void removeTask() {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isComplete) {
                indexes.add(i);
            }
        }

        if (indexes.isEmpty()) {
            System.out.println("No completed tasks...");
            return;
        }

        System.out.println("Completed tasks:");
        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            Task task = tasks.get(index);
            System.out.println((i + 1) + ". " + task);
        }

        System.out.print("Which task would you like to remove?: ");
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (java.util.InputMismatchException ime) {
            System.out.println("Invalid input, please enter a number.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (choice <= 0 || choice > indexes.size()) {
            System.out.println("Invalid index!");
            return;
        }

        int removedIndex = indexes.get(choice - 1);
        Task removedTask = tasks.remove(removedIndex);
        System.out.println("Task removed! Task: " + removedTask);
        saveTasks();
    }

    /**
     * Saves to the file
     * Each line in the file contains a task, and it's completion status
     */
    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasksFile))) {
            for (Task task : tasks) {
                writer.write((task.isComplete ? "1" : "0") + "|" + task.description);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the file
     * If there is no file then the task list remains empty
     */
    private void loadTasks() {
        File file = new File(tasksFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    boolean isComplete = parts[0].trim().equals("1");
                    tasks.add(new Task(parts[1].trim(), isComplete));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
