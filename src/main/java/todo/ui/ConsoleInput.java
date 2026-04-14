package todo.ui;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine(){
        return scanner.nextLine();
    }

    public String readNonEmptyLine(){
       String line = scanner.nextLine();
       if(line == null || line.trim().isEmpty()){
           throw new ConsoleInputException("Строка не может быть null или пустой");
       }
       else{
           return line.trim();
       }
    }

    public int readInt(){
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                throw new ConsoleInputException("Введите корректное число", e);
            }
    }

    public void closeInput(){
        scanner.close();
    }
}
