package service;

import service.History.HistoryManager;
import service.History.InMemoryHistoryManager;
import service.Task.InMemoryTaskManager;
import service.Task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}