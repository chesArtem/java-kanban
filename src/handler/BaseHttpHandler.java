package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected EndpointEnum getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 2) {
                    return EndpointEnum.GET_ALL_ELEMENTS;
                }
                if (pathParts.length == 3) {
                    return EndpointEnum.GET_ELEMENT_BY_ID;
                }
                if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                    return EndpointEnum.GET_SUBTASKS;
                }
                break;
            case "POST":
                if (pathParts.length == 2) {
                    return EndpointEnum.CREATE_ELEMENT;
                }
                if (pathParts.length == 3) {
                    return EndpointEnum.UPDATE_ELEMENT;
                }
                break;
            case "DELETE":
                if (pathParts.length == 3) {
                    return EndpointEnum.DELETE_ELEMENT;
                }
                break;
        }
        return EndpointEnum.UNKNOWN;
    }

    protected int getIdFromRequestPath(String requestPath) {
        String[] pathParts = requestPath.split("/");
        return Integer.parseInt(pathParts[2]);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] resp = "Requested element not found".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] resp = "Requested element has unexpected connections".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        byte[] resp = "Internal server error".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

}
