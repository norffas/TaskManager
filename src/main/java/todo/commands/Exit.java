package todo.commands;

import todo.service.TaskService;

public class Exit implements Command {
    private final TaskService service;

    public Exit(TaskService manager) {
        this.service = manager;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult("Завершение выполнения программы.", true);
    }
}
