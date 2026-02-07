// TransportService
package model;

import java.util.ArrayList;
import java.util.List;

public class TransportService {
    private List<Bus> buses = new ArrayList<>();

    public void addBus(Bus bus) {
        buses.add(bus);
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public List<Bus> findByCapacity(int min) {
        return buses.stream()
                .filter(b -> b.capacity() >= min)
                .toList();
    }

    public Bus findByNumber(String number) {
        return buses.stream()
                .filter(b -> b.number().equals(number))
                .findFirst()
                .orElse(null);
    }

    public List<Bus> sortByCapacity() {
        return buses.stream()
                .sorted((b1, b2) -> Integer.compare(b1.capacity(), b2.capacity()))
                .toList();
    }
}
