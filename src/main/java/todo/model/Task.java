package todo.model;

import java.time.LocalDateTime;

public class Task {
    private final Integer id;
    private final String description;
    private TaskStatus status;
    private final LocalDateTime createdAt;

    public TaskStatus getStatus() {
        return status;
    }

    private void setStatus(TaskStatus status){
        this.status = status;
    }

    public Task(String description) {
        this.id = null;
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Task(int id, String description, TaskStatus status, LocalDateTime createdAt){
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    public void complete() {
        if (this.status != TaskStatus.COMPLETED)
            this.status = TaskStatus.COMPLETED;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Task[%d: %s, completed=%s, created=%s]",
                id, description, status, createdAt);
    }

    public String toDisplay(){
        String string = status.getDisplayName();
        return String.format("№%d. %s, статус задачи: %s, создана: %s.%s.%s", id, description, string, createdAt.getDayOfMonth(), createdAt.getMonthValue(), createdAt.getYear());
    }
}
