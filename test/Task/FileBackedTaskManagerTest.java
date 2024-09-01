package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Task.FileBackedTaskManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest  {

    private FileBackedTaskManager fileBackedTaskManager;



    @Test
    public void saveTest() throws IOException {
        Files.delete(Path.of("testSaveTime.txt"));
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
    public void readFileTest() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager("testSaveTime.txt");
        Task task = fileBackedTaskManager.getTaskById(1);
        Epic epic = fileBackedTaskManager.getEpicById(3);
        Subtask subtask = fileBackedTaskManager.getSubtaskById(4);
        subtask = fileBackedTaskManager.updateSubtask(subtask.getId(), null, null,
                null, null, LocalDateTime.now().minusDays(6));
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
        assertEquals(epic.getId(), subtask.getParentEpic().getId());
        assertEquals(2, epic.getListSubtask().size());
        assertTrue(epic.getListSubtask().stream().map(Subtask::getId).collect(Collectors.toList()).contains(subtask.getId()));
    }
}