package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final int id;
    private final String title;
    private final String info;
    private final TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(int id, String title, String info) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = TaskStatus.NEW;
        this.duration = null;
        this.startTime = null;
    }

    public Task(int id, String title, String info, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = status;
        this.duration = null;
        this.startTime = null;
    }

    public Task(int id, String title, String info, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = TaskStatus.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String title, String info, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TaskUpdater getUpdater() {
        return new TaskUpdater(this);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getEndTime(){
        if (duration == null){
            return startTime;
        }
        return startTime != null ? startTime.plusMinutes(duration.toMinutes()) : null;
    };

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public Duration getDuration(){
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(info, task.info) && status == task.status;
    }

    public static class TaskUpdater {
        protected final Task originalTask;
        protected String newTitle;
        protected String newInfo;
        protected TaskStatus newStatus;
        protected Duration duration;
        protected LocalDateTime startTime;

        public TaskUpdater(Task originalTask) {
            this.originalTask = originalTask;
        }

        public TaskUpdater setNewTitle(String newTitle) {
            this.newTitle = newTitle;
            return this;
        }

        public TaskUpdater setNewInfo(String newInfo) {
            this.newInfo = newInfo;
            return this;
        }

        public TaskUpdater setNewStatus(TaskStatus newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        public TaskUpdater setDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public TaskUpdater setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Task updateTask() {
            return new Task(originalTask.getId(),
                    newTitle != null ? newTitle : originalTask.getTitle(),
                    newInfo != null ? newInfo : originalTask.getInfo(),
                    newStatus != null ? newStatus : originalTask.getStatus(),
                    duration != null ? duration : originalTask.getDuration(),
                    startTime != null ? startTime : originalTask.getStartTime());
        }
    }
}