// PostgresCommuterRepository
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresCommuterRepository implements CommuterRepository {

    @Override
    public void add(Commuter commuter) {
        String sql = "INSERT INTO commuter (name, bus_id) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, commuter.getName());
            ps.setInt(2, commuter.getBusId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commuter> getAll() {
        List<Commuter> commuters = new ArrayList<>();
        String sql = "SELECT * FROM commuter";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                commuters.add(new Commuter(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("bus_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commuters;
    }

    @Override
    public void updateBus(int commuterId, int newBusId) {
        String sql = "UPDATE commuter SET bus_id = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, newBusId);
            ps.setInt(2, commuterId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int commuterId) {
        String sql = "DELETE FROM commuter WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, commuterId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
