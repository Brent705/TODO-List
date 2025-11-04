package TODOList;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TODOTests {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = System.out;
    private final InputStream inputStream = System.in;
    private Path tempTasksFile;

    @BeforeEach
    void setUp() throws IOException {
        tempTasksFile = Files.createTempFile("tasks2", ".txt");
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(printStream);
        System.setIn(inputStream);
        Files.deleteIfExists(tempTasksFile);
    }

    private App createApp(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        App app = new App();
        var field = app.getClass().getDeclaredField("tasksFile");
        field.setAccessible(true);
        field.set(app, tempTasksFile.toString());
        return app;
    }

    @Test
    void testAddTask() throws Exception {
        App app = createApp("Task 1\n");
        app.addTask();

        List<String> tasks = Files.readAllLines(tempTasksFile);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals("0|Task 1", tasks.get(0));
    }

    @Test
    void testCompleteTask() throws Exception {
        App app = createApp("1\n");
        Files.write(tempTasksFile, List.of("0|Task 1"));
        var loadMethod = app.getClass().getDeclaredMethod("loadTasks");
        loadMethod.setAccessible(true);
        loadMethod.invoke(app);
        app.completeTask();
        List<String> tasks = Files.readAllLines(tempTasksFile);
        Assertions.assertTrue(tasks.get(0).startsWith("1|"));
    }

    @Test
    void testRemoveTask() throws Exception {
        App app = createApp("1\n");
        Files.write(tempTasksFile, List.of("1|Task 1"));
        var loadMethod = app.getClass().getDeclaredMethod("loadTasks");
        loadMethod.setAccessible(true);
        loadMethod.invoke(app);
        app.removeTask();
        List<String> tasks = Files.readAllLines(tempTasksFile);
        Assertions.assertTrue(tasks.isEmpty(), "File should be empty");
    }

    @Test
    void testEmptyList() throws Exception {
        App app = createApp("");
        app.listTasks();
        Assertions.assertTrue(outputStream.toString().contains("Nothing to do..."));
    }

    @Test
    void testListWithTasks() throws Exception {
        App app = createApp("");
        Files.write(tempTasksFile, List.of("1|Task 1", "0|Task 2"));
        var loadMethod = app.getClass().getDeclaredMethod("loadTasks");
        loadMethod.setAccessible(true);
        loadMethod.invoke(app);
        app.listTasks();
        String output = outputStream.toString();
        Assertions.assertTrue(output.contains("[X] Task 1"));
        Assertions.assertTrue(output.contains("[ ] Task 2"));
    }
}
