package todo;

import java.time.LocalDateTime;

public class Task {
    private final int id;
    private final String description;
    private TaskStatus status;
    private final LocalDateTime createdAt;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Task(int id, String description, String status, String time){
        this.id = id;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
        this.createdAt = LocalDateTime.parse(time);
    }

    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    public void complete() {
        if (this.status != TaskStatus.COMPLETED)
            this.status = TaskStatus.COMPLETED;
    }

    public int getId() {
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
