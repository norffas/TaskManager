package todo.manager;

import todo.model.Task;
import todo.model.TaskStatus;
import todo.storage.Storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks;
    private int nextId;
    private final Storage storage;
    private boolean needSave = false;

    public TaskManager(Storage storage) {
        this.storage = storage;
        this.nextId = 1;
        this.tasks = new ArrayList<>();
    }

    public void loadTasks() {
        tasks.clear();
        tasks.addAll(storage.load());
        calculateNextId();
        needSave = updateAbandonedStatus();
    }

    private void calculateNextId(){
        int maxId = 0;
        for(Task task : tasks){
            int tempId = task.getId();
            if(tempId > maxId)
                maxId = tempId;
        }
        this.nextId = maxId+1;
    }

    private boolean updateAbandonedStatus() {
        boolean hasUpdates = false;
        for (Task task : tasks) {
            if (task.getStatus() == TaskStatus.PENDING && task.getCreatedAt().isBefore(LocalDateTime.now().minusDays(20))) {
                task.setStatus(TaskStatus.ABANDONED);
                hasUpdates = true;
            }
        }
        return hasUpdates;
    }

    public TaskManagerOperationResult addTask(String description) {
        if(description == null || description.trim().isEmpty()){
            return new TaskManagerOperationResult(OperationStatus.NOT_ADDED, null);
        }
        Task task = new Task(nextId, description.trim());
        tasks.add(task);
        needSave = true;
        nextId++;
        return new TaskManagerOperationResult(OperationStatus.ADDED, task);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task findTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public TaskManagerOperationResult completeTask(int id){
        Task task = findTaskById(id);
        if(task == null)
            return new TaskManagerOperationResult(OperationStatus.NOT_FOUND, task);
        else if(task.isCompleted())
            return new TaskManagerOperationResult(OperationStatus.ALREADY_COMPLETED, task);
        task.complete();
        needSave = true;
        return new TaskManagerOperationResult(OperationStatus.COMPLETED_NOW, task);
    }

    public TaskManagerOperationResult deleteTask(int id){
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
           Task task = iterator.next();
            if (task.getId() == id) {
                    iterator.remove();
                    needSave = true;
                    return new TaskManagerOperationResult(OperationStatus.DELETED_NOW, task);
            }
        }
        return new TaskManagerOperationResult(OperationStatus.NOT_FOUND, null);
    }

    public List<Task> getCompletedTasks(){
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted()){
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public List<Task> getPendingTasks(){
        List<Task> pendingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == TaskStatus.PENDING){
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    public List<Task> getAbandonedTasks(){
        List<Task> abandonedTasks = new ArrayList<>();
            for(Task task : tasks){
                if(task.getStatus() == TaskStatus.ABANDONED)
                    abandonedTasks.add(task);
            }
            return abandonedTasks;
    }

    public void saveTasks(){
        if(needSave){
            storage.save(tasks);
            needSave = false;
        }
    }

}
