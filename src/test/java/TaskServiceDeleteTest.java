import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceDeleteTest {

    @TempDir
    Path tempDir;

    @Test
    void deleteTask_removesMatchingTask() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("First task");
        service.addTask("Second task");

        service.deleteTask(1);

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals(2, tasks.get(0).getId());
        assertEquals("Second task", tasks.get(0).getDescription());
    }

    @Test
    void deleteTask_doesNothingWhenIdDoesNotExist() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");

        service.deleteTask(99);

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals("Buy milk", tasks.get(0).getDescription());
    }

    @Test
    void deleteTask_canDeleteOnlyTask() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Only task");

        service.deleteTask(1);

        List<Task> tasks = repository.loadTasks();
        assertTrue(tasks.isEmpty());
    }
}