package service.History;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Task> historyList = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        if (historyList.containsKey(task.getId())) {
            historyList.remove(task.getId());
        }
        historyList.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTask(historyList);
    }

    public List<Task> getTask(Map historyList) {
        return new ArrayList<>(historyList.values());
    }
}
