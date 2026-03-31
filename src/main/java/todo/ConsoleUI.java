package todo;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final TaskManager manager;
    public static boolean exit = false;


    public ConsoleUI(TaskManager manager) {
        this.manager = manager;
        scanner = new Scanner(System.in);
    }

    public void start(){
        while(!exit){
            System.out.println(showMenu());
            Menu.getName(readInt(), manager, this);
        }
        scanner.close();
    }

    private String showMenu() {
        return """
        1. Показать все задачи
        2. Добавить задачу
        3. Изменить статус задачи
        4. Удалить задачу
        5. Показать выполненные задачи
        6. Показать невыполненные задачи
        0. Выход
        Ваш выбор:\s""";
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
