package todo;


public class Main {
    public static void main(String[] args) {
        FileStorage storage = new FileStorage();
        TaskManager manager = new TaskManager(storage);
        ConsoleUI console = new ConsoleUI(manager);
        console.start();
    }
}
