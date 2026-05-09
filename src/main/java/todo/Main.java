package todo;


import todo.manager.TaskManager;
import todo.storage.FileStorage;
import todo.ui.ConsoleInput;
import todo.ui.ConsoleOutput;
import todo.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        FileStorage storage = new FileStorage();
        TaskManager manager = new TaskManager(storage);
        ConsoleOutput output = new ConsoleOutput();
        ConsoleInput input = new ConsoleInput();
        UserInterface userInterface = new UserInterface(manager, output, input);
        userInterface.start();
    }
}
