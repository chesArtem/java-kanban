package service.history;

import model.Entity;

import java.util.List;

public interface HistoryManager {
    void add(Entity task);

    List<Entity> getHistory();

    void remove(int id);

}
