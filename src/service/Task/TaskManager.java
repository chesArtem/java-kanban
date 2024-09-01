package service.Task;

import model.Entity;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    int getId();

    Task createTask(String title, String info) throws IOException;

    Task createTask(String title, String info, Duration duration, LocalDateTime startTime) throws IOException;

    Epic createEpic(String title, String info) throws IOException;

    Subtask createSubtask(String title, String info, Epic parentEpic) throws IOException;

    Subtask createSubtask(String title, String info, Duration duration, LocalDateTime startTime, Epic parentEpic) throws IOException;

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtask();

    void deleteAllTask() throws IOException;

    void deleteAllEpic() throws IOException;

    void deleteAllSubtask() throws IOException;

    void deleteTaskById(Integer id) throws IOException;

    void deleteEpicById(Integer id) throws IOException;

    void deleteSubtaskById(Integer id) throws IOException;

    Task updateTask(int taskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                    LocalDateTime newStartTime) throws IOException;

    Epic updateEpic(int epicId, String newTitle, String newInfo) throws IOException;

    Subtask updateSubtask(int subtaskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                       LocalDateTime newStartTime) throws IOException;

    List<Subtask> allSubtaskInEpic(Integer id);

    List<Entity> getHistory();

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    TreeSet<Task> getPrioritizedTasks();
}