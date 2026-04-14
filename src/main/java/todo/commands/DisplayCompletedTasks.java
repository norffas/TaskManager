package todo.commands;

import todo.manager.TaskManager;

public class DisplayCompletedTasks implements Command {
    private final TaskManager manager;

    public DisplayCompletedTasks(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult("Список выполненных задач: ", manager.getCompletedTasks());
    }
}
