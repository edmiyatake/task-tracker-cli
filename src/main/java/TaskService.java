import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class TaskService {
    private final TaskFileRepository repository;

    public TaskService(TaskFileRepository repository) {
        this.repository = repository;
    }

    public void addTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Error: task description cannot be empty.");
            return;
        }

        try {
            List<Task> tasks = repository.loadTasks();

            int nextId = 1;
            for (Task task : tasks) {
                if (task.getId() >= nextId) {
                    nextId = task.getId() + 1;
                }
            }

            String now = Instant.now().toString();
            Task newTask = new Task(
                    nextId,
                    description.trim(),
                    TaskStatus.TODO,
                    now,
                    now
            );

            tasks.add(newTask);
            repository.saveTasks(tasks);

            System.out.println("Task added successfully (ID: " + nextId + ")");
        } catch (IOException e) {
            System.out.println("Error: could not save task.");
            System.out.println(e.getMessage());
        }
    }
}