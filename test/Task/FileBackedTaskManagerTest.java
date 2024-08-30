package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Task.FileBackedTaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest  {

    private FileBackedTaskManager fileBackedTaskManager;

    @Test
    public void saveTest() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager("testSaveTime.txt");
        Task task = fileBackedTaskManager.createTask("task", "info", null, null);
        Task task1 = fileBackedTaskManager.createTask("task1", "info1", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")));
        Epic epic = fileBackedTaskManager.createEpic("epic", "info");
        Subtask subtask = fileBackedTaskManager.createSubtask("subtask epic", "info3",  Duration.ofMinutes(120), null, epic);
        Subtask subtask1 = fileBackedTaskManager.createSubtask("subtask1 epic", "info4",null, null, epic);
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
    }

    @Test
    public void readFileTast() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager("testSaveTime.txt");
        Task task = fileBackedTaskManager.getTaskById(1);
        Epic epic = fileBackedTaskManager.getEpicById(2);
        Subtask subtask = fileBackedTaskManager.getSubtaskById(3);
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
        assertEquals(epic.getId(), subtask.getParentEpic().getId());
        assertEquals(1, epic.getListSubtask().size());
        assertEquals(subtask.getId(), epic.getListSubtask().get(0).getId());
    }
}