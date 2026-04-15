package todo.commands;

import todo.ui.Menu;
import todo.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandCreator {

    public List<Parameters> getParameters(Menu menu){
        List<Parameters> params = new ArrayList<>();
        switch (menu){
            case ADD_TASK:
                params.add(Parameters.TASK_DESCRIPTION);
                break;
            case COMPLETE_TASK:
                params.add(Parameters.TASK_ID);
                break;
            case DELETE_TASK:
                params.add(Parameters.TASK_ID);
                break;
            case DISPLAY_TASKS:
            case DISPLAY_COMPLETED_TASKS:
            case DISPLAY_NOT_COMPLETED_TASKS:
            case EXIT:
                break;
            }
        return params;
    }

    public Command createCommand(Menu menu, TaskManager manager, Map<Parameters, Object> params){
        switch (menu){
            case ADD_TASK:
                return new AddTask(manager, (String) params.get(Parameters.TASK_DESCRIPTION));
            case COMPLETE_TASK:
                return new CompleteTask(manager, (Integer) params.get(Parameters.TASK_ID));
            case DELETE_TASK:
                return new DeleteTask(manager, (Integer) params.get(Parameters.TASK_ID));
            case DISPLAY_TASKS:
                return new DisplayTasks(manager, DisplayTasksFilter.ALL_TASKS);
            case DISPLAY_COMPLETED_TASKS:
                return new DisplayTasks(manager, DisplayTasksFilter.COMPLETED_TASKS);
            case DISPLAY_NOT_COMPLETED_TASKS:
                return new DisplayTasks(manager, DisplayTasksFilter.NOT_COMPLETED_TASKS);
            case EXIT:
                return new Exit(manager);
            default:
                throw new IllegalArgumentException("Такой команды для создания нет.");
        }
    }
}
