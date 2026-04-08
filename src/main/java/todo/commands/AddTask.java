package todo.commands;

import todo.Command;
import todo.Task;
import todo.TaskManager;

public class AddTask implements Command<Task> {
    private final TaskManager manager;
    private final String description;

    public AddTask(TaskManager manager, String description) {
        this.description = description;
        this.manager = manager;
    }

    @Override
    public Task execute() {
        return manager.addTask(description);

    }
}
