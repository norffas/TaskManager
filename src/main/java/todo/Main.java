package todo;


import todo.manager.TaskManager;
import todo.storage.FileStorage;
import todo.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        FileStorage storage = new FileStorage();
        TaskManager manager = new TaskManager(storage);
    }
}
