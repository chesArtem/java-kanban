package History;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.History.HistoryManager;
import service.History.InMemoryHistoryManager;
import service.Task.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    @Test
    public void getTaskTest() {
        Task task1 = inMemoryTaskManager.createTask("title", "info");
        Task task2 = inMemoryTaskManager.createTask("title", "info");
        task1 = inMemoryTaskManager.getTaskById(task1.getId());
        task2 = inMemoryTaskManager.getTaskById(task2.getId());
        task1 = inMemoryTaskManager.getTaskById(task1.getId());
        var result = inMemoryTaskManager.getHistory();
        assertNotNull(task1);
        assertNotNull(task2);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0), task2);
        assertEquals(result.get(1), task1);
    }

    @Test
    public void getEpicTest() {
        Epic task1 = inMemoryTaskManager.createEpic("title", "info");
        Epic task2 = inMemoryTaskManager.createEpic("title", "info");
        task1 = inMemoryTaskManager.getEpicById(task1.getId());
        task2 = inMemoryTaskManager.getEpicById(task2.getId());
        task1 = inMemoryTaskManager.getEpicById(task1.getId());
        var result = inMemoryTaskManager.getHistory();
        assertNotNull(task1);
        assertNotNull(task2);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0), task2);
        assertEquals(result.get(1), task1);
    }

    @Test
    public void getSubtaskTest() {
        Epic task1 = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask1 = inMemoryTaskManager.createSubtask("title", "info", task1);
        Subtask subtask2 = inMemoryTaskManager.createSubtask("title", "info", task1);
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1.getId());
        subtask2 = inMemoryTaskManager.getSubtaskById(subtask2.getId());
        subtask1 = inMemoryTaskManager.getSubtaskById(subtask1.getId());
        task1 = inMemoryTaskManager.getEpicById(task1.getId());
        var result = inMemoryTaskManager.getHistory();
        assertNotNull(task1);
        assertNotNull(subtask1);
        assertNotNull(subtask2);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(result.get(0), subtask2);
        assertEquals(result.get(1), subtask1);
        assertEquals(result.get(2), task1);
    }

    @Test
    public void getHistoryTest() {
        Epic task1 = inMemoryTaskManager.createEpic("title", "info");
        var history = inMemoryTaskManager.getHistory();
        assertEquals(0, history.size());
        task1 = inMemoryTaskManager.getEpicById(task1.getId());
        history = inMemoryTaskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(history.get(0), task1);
        inMemoryTaskManager.deleteEpicById(task1.getId());
        history = inMemoryTaskManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    public void deleteTaskNullTest() {
        assertThrows(IllegalArgumentException.class, () -> inMemoryTaskManager.deleteTaskById(null));
    }

    @Test
    public void deeleteTaskTest() {
        inMemoryTaskManager.deleteTaskById(0);
    }
}