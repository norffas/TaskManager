package todo.commands;

import todo.manager.TaskManager;
import todo.storage.StorageException;

public class Exit implements Command {
    private final TaskManager manager;

    public Exit(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public CommandResult execute() {
        try {
            manager.saveTasks();
            return new CommandResult("Завершение выполнения программы. Все изменения сохранены.", true);
        }
        catch (StorageException e){
            return new CommandResult("Ошибка при сохранении изменений. Попробуйте еще раз", false);
        }
    }
}
