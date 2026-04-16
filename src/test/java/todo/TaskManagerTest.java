package todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.manager.OperationStatus;
import todo.manager.TaskManager;
import todo.manager.TaskManagerOperationResult;
import todo.model.Task;
import todo.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest { //
    private TaskManager manager;

    @BeforeEach
    void setUp(){
        manager = new TaskManager(new StorageForTests());
    }


    @Test
    public void addTaskTest(){
        Task task = manager.addTask("asdasd").getTask();
        assertNotNull(task);
        assertEquals(OperationStatus.NOT_ADDED, manager.addTask("").getStatus());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals(OperationStatus.NOT_ADDED, manager.addTask(" ").getStatus());
        assertEquals(OperationStatus.NOT_ADDED, manager.addTask("\n").getStatus());
        assertEquals(OperationStatus.NOT_ADDED, manager.addTask(null).getStatus());
    }

    @Test
    void addTaskWithEmptyDescription(){
        assertEquals(0, manager.getAllTasks().size());
        assertDoesNotThrow( () -> {
            manager.addTask(" ");
        });
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void addTaskShouldTrimSpaces(){
        manager.addTask("         S     ");
        assertEquals("S", manager.findTaskById(1).getDescription());
        manager.addTask(" \n E \n ");
        assertEquals("E", manager.findTaskById(2).getDescription());
        manager.addTask(" S \n S ");
        assertEquals("S \n S", manager.findTaskById(3).getDescription());
    }

    @Test
    void getAllTasksTest(){
        assertNotNull(manager.getAllTasks());
        assertEquals(0, manager.getAllTasks().size());
        assertInstanceOf(List.class, manager.getAllTasks());
        manager.addTask("asdasd");
        assertEquals(1, manager.getAllTasks().size());
        List<Task> copyTasks = manager.getAllTasks();
        copyTasks.add(new Task(33, "asd"));
        assertEquals(1, manager.getAllTasks().size());
        copyTasks.clear();
        assertEquals(1, manager.getAllTasks().size());
    }

    private void addTasks(){
        manager.addTask("aaaaaaaaa");
        manager.addTask("qweqwe");
    }

    @Test
    void completeTaskTest(){
        addTasks();
        assertEquals(2, manager.getPendingTasks().size());
        assertEquals(0, manager.getCompletedTasks().size());
        assertEquals(OperationStatus.COMPLETED_NOW, manager.completeTask(1).getStatus());
        assertEquals(1, manager.getPendingTasks().size());
        assertEquals(1, manager.getCompletedTasks().size());
        assertEquals(OperationStatus.NOT_FOUND, manager.completeTask(44).getStatus());
        assertEquals(OperationStatus.ALREADY_COMPLETED, manager.completeTask(1).getStatus());
        TaskManagerOperationResult result = manager.completeTask(2);
        assertEquals( OperationStatus.COMPLETED_NOW, result.getStatus());
    }

    @Test
    void deleteTaskTest(){
        addTasks();
        assertEquals(2, manager.getAllTasks().size());
        assertEquals(OperationStatus.DELETED_NOW, manager.deleteTask(1).getStatus());
        assertEquals(1, manager.getAllTasks().size());
        assertNull(manager.deleteTask(15).getTask());
        assertEquals(OperationStatus.NOT_FOUND, manager.deleteTask(1).getStatus());
    }

    @Test
    void getCompletedTasksTest(){
        addTasks();
        manager.completeTask(1);
        assertEquals(1, manager.getCompletedTasks().size());
        manager.deleteTask(1);
        assertEquals(0, manager.getCompletedTasks().size());
    }

    @Test
    void getPendingTasksTest(){
        addTasks();
        assertEquals(2, manager.getPendingTasks().size());
        manager.deleteTask(1);
        assertEquals(1, manager.getPendingTasks().size());
        manager.completeTask(2);
        assertEquals(0, manager.getPendingTasks().size());
    }
}



