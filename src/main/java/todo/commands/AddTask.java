package todo.commands;

import todo.manager.OperationStatus;
import todo.manager.TaskManager;
import todo.manager.TaskManagerOperationResult;

public class AddTask implements Command{
    private final TaskManager manager;
    private final String description;

    public AddTask(TaskManager manager, String description) {
        this.description = description;
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        TaskManagerOperationResult result = manager.addTask(description);
        if(result.getStatus() == OperationStatus.ADDED){
            return new CommandResult("Задача успешно создана", result.getTask());
        }
        else{
            return new CommandResult("Не удалось добавить задачу.");
        }
    }
}
