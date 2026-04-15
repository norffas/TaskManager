package todo.commands;

import todo.manager.TaskManager;

public class DisplayTasks implements Command {
    private final TaskManager manager;
    private final DisplayTasksFilter filter;

    public DisplayTasks(TaskManager manager, DisplayTasksFilter filter) {
        this.manager = manager;
        this.filter = filter;
    }

    @Override
    public CommandResult execute() {
        if(filter == DisplayTasksFilter.ALL_TASKS)
            return new CommandResult("Список всех задач: ", manager.getAllTasks());
        else if(filter == DisplayTasksFilter.COMPLETED_TASKS)
            return new CommandResult("Список выполненных задач: ", manager.getCompletedTasks());
        else
            return new CommandResult("Список невыполненных задач: ", manager.getNotCompletedTasks());
    }
}
