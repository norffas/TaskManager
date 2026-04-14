package todo.commands;

import todo.manager.OperationStatus;
import todo.manager.TaskManager;
import todo.manager.TaskManagerOperationResult;

public class DeleteTask implements Command {
    private final TaskManager manager;
    private final int id;

    public DeleteTask(TaskManager manager, int id) {
        this.manager = manager;
        this.id = id;
    }


    @Override
    public CommandResult execute() {
        TaskManagerOperationResult result = manager.deleteTask(id);
        if(result.getStatus() == OperationStatus.NOT_FOUND)
            return new CommandResult("Задача не найдена");
        else{
            return new CommandResult("Задача успешно удалена", result.getTask());
        }
    }
}
