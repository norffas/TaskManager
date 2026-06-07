package todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import todo.repository.Repository;
import todo.service.OperationStatus;
import todo.service.TaskServiceOperationResult;
import todo.service.TaskService;
import todo.model.Task;
import todo.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest { //
    private TaskService service;
    Repository repo;

    @BeforeEach
    void setUp(){
        repo = mock(Repository.class);
        service = new TaskService(repo);
    }


    @Test
    public void shouldUpdateAbandonedStatus(){
        service.updateAbandonedStatus();
        verify(repo).statusAutoUpdate(any());
    }

    @Test
    public void addTaskTest(){
        Task task = new Task(1, "qwerty", TaskStatus.PENDING, LocalDateTime.now());
        when(repo.saveTask(any())).thenReturn(task);
        TaskServiceOperationResult result = service.addTask("qwerty");
        assertEquals(OperationStatus.ADDED, result.getStatus());
        assertEquals(task, result.getTask());
        verify(repo).saveTask(any());
    }

    @Test
    void addTaskWithEmptyDescription(){
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, service.addTask(" ").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, service.addTask("").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_EMPTY_DESCRIPTION, service.addTask(null).getStatus());
        verifyNoInteractions(repo);
    }

    @Test
    void addTaskWithNotValidDescription(){
        assertEquals(OperationStatus.NOT_ADDED_INVALID_DESCRIPTION, service.addTask("S\nS").getStatus());
        assertEquals(OperationStatus.NOT_ADDED_INVALID_DESCRIPTION, service.addTask(":").getStatus());
        verifyNoInteractions(repo);
    }

    @Test
    void addTaskWithNotValidDescriptionCheckNumberTasks(){
        service.addTask("  S      \n  S   \n");
        service.addTask(":");
        verifyNoInteractions(repo);
    }

    @Test
    void addTaskShouldTrimSpaces(){
        service.addTask("         S     ");
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repo).saveTask(captor.capture());
        Task task = captor.getValue();
        assertEquals("S", task.getDescription());
    }

    @Test
    void getAllTasksTest(){
        when(repo.findAll()).thenReturn(List.of());
        List<Task> list = service.getAllTasks();
        assertEquals(0, list.size());
        verify(repo).findAll();
    }

    @Test
    void completeTaskTest(){
        assertEquals(OperationStatus.NOT_FOUND, service.completeTask(1).getStatus());
        when(repo.findTaskById(anyInt())).thenReturn(new Task(1, "asd", TaskStatus.COMPLETED, LocalDateTime.now()));
        assertEquals(OperationStatus.ALREADY_COMPLETED, service.completeTask(1).getStatus());
        when(repo.findTaskById(anyInt())).thenReturn(new Task(1, "asd", TaskStatus.PENDING, LocalDateTime.now()));
        assertEquals(OperationStatus.COMPLETED_NOW, service.completeTask(1).getStatus());
    }

    @Test
    void deleteTaskTest(){
        when(repo.deleteTaskById(anyInt())).thenReturn(new Task(1, "aqwerty", TaskStatus.PENDING, LocalDateTime.now()));
        assertEquals(OperationStatus.DELETED_NOW, service.deleteTask(1).getStatus());
        when(repo.deleteTaskById(anyInt())).thenReturn(null);
        assertEquals(OperationStatus.NOT_FOUND, service.deleteTask(1).getStatus());
    }

    @Test
    void getCompletedTasksTest(){
        service.getCompletedTasks();
        verify(repo).findTasksByStatus(TaskStatus.COMPLETED);
    }

    @Test
    void getPendingTasksTest(){
        service.getPendingTasks();
        verify(repo).findTasksByStatus(TaskStatus.PENDING);
    }
}



