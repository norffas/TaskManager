package todo;


import todo.repository.DatabaseFactory;
import todo.repository.Repository;
import todo.repository.DatabaseRepo;
import todo.service.TaskService;
import todo.ui.ConsoleInput;
import todo.ui.ConsoleOutput;
import todo.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        Repository repo = new DatabaseRepo();
        DatabaseFactory db = new DatabaseFactory();
        db.createTable();
        TaskService service = new TaskService(repo);
        ConsoleOutput output = new ConsoleOutput();
        ConsoleInput input = new ConsoleInput();
        UserInterface userInterface = new UserInterface(service, output, input);
        userInterface.start();
    }
}
