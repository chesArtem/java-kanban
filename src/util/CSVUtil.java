package util;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class CSVUtil {

    public static String taskToCsvString(Task task) {
        return task.getId() + ",TASK" + "," + task.getTitle() + "," + task.getStatus() + "," + task.getInfo() + "," +
                (task.getDuration() != null ? task.getDuration() : "-") + "," +
                (task.getStartTime() != null ? task.getStartTime() : "-");
    }

    public static String epicToCsvString(Epic epic) {
        return epic.getId() + ",EPIC" + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getInfo() + "," +
                (epic.getDuration() != null ? epic.getDuration() : "-")  + "," +
                (epic.getStartTime() != null ? epic.getStartTime() : "-");
    }

    public static String subtaskToCsvString(Subtask subtask) {
        return subtask.getId() + ",SUBTASK," + subtask.getTitle() + "," +
                subtask.getStatus() + "," + subtask.getInfo() + "," +
                (subtask.getDuration() != null ? subtask.getDuration() : "-")  + "," +
                (subtask.getStartTime() != null ? subtask.getStartTime() : "-")  + "," +
                subtask.getParentEpic().getId();
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
                Epic epicInList = epics.get(Integer.parseInt(el[7]));
                Subtask subtask = new Subtask(
                        Integer.parseInt(el[0]),
                        el[2],
                        el[3],
//                        el[5] != "-" ? Duration.parse(el[5]) : null,
//                        el[6] != "-" ? LocalDateTime.parse(el[6]) : null,
                        epicInList);

                epicInList.addSubtask(subtask);
                subTasks.put(Integer.parseInt(el[0]), subtask);
                break;
        }
    }
}
