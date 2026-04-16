package todo.ui;

public enum Menu {
    ADD_TASK(1, "Добавить задачу"),
    COMPLETE_TASK(2, "Отметить задачу выполненной"),
    DELETE_TASK(3, "Удалить задачу"),
    DISPLAY_TASKS(4, "Показать все задачи"),
    DISPLAY_COMPLETED_TASKS(5, "Показать выполненные задачи"),
    DISPLAY_PENDING_TASKS(6, "Показать невыполненные задачи"),
    DISPLAY_ABANDONED_TASKS(7, "Показать заброшенные задачи"),
    EXIT(0, "Выход");

    private final int code;
    private final String displayName;

    Menu(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode(){
        return code;
    }

    public String getDisplayName(){
        return displayName;
    }

    public static Menu getMenuObject(int code){
        for(Menu value : Menu.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        return null;
    }

}
