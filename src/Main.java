import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task(taskManager.getId(), "title 1", "info 1"));
        taskManager.createTask(new Task(taskManager.getId(), "title 2", "info 2"));

        Epic epic1 = new Epic(taskManager.getId(), "epic 1", "epic 1");
        Epic epic2 = new Epic(taskManager.getId(), "epic 2", "epic 2");

        taskManager.createEpic(epic1);
        taskManager.createSubtasks(new Subtask(taskManager.getId(), "epic 1 subTasks 1", "subTasks", epic1));
        taskManager.createSubtasks(new Subtask(taskManager.getId(), "epic 1 subTasks 2", "subTasks", epic1));

        taskManager.createEpic(epic2);
        taskManager.createSubtasks(new Subtask(taskManager.getId(), "epic 2 subTasks 1", "subTasks", epic2));
        taskManager.createSubtasks(new Subtask(taskManager.getId(), "epic 2 subTasks 2", "subTasks", epic2));
        taskManager.createSubtasks(new Subtask(taskManager.getId(), "epic 2 subTasks 3", "subTasks", epic2));
    }
}