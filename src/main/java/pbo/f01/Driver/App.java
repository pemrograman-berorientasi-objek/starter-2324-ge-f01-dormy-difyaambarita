package pbo.f01.Driver;

import pbo.f01.model.DormDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            DormDatabase database = new DormDatabase("jdbc:h2:./db/dormy");
            createTablesIfNotExist(database);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String input = scanner.nextLine();
                if (input.equals("---")) {
                    break;
                }

                String[] tokens = input.split("#");
                String command = tokens[0];

                switch (command) {
                    case "display-all":
                        displayAll(database);
                        break;
                    case "student-add":
                        // student-add#<id>#<name>#<year>#<gender>
                        String id = tokens[1];
                        String name = tokens[2];
                        int angkatan = Integer.parseInt(tokens[3]);
                        String gender = tokens[4];
                        registerStudent(database, id, name, angkatan, gender);
                        break;
                    case "dorm-add":
                        // dorm-add#<name>#<capacity>#<gender>
                        String dormName = tokens[1];
                        int capacity = Integer.parseInt(tokens[2]);
                        String dormGender = tokens[3];
                        registerDorm(database, dormName, capacity, dormGender);
                        break;
                    case "assign":
                        // assign#<student-id>#<dorm-name>
                        String studentId = tokens[1];
                        String dormNameAssign = tokens[2];
                        assignStudentToDorm(database, studentId, dormNameAssign);
                        break;
                }
            }

            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExist(DormDatabase db) throws SQLException {
        String createDormTable = "CREATE TABLE IF NOT EXISTS dorm (name VARCHAR(255), gender VARCHAR(255), capacity INT, current_count INT)";
        db.getConnection().createStatement().executeUpdate(createDormTable);

        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (id VARCHAR(255), name VARCHAR(255), angkatan INT, gender VARCHAR(255), dorm_name VARCHAR(255))";
        db.getConnection().createStatement().executeUpdate(createStudentTable);
    }

    private static void displayAll(DormDatabase db) throws SQLException {
        String queryDorms = "SELECT * FROM dorm ORDER BY name ASC";
        ResultSet dorms = db.getConnection().createStatement().executeQuery(queryDorms);

        while (dorms.next()) {
            String dormName = dorms.getString("name");
            String dormGender = dorms.getString("gender");
            int capacity = dorms.getInt("capacity");
            int currentCount = dorms.getInt("current_count");
            System.out.println(dormName + "|" + dormGender + "|" + capacity + "|" + currentCount);

            String queryStudents = "SELECT * FROM student WHERE dorm_name = '" + dormName + "' ORDER BY name ASC";
            ResultSet students = db.getConnection().createStatement().executeQuery(queryStudents);

            while (students.next()) {
                String studentId = students.getString("id");
                String studentName = students.getString("name");
                int studentYear = students.getInt("angkatan");
                System.out.println(studentId + "|" + studentName + "|" + studentYear);
            }
            students.close();
        }
        dorms.close();
    }

    private static void registerStudent(DormDatabase db, String id, String name, int angkatan, String gender) throws SQLException {
        String insertStudent = "INSERT INTO student (id, name, angkatan, gender) VALUES ('" + id + "', '" + name + "', " + angkatan + ", '" + gender + "')";
        db.getConnection().createStatement().executeUpdate(insertStudent);
    }

    private static void registerDorm(DormDatabase db, String name, int capacity, String gender) throws SQLException {
        // Check if the dorm already exists
        String checkDorm = "SELECT COUNT(*) FROM dorm WHERE name = '" + name + "'";
        ResultSet resultSet = db.getConnection().createStatement().executeQuery(checkDorm);
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();

        if (count == 0) {
            // Dorm does not exist, insert it
            String insertDorm = "INSERT INTO dorm (name, capacity, gender) VALUES ('" + name + "', " + capacity + ", '" + gender + "')";
            db.getConnection().createStatement().executeUpdate(insertDorm);
        }
    }

    private static void assignStudentToDorm(DormDatabase db, String studentId, String dormName) throws SQLException {
        String updateStudent = "UPDATE student SET dorm_name = '" + dormName + "' WHERE id = '" + studentId + "'";
        db.getConnection().createStatement().executeUpdate(updateStudent);

        // Update current_count in dorm table
        String updateDorm = "UPDATE dorm SET current_count = current_count + 1 WHERE name = '" + dormName + "'";
        db.getConnection().createStatement().executeUpdate(updateDorm);
    }
}

