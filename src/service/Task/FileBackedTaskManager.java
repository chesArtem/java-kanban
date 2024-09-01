package service.Task;

import model.Entity;
import model.Epic;
import model.Subtask;
import model.Task;
import util.CSVUtil;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(String path) {
        super();
        if (path == null || path.isEmpty()) {
            System.out.println("empty path");
            throw new IllegalArgumentException("empty path");
        }

        file = new File(path);
        try {
            if (!file.createNewFile()) {
                readFile();
            }
        } catch (IOException e) {
            System.out.println("error creating file");
            throw new IllegalArgumentException("error creating file", e);
        }
    }

    public void save() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file.getCanonicalPath()))) {
            fileWriter.write("id,type,name,status,description,duration,startTime,epic \n");
            for (Task task : getAllTask()) {
                fileWriter.write(CSVUtil.taskToCsvString(task) + "\n");
            }
            for (Epic epic : getAllEpic()) {
                fileWriter.write(CSVUtil.epicToCsvString(epic) + "\n");
            }
            for (Subtask subTask : getAllSubtask()) {
                fileWriter.write(CSVUtil.subtaskToCsvString(subTask) + "\n");
            }
        }
    }

    public void readFile() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(file.getCanonicalPath()));
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);

                CSVUtil.csvStringToTask(line, this);

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error while reading from file");
        }
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public Task createTask(String title, String info) throws IOException {
        return createTask(title, info, null, null);
    }

    @Override
    public Task createTask(String title, String info, Duration duration, LocalDateTime startTime) throws IOException {
        Task result = super.createTask(title, info, duration, startTime);
        save();
        return result;
    }

    @Override
    public Epic createEpic(String title, String info) throws IOException {
        Epic result = super.createEpic(title, info);
        save();
        return result;
    }

    @Override
    public Subtask createSubtask(String title, String info, Epic parentEpic) throws IOException {
        return createSubtask(title, info, null, null, parentEpic);
    }

    @Override
    public Subtask createSubtask(String title, String info, Duration duration, LocalDateTime startTime, Epic parentEpic) throws IOException {
        Subtask result = super.createSubtask(title, info, duration, startTime, parentEpic);
        save();
        return result;
    }

    @Override
    public Task getTaskById(Integer id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        return super.getSubtaskById(id);
    }

    @Override
    public void deleteAllTask() throws IOException {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() throws IOException {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() throws IOException {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteTaskById(Integer id) throws IOException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) throws IOException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) throws IOException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Task updateTask(int taskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                           LocalDateTime newStartTime) throws IOException {
        Task result = super.updateTask(taskId, newTitle, newInfo, newStatus, newDuration, newStartTime);
        save();
        return result;
    }

    @Override
    public Epic updateEpic(int epicId, String newTitle, String newInfo) throws IOException {
        Epic result = super.updateEpic(epicId, newTitle, newInfo);
        save();
        return result;
    }

    @Override
    public Subtask updateSubtask(int subtaskId, String newTitle, String newInfo, TaskStatus newStatus, Duration newDuration,
                              LocalDateTime newStartTime) throws IOException {
        Subtask result = super.updateSubtask(subtaskId, newTitle, newInfo, newStatus, newDuration, newStartTime);
        save();
        return result;
    }

    @Override
    public List<Subtask> allSubtaskInEpic(Integer id) {
        return super.allSubtaskInEpic(id);
    }

    @Override
    public List<Entity> getHistory() {
        return super.getHistory();
    }
}