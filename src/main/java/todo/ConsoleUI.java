package todo;

import todo.commands.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final TaskManager manager;
    private boolean exit = false;


    public ConsoleUI(TaskManager manager) {
        this.manager = manager;
        scanner = new Scanner(System.in);
    }

    public void start(){
        try{
            manager.loadTasks();
        }
        catch (StorageException e){
            System.out.println(e.getMessage());
        }
        while(!exit) {
            for(Menu value : Menu.values()){
                System.out.println(value.getCode() + ". " + value.getDisplayName());
            }
            Menu choice = Menu.getMenuObject(readInt());
            if(choice == null){
                System.out.println("Ошибка ввода. Введите повторно.");
                continue;
            }
            switch (choice) {
                case EXIT:
                    scanner.close();
                    try{
                        exit = new Exit(manager).execute();

                    }
                    catch (StorageException e){
                        System.out.println("Ошибка при выходе. Повторите");
                    }
                    break;
                case DISPLAY_TASKS:
                    List<Task> tasks;
                    tasks = new DisplayTasks(manager).execute();
                    if (tasks.isEmpty()) {
                        System.out.println("Список задач пуст.");
                    } else {
                        for (Task task : tasks) {
                            System.out.println(task.toDisplay());
                        }
                    }
                    break;
                case ADD_TASK:
                    System.out.println("Введите описание задачи: ");
                    String description = scanner.nextLine();
                    AddTask addTask = new AddTask(manager, description);
                    try{
                        System.out.println("Добавлена задача: " + addTask.execute().toDisplay());
                    }
                    catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case COMPLETE_TASK:
                    System.out.println("Введите id задачи: ");
                    int id = readInt();
                    CompleteTask completeTask = new CompleteTask(manager, id);
                    if (completeTask.execute()) {
                        System.out.println("Задача под номером " + id + " выполнена.");
                    } else {
                        System.out.println("Задача под номером " + id + " не найдена.");
                    }
                    break;
                case DELETE_TASK:
                    System.out.println("Введите id задачи: ");
                    int idToDelete = readInt();
                    DeleteTask deleteTask = new DeleteTask(manager, idToDelete);
                    if (deleteTask.execute()) {
                        System.out.println("Задача под номером " + idToDelete + " удалена");
                    } else {
                        System.out.println("Такой задачи нет.");
                    }
                    break;
                case DISPLAY_COMPLETED_TASKS:
                    DisplayCompleteTasks displayCompleteTasks = new DisplayCompleteTasks(manager);
                    List<Task> completedTasks = displayCompleteTasks.execute();
                    if (completedTasks.isEmpty()) {
                        System.out.println("Нет выполненных задач.");
                    } else {
                        for (Task task : completedTasks) {
                            System.out.println(task.toDisplay());
                        }
                    }
                    break;
                case DISPLAY_NOT_COMPLETED_TASKS:
                    DisplayNotCompletedTasks displayNotCompletedTasks = new DisplayNotCompletedTasks(manager);
                    List<Task> notCompletedTasks = displayNotCompletedTasks.execute();
                    if (notCompletedTasks.isEmpty()) {
                        System.out.println("Нет невыполненных задач.");
                    } else {
                        for (Task task : notCompletedTasks) {
                            System.out.println(task.toDisplay());
                        }
                    }
                    break;
                default:
            }
        }
    }

    public int readInt(){
        while(true){
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }

}
