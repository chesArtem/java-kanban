package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Entity {
    private final Map<Integer, Subtask> mapSubtask;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Epic(int id, String title, String info, Map<Integer, Subtask> mapSubtask) {
        super(id, title, info);
        this.mapSubtask = mapSubtask;
        recalculateFields();
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void addSubtask(Subtask subtask) {
        mapSubtask.put(subtask.getId(), subtask);
        recalculateFields();
    }

    public void removeSubtask(Subtask subtask) {
        mapSubtask.remove(subtask.getId());
        recalculateFields();
    }

    public void removeAllSubtasks() {
        mapSubtask.clear();
        recalculateFields();
    }

    public ArrayList<Subtask> getListSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    public Map<Integer, Subtask> getMapSubtask() {
        return mapSubtask;
    }

    public void recalculateFields() {
        if (mapSubtask.values().stream().allMatch(item -> item.getStatus().equals(TaskStatus.NEW))) {
            status = TaskStatus.NEW;
        } else if (mapSubtask.values().stream().allMatch(item -> item.getStatus().equals(TaskStatus.DONE))) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }

        startTime = mapSubtask.values().stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
        duration = mapSubtask.values().stream()
                .map(Task::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
        endTime = mapSubtask.values().stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo).orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    public static EpicBuilder builder() {
        return new EpicBuilder();
    }

    public static class EpicBuilder {
        private int id;
        private String title;
        private String info;
        private Map<Integer, Subtask> mapSubtask = new HashMap<>();

        public EpicBuilder id(int id) {
            this.id = id;
            return this;
        }

        public EpicBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EpicBuilder info(String info) {
            this.info = info;
            return this;
        }

        public EpicBuilder mapSubtask(Map<Integer, Subtask> mapSubtask) {
            this.mapSubtask = mapSubtask;
            return this;
        }

        public Epic build() {
            return new Epic(id, title, info, mapSubtask);
        }
    }
}
