package todo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final TaskManager manager;


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
        while(true) {
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
                        manager.saveTasks();
                    }
                    catch (StorageException e){
                        System.out.println(e.getMessage());
                    }
                    return;
                case DISPLAY_TASKS:
                    List<Task> tasks;
                    tasks = manager.getAllTasks();
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
                    try{
                        System.out.println("Добавлена задача: " + manager.addTask(description).toDisplay());
                    }
                    catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case COMPLETE_TASK:
                    System.out.println("Введите id задачи: ");
                    int id = readInt();
                    if (manager.completeTask(id)) {
                        System.out.println("Задача " + id + " выполнена.");
                    } else {
                        System.out.println("Задачи под номером " + id + " не найдено.");
                    }
                    break;
                case DELETE_TASK:
                    System.out.println("Введите id задачи: ");
                    int idToDelete = readInt();
                    if (manager.deleteTask(idToDelete)) {
                        System.out.println("Задача " + idToDelete + " удалена");
                    } else {
                        System.out.println("Такой задачи нет.");
                    }
                    break;
                case DISPLAY_COMPLETED_TASKS:
                    List<Task> completedTasks = manager.getCompletedTasks();
                    if (completedTasks.isEmpty()) {
                        System.out.println("Нет выполненных задач.");
                    } else {
                        for (Task task : completedTasks) {
                            System.out.println(task.toDisplay());
                        }
                    }
                    break;
                case DISPLAY_NOT_COMPLETED_TASKS:
                    List<Task> pendingTasks = manager.getPendingTasks();
                    if (pendingTasks.isEmpty()) {
                        System.out.println("Нет невыполненных задач.");
                    } else {
                        for (Task task : pendingTasks) {
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
