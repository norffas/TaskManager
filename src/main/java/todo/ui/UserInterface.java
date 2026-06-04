package todo.ui;

import todo.commands.CommandCreator;
import todo.commands.Parameters;
import todo.storage.StorageException;
import todo.commands.CommandResult;
import todo.service.TaskService;
import todo.model.Task;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class UserInterface {
    private final TaskService manager;
    private volatile boolean exit = false;
    private final Output output;
    private final Input input;

    public UserInterface(TaskService manager, Output output, Input input) {
        this.manager = manager;
        this.output = output;
        this.input = input;
    }

    public void start() {
        try {
            manager.loadTasks();
        } catch (StorageException e) {
            output.printError(e.getMessage());
        }
        Runnable autoSave = new Runnable() {
            @Override
            public void run() {
                while(!exit){
                    try {
                        Thread.sleep(50000);
                        manager.saveTasks();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        Thread saveThread = new Thread(autoSave);
        saveThread.start();
        CommandCreator commandCreator = new CommandCreator();
        while (true) {
            output.printMenu();
            Menu choice = Menu.getMenuObject(readIntSafely());
            if (choice == null) {
                output.printError("Ошибка ввода");
                continue;
            }
            List<Parameters> params = commandCreator.getParameters(choice);
            Map<Parameters, Object> parameters = new EnumMap<>(Parameters.class);
            for (Parameters param : params){
                switch (param){
                    case TASK_DESCRIPTION:
                        output.prompt("Введите описание задачи: ");
                        parameters.put(param, readNonEmptyLineSafely());
                        break;
                    case TASK_ID:
                        output.prompt("Введите номер задача: ");
                        parameters.put(param, readIntSafely());
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
            if(exit){
                input.closeInput();
                saveThread.interrupt();
                break;
            }
        }

    }

    int readIntSafely(){
        while(true){
            try {
                return input.readInt();
            }
            catch (InputException e){
                output.printError(e.getMessage());
            }
        }
    }

    String readNonEmptyLineSafely(){
        while(true){
            try{
                return input.readNonEmptyLine();
            }
            catch (InputException e){
                output.printError(e.getMessage());
            }
        }
    }

}
