package model;

import service.Task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Entity {
    private final int id;
    private final String title;
    private final String info;

    protected Entity(int id, String title, String info) {
        this.id = id;
        this.title = title;
        this.info = info;
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

    public abstract TaskStatus getStatus();

    public abstract LocalDateTime getStartTime();

    public abstract Duration getDuration();

    public abstract LocalDateTime getEndTime();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id && Objects.equals(title, entity.title) && Objects.equals(info, entity.info);
    }
}
