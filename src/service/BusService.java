package service;

import model.Bus;
import java.util.List;
import java.util.stream.Collectors;

public class BusService {

    private List<Bus> buses;

    public BusService(List<Bus> buses) {
        this.buses = buses;
    }

    public List<Bus> sortByCapacity() {
        return buses.stream()
                .sorted((a,b) -> Integer.compare(a.capacity(), b.capacity()))
                .collect(Collectors.toList());
    }

    public List<Bus> findByCapacity(int min) {
        return buses.stream()
                .filter(b -> b.capacity() >= min)
                .collect(Collectors.toList());
    }
}
