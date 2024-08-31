package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Task.InMemoryTaskManager;
import service.Task.TaskStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void getTaskTest() throws IOException {
        Task initial = inMemoryTaskManager.createTask("title", "info");
        Task task1 = inMemoryTaskManager.getTaskById(initial.getId());
        Task task2 = inMemoryTaskManager.getTaskById(initial.getId());
        assertNotNull(task1);
        assertNotNull(task2);
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1, task2);
    }

    @Test
    public void getEpicTest() throws IOException {
        Epic initial = inMemoryTaskManager.createEpic("title", "info");
        Epic task1 = inMemoryTaskManager.getEpicById(initial.getId());
        Epic task2 = inMemoryTaskManager.getEpicById(initial.getId());
        assertNotNull(task1);
        assertNotNull(task2);
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1, task2);
    }

    @Test
    public void getSubtaskTest() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Task initial = inMemoryTaskManager.createSubtask("title", "info", epic);
        Task task1 = inMemoryTaskManager.getSubtaskById(initial.getId());
        Task task2 = inMemoryTaskManager.getSubtaskById(initial.getId());
        assertNotNull(task1);
        assertNotNull(task2);
        assertEquals(task1.getId(), task2.getId());
        assertEquals(task1, task2);
    }

    @Test
    public void taskTest() throws IOException {
        Task initial = inMemoryTaskManager.createTask("title", "info");
        Task task1 = inMemoryTaskManager.getTaskById(initial.getId());
        task1 = inMemoryTaskManager.updateTask(task1.getId(), null, "new Info", TaskStatus.IN_PROGRESS,
                null, null);
        Task task2 = inMemoryTaskManager.getTaskById(initial.getId());
        assertNotNull(task1);
        assertEquals(task1.getId(), initial.getId());
        assertEquals(task1, task2);
    }

    @Test
    public void updateTaskTest() throws IOException {
        Task initial = inMemoryTaskManager.createTask("title", "info");
        Task task1 = inMemoryTaskManager.getTaskById(initial.getId());
        task1 = inMemoryTaskManager.updateTask(task1.getId(), "NewTitle", "NewInfo",
                TaskStatus.IN_PROGRESS, null, null);
        Task task2 = inMemoryTaskManager.getTaskById(task1.getId());
        assertNotNull(task1);
        assertNotNull(task2);
        assertEquals(task1.getId(), task2.getId());
        assertEquals("NewInfo", task2.getInfo());
        assertEquals("NewTitle", task2.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, task2.getStatus());
        assertEquals(task1, task2);
    }

    @Test
    public void updateEpicTest() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Epic epic1 = inMemoryTaskManager.getEpicById(epic.getId());
        epic1 = inMemoryTaskManager.updateEpic(epic1.getId(), "NewTitle", "NewInfo");
        Epic epic2 = inMemoryTaskManager.getEpicById(epic1.getId());
        assertNotNull(epic1);
        assertNotNull(epic2);
        assertEquals(epic1.getId(), epic2.getId());
        assertEquals("NewInfo", epic2.getInfo());
        assertEquals("NewTitle", epic2.getTitle());
        assertEquals(epic1, epic2);
    }

    @Test
    public void updateSubtaskTest() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask = inMemoryTaskManager.createSubtask("title", "info", epic);
        Subtask subtask1 = inMemoryTaskManager.getSubtaskById(subtask.getId());
        subtask1 = inMemoryTaskManager.updateSubtask(subtask1.getId(),  "NewTitle", "NewInfo",
                TaskStatus.IN_PROGRESS, null, null);
        Subtask subtask2 = inMemoryTaskManager.getSubtaskById(subtask1.getId());
        Epic epic2 = inMemoryTaskManager.getEpicById(epic.getId());
        assertNotNull(subtask2);
        assertNotNull(epic2);
        assertEquals(epic.getId(), epic2.getId());
        assertEquals(subtask.getId(), subtask2.getId());
        assertEquals("NewInfo", subtask2.getInfo());
        assertEquals("NewTitle", subtask2.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, subtask2.getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, epic2.getStatus());
        assertEquals(1, epic2.getListSubtask().size());
        assertTrue(epic2.getListSubtask().contains(subtask2));
    }
}