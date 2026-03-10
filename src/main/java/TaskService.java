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

    public void updateTask(int id, String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            System.out.println("Error: new task description cannot be empty.");
            return;
        }

        try {
            List<Task> tasks = repository.loadTasks();

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);

                if (task.getId() == id) {
                    Task updatedTask = new Task(
                            task.getId(),
                            newDescription.trim(),
                            task.getStatus(),
                            task.getCreatedAt(),
                            java.time.Instant.now().toString()
                    );

                    tasks.set(i, updatedTask);
                    repository.saveTasks(tasks);

                    System.out.println("Task updated successfully (ID: " + id + ")");
                    return;
                }
            }

            System.out.println("Error: task with ID " + id + " not found.");
        } catch (IOException e) {
            System.out.println("Error: could not update task.");
            System.out.println(e.getMessage());
        }
    }

    public void deleteTask(int id) {
        try {
            List<Task> tasks = repository.loadTasks();

            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getId() == id) {
                    tasks.remove(i);
                    repository.saveTasks(tasks);
                    System.out.println("Task deleted successfully (ID: " + id + ")");
                    return;
                }
            }

            System.out.println("Error: task with ID " + id + " not found.");
        } catch (IOException e) {
            System.out.println("Error: could not delete task.");
            System.out.println(e.getMessage());
        }
    }

    public void markTaskInProgress(int id) {
        updateTaskStatus(id, TaskStatus.IN_PROGRESS);
    }

    public void markTaskDone(int id) {
        updateTaskStatus(id, TaskStatus.DONE);
    }

    private void updateTaskStatus(int id, TaskStatus newStatus) {
        try {
            List<Task> tasks = repository.loadTasks();

            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);

                if (task.getId() == id) {
                    Task updatedTask = new Task(
                            task.getId(),
                            task.getDescription(),
                            newStatus,
                            task.getCreatedAt(),
                            Instant.now().toString()
                    );

                    tasks.set(i, updatedTask);
                    repository.saveTasks(tasks);

                    System.out.println("Task status updated successfully (ID: " + id + ")");
                    return;
                }
            }

            System.out.println("Error: task with ID " + id + " not found.");
        } catch (IOException e) {
            System.out.println("Error: could not update task status.");
            System.out.println(e.getMessage());
        }
    }
}