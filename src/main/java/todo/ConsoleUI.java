package todo;

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
        while(true){
            int n = 0;
            System.out.println(showMenu());
            n = readInt();
            switch (n) {
                case 0:
                    return;
                case 1:
                    List<Task> tasks;
                    tasks = manager.getAllTasks();
                    if(tasks.isEmpty()){
                        System.out.println("Список задач пуст.");
                    }
                    else {
                        for (Task task : tasks) {
                            System.out.println(task.toDisplayString());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Введите описание задачи: ");
                    String description;
                    description = scanner.nextLine().trim();
                    if(!description.isEmpty()){
                        System.out.println("Добавлена задача: " + manager.addTask(description).toDisplayString());
                    }
                    else{
                        System.out.println("Добавьте описание задачи.");
                    }
                    break;
                case 3:
                    System.out.println("Введите id задачи: ");
                    int id = readInt();
                    if(manager.completeTask(id)){
                        System.out.println("Задача " + id + " выполнена.");
                    }
                    else{
                        System.out.println("Задачи под номером " + id + " не найдено.");
                    }
                    break;
                case 4:
                    System.out.println("Введите id задачи: ");
                    int idToDelete = readInt();
                    if(manager.deleteTask(idToDelete))
                    {
                        System.out.println("Задача " + idToDelete + " удалена");
                    }
                    else{
                        System.out.println("Такой задачи нет.");
                    }
                    break;
                case 5:
                    List<Task> completedTasks = manager.getCompletedTasks();
                    if(completedTasks.isEmpty()){
                        System.out.println("Нет выполненных задач.");
                    }
                    else {
                        for (Task task : completedTasks) {
                            System.out.println(task.toDisplayString());
                        }
                    }
                    break;
                case 6:
                    List<Task> pendingTasks = manager.getPendingTasks();
                    if(pendingTasks.isEmpty()){
                        System.out.println("Нет невыполненных задач.");
                    }
                    else {
                        for (Task task : pendingTasks) {
                            System.out.println(task.toDisplayString());
                        }
                    }
                    break;
                default:
                    System.out.println("Ошибка ввода. Введите повторно.");
            }
        }
    }

    private String showMenu() {
        return """
        1. Показать все задачи
        2. Добавить задачу
        3. Отметить задачу как выполненную
        4. Удалить задачу
        5. Показать выполненные задачи
        6. Показать невыполненные задачи
        0. Выход
        Ваш выбор:""";
    }

    private int readInt(){
        while(true){
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }

}
