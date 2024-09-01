package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task extends Entity implements Comparable<Task> {
    private final TaskStatus status;
    private final Duration duration;
    private final LocalDateTime startTime;

    protected Task(int id, String title, String info, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(id, title, info);
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime;
        }
        return startTime != null ? startTime.plusMinutes(duration.toMinutes()) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return super.equals(o) && status == task.status;
    }

    public boolean isIntersectsWith(Task task) {
        return (task.getStartTime() != null && task.getEndTime() != null && getStartTime() != null &&
                getEndTime() != null &&
                ((task.getStartTime().isBefore(getEndTime()) && task.getStartTime().isAfter(getStartTime()))
                        || (task.getEndTime().isBefore(getEndTime()) && task.getEndTime().isAfter(getStartTime()))
                        || (task.getStartTime().isBefore(getStartTime()) && task.getEndTime().isAfter(getEndTime()))));
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    @Override
    public int compareTo(Task o) {
        return startTime.isBefore(o.getStartTime()) ? -1 : startTime.isAfter(o.getStartTime()) ? 1 : 0;
    }

    public static class TaskBuilder {
        protected int id;
        protected String title;
        protected String info;
        protected TaskStatus status = TaskStatus.NEW;
        protected Duration duration;
        protected LocalDateTime startTime;

        public TaskBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskBuilder info(String info) {
            this.info = info;
            return this;
        }

        public TaskBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TaskBuilder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public TaskBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Task build() {
            return new Task(id, title, info, status, duration, startTime);
        }
    }
}