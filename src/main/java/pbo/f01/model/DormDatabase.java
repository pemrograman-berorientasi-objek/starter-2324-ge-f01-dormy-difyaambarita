package pbo.f01.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                           "dormname TEXT NOT NULL PRIMARY KEY, " +
                           "capacity INTEGER NOT NULL, " +
                           "gender TEXT NOT NULL);";

        String studentTable = "CREATE TABLE IF NOT EXISTS student (" +
                              "id TEXT NOT NULL PRIMARY KEY, " +
                              "name TEXT NOT NULL, " +
                              "angkatan INTEGER NOT NULL, " +
                              "gender TEXT NOT NULL, " +
                              "dorm_name TEXT, " +
                              "FOREIGN KEY(dorm_name) REFERENCES dorm(name));";

        Statement statement = this.getConnection().createStatement();
        statement.execute(dormTable);
        statement.execute(studentTable);
        statement.close();
    }
        

    public void dormAdd(String dormname, int capacity, String gender) throws SQLException {
        String sql = "INSERT INTO dorm (name, capacity, gender) VALUES (?, ?, ?)";
        PreparedStatement statement = this.getConnection().prepareStatement(sql);
        statement.setString(1, dormname);
        statement.setInt(2, capacity);
        statement.setString(3, gender);
        statement.executeUpdate();
        statement.close();
    }

    public void studentAdd(String id, String name, int angkatan, String gender) throws SQLException {
        //student-add#<id>#<name>#<year>#<gender>
        String sql = "INSERT INTO student student (id, name, angkatan, gender) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = this.getConnection().prepareStatement(sql);
        statement.setString(1, id);
        statement.setString(2, name);
        statement.setInt(3, angkatan);
        statement.setString(4, gender);
        
        statement.executeUpdate();
        statement.close();
    }

    



    public void printDorms() throws SQLException {
        Statement statement = this.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM dorm");
        while (resultSet.next()) {
            System.out.println(
                resultSet.getString("name") + "|" +
                resultSet.getInt("capacity") + "|" +
                resultSet.getString("gender")
            );
        }
        resultSet.close();
        statement.close();
    }

    public void printStudents() throws SQLException {
        Statement statement = this.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM student");
        while (resultSet.next()) {
            System.out.println(
                resultSet.getString("id") + "|" +
                resultSet.getString("name") + "|" +
                resultSet.getInt("angkatan") + "|" +
                resultSet.getString("gender") + "|" +
                resultSet.getString("dorm_name")
            );
        }
        resultSet.close();
        statement.close();
    }

    public static void main(String[] args) {
        try {
            DormDatabase db = new DormDatabase("dorm.db");
            db.createTables();
            db.printDorms();
            db.printStudents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
