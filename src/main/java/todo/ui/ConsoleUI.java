package todo.ui;

import todo.commands.CommandCreator;
import todo.commands.Parameters;
import todo.storage.StorageException;
import todo.commands.CommandResult;
import todo.manager.TaskManager;
import todo.model.Task;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ConsoleUI {
    private final TaskManager manager;
    private boolean exit = false;
    private final ConsoleOutput output = new ConsoleOutput();
    private final ConsoleInput input = new ConsoleInput(output);

    public ConsoleUI(TaskManager manager) {
        this.manager = manager;
    }

    public void start() {
        try {
            manager.loadTasks();
        } catch (StorageException e) {
            System.out.println(e.getMessage());
        }
        CommandCreator commandCreator = new CommandCreator();
        while (!exit) {
            output.printMenu();
            Menu choice = Menu.getMenuObject(input.readInt());
            if (choice == null) {
                output.printError("Ошибка ввода");
                continue;
            }
            List<Parameters> params = commandCreator.getParameters(choice);
            Map<Parameters, Object> parameters = new EnumMap<>(Parameters.class);
            for (Parameters param : params){
                switch (param){
                    case TASK_DESCRIPTION:
                        output.printTaskDescriptionInputMessage();
                        parameters.put(param, input.readNonEmptyLine());
                        break;
                    case TASK_ID:
                        output.printTaskIdInputMessage();
                        parameters.put(param, input.readInt());
                        break;
                    default:
                        output.printError("Введите корректную команду.");
                }
            }
            CommandResult result;
            try{
                result = commandCreator.createCommand(choice, manager, parameters).execute();
            }catch (IllegalArgumentException e){
                output.printError(e.getMessage());
                continue;
            }
            String message = result.getMessage();
            Task task = result.getTask();
            List<Task> tasks = result.getTasks();
            exit = result.isExit();
            output.printMessage(message);
            output.printTask(task);
            output.printTasks(tasks);
        }

    }

}
