package todo.ui;

public enum Menu {
    DISPLAY_TASKS(1, "Показать все задачи"),
    ADD_TASK(2, "Добавить задачу"),
    COMPLETE_TASK(3, "Отметить задачу выполненной"),
    DELETE_TASK(4, "Удалить задачу"),
    DISPLAY_COMPLETED_TASKS(5, "Показать выполненные задачи"),
    DISPLAY_NOT_COMPLETED_TASKS(6, "Показать невыполненные задачи"),
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
