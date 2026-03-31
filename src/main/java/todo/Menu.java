package todo;

import java.util.List;

public enum Menu {
    DISPLAY_TASKS(1) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            List<Task> tasks;
            tasks = manager.getAllTasks();
            if(tasks.isEmpty()){
                System.out.println("Список задач пуст.");
            }
            else {
                for (Task task : tasks) {
                    System.out.println(task.toDisplay());
                }
            }
        }
    },
    ADD_TASK(2) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
           /* System.out.println("Введите описание задачи: ");
            String description;
            description = console.nextLine().trim();
            if(!description.isEmpty()){
                System.out.println("Добавлена задача: " + manager.addTask(description).toDisplay());
            }
            else{
                System.out.println("Добавьте описание задачи.");
            } */
        }
    },
    COMPLETE_TASK(3) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            System.out.println("Введите id задачи: ");
            int id = console.readInt();
            if(manager.completeTask(id)){
                System.out.println("Задача " + id + " выполнена.");
            }
            else{
                System.out.println("Задачи под номером " + id + " не найдено.");
            }
        }
    },
    DELETE_TASK(4) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            System.out.println("Введите id задачи: ");
            int idToDelete = console.readInt();
            if(manager.deleteTask(idToDelete))
            {
                System.out.println("Задача " + idToDelete + " удалена");
            }
            else{
                System.out.println("Такой задачи нет.");
            }
        }
    },
    DISPLAY_COMPLETED_TASKS(5) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            List<Task> completedTasks = manager.getCompletedTasks();
            if(completedTasks.isEmpty()){
                System.out.println("Нет выполненных задач.");
            }
            else {
                for (Task task : completedTasks) {
                    System.out.println(task.toDisplay());
                }
            }
        }
    },
    DISPLAY_NOT_COMPLETED_TASKS(6) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            List<Task> pendingTasks = manager.getPendingTasks();
            if(pendingTasks.isEmpty()){
                System.out.println("Нет невыполненных задач.");
            }
            else {
                for (Task task : pendingTasks) {
                    System.out.println(task.toDisplay());
                }
            }
        }
    },
    EXIT(0) {
        @Override
        void menuAction(TaskManager manager, ConsoleUI console) {
            ConsoleUI.exit = true;
        }
    };

    Menu(int code) {
        this.CODE = code;
    }

    private final int CODE;

    abstract void menuAction(TaskManager manager, ConsoleUI console);

    public static void getName(int i, TaskManager manager, ConsoleUI console){
        boolean flag = false;
        for(Menu value : Menu.values()){
            if(value.CODE == i){
                value.menuAction(manager, console);
                flag = true;
                break;
            }
        }
        if(!flag)
            System.out.println("Введите корректное значение");
    }
}
