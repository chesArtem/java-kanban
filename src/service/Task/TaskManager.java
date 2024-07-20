package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int getId();
    Task createTask(String title, String info);
    Epic createEpic(String title, String info);
    Subtask createSubtask(String title, String info, Epic parentEpic);
    Task getTaskById(Integer id);
    Epic getEpicById(Integer id);
    Subtask getSubtaskById(Integer id);
    List<Task> getAllTask();
    List<Epic> getAllEpic();
    List<Subtask> getAllSubtask();
    void deleteAllTask();
    void deleteAllEpic();
    void deleteAllSubtask();
    void deleteTaskById(Integer id);
    void deleteEpicById(Integer id);
    void deleteSubtaskById(Integer id);
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);
    void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics);
    List<Subtask> allSubtaskInEpic(Integer id);
    List<Task> getHistory();
}