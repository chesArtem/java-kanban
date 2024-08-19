package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;
import util.CSVUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

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
            fileWriter.write("id,type,name,status,description,epic \n");
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

                CSVUtil.csvStringToTask(line, tasks, epics, subTasks);

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
        Task result = super.createTask(title, info);
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
        Subtask result = super.createSubtask(title, info, parentEpic);
        save();
        return result;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task result = super.getTaskById(id);
        return result;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic result = super.getEpicById(id);
        return result;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask result = super.getSubtaskById(id);
        return result;
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
        super.getEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) throws IOException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(Task task) throws IOException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateStatusEpic(ArrayList<Subtask> listSubtaskInEpic, Epic epics) throws IOException {
        super.updateStatusEpic(listSubtaskInEpic, epics);
        save();
    }

    @Override
    public List<Subtask> allSubtaskInEpic(Integer id) {
        return super.allSubtaskInEpic(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}