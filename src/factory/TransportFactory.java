package factory;

import model.Bus;
import model.Commuter;

public class TransportFactory {

    public static Bus createBus(int id, String number, int capacity) {
        return new Bus(id, number, capacity);
    }

    public static Commuter createCommuter(int id, String name, int busId) {
        return new Commuter(id, name, busId);
    }
}
