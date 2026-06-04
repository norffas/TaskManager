package todo.repository;

import todo.model.Task;
import todo.model.TaskStatus;

import java.util.List;

public interface Repository {

    Task saveTask(String description);

    Task findTaskById(int id);

    Task deleteTaskById(int id);

    Task update(int id, TaskStatus status);


    List<Task> findAll();


}
