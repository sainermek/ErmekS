package Repository;

import model.Bus;
import java.util.List;

public interface BusRepository {
    void add(Bus bus);
    List<Bus> getAll();
    void deleteByNumber(String number);


    default <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }


    static void printSystemInfo() {
        System.out.println("Transport System Repository v1.0");
    }
}