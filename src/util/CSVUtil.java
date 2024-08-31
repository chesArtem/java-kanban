package util;

import model.Epic;
import model.Subtask;
import model.Task;
import service.Task.TaskManager;
import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVUtil {

    public static String taskToCsvString(Task task) {
        return String.format("%s,TASK,%s,%s,%s,%s,%s", task.getId(), task.getTitle(), task.getStatus(), task.getInfo(),
                task.getDuration(), task.getStartTime());
    }

    public static String epicToCsvString(Epic epic) {
        return String.format("%s,EPIC,%s,%s,%s,%s,%s", epic.getId(), epic.getTitle(), null, epic.getInfo(), null, null);
    }

    public static String subtaskToCsvString(Subtask subtask) {
        return String.format("%s,SUBTASK,%s,%s,%s,%s,%s,%s", subtask.getId(), subtask.getTitle(), subtask.getStatus(),
                subtask.getInfo(), subtask.getDuration(), subtask.getStartTime(), subtask.getParentEpic().getId());
    }

    public static void csvStringToTask(String line, TaskManager manager) {
        String[] el = line.split(",");
        Duration duration;
        LocalDateTime startTime;
        switch (el[1]) {
            case "TASK":
                duration = el[5].equals("null") ? null : Duration.parse(el[5]);
                startTime = el[6].equals("null") ? null : LocalDateTime.parse(el[6]);
                Task task = Task.builder()
                        .id(Integer.parseInt(el[0]))
                        .title(el[2])
                        .info(el[4])
                        .status(TaskStatus.valueOf(el[3]))
                        .duration(duration)
                        .startTime(startTime)
                        .build();
                manager.addTask(task);
                break;
            case "EPIC":
                Epic epic = (Epic) Epic.builder()
                        .id(Integer.parseInt(el[0]))
                        .title(el[2])
                        .info(el[4])
                        .build();
                manager.addEpic(epic);
                break;
            case "SUBTASK":
                Epic epicInList = manager.getEpicById(Integer.parseInt(el[7]));
                duration = el[5].equals("null") ? null : Duration.parse(el[5]);
                startTime = el[6].equals("null") ? null : LocalDateTime.parse(el[6]);
                Subtask subtask = (Subtask) Subtask.builder()
                        .parentEpic(epicInList)
                        .id(Integer.parseInt(el[0]))
                        .title(el[2])
                        .info(el[4])
                        .status(TaskStatus.valueOf(el[3]))
                        .duration(duration)
                        .startTime(startTime)
                        .build();

                epicInList.addSubtask(subtask);
                manager.addSubtask(subtask);
                break;
        }
    }
}
