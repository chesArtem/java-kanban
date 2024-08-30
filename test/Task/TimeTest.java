package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Task.InMemoryTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class TimeTest {
    private InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void taskTestTime() throws IOException {
        Task task = inMemoryTaskManager.createTask("title", "info", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")));
        assertNotNull(task);
        assertNotNull(task.getStartTime());
        assertNotNull(task.getDuration());
    }

    @Test
    public void taskTestTimeNull() throws IOException {
        Task task = inMemoryTaskManager.createTask("title", "info", null,
                LocalDateTime.now(ZoneId.of("UTC")));
        Task task2 = inMemoryTaskManager.createTask("title", "info", null,
                null);
        assertNotNull(task);
        assertNotNull(task.getStartTime());
        assertNull(task.getDuration());
        assertNull(task2.getStartTime());
        assertNull(task2.getDuration());
        task2 = task2.getUpdater().setDuration(Duration.ofMinutes(120)).updateTask();
        assertNotNull(task2.getDuration());
        assertNull(task2.getEndTime());
        task2 = task2.getUpdater().setStartTime( LocalDateTime.now(ZoneId.of("UTC"))).updateTask();
        assertNotNull(task2.getEndTime());
    }

    @Test
    public void subtaskTestTime() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask = inMemoryTaskManager.createSubtask("title", "info", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")), epic);
        assertNotNull(subtask);
        assertNotNull(subtask.getStartTime());
        assertNotNull(subtask.getDuration());
    }

    @Test
    public void epicTestTimeDuration() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask1 = inMemoryTaskManager.createSubtask("title", "info", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")), epic);
        Subtask subtask2 = inMemoryTaskManager.createSubtask("title", "info", Duration.ofMinutes(180),
                LocalDateTime.now(ZoneId.of("UTC")), epic);
        assertNotNull(epic);
        assertNotNull(subtask1);
        assertNotNull(subtask2);
        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getDuration());
        assertEquals(300, epic.getDuration().toMinutes());
    }

    @Test
    public void epicTestStartTimeAndDurationNull() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask1 = inMemoryTaskManager.createSubtask("title", "info", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")), epic);
        Subtask subtask2 = inMemoryTaskManager.createSubtask("title", "info", null, null, epic);
        Subtask subtask3 = inMemoryTaskManager.createSubtask("title", "info", null,
                LocalDateTime.now(ZoneId.of("UTC")), epic);
        Subtask subtask4 = inMemoryTaskManager.createSubtask("title", "info", Duration.ofMinutes(120),null , epic);
        assertNotNull(epic);
        assertNotNull(epic.getStartTime());
        assertNotNull(epic.getDuration());
        assertEquals(240, epic.getDuration().toMinutes());
    }

    @Test
    public void epicTestTimeNull() throws IOException {
        Epic epic = inMemoryTaskManager.createEpic("title", "info");
        Subtask subtask1 = inMemoryTaskManager.createSubtask("title", "info", null, null, epic);
        Subtask subtask2 = inMemoryTaskManager.createSubtask("title", "info", null, null, epic);
        assertNull(epic.getStartTime());
        assertEquals(Duration.ZERO, epic.getDuration());
        assertNull(epic.getEndTime());
        subtask1 = (Subtask) subtask1.getUpdater().setStartTime(LocalDateTime.now(ZoneId.of("UTC"))).updateTask();
        epic.addSubtask(subtask1);
        subtask2 = (Subtask) subtask2.getUpdater().setDuration(Duration.ofMinutes(120)).setStartTime(LocalDateTime.now(ZoneId.of("UTC"))).updateTask();
        epic.addSubtask(subtask2);
        assertNotNull(epic.getEndTime());
        subtask1 = (Subtask) subtask1.getUpdater().setDuration(Duration.ofMinutes(10)).updateTask();
        epic.addSubtask(subtask1);
        assertNotNull(epic.getEndTime());
    }
}