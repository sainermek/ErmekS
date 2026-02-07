// CommuterRepository
package model;
import java.util.List;

public interface CommuterRepository {
    void add(Commuter commuter);
    List<Commuter> getAll();
    void updateBus(int commuterId, int newBusId);
    void deleteById(int commuterId);
}
