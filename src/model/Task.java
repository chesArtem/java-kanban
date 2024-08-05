package model;

import service.Task.TaskStatus;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String info;
    private TaskStatus status;

    public Task(int id, String title, String info) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = TaskStatus.NEW;
    }

    public Task(int id, String title, String info, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(info, task.info) && status == task.status;
    }

    @Override
    public String toString() {
        return getId() + ",TASK, " + "," + title + "," + status + "," + info + ",";
    }

    public static class TaskUpdater {
        protected final Task originalTask;
        protected String newTitle;
        protected String newInfo;
        protected TaskStatus newStatus;

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

        public Task updateTask() {
            return new Task(originalTask.getId(),
                    newTitle != null ? newTitle : originalTask.getTitle(),
                    newInfo != null ? newInfo : originalTask.getInfo(),
                    newStatus != null ? newStatus : originalTask.getStatus());
        }
    }
}