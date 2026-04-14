package todo.manager;

import todo.model.Task;

public class TaskManagerOperationResult {
    private final Task task;
    private final OperationStatus status;

    public TaskManagerOperationResult(OperationStatus status, Task task) {
        if(status == null)
            throw new NullPointerException();
        this.status = status;
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public OperationStatus getStatus() {
        return status;
    }
}
