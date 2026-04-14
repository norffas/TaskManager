package todo.commands;

import todo.manager.OperationStatus;
import todo.manager.TaskManager;
import todo.manager.TaskManagerOperationResult;
import todo.model.Task;

public class CompleteTask implements Command {
    private final int id;
    private final TaskManager manager;

    public CompleteTask(TaskManager manager, int id) {
        this.id = id;
        this.manager = manager;
    }

    @Override
    public CommandResult execute(){
        TaskManagerOperationResult tmResult = manager.completeTask(id);
        Task task = tmResult.getTask();
        OperationStatus status = tmResult.getStatus();
        String msg;
        if (status == OperationStatus.NOT_FOUND) {
            msg = "Задача не найдена";
        }
        else if (status == OperationStatus.ALREADY_COMPLETED)
            msg = "Задача уже выполнена";
        else
            msg = "Задаче успешно присвоен статус выполненной.";
        return new CommandResult(msg, task);
    }
}
