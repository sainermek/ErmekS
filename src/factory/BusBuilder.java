package factory;

import model.Bus;

public class BusBuilder {
    private int id;
    private String number;
    private int capacity;

    public BusBuilder setId(int id) { this.id = id; return this; }
    public BusBuilder setNumber(String number) { this.number = number; return this; }
    public BusBuilder setCapacity(int capacity) { this.capacity = capacity; return this; }

    public Bus build() {
        return new Bus(id, number, capacity);
    }
}
