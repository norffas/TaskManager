package todo.commands;

import todo.Command;
import todo.TaskManager;

public class DeleteTask implements Command<Boolean> {
    private final TaskManager manager;
    private final int id;

    public DeleteTask(TaskManager manager, int id) {
        this.manager = manager;
        this.id = id;
    }


    @Override
    public Boolean execute() {
        return manager.deleteTask(id);
    }
}
