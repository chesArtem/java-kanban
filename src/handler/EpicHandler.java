package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import service.Managers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EpicHandler extends BaseHttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Gson gson = new Gson();
            String requestPath = httpExchange.getRequestURI().getPath();
            EndpointEnum endpoint = getEndpoint(requestPath, httpExchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_ELEMENTS: {
                    List<Epic> tasks = Managers.getTaskManager().getAllEpic();
                    sendText(httpExchange, gson.toJson(tasks));
                    return;
                }
                case GET_ELEMENT_BY_ID: {
                    int id = getIdFromRequestPath(requestPath);
                    Epic task = Managers.getTaskManager().getEpicById(id);
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case CREATE_ELEMENT: {
                    Map<String, String> request =
                            gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes()), Map.class);

                    Epic task = Managers.getTaskManager().createEpic(request.get("title"), request.get("info"));
                    sendText(httpExchange, gson.toJson(task));
                    return;
                }
                case GET_SUBTASKS: {
                    int id = getIdFromRequestPath(requestPath);
                    List<Subtask> subtaskList = Managers.getTaskManager().getEpicById(id).getListSubtask();
                    sendText(httpExchange, gson.toJson(subtaskList));
                    return;
                }
                case DELETE_ELEMENT:
                    int id = getIdFromRequestPath(requestPath);
                    Managers.getTaskManager().deleteEpicById(id);
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
            case UPDATE_ELEMENT:
            case UNKNOWN:
                throw new IllegalStateException("Unsupported path for task controller: " + requestPath);
        }
        return endpoint;
    }
}
