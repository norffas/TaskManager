package todo.ui;

public class ConsoleInputException extends RuntimeException{
    public ConsoleInputException(String message, Throwable cause){
        super(message, cause);
    }
    public ConsoleInputException(String message){
        super(message);
    }
}
