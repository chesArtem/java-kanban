package utils;

import model.Epic;
import model.Subtask;
import model.Task;
import service.task.TaskStatus;
import util.CSVUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TestFileUtil {
    private static final String path = "testSaveTime.txt";
    private static StringBuilder stringBuilder = new StringBuilder();

    public static String createFile() throws IOException {
        File file = new File(path);
        try {
            if (!file.createNewFile()) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("error creating file");
            throw new IllegalArgumentException("error creating file", e);
        }

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file.getCanonicalPath()))) {
            fileWriter.write("id,type,name,status,description,duration,startTime,epic \n");
            fileWriter.write(stringBuilder.toString());
        }
        return path;
    }

    public static void resetFile() {
        stringBuilder = new StringBuilder();
    }

    public static Task addTask(int id, String title, String info, TaskStatus status, Duration duration,
                               LocalDateTime startTime) {
        Task task = Task.builder()
                .id(id)
                .title(title)
                .info(info)
                .status(status)
                .duration(duration)
                .startTime(startTime)
                .build();
        stringBuilder.append(CSVUtil.taskToCsvString(task)).append("\n");
        return task;
    }

    public static Epic addEpic(int id, String title, String info) {
        Epic epic = Epic.builder()
                .id(id)
                .title(title)
                .info(info)
                .build();
        stringBuilder.append(CSVUtil.epicToCsvString(epic)).append("\n");
        return epic;
    }

    public static Subtask addSubtask(int id, String title, String info, TaskStatus status, Duration duration,
                                     LocalDateTime startTime, Epic parentEpic) {
        Subtask subtask = (Subtask) Subtask.builder()
                .parentEpic(parentEpic)
                .id(id)
                .title(title)
                .info(info)
                .status(status)
                .duration(duration)
                .startTime(startTime)
                .build();
        stringBuilder.append(CSVUtil.subtaskToCsvString(subtask)).append("\n");
        return subtask;
    }
}