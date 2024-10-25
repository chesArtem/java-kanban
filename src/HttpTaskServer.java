import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        Managers.initMemoryHistoryManager();
        Managers.initFileTaskManager("testSaveTime.txt");
        httpServer = HttpServer.create(new InetSocketAddress(8085), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
    }

}