package todo.commands;

import todo.Command;
import todo.Task;
import todo.TaskManager;

import java.util.List;

public class DisplayNotCompletedTasks implements Command<List<Task>> {
    private final TaskManager manager;

    public DisplayNotCompletedTasks(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Task> execute() {
        return manager.getNotCompletedTasks();
    }
}
