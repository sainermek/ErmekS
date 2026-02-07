package controller;
//
import Repository.BusRepository;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Bus;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class TransportController {
    private final BusRepository busRepository;

    // DIP: Зависим от интерфейса, а не реализации
    public TransportController(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint: http://localhost:8080/buses
        server.createContext("/buses", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    List<Bus> buses = busRepository.getAll();

                    // Превращаем список в простой JSON массив вручную [cite: 4]
                    String jsonResponse = "[" + buses.stream()
                            .map(b -> String.format("{\"id\":%d, \"number\":\"%s\", \"capacity\":%d}",
                                    b.id(), b.number(), b.capacity()))
                            .collect(Collectors.joining(",")) + "]";

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080. Try: http://localhost:8080/buses");
    }
}