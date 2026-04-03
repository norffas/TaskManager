package todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks;
    private int nextId;
    private final FileStorage storage;
    private boolean needSave = false;

    public TaskManager(FileStorage storage) {
        this.storage = storage;
        this.nextId = 1;
        this.tasks = new ArrayList<>();
    }

    public void loadTasks() {
        tasks.clear();
        tasks.addAll(storage.load());
        int tempId = 0;
        for(Task task : tasks){
            if(task.getId() > tempId)
                tempId = task.getId();
            if(task.getStatus() == TaskStatus.PENDING){
                if(task.getCreatedAt().isBefore(LocalDateTime.now().minusDays(2))){
                    task.setStatus(TaskStatus.ABANDONED);
                }
            }
        }
        this.nextId = tempId + 1;
        needSave = false;
    }

    public Task addTask(String description) {
        if(description == null || description.trim().isEmpty()){
            throw new IllegalArgumentException("Описание не может быть пустым");
        }
        Task task = new Task(nextId, description.trim());
        tasks.add(task);
        needSave = true;
        nextId++;
        return task;
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

    public boolean completeTask(int id){
        Task task = findTaskById(id);
        if (task != null) {
            task.complete();
            needSave = true;
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id){
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                needSave = true;
                return true;
            }
        }
        return false;
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
            if (!task.isCompleted()){
                pendingTasks.add(task);
            }
        }
        return pendingTasks;
    }

    public void saveTasks(){
        if(needSave){
            storage.save(tasks);
            needSave = false;
        }
    }

}
