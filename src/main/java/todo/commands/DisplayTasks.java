package todo.commands;

import todo.Command;
import todo.Task;
import todo.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class DisplayTasks implements Command<List<Task>> {
    private final TaskManager manager;

    public DisplayTasks(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public List<Task> execute() {
        List<Task> tasks = new ArrayList<>();
        tasks = manager.getAllTasks();
        return tasks;
    }
}
