//PostgresBusRepository
package Repository;

import model.Bus;
import model.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class PostgresBusRepository implements BusRepository {

    @Override
    public void add(Bus bus) {
        String sql = """
                     INSERT INTO bus (number, capacity) 
                     VALUES (?, ?);
                     """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bus.number());
            ps.setInt(2, bus.capacity());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<Bus> getAll() {
        List<Bus> buses = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM bus")) {
            while (rs.next()) {
                buses.add(new Bus(rs.getInt("id"), rs.getString("number"), rs.getInt("capacity")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return buses;
    }

    @Override
    public void deleteByNumber(String number) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM bus WHERE number = ?")) {
            ps.setString(1, number);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateCapacity(String number, int newCapacity) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "UPDATE bus SET capacity = ? WHERE number = ?")) {
            ps.setInt(1, newCapacity);
            ps.setString(2, number);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
