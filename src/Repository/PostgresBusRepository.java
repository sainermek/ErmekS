package Repository;

import model.Bus;
import model.DBConnection;
import model.EntityNotFoundException;
import model.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class PostgresBusRepository implements BusRepository {

    @Override
    public void add(Bus bus) {
        String sql = "INSERT INTO bus (number, capacity) VALUES (?, ?);";
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


    public void updateCapacity(String number, int newCapacity) throws EntityNotFoundException, ValidationException {
        // Проверка валидности данных (ValidationException)
        if (newCapacity < 0) {
            throw new ValidationException("Вместимость не может быть отрицательной: " + newCapacity);
        }

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE bus SET capacity = ? WHERE number = ?")) {
            ps.setInt(1, newCapacity);
            ps.setString(2, number);
            int rowsAffected = ps.executeUpdate();


            if (rowsAffected == 0) {
                throw new EntityNotFoundException("Автобус с номером " + number + " не найден в базе данных.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}