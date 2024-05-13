package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;
import service.History.HistoryManager;
import service.History.InMemoryHistoryManager;
import service.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subTasks = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 1;


    @Override
    public int getId() {
        return id;
    }
    @Override
    public void createTask(Task task){
        task.id = id++;
        tasks.put(task.id, task);
    }
    @Override
    public void createEpic(Epic task){
        task.id = id++;
        epics.put(task.id, task);
    }
    @Override
    public void createSubtasks(Subtask task){
        task.id = id++;
        subTasks.put(task.id, task);
    }
    @Override
    public Task getTaskToId(Integer id){
        if (id != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }
    @Override
    public Epic getEpicToId(Integer id){
        if (id != null) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }
    @Override
    public Subtask getSubtasksToId(Integer id){
        if (id != null){
            historyManager.add(subTasks.get(id));
        }
        return subTasks.get(id);
    }
    @Override
    public List<Task> allTask(){
        return new ArrayList<>(tasks.values());
    }
    @Override
    public List<Epic> allEpic(){
        return new ArrayList<>(epics.values());
    }
    @Override
    public List<Subtask> allSubtask(){
        return new ArrayList<>(subTasks.values());
    }
    @Override
    public void removeAllTask(){
        tasks.clear();
    }
    @Override
    public void removeAllEpic(){
        epics.clear();
        subTasks.clear();
    }
    @Override
    public void removeAllSubtask(){
        subTasks.clear();
    }
    @Override
    public void removeTaskById(Integer id){
        tasks.remove(id);
    }
    @Override
    public void removeEpicById(Integer id){
        for (Task subtask : allSubtaskInEpic(id)){
            removeSubtaskById(subtask.getId());
        }
        epics.remove(id);
    }
    @Override
    public void removeSubtaskById(Integer id){
        subTasks.remove(id);
    }
    @Override
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    @Override
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
    }
    @Override
    public void updateSubtask(Subtask subtask){
        subTasks.put(subtask.getId(), subtask);

        Epic epics = subTasks.get(subtask.getId()).getParentEpic();
        ArrayList<Subtask> listSubtaskInEpic = epics.getListSubtask();

        updateStatusEpic(listSubtaskInEpic, epics);

    }
    @Override
    public void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics){
        if ( listSubtaskInEpic.stream().allMatch(item -> item.status.equals(TaskStatus.NEW)) ){
            epics.status = TaskStatus.NEW;
        } else if ( listSubtaskInEpic.stream().allMatch(item -> item.status.equals(TaskStatus.DONE)) ) {
            epics.status = TaskStatus.DONE;
        } else {
            epics.status = TaskStatus.IN_PROGRESS;
        }
    }
    @Override
    public List<Subtask> allSubtaskInEpic(Integer id){
        return epics.get(id).getListSubtask();
    }

    @Override
    public List<Task> getHistory() {
        return null;
    }

}