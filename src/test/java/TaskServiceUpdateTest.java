import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceUpdateTest {

    @TempDir
    Path tempDir;

    @Test
    void updateTask_updatesDescriptionAndKeepsOtherFields() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");

        List<Task> beforeTasks = repository.loadTasks();
        Task originalTask = beforeTasks.get(0);

        service.updateTask(1, "Buy bread");

        List<Task> afterTasks = repository.loadTasks();
        assertEquals(1, afterTasks.size());

        Task updatedTask = afterTasks.get(0);
        assertEquals(1, updatedTask.getId());
        assertEquals("Buy bread", updatedTask.getDescription());
        assertEquals(TaskStatus.TODO, updatedTask.getStatus());
        assertEquals(originalTask.getCreatedAt(), updatedTask.getCreatedAt());
    }

    @Test
    void updateTask_updatesOnlyTheMatchingTask() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("First task");
        service.addTask("Second task");

        service.updateTask(2, "Updated second task");

        List<Task> tasks = repository.loadTasks();
        assertEquals(2, tasks.size());

        assertEquals("First task", tasks.get(0).getDescription());
        assertEquals("Updated second task", tasks.get(1).getDescription());
        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
    }

    @Test
    void updateTask_doesNothingWhenTaskIdDoesNotExist() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");

        List<Task> beforeTasks = repository.loadTasks();
        Task beforeTask = beforeTasks.get(0);

        service.updateTask(99, "This should not work");

        List<Task> afterTasks = repository.loadTasks();
        assertEquals(1, afterTasks.size());

        Task afterTask = afterTasks.get(0);
        assertEquals(beforeTask.getId(), afterTask.getId());
        assertEquals(beforeTask.getDescription(), afterTask.getDescription());
        assertEquals(beforeTask.getStatus(), afterTask.getStatus());
        assertEquals(beforeTask.getCreatedAt(), afterTask.getCreatedAt());
        assertEquals(beforeTask.getUpdatedAt(), afterTask.getUpdatedAt());
    }

    @Test
    void updateTask_doesNothingWhenDescriptionIsBlank() throws IOException {
        Path testFile = tempDir.resolve("tasks.json");
        TaskFileRepository repository = new TaskFileRepository(testFile);
        TaskService service = new TaskService(repository);

        service.addTask("Buy milk");

        List<Task> beforeTasks = repository.loadTasks();
        Task beforeTask = beforeTasks.get(0);

        service.updateTask(1, "   ");

        List<Task> afterTasks = repository.loadTasks();
        assertEquals(1, afterTasks.size());

        Task afterTask = afterTasks.get(0);
        assertEquals(beforeTask.getId(), afterTask.getId());
        assertEquals(beforeTask.getDescription(), afterTask.getDescription());
        assertEquals(beforeTask.getStatus(), afterTask.getStatus());
        assertEquals(beforeTask.getCreatedAt(), afterTask.getCreatedAt());
        assertEquals(beforeTask.getUpdatedAt(), afterTask.getUpdatedAt());
    }
}