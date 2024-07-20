package model;

import service.Task.TaskStatus;

import java.util.Objects;

public class Subtask extends Task{
    private final Epic parentEpic;
    public Subtask(int id, String title, String info, Epic parentEpic) {
        super(id, title, info);
        this.parentEpic = parentEpic;
    }

    public Subtask(int id, String title, String info, TaskStatus status, Epic parentEpic) {
        super(id, title, info, status);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public SubtaskUpdater getUpdater() {
        return new SubtaskUpdater(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(parentEpic, subtask.parentEpic);
    }

    public static class SubtaskUpdater extends TaskUpdater {
        public SubtaskUpdater(Subtask originalTask) {
            super(originalTask);
        }

        public Subtask updateTask() {
            Epic parentEpic = ((Subtask)originalTask).getParentEpic();
            parentEpic.removeSubtask((Subtask) originalTask);
            return new Subtask(originalTask.getId(),
                    newTitle != null ? newTitle : originalTask.getTitle(),
                    newInfo != null ? newInfo : originalTask.getInfo(),
                    newStatus != null ? newStatus : originalTask.getStatus(),
                    parentEpic);
        }
    }
}
