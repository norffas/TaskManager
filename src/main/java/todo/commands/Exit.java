package todo.commands;

import todo.Command;
import todo.TaskManager;

public class Exit implements Command<Boolean> {
    private final TaskManager manager;

    public Exit(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public Boolean execute() {
        manager.saveTasks();
        return true;
    }
}
