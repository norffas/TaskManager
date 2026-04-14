package todo.commands;

import todo.model.Task;

import java.util.List;

public class CommandResult {
    private final String message;
    private final Task task;
    private final List<Task> tasks;
    private final boolean exit;

    public CommandResult(String message, Task task) {
        this.message = message;
        this.tasks = null;
        this.task = task;
        this.exit = false;
    }

    public CommandResult(String message, List<Task> tasks) {
        this.message = message;
        this.tasks = tasks;
        this.task = null;
        this.exit = false;
    }

    public CommandResult(String message, boolean exit) {
        this.message = message;
        this.tasks = null;
        this.task = null;
        this.exit = exit;
    }

    public CommandResult(String message) {
        this.message = message;
        this.tasks = null;
        this.task = null;
        this.exit = false;
    }

    public String getMessage() {
        return message;
    }

    public Task getTask() {
        return task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean isExit() {
        return exit;
    }
}
