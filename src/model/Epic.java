package model;

import service.Task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer, Subtask> mapSubtask = new HashMap<>();

    public Epic(int id, String title, String info) {
        super(id, title, info);
    }

    public Epic(int id, String title, String info, TaskStatus taskStatus, Map<Integer, Subtask> subtasks) {
        super(id, title, info, taskStatus);
        mapSubtask = subtasks;
    }

    private Map<Integer, Subtask> getMapSubtask() {
        return mapSubtask;
    }

    public void addSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getId(), subtask);
    }

    public void removeSubtask(Subtask subtask) {
        mapSubtask.remove(subtask.getId());
    }

    public void removeAllSubtasks() {
        mapSubtask.clear();
    }

    public ArrayList<Subtask> getListSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    public EpicUpdater getUpdater() {
        return new EpicUpdater(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    public static class EpicUpdater extends TaskUpdater {
        public EpicUpdater(Epic originalTask) {
            super(originalTask);
        }

        public Epic updateTask() {
            return new Epic(originalTask.getId(),
                    newTitle != null ? newTitle : originalTask.getTitle(),
                    newInfo != null ? newInfo : originalTask.getInfo(),
                    newStatus != null ? newStatus : originalTask.getStatus(),
                    ((Epic)originalTask).getMapSubtask());
        }
    }
}
