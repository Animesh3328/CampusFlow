package com.campusflow.jdbc;

import java.sql.*;

public class JdbcReportService {

    private final String url = "jdbc:h2:./data/campusflow";

    public int countBookings() throws SQLException {
        String sql = "select count(*) from bookings";

        try (Connection connection = DriverManager.getConnection(url, "sa", "");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            return result.next() ? result.getInt(1) : 0;
        }
    }
}
