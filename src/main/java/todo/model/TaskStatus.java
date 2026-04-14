package todo.model;

public enum TaskStatus {
    PENDING("в процессе"),
    COMPLETED("выполнена"),
    ABANDONED("заброшена");

    private final String DISPLAY_NAME;

    TaskStatus(String displayName) {
        this.DISPLAY_NAME = displayName;
    }

    public String getDisplayName(){
        return DISPLAY_NAME;
    }
}
