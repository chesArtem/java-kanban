package Task;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.task.FileBackedTaskManager;
import service.task.TaskStatus;
import utils.TestFileUtil;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestFileUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;

    @Test
    public void testTitle() throws IOException {
        addTask(1, "task1", "infoTask1", null, null, null);
        addTask(2, null, "infoTask2", TaskStatus.NEW, null, null);

        String path = createFile();
        Managers.initMemoryHistoryManager();
        assertThrows(IllegalArgumentException.class, () -> Managers.initFileTaskManager(path));
    }

    @Test
    public void testDuplicated() throws IOException {
        addTask(3, "task2", "infoTask2", TaskStatus.NEW, null, null);
        Epic epic = addEpic(4, "epic1", "infoEpic2");
        addSubtask(5, "subtask1", "infoSubtask1", TaskStatus.NEW, Duration.ofMinutes(120), null, epic);
        addSubtask(6, "subtask2", "infoSubtask2", TaskStatus.NEW, null, null, epic);
        addSubtask(6, "null", "infoSubtask2", TaskStatus.NEW, null, null, epic);

        String path = createFile();
        Managers.initMemoryHistoryManager();
        assertThrows(IllegalArgumentException.class, () -> Managers.initFileTaskManager(path));
    }

    @Test
    public void testEpicNotFile() throws IOException {
        Epic epicNotFile = Epic.builder().id(40).title("epic").info("epic is not in the file").build();
        addSubtask(7, "subtask2", "infoSubtask2", TaskStatus.NEW, null, null, epicNotFile);
        addSubtask(8, null, "infoSubtask2", TaskStatus.NEW, null, null, epicNotFile);

        String path = createFile();
        Managers.initMemoryHistoryManager();
        assertThrows(NullPointerException.class, () -> Managers.initFileTaskManager(path));
    }

    @Test
    public void saveTest() throws IOException {
        String path = createFile();
        Managers.initMemoryHistoryManager();
        Managers.initFileTaskManager("testSaveTime.txt");
        fileBackedTaskManager = (FileBackedTaskManager) Managers.getTaskManager();
        Task task = fileBackedTaskManager.createTask("task", "info", null, null);
        Task task1 = fileBackedTaskManager.createTask("task1", "info1", Duration.ofMinutes(120),
                LocalDateTime.now(ZoneId.of("UTC")));
        Epic epic = fileBackedTaskManager.createEpic("epic", "info");
        Subtask subtask = fileBackedTaskManager.createSubtask("subtask epic", "info3", Duration.ofMinutes(120), null, epic);
        Subtask subtask1 = fileBackedTaskManager.createSubtask("subtask1 epic", "info4", null, null, epic);
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
    }

    @Test
    public void readFileTest() throws IOException {
        TestFileUtil.resetFile();
        addTask(1, "taskTitle1", "taskInfo1", TaskStatus.NEW, null, null);
        addTask(2, "taskTitle2", "taskInfo2", TaskStatus.NEW, null, null);
        addTask(3, "taskTitle3", "taskInfo3", TaskStatus.NEW, null, null);
        Epic epic = addEpic(4, "epicTitle4", "epicInfo4");
        addSubtask(5, "subtaskTitle5", "subtaskInfo5", TaskStatus.NEW, null, null, epic);
        addSubtask(6, "subtaskTitle6", "subtaskInfo6", TaskStatus.NEW, null, null, epic);
        String path = createFile();
        Managers.initMemoryHistoryManager();
        Managers.initFileTaskManager(path);
        fileBackedTaskManager = (FileBackedTaskManager) Managers.getTaskManager();
        Task task = fileBackedTaskManager.getTaskById(1);
        epic = fileBackedTaskManager.getEpicById(4);
        Subtask subtask = fileBackedTaskManager.getSubtaskById(5);
        subtask = fileBackedTaskManager.updateSubtask(subtask.getId(), null, null,
                null, null, LocalDateTime.now().minusDays(6));
        assertNotNull(task);
        assertNotNull(epic);
        assertNotNull(subtask);
        assertEquals(epic.getId(), subtask.getParentEpic().getId());
        assertEquals(2, epic.getListSubtask().size());
        assertTrue(epic.getListSubtask().stream().map(Subtask::getId).collect(Collectors.toList()).contains(subtask.getId()));
    }

    @AfterEach
    public void afterEach() {
        Managers.resetManagers();
        TestFileUtil.resetFile();
    }
}