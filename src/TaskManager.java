import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epicTasks = new HashMap<>();
    private Map<Integer, Subtask> subTasks = new HashMap<>();
    private int id = 1;

    public int getId() {
        return id;
    }

    public void createTask(Task task){
        task.id = id++;
        tasks.put(task.id, task);
    }
    public void createEpic(Epic task){
        task.id = id++;
        epicTasks.put(task.id, task);
    }
    public void createSubtasks(Subtask task){
        task.id = id++;
        subTasks.put(task.id, task);
    }

    public Task taskToId (Integer id){
        return tasks.get(id);
    }
    public Epic epicToId (Integer id){
        return epicTasks.get(id);
    }
    public Subtask subtasksToId (Integer id){
        return subTasks.get(id);
    }

    public ArrayList<Task> allTask (){
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Epic> allEpic (){
        return new ArrayList<>(epicTasks.values());
    }
    public ArrayList<Subtask> allSubtask (){
        return new ArrayList<>(subTasks.values());
    }

    public void removeAllTask(){
        tasks.clear();
    }
    public void removeAllEpic(){
        epicTasks.clear();
    }
    public void removeAllSubtask(){
        subTasks.clear();
    }

    public void removeTaskById(Integer id){
        tasks.remove(id);
    }
    public void removeEpicById(Integer id){
        epicTasks.remove(id);
    }
    public void removeSubtaskById(Integer id){
        subTasks.remove(id);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void updateEpic(Epic epic){
        epicTasks.put(epic.getId(), epic);
    }
    public void updateSubtask(Subtask subtask){
        subTasks.put(subtask.getId(), subtask);

        Epic epic = subTasks.get(subtask.getId()).getParentEpic();
        ArrayList<Subtask> listSubtaskInEpic = epic.getListSubtask();

        if ( listSubtaskInEpic.stream().allMatch(item -> item.status.equals(TaskStatus.NEW)) ){
            epic.status = TaskStatus.NEW;
        } else if ( listSubtaskInEpic.stream().allMatch(item -> item.status.equals(TaskStatus.DONE)) ) {
            epic.status = TaskStatus.DONE;
        } else {
            epic.status = TaskStatus.IN_PROGRESS;
        }
    }

    public ArrayList<Subtask> allSubtaskInEpic (Integer id){
        return epicTasks.get(id).getListSubtask();
    }
}