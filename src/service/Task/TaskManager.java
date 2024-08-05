package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    int getId();

    Task createTask(String title, String info) throws IOException;

    Epic createEpic(String title, String info) throws IOException;

    Subtask createSubtask(String title, String info, Epic parentEpic) throws IOException;

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

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;

    void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics) throws IOException;

    List<Subtask> allSubtaskInEpic(Integer id);

    List<Task> getHistory();

}