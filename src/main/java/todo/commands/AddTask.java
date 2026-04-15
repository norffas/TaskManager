package todo.commands;

import todo.manager.TaskManager;

public class AddTask implements Command{
    private final TaskManager manager;
    private final String description;

    public AddTask(TaskManager manager, String description) {
        this.description = description;
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        try {
            return new CommandResult("Задача успешно создана", manager.addTask(description));
        }catch (IllegalArgumentException e){
            return new CommandResult("Не получилось создать задачу");
        }

    }
}
