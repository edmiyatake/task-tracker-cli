import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void addTask_createsFileAndAddsFirstTask() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");

        assertTrue(java.nio.file.Files.exists(testFile));

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());

        Task task = tasks.get(0);
        assertEquals(1, task.getId());
        assertEquals("Buy milk", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    void addTask_incrementsIdWhenTasksAlreadyExist() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("First task");
        service.addTask("Second task");

        List<Task> tasks = repository.loadTasks();
        assertEquals(2, tasks.size());

        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
        assertEquals("Second task", tasks.get(1).getDescription());
    }

    @Test
    void addTask_trimsDescription() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("   Buy milk   ");

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals("Buy milk", tasks.get(0).getDescription());
    }

    @Test
    void addTask_doesNotAddEmptyDescription() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("   ");

        List<Task> tasks = repository.loadTasks();
        assertTrue(tasks.isEmpty());
    }
}