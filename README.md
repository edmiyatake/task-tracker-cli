# Task Tracker CLI

A simple command-line task tracker built in Java. This application lets users add, update, delete, and manage tasks from the terminal, while storing all task data in a local `tasks.json` file.

## Features

- Add tasks
- Update tasks
- Delete tasks
- Mark tasks as in progress
- Mark tasks as done
- List all tasks
- List tasks by status:
    - done
    - todo
    - in-progress

## Technologies Used

- Java
- Maven
- Local JSON file storage
- JUnit for testing

## Project Structure

```text
src/
├─ main/
│  └─ java/
│     ├─ Main.java
│     ├─ Task.java
│     ├─ TaskStatus.java
│     ├─ TaskService.java
│     └─ TaskFileRepository.java
└─ test/
   └─ java/
      ├─ TaskServiceTest.java
      ├─ TaskServiceUpdateTest.java
      ├─ TaskServiceDeleteTest.java
      ├─ TaskServiceStatusTest.java
      └─ TaskServiceListTest.java
```

## How to Run

From the project root:

```bash
mvn -q exec:java -Dexec.args="add \"Buy milk\""
```

On Windows cmd.exe, use:

```bash
mvn -q exec:java -Dexec.args="add ""Buy milk"""
Commands
Add a task
add <description>
```

Example:

```bash
mvn -q exec:java -Dexec.args="add \"Buy milk\""
Update a task
update <id> <new description>
```

Example:

```bash
mvn -q exec:java -Dexec.args="update 1 \"Buy bread\""
Delete a task
delete <id>
```

Example:

```bash
mvn -q exec:java -Dexec.args="delete 1"
Mark task as in progress
mark-in-progress <id>
```

Example:

```bash
mvn -q exec:java -Dexec.args="mark-in-progress 1"
Mark task as done
mark-done <id>
```

Example:

```bash
mvn -q exec:java -Dexec.args="mark-done 1"
List all tasks
list
```

Example:

```bash
mvn -q exec:java -Dexec.args="list"
List done tasks
list done
List todo tasks
list todo
List in-progress tasks
list in-progress
```

## Task Storage

Tasks are stored in a tasks.json file in the current project directory. If the file does not exist, it is created automatically.

Each task contains:
- id
- description 
- status 
- createdAt 
- updatedAt

## Running Tests

```bash
mvn test
```
## Notes
- No external JSON libraries or frameworks were used
- The application uses Java file system APIs to read and write task data
- Input validation and common edge cases are handled in the CLI and service layer
- Implemented from https://roadmap.sh/projects/task-tracker
