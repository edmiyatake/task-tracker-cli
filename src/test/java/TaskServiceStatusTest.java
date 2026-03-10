import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceStatusTest {

    @TempDir
    Path tempDir;

    @Test
    void markTaskInProgress_updatesStatus() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        service.markTaskInProgress(1);

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.IN_PROGRESS, tasks.get(0).getStatus());
    }

    @Test
    void markTaskDone_updatesStatus() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        service.markTaskDone(1);

        List<Task> tasks = repository.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.DONE, tasks.get(0).getStatus());
    }

    @Test
    void markTaskStatus_keepsDescriptionAndCreatedAt() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        Task before = repository.loadTasks().get(0);

        service.markTaskDone(1);
        Task after = repository.loadTasks().get(0);

        assertEquals(before.getId(), after.getId());
        assertEquals(before.getDescription(), after.getDescription());
        assertEquals(before.getCreatedAt(), after.getCreatedAt());
        assertEquals(TaskStatus.DONE, after.getStatus());
        assertNotEquals(before.getUpdatedAt(), after.getUpdatedAt());
    }

    @Test
    void markTaskStatus_doesNothingWhenIdDoesNotExist() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");
        Task before = repository.loadTasks().get(0);

        service.markTaskDone(99);
        Task after = repository.loadTasks().get(0);

        assertEquals(before.getId(), after.getId());
        assertEquals(before.getDescription(), after.getDescription());
        assertEquals(before.getStatus(), after.getStatus());
        assertEquals(before.getCreatedAt(), after.getCreatedAt());
        assertEquals(before.getUpdatedAt(), after.getUpdatedAt());
    }
}