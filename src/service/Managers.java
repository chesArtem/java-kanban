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
        activeTaskManager = new FileBackedTaskManager(path);
    }

    public static void initMemoryTaskManager() {
        if (activeTaskManager != null) {
            throw new IllegalStateException("Task manager already created");
        }
        activeTaskManager = new InMemoryTaskManager();
    }

    public static void initMemoryHistoryManager() {
        if (activeHistoryManager != null) {
            throw new IllegalStateException("History manager already created");
        }
        activeHistoryManager = new InMemoryHistoryManager();
    }

    public static TaskManager getTaskManager() {
        if (activeTaskManager == null) {
            throw new IllegalStateException("Task manager has not been initialized");
        }
        return activeTaskManager;
    }

    public static HistoryManager getHistoryManager() {
        if (activeHistoryManager == null) {
            throw new IllegalStateException("History manager has not been initialized");
        }
        return activeHistoryManager;
    }

    public static void resetManagers() {
        activeTaskManager = null;
        activeHistoryManager = null;
    }
}