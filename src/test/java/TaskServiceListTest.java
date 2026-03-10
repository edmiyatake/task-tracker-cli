import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskServiceListTest {

    @TempDir
    Path tempDir;

    @Test
    void listTasks_printsAllTasks() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        service.addTask("Do homework");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            service.listTasks();
        } finally {
            System.setOut(originalOut);
        }

        String printed = output.toString();
        assertTrue(printed.contains("Buy milk"));
        assertTrue(printed.contains("Do homework"));
    }

    @Test
    void listTasksByStatus_printsOnlyDoneTasks() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        service.addTask("Do homework");
        service.markTaskDone(2);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            service.listTasksByStatus(TaskStatus.DONE);
        } finally {
            System.setOut(originalOut);
        }

        String printed = output.toString();
        assertTrue(printed.contains("Do homework"));
        assertTrue(!printed.contains("Buy milk"));
    }

    @Test
    void listTasksByStatus_printsOnlyTodoTasks() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        service.addTask("Do homework");
        service.markTaskDone(2);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            service.listTasksByStatus(TaskStatus.TODO);
        } finally {
            System.setOut(originalOut);
        }

        String printed = output.toString();
        assertTrue(printed.contains("Buy milk"));
        assertTrue(!printed.contains("Do homework"));
    }

    @Test
    void listTasks_printsNoTasksMessageWhenEmpty() {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            service.listTasks();
        } finally {
            System.setOut(originalOut);
        }

        String printed = output.toString();
        assertTrue(printed.contains("No tasks found."));
    }
}