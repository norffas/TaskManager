package todo.ui;

import todo.model.Task;

import java.util.List;

public interface Output {
    void printMenu();
    void printError(String message);
    void prompt(String string);
    void printMessage(String message);
    void printTask(Task task);
    void printTasks(List<Task> tasks);

}
