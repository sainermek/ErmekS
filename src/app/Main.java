package app;

import model.*;
import Repository.*;
import java.util.List;
import java.io.IOException; // Добавили импорт для обработки ошибок сервера

public class Main {
    public static void main(String[] args) {
        // Репозитории
        PostgresBusRepository busRepo = new PostgresBusRepository();
        PostgresCommuterRepository commuterRepo = new PostgresCommuterRepository();

        System.out.println("=== Работа с Bus через Postgres ===");

        // 1. Очистим таблицы для стабильного теста
        List<Bus> oldBuses = busRepo.getAll();
        for (Bus b : oldBuses) {
            busRepo.deleteByNumber(b.number());
        }

        // Добавим автобусы
        busRepo.add(new Bus(0, "12A", 45));
        busRepo.add(new Bus(0, "20B", 50));
        busRepo.add(new Bus(0, "30C", 40));

        // Получаем автобусы, чтобы использовать их ID
        List<Bus> buses = busRepo.getAll();
        for (Bus b : buses) System.out.println(b);

        // Обновим вместимость
        busRepo.updateCapacity("12A", 48);

        // Удалим один автобус
        busRepo.deleteByNumber("20B");

        System.out.println("Список автобусов после изменений:");
        busRepo.getAll().forEach(System.out::println);

        System.out.println("\n=== Работа с Commuter через Postgres ===");

        // Очистим таблицу commuter
        List<Commuter> oldCommuters = commuterRepo.getAll();
        for (Commuter c : oldCommuters) {
            commuterRepo.deleteById(c.getCommuterId());
        }

        // Добавим пассажиров, привязав к существующему автобусу
        Bus bus12A = busRepo.getAll().stream()
                .filter(b -> b.number().equals("12A"))
                .findFirst().orElseThrow();

        commuterRepo.add(new Commuter(0, "Ermek", bus12A.id()));
        commuterRepo.add(new Commuter(0, "Aidar", bus12A.id()));

        System.out.println("Список пассажиров после добавления:");
        commuterRepo.getAll().forEach(System.out::println);

        // Изменим автобус у пассажира
        Bus bus30C = busRepo.getAll().stream()
                .filter(b -> b.number().equals("30C"))
                .findFirst().orElseThrow();

        Commuter firstCommuter = commuterRepo.getAll().get(0);
        commuterRepo.updateBus(firstCommuter.getCommuterId(), bus30C.id());

        System.out.println("Список пассажиров после изменения автобуса у первого:");
        commuterRepo.getAll().forEach(System.out::println);

        //
        commuterRepo.deleteById(firstCommuter.getCommuterId());
        System.out.println("Список пассажиров после удаления первого:");
        commuterRepo.getAll().forEach(System.out::println);

        System.out.println("\n=== Работа с TransportService (сортировка и фильтрация) ===");
        TransportService service = new TransportService();
        service.addBus(new Bus(0, "10A", 30));
        service.addBus(new Bus(0, "20B", 50));
        service.addBus(new Bus(0, "30C", 40));

        System.out.println("Все автобусы:");
        service.getBuses().forEach(System.out::println);

        System.out.println("Автобусы отсортированные по вместимости:");
        service.sortByCapacity().forEach(System.out::println);

        System.out.println("Автобусы с вместимостью >= 40:");
        service.findByCapacity(40).forEach(System.out::println);

        // --- КОД ЗАПУСКА СЕРВЕРА ДОЛЖЕН БЫТЬ ВНУТРИ МЕТОДА MAIN ---
        System.out.println("\n=== Запуск REST API сервера ===");
        try {
            // Создаем объект контроллера, передавая ему репозиторий
            controller.TransportController apiController = new controller.TransportController(busRepo);
            apiController.startServer();
        } catch (IOException e) {
            System.err.println("Ошибка при старте сервера: " + e.getMessage());
        }
    } // Конец метода main
} // Конец класса Main