package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;
import service.History.HistoryManager;
import service.Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Map<Integer, Subtask> subTasks = new HashMap<>();


    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int id = 1;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public Task createTask(String title, String info) throws IOException {
        Task result = new Task(id, title, info);
        tasks.put(id++, result);
        return result;
    }

    @Override
    public Epic createEpic(String title, String info) throws IOException {
        Epic result = new Epic(id, title, info);
        epics.put(id++, result);
        return result;
    }

    @Override
    public Subtask createSubtask(String title, String info, Epic parentEpic) throws IOException {
        Subtask result = new Subtask(id, title, info, parentEpic);
        subTasks.put(id++, result);
        result.getParentEpic().addSubtask(result);
        return result;
    }

    @Override
    public Task getTaskById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Task id can't be null");
        }
        Task result = tasks.get(id);
        historyManager.add(result);
        return result;
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Epic id can't be null");
        }
        Epic result = epics.get(id);
        historyManager.add(result);
        return result;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Subtask id can't be null");
        }
        Subtask result = subTasks.get(id);
        historyManager.add(result);
        return result;
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllTask() throws IOException {
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() throws IOException {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubtask() throws IOException {
        subTasks.clear();
        epics.values().forEach(Epic::removeAllSubtasks);
    }

    @Override
    public void deleteTaskById(Integer id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("Task id can't be null");
        }
        if (tasks.remove(id) == null) {
            System.out.println("Deleting nonexisting task with id " + id);
        }
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) throws IOException {
        for (Task subtask : allSubtaskInEpic(id)) {
            deleteSubtaskById(subtask.getId());
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id) throws IOException {
        Subtask removed = subTasks.remove(id);
        removed.getParentEpic().removeSubtask(removed);
    }

    @Override
    public void updateTask(Task task) throws IOException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        subTasks.put(subtask.getId(), subtask);

        Epic epic = subtask.getParentEpic();
        epic.addSubtask(subtask);
        ArrayList<Subtask> listSubtaskInEpic = epic.getListSubtask();

        updateStatusEpic(listSubtaskInEpic, epic);

    }

    @Override
    public void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics) throws IOException {
        if (listSubtaskInEpic.stream().allMatch(item -> item.getStatus().equals(TaskStatus.NEW))) {
            updateEpic((Epic) epics.getUpdater().setNewStatus(TaskStatus.NEW).updateTask());
        } else if (listSubtaskInEpic.stream().allMatch(item -> item.getStatus().equals(TaskStatus.DONE))) {
            updateEpic((Epic) epics.getUpdater().setNewStatus(TaskStatus.DONE).updateTask());
        } else {
            updateEpic((Epic) epics.getUpdater().setNewStatus(TaskStatus.IN_PROGRESS).updateTask());
        }
    }

    @Override
    public List<Subtask> allSubtaskInEpic(Integer id) {
        return epics.get(id).getListSubtask();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}