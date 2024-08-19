package util;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Map;

public class CSVUtil {

    public static String taskToCsvString(Task task) {
        return task.getId() + ",TASK" + "," + task.getTitle() + "," + task.getStatus() + "," + task.getInfo() + ",";
    }

    public static String epicToCsvString(Epic epic) {
        return epic.getId() + ",EPIC" + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getInfo() + ",";
    }

    public static String subtaskToCsvString(Subtask subtask) {
        return subtask.getId() + ",SUBTASK," + subtask.getTitle() + "," +
                subtask.getStatus() + "," + subtask.getInfo() + "," + subtask.getParentEpic().getId();
    }

    public static void csvStringToTask(String line, Map<Integer, Task> tasks, Map<Integer, Epic> epics, Map<Integer, Subtask> subTasks) {
        String[] el = line.split(",");
        switch (el[1]) {
            case "TASK":
                Task task = new Task(Integer.parseInt(el[0]), el[2], el[3]);

                tasks.put(Integer.parseInt(el[0]), task);
                break;
            case "EPIC":
                Epic epic = new Epic(Integer.parseInt(el[0]), el[2], el[3]);

                epics.put(Integer.parseInt(el[0]), epic);
                break;
            case "SUBTASK":
                Epic epicInList = epics.get(Integer.parseInt(el[5]));
                Subtask subtask = new Subtask(
                        Integer.parseInt(el[0]),
                        el[2],
                        el[3],
                        epicInList);

                epicInList.addSubtask(subtask);
                subTasks.put(Integer.parseInt(el[0]), subtask);
                break;
        }
    }
}
