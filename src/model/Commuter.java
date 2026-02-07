// Commuter
package model;
public class Commuter extends Person implements Comparable<Commuter> {
    private int commuterId;
    private int busId; // Добавляем это поле, чтобы связать с БД

    // Обновляем конструктор под твою таблицу в БД (id, name, bus_id)
    public Commuter(int commuterId, String name, int busId) {
        super(name, 0); // возраст пока ставим 0, так как в БД его нет
        this.commuterId = commuterId;
        this.busId = busId;
    }

    public int getCommuterId() { return commuterId; }
    public int getBusId() { return busId; }

    @Override
    public void displayInfo() { System.out.println(this); }

    @Override
    public int compareTo(Commuter other) { return Integer.compare(this.commuterId, other.commuterId); }

    @Override
    public String toString() {
        return "Commuter [ID=" + commuterId + ", Name=" + name + ", BusID=" + busId + "]";
    }
}