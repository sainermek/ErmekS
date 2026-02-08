package Controller;

import Repository.BusRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Bus;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class TransportController {
    private final BusRepository busRepository;

    public TransportController(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/buses", exchange -> {
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                handleGet(exchange);
            } else if ("POST".equals(method)) {
                handlePost(exchange);
            } else if ("DELETE".equals(method)) {
                handleDelete(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("REST API Server started on port 8080.");
    }

    // Обработка GET (Получение всех)
    private void handleGet(HttpExchange exchange) throws IOException {
        List<Bus> buses = busRepository.getAll();
        String jsonResponse = "[" + buses.stream()
                .map(b -> String.format("{\"id\":%d, \"number\":\"%s\", \"capacity\":%d}",
                        b.id(), b.number(), b.capacity()))
                .collect(Collectors.joining(",")) + "]";
        sendResponse(exchange, jsonResponse, 200);
    }

    // Обработка POST (Добавление нового через JSON)
    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        // Очень простой парсинг JSON (ищем значения между кавычками)
        // Ожидаемый формат: {"number":"55X", "capacity":60}
        try {
            String number = body.split("\"number\":\"")[1].split("\"")[0];
            String capStr = body.split("\"capacity\":")[1].split("}")[0].trim();
            int capacity = Integer.parseInt(capStr);

            busRepository.add(new Bus(0, number, capacity));
            sendResponse(exchange, "{\"status\":\"Bus added successfully\"}", 201);
        } catch (Exception e) {
            sendResponse(exchange, "{\"error\":\"Invalid JSON format\"}", 400);
        }
    }

    // Обработка DELETE
    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.contains("number=")) {
            String busNum = query.split("=")[1];
            busRepository.deleteByNumber(busNum);
            sendResponse(exchange, "{\"message\":\"Bus deleted\"}", 200);
        } else {
            sendResponse(exchange, "{\"error\":\"Parameter 'number' is required\"}", 400);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}