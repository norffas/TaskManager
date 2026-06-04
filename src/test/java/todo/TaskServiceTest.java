package todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import todo.service.OperationStatus;
import todo.service.TaskService;
import todo.service.TaskManagerOperationResult;
import todo.model.Task;
import todo.model.TaskStatus;
import todo.storage.Storage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class TaskServiceTest { //
    private TaskService manager;

    @BeforeEach
    void setUp(){
        manager = new TaskService(new StorageForTests());
    }


    @Test
    public void shouldSaveTasks(){
        Storage storage = mock(Storage.class);
        TaskService manager = new TaskService(storage);
        manager.addTask("heh");
        manager.saveTasks();
        manager.saveTasks();
        verify(storage, times(1)).save(anyList());
    }

    @Test
    public void shouldNotSaveTasks(){
        Storage storage = mock(Storage.class);
        TaskService manager = new TaskService(storage);
        manager.getAllTasks();
        manager.getPendingTasks();
        manager.saveTasks();
        verify(storage, never()).save(anyList());
    }

    @Test
    public void shouldLoadTasks(){
        Storage storage = mock(Storage.class);
        TaskService manager = new TaskService(storage);
        manager.loadTasks();
        verify(storage).load();
    }

    @Test
    public void shouldUpdateAbandonedStatus(){
        Storage storage = mock(Storage.class);
        TaskService manager = new TaskService(storage);
        Task task = new Task(1, "hehe", TaskStatus.PENDING, LocalDateTime.now().minusMonths(1));
        when(storage.load()).thenReturn(List.of(task));
        manager.loadTasks();
        manager.saveTasks();
        assertEquals(task.getStatus(), TaskStatus.ABANDONED);
        verify(storage).save(anyList());
    }

    @Test
    public void addTaskTest(){
        Task task = manager.addTask("qwerty").getTask();
        assertNotNull(task);
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals(OperationStatus.ADDED, manager.addTask("qwe").getStatus());
    }

    @Test
    void addTaskWithEmptyDescription(){
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, manager.addTask(" ").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, manager.addTask("").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, manager.addTask(null).getStatus());
        assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void addTaskWithNotValidDescription(){
        assertEquals(OperationStatus.NOT_ADDED_INVALID_DESCRIPTION, manager.addTask("S\nS").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_INVALID_DESCRIPTION, manager.addTask(":").getStatus());
    }

    @Test
    void addTaskWithNotValidDescriptionCheckNumberTasks(){
        manager.addTask("  S      \n  S   \n");
        assertEquals(0, manager.getAllTasks().size());
        manager.addTask(":");
        Task task = manager.addTask("test").getTask();
        assertEquals(1, task.getId());
    }

    @Test
    void addTaskShouldTrimSpaces(){
        manager.addTask("         S     ");
        assertEquals("S", manager.findTaskById(1).getDescription());
        assertEquals(OperationStatus.ADDED, manager.addTask("S\n").getStatus());
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



