package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Task.FileBackedTaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest  {

    private FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

    @Test
    public void saveTest() throws IOException {
        Task task = fileBackedTaskManager.createTask("task1", "info");
        Epic epic = fileBackedTaskManager.createEpic("epic1", "info");
        Subtask subtask = fileBackedTaskManager.createSubtask("subtask1 epic1", "info", epic);
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
    }
}