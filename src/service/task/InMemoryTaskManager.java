package service.task;

import model.Entity;
import model.Epic;
import model.Subtask;
import model.Task;
import service.history.HistoryManager;
import service.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static InMemoryTaskManager instance;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();
    private final HistoryManager historyManager = Managers.getHistoryManager();
    private int id = 1;

    public InMemoryTaskManager() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Task createTask(String title, String info) throws IOException {
        return createTask(title, info, null, null);
    }

    @Override
    public Task createTask(String title, String info, Duration duration, LocalDateTime startTime) throws IOException {
        Task result = Task.builder()
                .id(id)
                .title(title)
                .info(info)
                .duration(duration)
                .startTime(startTime)
                .build();
        validateNewTask(result);
        tasks.put(id++, result);
        checkPrioritizedTask(result, false);
        return result;
    }

    @Override
    public Epic createEpic(String title, String info) throws IOException {
        Epic result = Epic.builder()
                .id(id)
                .title(title)
                .info(info)
                .build();
        validateNewEpic(result);
        epics.put(id++, result);
        return result;
    }

    @Override
    public Subtask createSubtask(String title, String info, Epic parentEpic) throws IOException {
        return createSubtask(title, info, null, null, parentEpic);
    }

    @Override
    public Subtask createSubtask(String title, String info, Duration duration, LocalDateTime localDateTime, Epic parentEpic) throws IOException {
        Subtask result = (Subtask) Subtask.builder()
                .parentEpic(parentEpic)
                .id(id)
                .title(title)
                .info(info)
                .duration(duration)
                .startTime(localDateTime)
                .build();
        validateNewTask(result);
        subTasks.put(id++, result);
        result.getParentEpic().addSubtask(result);
        checkPrioritizedTask(result, false);
        return result;
    }

    @Override
    public Task getTaskById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Task id can't be null");
        }
        if (tasks.get(id) == null) {
            throw new NullPointerException("task not found");
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
        if (epics.get(id) == null) {
            throw new NullPointerException("epic not found");
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
        if (subTasks.get(id) == null) {
            throw new NullPointerException("subtask not found");
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
        tasks.forEach((integer, task) -> checkPrioritizedTask(task, true));
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() throws IOException {
        subTasks.forEach((integer, task) -> checkPrioritizedTask(task, true));
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubtask() throws IOException {
        subTasks.forEach((integer, task) -> checkPrioritizedTask(task, true));
        subTasks.clear();
        epics.values().forEach(Epic::removeAllSubtasks);
    }

    @Override
    public void deleteTaskById(Integer id) throws IOException {
        if (id == null) {
            throw new IllegalArgumentException("Task id can't be null");
        }
        Task removed;
        if ((removed = tasks.remove(id)) == null) {
            System.out.println("Deleting nonexisting task with id " + id);
        } else {
            checkPrioritizedTask(removed, true);
        }
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
        checkPrioritizedTask(removed, true);
        removed.getParentEpic().removeSubtask(removed);
    }

    @Override
    public Task updateTask(int taskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                           LocalDateTime newStartTime) throws IOException {
        Task existingTask = getTaskById(taskId);
        Task newTask = Task.builder()
                .id(taskId)
                .title(newTitle != null ? newTitle : existingTask.getTitle())
                .info(newInfo != null ? newInfo : existingTask.getInfo())
                .status(newStatus != null ? newStatus : existingTask.getStatus())
                .startTime(newStartTime != null ? newStartTime : existingTask.getStartTime())
                .duration(newDuration != null ? newDuration : existingTask.getDuration())
                .build();
        validateUpdateTask(newTask);
        tasks.put(taskId, newTask);
        checkPrioritizedTask(existingTask, true);
        checkPrioritizedTask(newTask, false);
        return newTask;
    }

    @Override
    public Epic updateEpic(int epicId, String newTitle, String newInfo) throws IOException {
        Epic existingEpic = getEpicById(epicId);
        Epic newEpic = Epic.builder()
                .id(epicId)
                .title(newTitle != null ? newTitle : existingEpic.getTitle())
                .info(newInfo != null ? newInfo : existingEpic.getInfo())
                .mapSubtask(existingEpic.getMapSubtask())
                .build();
        validateUpdateEpic(newEpic);
        epics.put(epicId, newEpic);
        newEpic.getListSubtask().forEach(it -> it.setParentEpic(newEpic));
        return newEpic;
    }

    @Override
    public Subtask updateSubtask(int subtaskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                                 LocalDateTime newStartTime) throws IOException {
        Subtask existingSubtask = getSubtaskById(subtaskId);
        Subtask newSubtask = (Subtask) Subtask.builder()
                .parentEpic(existingSubtask.getParentEpic())
                .id(subtaskId)
                .title(newTitle != null ? newTitle : existingSubtask.getTitle())
                .info(newInfo != null ? newInfo : existingSubtask.getInfo())
                .status(newStatus != null ? newStatus : existingSubtask.getStatus())
                .startTime(newStartTime != null ? newStartTime : existingSubtask.getStartTime())
                .duration(newDuration != null ? newDuration : existingSubtask.getDuration())
                .build();
        validateUpdateTask(newSubtask);
        subTasks.put(subtaskId, newSubtask);
        newSubtask.getParentEpic().addSubtask(newSubtask);
        checkPrioritizedTask(existingSubtask, true);
        checkPrioritizedTask(newSubtask, false);
        return newSubtask;
    }

    @Override
    public List<Subtask> allSubtaskInEpic(Integer id) {
        return epics.get(id).getListSubtask();
    }

    @Override
    public List<Entity> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {
        validateNewTask(task);
        tasks.put(task.getId(), task);
        checkPrioritizedTask(task, false);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        validateNewTask(subtask);
        subTasks.put(subtask.getId(), subtask);
        checkPrioritizedTask(subtask, false);
    }

    @Override
    public void addEpic(Epic epic) {
        validateNewEpic(epic);
        epics.put(epic.getId(), epic);
    }

    private void checkPrioritizedTask(Task task, boolean delete) {
        if (task.getStartTime() != null) {
            if (!delete) {
                prioritizedTasks.add(task);
            } else {
                prioritizedTasks.remove(task);
            }
        }
    }

    private void validateNewTask(Task task) {
        if (task.getTitle() == null) {
            throw new IllegalArgumentException("title should not be empty");
        }
        if (task.getStatus() == null) {
            throw new IllegalArgumentException("status should not be empty");
        }
        if (tasks.containsKey(task.getId()) || subTasks.containsKey(task.getId()) || epics.containsKey(task.getId())) {
            throw new IllegalArgumentException("duplicated id");
        }

        if (prioritizedTasks.stream().anyMatch(task::isIntersectsWith)) {
            throw new IllegalArgumentException(String.format("Task %s has overlapping duration with one of the tasks", task));
        }
    }

    private void validateNewEpic(Epic epic) {
        if (epic.getTitle() == null) {
            throw new IllegalArgumentException("title should not be empty");
        }
        if (epic.getStatus() == null) {
            throw new IllegalArgumentException("status should not be empty");
        }
        if (tasks.containsKey(epic.getId()) || subTasks.containsKey(epic.getId()) || epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("duplicated id");
        }
    }

    private void validateUpdateTask(Task task) {
        if (task.getTitle() == null) {
            throw new IllegalArgumentException("title should not be empty");
        }
        if (task.getStatus() == null) {
            throw new IllegalArgumentException("status should not be empty");
        }

        if (prioritizedTasks.stream().anyMatch(task::isIntersectsWith)) {
            throw new IllegalArgumentException(String.format("Task %s has overlapping duration with one of the tasks", task));
        }
    }

    private void validateUpdateEpic(Epic epic) {
        if (epic.getTitle() == null) {
            throw new IllegalArgumentException("title should not be empty");
        }
        if (epic.getStatus() == null) {
            throw new IllegalArgumentException("status should not be empty");
        }
    }


    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}