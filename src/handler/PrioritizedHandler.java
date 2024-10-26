package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.Managers;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Gson gson = new Gson();
            String requestPath = httpExchange.getRequestURI().getPath();
            EndpointEnum endpoint = getEndpoint(requestPath, httpExchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_ELEMENTS: {
                    TreeSet<Task> tasks = Managers.getTaskManager().getPrioritizedTasks();
                    sendText(httpExchange, gson.toJson(tasks));
                    return;
                }
            }
        } catch (IllegalStateException e) {
            sendNotFound(httpExchange);
        } catch (NumberFormatException e) {
            sendInternalError(httpExchange);
        } catch (Exception e) {
            sendInternalError(httpExchange);
        }
    }

    protected EndpointEnum getEndpoint(String requestPath, String requestMethod) {
        EndpointEnum endpoint = super.getEndpoint(requestPath, requestMethod);
        switch (endpoint) {
            case CREATE_ELEMENT:
            case DELETE_ELEMENT:
            case GET_ELEMENT_BY_ID:
            case UPDATE_ELEMENT:
            case GET_SUBTASKS:
            case UNKNOWN:
                throw new IllegalStateException("Unsupported path for task controller: " + requestPath);
        }
        return endpoint;
    }
}
