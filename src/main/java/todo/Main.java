package todo;


import todo.storage.database.DatabaseStorage;
import todo.service.TaskService;
import todo.ui.ConsoleInput;
import todo.ui.ConsoleOutput;
import todo.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        DatabaseStorage storage = new DatabaseStorage();
        TaskService manager = new TaskService(storage);
        ConsoleOutput output = new ConsoleOutput();
        ConsoleInput input = new ConsoleInput();
        UserInterface userInterface = new UserInterface(manager, output, input);
        userInterface.start();
    }
}
