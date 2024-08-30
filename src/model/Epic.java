package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Map<Integer, Subtask> mapSubtask = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(int id, String title, String info) {
        super(id, title, info);
    }

    public Epic(int id, String title, String info, TaskStatus taskStatus, Map<Integer, Subtask> subtasks) {
        super(id, title, info, taskStatus);
        mapSubtask = subtasks;
    }

    public Epic(int id, String title, String info, TaskStatus status, Duration duration,
                LocalDateTime startTime, Map<Integer, Subtask> mapSubtask) {
        super(id, title, info, status, duration, startTime);
        this.mapSubtask = mapSubtask;
        this.startTime = mapSubtask.values().stream()
                .map(Task::getStartTime).filter(Objects::nonNull).min(LocalDateTime::compareTo).orElse(null);
        this.duration = mapSubtask.values().stream()
                .map(Task::getDuration).filter(Objects::nonNull).reduce(Duration.ZERO, Duration::plus);
        this.endTime = mapSubtask.values().stream()
                .map(Task::getEndTime).filter(Objects::nonNull).max(LocalDateTime::compareTo).orElse(null);
    }

    private Map<Integer, Subtask> getMapSubtask() {
        return mapSubtask;
    }

    public void addSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getId(), subtask);

        startTime = mapSubtask.values().stream().map(Task::getStartTime).filter(Objects::nonNull).min(LocalDateTime::compareTo).orElse(null);
        duration = mapSubtask.values().stream().map(Task::getDuration).filter(Objects::nonNull).reduce(Duration.ZERO, Duration::plus);
        endTime = mapSubtask.values().stream().map(Task::getEndTime).filter(Objects::nonNull).max(LocalDateTime::compareTo).orElse(null);
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

    public LocalDateTime getEndTime(){
        return endTime;
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
