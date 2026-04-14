package todo.commands;

import todo.manager.TaskManager;

public class DisplayNotCompletedTasks implements Command {
    private final TaskManager manager;

    public DisplayNotCompletedTasks(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult("Список невыполненных задач: ", manager.getNotCompletedTasks());
    }
}
