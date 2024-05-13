package service.History;

import model.Task;
import service.History.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private List<Task> historyList = new LinkedList<>();
    @Override
    public void add(Task task){
        historyList.add(task);
        if (historyList.size() > 10) {
            historyList.remove(0);
        }
    }
    @Override
    public List<Task> getHistory(){
        return new ArrayList<>(historyList);
    }
}
