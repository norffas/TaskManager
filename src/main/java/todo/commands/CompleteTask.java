package todo.commands;

import todo.service.OperationStatus;
import todo.service.TaskService;
import todo.service.TaskServiceOperationResult;
import todo.model.Task;

public class CompleteTask implements Command {
    private final int id;
    private final TaskService manager;

    public CompleteTask(TaskService manager, int id) {
        this.id = id;
        this.manager = manager;
    }

    @Override
    public CommandResult execute(){
        TaskServiceOperationResult tmResult = manager.completeTask(id);
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
