package todo.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.ui.Menu;
import todo.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandCreator {
    private static final Logger logger = LoggerFactory.getLogger(CommandCreator.class);

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
            case DISPLAY_PENDING_TASKS:
            case DISPLAY_ABANDONED_TASKS:
            case EXIT:
                break;
        }
        logger.debug("Для пункта меню {} требуются следующие параметры: {}", menu, params);
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
            case DISPLAY_PENDING_TASKS:
                return new DisplayTasks(manager, DisplayTasksFilter.PENDING_TASKS);
            case DISPLAY_ABANDONED_TASKS:
                return new DisplayTasks(manager, DisplayTasksFilter.ABANDONED_TASKS);
            case EXIT:
                return new Exit(manager);
            default:
                logger.warn("Команды для пункта меню {} нет", menu);
                throw new IllegalArgumentException("Такой команды для создания нет.");
        }
    }
}
