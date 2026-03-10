public class Task {
    private final int id;
    private final String description;
    private final TaskStatus status;
    private final String createdAt;
    private final String updatedAt;

    public Task(int id, String description, TaskStatus status, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}