package todo.commands;

import todo.manager.TaskManager;

public class DisplayTasks implements Command {
    private final TaskManager manager;

    public DisplayTasks(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult("Список всех задач: ", manager.getAllTasks());
    }
}
