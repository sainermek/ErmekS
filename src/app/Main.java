package app;

import model.*;
import Repository.*;
import Controller.TransportController; // Импорт с маленькой буквы, как в твоем файле TransportController
import java.util.List;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Вызов статического метода интерфейса (Requirement: Language Features)
        BusRepository.printSystemInfo();

        PostgresBusRepository busRepo = new PostgresBusRepository();
        PostgresCommuterRepository commuterRepo = new PostgresCommuterRepository();

        System.out.println("\n=== Очистка данных (учитывая внешние ключи) ===");
        try {
            // ВАЖНО: Сначала удаляем пассажиров, так как они ссылаются на автобусы
            List<Commuter> currentCommuters = commuterRepo.getAll();
            currentCommuters.forEach(c -> commuterRepo.deleteById(c.getCommuterId()));
            System.out.println("Таблица Commuter очищена.");

            // ТЕПЕРЬ удаляем автобусы
            List<Bus> currentBuses = busRepo.getAll();
            currentBuses.forEach(b -> busRepo.deleteByNumber(b.number()));
            System.out.println("Таблица Bus очищена.");
        } catch (Exception e) {
            System.err.println("Ошибка при очистке БД: " + e.getMessage());
        }

        System.out.println("\n=== Работа с Bus через Postgres ===");
        // Добавление новых данных (теперь без ошибок уникальности)
        busRepo.add(new Bus(0, "12A", 45));
        busRepo.add(new Bus(0, "20B", 50));
        busRepo.add(new Bus(0, "30C", 40));

        // Работа с исключениями (Requirement: Exception Handling)
        try {
            System.out.println("Попытка обновления вместимости...");
            busRepo.updateCapacity("12A", 48);
            System.out.println("Обновление успешно.");
        } catch (EntityNotFoundException | ValidationException e) {
            System.err.println("ПЕРЕХВАЧЕНО ИСКЛЮЧЕНИЕ: " + e.getMessage());
        }

        // Получение списка из БД
        List<Bus> busList = busRepo.getAll();

        // Использование Default метода и Generics (Requirement: Language Features)
        if (!busRepo.isEmpty(busList)) {
            System.out.println("Список автобусов в базе:");
            busList.forEach(b -> System.out.println(b));
        }

        System.out.println("\n=== Работа с Commuter через Postgres ===");
        // Находим ID автобуса 12A для связи
        Bus targetBus = busList.stream()
                .filter(b -> b.number().equals("12A"))
                .findFirst()
                .orElse(busList.get(0));

        // Добавляем пассажиров
        commuterRepo.add(new Commuter(0, "Ermek", targetBus.id()));
        commuterRepo.add(new Commuter(0, "Aidar", targetBus.id()));

        System.out.println("Пассажиры в базе данных:");
        commuterRepo.getAll().forEach(c -> System.out.println(c));

        System.out.println("\n=== Работа с TransportService (In-Memory Data Pool) ===");
        // Requirement: Поиск, фильтрация и сортировка в коллекции
        TransportService service = new TransportService();
        busList.forEach(b -> service.addBus(b));

        System.out.println("Сортировка по вместимости (Stream API):");
        service.sortByCapacity().forEach(b -> System.out.println(b));

        System.out.println("Фильтрация (вместимость >= 45):");
        service.findByCapacity(45).forEach(b -> System.out.println(b));

        System.out.println("\n=== Запуск REST API сервера ===");
        try {
            TransportController apiController = new TransportController(busRepo);
            apiController.startServer();
            System.out.println("Сервер запущен! Проверь данные тут: http://localhost:8080/buses");
        } catch (IOException e) {
            System.err.println("Ошибка старта сервера: " + e.getMessage());
        }
    }
}