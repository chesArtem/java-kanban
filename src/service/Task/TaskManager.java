package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {
    int getId();
    void createTask(Task task);
    void createEpic(Epic task);
    void createSubtasks(Subtask task);
    Task taskToId(Integer id);
    Epic epicToId(Integer id);
    Subtask subtasksToId(Integer id);
    ArrayList<Task> allTask();
    ArrayList<Epic> allEpic();
    ArrayList<Subtask> allSubtask();
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
    ArrayList<Subtask> allSubtaskInEpic(Integer id);
    List<Task> getHistory();
}