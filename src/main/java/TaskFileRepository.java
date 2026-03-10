import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskFileRepository {
    private final Path tasksFile;

    public TaskFileRepository(Path tasksFile) {
        this.tasksFile = tasksFile;
    }

    public List<Task> loadTasks() throws IOException {
        ensureFileExists();

        String json = Files.readString(tasksFile).trim();
        List<Task> tasks = new ArrayList<>();

        if (json.isEmpty() || json.equals("[]")) {
            return tasks;
        }

        Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
        Matcher objectMatcher = objectPattern.matcher(json);

        while (objectMatcher.find()) {
            String taskBlock = objectMatcher.group();

            int id = extractInt(taskBlock, "id");
            String description = extractString(taskBlock, "description");
            String status = extractString(taskBlock, "status");
            String createdAt = extractString(taskBlock, "createdAt");
            String updatedAt = extractString(taskBlock, "updatedAt");

            Task task = new Task(
                    id,
                    description,
                    TaskStatus.fromJsonValue(status),
                    createdAt,
                    updatedAt
            );

            tasks.add(task);
        }

        return tasks;
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        ensureFileExists();

        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            sb.append("  {\n");
            sb.append("    \"id\": ").append(task.getId()).append(",\n");
            sb.append("    \"description\": \"").append(escapeJson(task.getDescription())).append("\",\n");
            sb.append("    \"status\": \"").append(task.getStatus().getJsonValue()).append("\",\n");
            sb.append("    \"createdAt\": \"").append(escapeJson(task.getCreatedAt())).append("\",\n");
            sb.append("    \"updatedAt\": \"").append(escapeJson(task.getUpdatedAt())).append("\"\n");
            sb.append("  }");

            if (i < tasks.size() - 1) {
                sb.append(",");
            }

            sb.append("\n");
        }

        sb.append("]");

        Files.writeString(tasksFile, sb.toString());
    }

    private void ensureFileExists() throws IOException {
        if (!Files.exists(tasksFile)) {
            Files.writeString(tasksFile, "[]");
        }
    }

    private int extractInt(String jsonObject, String fieldName) throws IOException {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(jsonObject);

        if (!matcher.find()) {
            throw new IOException("Missing or invalid integer field: " + fieldName);
        }

        return Integer.parseInt(matcher.group(1));
    }

    private String extractString(String jsonObject, String fieldName) throws IOException {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"((?:\\\\.|[^\"])*)\"");
        Matcher matcher = pattern.matcher(jsonObject);

        if (!matcher.find()) {
            throw new IOException("Missing or invalid string field: " + fieldName);
        }

        return unescapeJson(matcher.group(1));
    }

    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text
                .replace("\\t", "\t")
                .replace("\\r", "\r")
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}