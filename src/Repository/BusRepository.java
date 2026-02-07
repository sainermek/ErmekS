// BusRepository
package Repository;

import model.Bus;

import java.util.List;

public interface BusRepository {
    void add(Bus bus);
    List<Bus> getAll();
    void deleteByNumber(String number);

    static Bus createBus(int id, String number, int capacity) {
        return new Bus(id, number, capacity);
    }
}
