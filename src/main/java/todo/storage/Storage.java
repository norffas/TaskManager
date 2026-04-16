package todo.storage;

import todo.model.Task;

import java.util.List;

public interface Storage {
    public void save(List<Task> tasks);
    public List<Task> load();
}
