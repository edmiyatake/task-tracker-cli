public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    private final String jsonValue;

    TaskStatus(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public static TaskStatus fromJsonValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.jsonValue.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid task status: " + value);
    }
}