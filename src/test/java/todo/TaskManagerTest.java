package todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp(){
        manager = new TaskManager();
    }


    @Test
    public void addTaskTest(){
        Task task = manager.addTask("asdasd");
        assertNotNull(task);
        assertEquals(task, manager.findTaskById(1));
        assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(" ");
        });
        assertThrows(IllegalArgumentException.class, () -> manager.addTask(null));
        assertThrows(IllegalArgumentException.class, () -> manager.addTask(" \n "));
    }

    @Test
    void addTaskTestWithInvalidSyntax(){
        assertEquals(0, manager.getAllTasks().size());
        assertThrows(IllegalArgumentException.class, () -> {
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

    void addTasks(){
        manager.addTask("aaaaaaaaa");
        manager.addTask("qweqwe");
    }

    @Test
    void findTaskByIdTest(){
        addTasks();
        assertEquals("aaaaaaaaa", manager.findTaskById(1).getDescription());
        assertEquals(1, manager.findTaskById(1).getId());
        assertFalse(manager.findTaskById(1).isCompleted());
        assertNull(manager.findTaskById(3));
        assertNull(manager.findTaskById(-1));
        assertNull(manager.findTaskById(0));
        manager.deleteTask(1);
        assertNull(manager.findTaskById(1));
    }

    @Test
    void completeTaskTest(){
        addTasks();
        assertEquals(2, manager.getPendingTasks().size());
        assertEquals(0, manager.getCompletedTasks().size());
        assertTrue(manager.completeTask(1));
        assertTrue(manager.findTaskById(1).isCompleted());
        assertEquals(1, manager.getPendingTasks().size());
        assertEquals(1, manager.getCompletedTasks().size());
        assertTrue(manager.completeTask(1));
        assertFalse(manager.completeTask(44));
    }

    @Test
    void deleteTaskTest(){
        addTasks();
        assertEquals(2, manager.getAllTasks().size());
        assertTrue(manager.deleteTask(1));
        assertEquals(1, manager.getAllTasks().size());
        assertNull(manager.findTaskById(1));
        assertFalse(manager.deleteTask(1));
        assertFalse(manager.deleteTask(1233));
    }

    @Test
    void getCompletedTasksTest(){
        addTasks();
        manager.completeTask(1);
        assertEquals(1, manager.getCompletedTasks().size());
        assertEquals(manager.findTaskById(1), manager.getCompletedTasks().get(0));
        manager.deleteTask(1);
        assertEquals(0, manager.getCompletedTasks().size());
    }

    @Test
    void getPendingTasksTest(){
        addTasks();
        assertEquals(2, manager.getPendingTasks().size());
        manager.deleteTask(1);
        assertEquals(1, manager.getPendingTasks().size());
        assertEquals("qweqwe", manager.findTaskById(2).getDescription());
        manager.findTaskById(2).complete();
        assertTrue(manager.findTaskById(2).isCompleted());
        assertEquals(0, manager.getPendingTasks().size());
    }
}



