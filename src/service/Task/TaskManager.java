package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int getId();

    void createTask(Task task);

    void createEpic(Epic task);

    void createSubtasks(Subtask task);

    Task getTaskToId(Integer id);

    Epic getEpicToId(Integer id);

    Subtask getSubtasksToId(Integer id);

    List<Task> allTask();

    List<Epic> allEpic();

    List<Subtask> allSubtask();

    void removeAllTask();

    void removeAllEpic();
    void removeAllSubtask();

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics);

    List<Subtask> allSubtaskInEpic(Integer id);

    List<Task> getHistory();
}