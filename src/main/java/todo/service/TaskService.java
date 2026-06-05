package todo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.model.Task;
import todo.model.TaskStatus;
import todo.repository.Repository;
import java.util.List;

public class TaskService {
    private final Repository repo;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(Repository repo) {
        this.repo = repo;
    }

    public synchronized TaskManagerOperationResult addTask(String description) {
        if(description == null || description.trim().isEmpty()){
            return new TaskManagerOperationResult(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, null);
        }
        description = description.trim();
        if(!isValidDescription(description))
            return new TaskManagerOperationResult(OperationStatus.NOT_ADDED_INVALID_DESCRIPTION, null);
        Task task = new Task(description);
        task = repo.saveTask(task);
        return new TaskManagerOperationResult(OperationStatus.ADDED, task);
    }

    public Task findTaskById(int id) {
        return repo.findTaskById(id);
    }

    public synchronized TaskManagerOperationResult deleteTask(int id){
        Task task = repo.deleteTaskById(id);
        if(task == null)
            return new TaskManagerOperationResult(OperationStatus.NOT_FOUND, null);
        else
            return new TaskManagerOperationResult(OperationStatus.DELETED_NOW, task);
    }

    public synchronized TaskManagerOperationResult completeTask(int id){
        Task task = repo.findTaskById(id);
        if(task == null)
            return new TaskManagerOperationResult(OperationStatus.NOT_FOUND, task);
        else if(task.isCompleted())
            return new TaskManagerOperationResult(OperationStatus.ALREADY_COMPLETED, task);
        task = repo.update(id, TaskStatus.COMPLETED);
        return new TaskManagerOperationResult(OperationStatus.COMPLETED_NOW, task);
    }
    public synchronized int updateAbandonedStatus() {
        return repo.statusAutoUpdate();
    }

    public synchronized List<Task> getAllTasks() {
        return repo.findAll();
    }

    public synchronized List<Task> getCompletedTasks(){
        return repo.findTasksByStatus(TaskStatus.COMPLETED);
    }

    public synchronized List<Task> getPendingTasks(){
        return repo.findTasksByStatus(TaskStatus.PENDING);
    }

    public synchronized List<Task> getAbandonedTasks(){
        return repo.findTasksByStatus(TaskStatus.ABANDONED);
    }

    private boolean isValidDescription(String description){
        return description.matches("[\\p{L}\\p{N} :,.?!\\-\\[\\]{}\\\\/]+") && description.matches(".*[\\p{L}\\p{N}].*");
    }

}
