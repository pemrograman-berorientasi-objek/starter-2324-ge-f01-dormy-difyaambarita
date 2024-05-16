package pbo.f01.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DormDatabase {

    private Connection connection;

    public DormDatabase(String file) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:./db/dormy");
    }

    public Connection getConnection() {
        return this.connection;
    }

    protected void createTables() throws SQLException {
        String dormTable = "CREATE TABLE IF NOT EXISTS dorm (" +
                           "name VARCHAR(255) NOT NULL PRIMARY KEY, " +
                           "capacity INT NOT NULL, " +
                           "gender VARCHAR(255) NOT NULL, " +
                           "current_count INT DEFAULT 0)";

        String studentTable = "CREATE TABLE IF NOT EXISTS student (" +
                              "id VARCHAR(255) NOT NULL PRIMARY KEY, " +
                              "name VARCHAR(255) NOT NULL, " +
                              "angkatan INT NOT NULL, " +
                              "gender VARCHAR(255) NOT NULL, " +
                              "dorm_name VARCHAR(255), " +
                              "FOREIGN KEY(dorm_name) REFERENCES dorm(name))";

        connection.createStatement().execute(dormTable);
        connection.createStatement().execute(studentTable);
    }
}
