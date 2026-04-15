package todo.ui;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;
    private final ConsoleOutput output;

    public ConsoleInput(ConsoleOutput output) {
        this.scanner = new Scanner(System.in);
        this.output = output;
    }

    public String readNonEmptyLine(){
        while(true){
            String line = scanner.nextLine();
            if(line == null || line.trim().isEmpty()){
                output.printError("Строка не может быть null или пустой");
            }
            else{
                return line.trim();
            }
        }
    }

    public int readInt(){
        while(true){
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                output.printError("Введите корректное число");
            }
        }
    }

    public void closeInput(){
        scanner.close();
    }
}
