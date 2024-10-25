package service;

import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;
import service.task.FileBackedTaskManager;
import service.task.InMemoryTaskManager;
import service.task.TaskManager;

public class Managers {
    private static TaskManager activeTaskManager;
    private static HistoryManager activeHistoryManager;

    public static void initFileTaskManager(String path) {
        if (activeTaskManager != null) {
            throw new IllegalStateException("Task manager already created");
        }
        activeTaskManager = FileBackedTaskManager.createInstance(path);
    }

    public static void initMemoryTaskManager() {
        if (activeTaskManager != null) {
            throw new IllegalStateException("Task manager already created");
        }
        activeTaskManager = InMemoryTaskManager.createInstance();
    }

    public static void initMemoryHistoryManager() {
        if (activeHistoryManager != null) {
            throw new IllegalStateException("History manager already created");
        }
        activeHistoryManager = InMemoryHistoryManager.createInstance();
    }

    public static TaskManager getTaskManager() {
        if (activeTaskManager == null) {
            throw new IllegalStateException("");
        }
        return activeTaskManager;
    }

    public static HistoryManager getHistoryManager() {
        if (activeHistoryManager == null) {
            throw new IllegalStateException();
        }
        return activeHistoryManager;
    }
}