package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.Managers;
import service.task.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TaskHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Gson gson = new Gson();
            String requestPath = httpExchange.getRequestURI().getPath();
            EndpointEnum endpoint = getEndpoint(requestPath, httpExchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_ELEMENTS: {
                    List<Task> tasks = Managers.getTaskManager().getAllTask();
                    sendText(httpExchange, gson.toJson(tasks));
                    return;
                }
                case GET_ELEMENT_BY_ID: {
                    int id = getIdFromRequestPath(requestPath);
                    Task task = Managers.getTaskManager().getTaskById(id);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case CREATE_ELEMENT: {
                    Map<String, String> request =
                            gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Map.class);
                    String durationString = request.get("duration");
                    String startTimeString = request.get("startTime");

                    Duration duration = durationString == null ? null : Duration.parse(durationString);
                    LocalDateTime startTime = startTimeString == null ? null : LocalDateTime.parse(startTimeString);

                    Task task = Managers.getTaskManager().createTask(request.get("title"), request.get("info"),
                            duration, startTime);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case UPDATE_ELEMENT: {
                    int id = getIdFromRequestPath(requestPath);
                    Map<String, String> request =
                            gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Map.class);
                    String statusString = request.get("status");
                    String durationString = request.get("duration");
                    String startTimeString = request.get("startTime");

                    TaskStatus taskStatus = statusString == null ? null : TaskStatus.valueOf(statusString);
                    Duration duration = durationString == null ? null : Duration.parse(durationString);
                    LocalDateTime startTime = startTimeString == null ? null : LocalDateTime.parse(startTimeString);

                    Task task = Managers.getTaskManager().updateTask(id, request.get("title"), request.get("info"),
                            taskStatus, duration, startTime);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case DELETE_ELEMENT:
                    int id = getIdFromRequestPath(requestPath);
                    Managers.getTaskManager().deleteTaskById(id);
                    sendText(httpExchange, "");
                    return;
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
            case GET_SUBTASKS:
            case UNKNOWN:
                throw new IllegalStateException("Unsupported path for task controller: " + requestPath);
        }
        return endpoint;
    }
}
