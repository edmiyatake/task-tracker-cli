import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        TaskService taskService = new TaskService();

        String command = args[0];
        // handle user input from args
        switch (command) {
            case "add" -> {
                if (args.length < 2) {
                    System.out.println("Error: missing task description.");
                    System.out.println("Usage: add <description>");
                    return;
                }

                String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                taskService.addTask(description);
            }
            case "update" -> handleUpdate(args);
            case "delete" -> handleDelete(args);
            case "mark-in-progress" -> handleMarkInProgress(args);
            case "mark-done" -> handleMarkDone(args);
            case "list" -> handleList(args);
            default -> {
                System.out.println("Unknown command: " + command);
                printUsage();
            }
        }
    }

    private static void handleAdd(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: missing task description.");
            System.out.println("Usage: add <description>");
            return;
        }

        String description = joinArgs(args, 1);
        System.out.println("ADD -> " + description);
    }

    private static void handleUpdate(String[] args) {
        if (args.length < 3) {
            System.out.println("Error: missing task id or new description.");
            System.out.println("Usage: update <id> <new description>");
            return;
        }

        int id = parseId(args[1]);
        if (id == -1) return;

        String newDescription = joinArgs(args, 2);
        System.out.println("UPDATE -> id=" + id + ", description=" + newDescription);
    }

    private static void handleDelete(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: missing task id.");
            System.out.println("Usage: delete <id>");
            return;
        }

        int id = parseId(args[1]);
        if (id == -1) return;

        System.out.println("DELETE -> id=" + id);
    }

    private static void handleMarkInProgress(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: missing task id.");
            System.out.println("Usage: mark-in-progress <id>");
            return;
        }

        int id = parseId(args[1]);
        if (id == -1) return;

        System.out.println("MARK IN PROGRESS -> id=" + id);
    }

    private static void handleMarkDone(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: missing task id.");
            System.out.println("Usage: mark-done <id>");
            return;
        }

        int id = parseId(args[1]);
        if (id == -1) return;

        System.out.println("MARK DONE -> id=" + id);
    }

    private static void handleList(String[] args) {
        if (args.length == 1) {
            System.out.println("LIST -> all tasks");
            return;
        }

        String filter = args[1];

        switch (filter) {
            case "done" -> System.out.println("LIST -> done tasks");
            case "todo" -> System.out.println("LIST -> todo tasks");
            case "in-progress" -> System.out.println("LIST -> in-progress tasks");
            default -> {
                System.out.println("Error: invalid list filter.");
                System.out.println("Usage: list | list done | list todo | list in-progress");
            }
        }
    }

    private static int parseId(String value) {
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                System.out.println("Error: id must be a positive integer.");
                return -1;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Error: id must be a number.");
            return -1;
        }
    }

    private static String joinArgs(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private static void printUsage() {
        System.out.println("Task Tracker CLI");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  add <description>");
        System.out.println("  update <id> <new description>");
        System.out.println("  delete <id>");
        System.out.println("  mark-in-progress <id>");
        System.out.println("  mark-done <id>");
        System.out.println("  list");
        System.out.println("  list done");
        System.out.println("  list todo");
        System.out.println("  list in-progress");
    }
}