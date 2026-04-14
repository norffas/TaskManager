package todo.commands;

import todo.manager.TaskManager;

public class Exit implements Command {
    private final TaskManager manager;

    public Exit(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        manager.saveTasks();
        return new CommandResult("Завершение выполнения программы. Все измененные данные сохранены.", true);
    }
}
