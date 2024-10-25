package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.task.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SubtaskHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Gson gson = new Gson();
            String requestPath = httpExchange.getRequestURI().getPath();
            EndpointEnum endpoint = getEndpoint(requestPath, httpExchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_ELEMENTS: {
                    List<Subtask> tasks = Managers.getTaskManager().getAllSubtask();
                    sendText(httpExchange, gson.toJson(tasks));
                    return;
                }
                case GET_ELEMENT_BY_ID: {
                    int id = getIdFromRequestPath(requestPath);
                    Subtask task = Managers.getTaskManager().getSubtaskById(id);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case CREATE_ELEMENT: {
                    Map<String, String> request =
                            gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Map.class);
                    String durationString = request.get("duration");
                    String startTimeString = request.get("startTime");
                    String epicIdString = request.get("epicId");

                    Duration duration = durationString == null ? null : Duration.parse(durationString);
                    LocalDateTime startTime = startTimeString == null ? null : LocalDateTime.parse(startTimeString);
                    Epic epic = Managers.getTaskManager().getEpicById(Integer.parseInt(epicIdString));

                    Subtask task = Managers.getTaskManager().createSubtask(request.get("title"), request.get("info"),
                            duration, startTime, epic);
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

                    Subtask task = Managers.getTaskManager().updateSubtask(id, request.get("title"),
                            request.get("info"), taskStatus, duration, startTime);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case DELETE_ELEMENT:
                    int id = getIdFromRequestPath(requestPath);
                    Managers.getTaskManager().deleteSubtaskById(id);
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
