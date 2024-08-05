package service.Task;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager() {
        super();
        readFile();
    }

    public void save() throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter("task.txt"))) {
            fileWriter.write("id,type,name,status,description,epic \n");
            for (Task task : getAllTask()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Task epic : getAllEpic()) {
                fileWriter.write(epic.toString() + "\n");
            }
            for (Task subTask : getAllSubtask()) {
                fileWriter.write(subTask.toString() + "\n");
            }
        }
    }

    public void readFile() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("task.txt"));
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
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
                        Epic epicInList = epics.get(Integer.parseInt(el[4]));
                        Subtask subtask = new Subtask(
                                Integer.parseInt(el[0]),
                                el[2],
                                el[3],
                                epicInList);

                        epicInList.addSubtask(subtask);
                        subTasks.put(Integer.parseInt(el[0]), subtask);
                        break;
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        Subtask result = getSubtaskById(id);
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