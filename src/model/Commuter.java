package model;
public class Commuter extends Person
implements Comparable<Commuter> {
    private int busId;
    public Commuter(int commuterId, String name, int busId) {
        super(name, 0);
        this.commuterId = commuterId;
        this.busId = busId;}
    private int commuterId;
    public int getCommuterId(){return commuterId;}
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