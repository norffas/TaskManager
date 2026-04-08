package todo.commands;

import todo.Command;
import todo.TaskManager;

public class CompleteTask implements Command<Boolean> {
    private final int id;
    private final TaskManager manager;

    public CompleteTask(TaskManager manager, int id) {
        this.id = id;
        this.manager = manager;
    }

    @Override
    public Boolean execute() {
        return manager.completeTask(id);
    }
}
