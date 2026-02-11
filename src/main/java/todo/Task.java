package todo;

import java.time.LocalDateTime;

public class Task {
    private final int id;
    private final String description;
    private boolean completed;
    private final LocalDateTime createdAt;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        if (!this.completed) {
            this.completed = true;
        }
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
        return String.format("todo.Task[%d: %s, completed=%s, created=%s]",
                id, description, completed, createdAt);
    }

    public String toDisplayString() {
        String status = completed ? "[✓]" : "[ ]";
        return String.format("#%d %s %s", id, status, description);
    }
}
