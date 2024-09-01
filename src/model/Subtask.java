package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Epic parentEpic;

    private Subtask(int id, String title, String info, TaskStatus status, Duration duration, LocalDateTime startTime,
                    Epic parentEpic) {
        super(id, title, info, status, duration, startTime);
        this.parentEpic = parentEpic;
    }

    public Epic getParentEpic() {
        return parentEpic;
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(parentEpic, subtask.parentEpic);
    }

    public static SubtaskBuilder builder() {
        return new SubtaskBuilder();
    }

    public static class SubtaskBuilder extends TaskBuilder {
        private Epic parentEpic;

        public SubtaskBuilder parentEpic(Epic parentEpic) {
            this.parentEpic = parentEpic;
            return this;
        }

        public Subtask build() {
            return new Subtask(id, title, info, status, duration, startTime, parentEpic);
        }
    }
}
