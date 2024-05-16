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
        String deleteStudentTable = "DROP TABLE IF EXISTS student";
        db.getConnection().createStatement().executeUpdate(deleteStudentTable);

        String deleteDormTable = "DROP TABLE IF EXISTS dorm";
        db.getConnection().createStatement().executeUpdate(deleteDormTable);

        String createDormTable = "CREATE TABLE IF NOT EXISTS dorm (name VARCHAR(255), gender VARCHAR(255), capacity INT, current_count INT DEFAULT 0)";
        db.getConnection().createStatement().executeUpdate(createDormTable);

        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (id VARCHAR(255), name VARCHAR(255), angkatan INT, gender VARCHAR(255), dorm_name VARCHAR(255) DEFAULT NULL)";
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
        // Check if the student already exists
        String checkStudent = "SELECT COUNT(*) FROM student WHERE id = '" + id + "'";
        ResultSet resultSet = db.getConnection().createStatement().executeQuery(checkStudent);
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
    
        if (count == 0) {
            // Student does not exist, insert new student
            String insertStudent = "INSERT INTO student (id, name, angkatan, gender) VALUES ('" + id + "', '" + name + "', " + angkatan + ", '" + gender + "')";
            db.getConnection().createStatement().executeUpdate(insertStudent);
            //System.out.println("Student " + name + " added successfully.");
        } 
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
            String insertDorm = "INSERT INTO dorm (name, capacity, gender, current_count) VALUES ('" + name + "', " + capacity + ", '" + gender + "', 0)";
            db.getConnection().createStatement().executeUpdate(insertDorm);
        }
    }

    private static void assignStudentToDorm(DormDatabase db, String studentId, String dormName) throws SQLException {
        // Check if the dorm has available capacity and matching gender
        String checkCapacity = "SELECT capacity, current_count, gender FROM dorm WHERE name = '" + dormName + "'";
        ResultSet resultSet = db.getConnection().createStatement().executeQuery(checkCapacity);
        if (resultSet.next()) {
            int capacity = resultSet.getInt("capacity");
            int currentCount = resultSet.getInt("current_count");
            String dormGender = resultSet.getString("gender");

            // Get student gender
            String checkStudentGender = "SELECT gender FROM student WHERE id = '" + studentId + "'";
            ResultSet studentResult = db.getConnection().createStatement().executeQuery(checkStudentGender);
            if (studentResult.next()) {
                String studentGender = studentResult.getString("gender");

                // Check if genders match
                if (!dormGender.equals(studentGender)) {
                    //System.out.println("Gender does not match.");
                    return;
                }

                // Check if there is space in the dorm
                if (currentCount >= capacity) {
                    // System.out.println("The dorm is full.");
                    return;
                }

                // Update the student record to assign the dorm
                String updateStudent = "UPDATE student SET dorm_name = '" + dormName + "' WHERE id = '" + studentId + "'";
                db.getConnection().createStatement().executeUpdate(updateStudent);

                // Update current_count in dorm table
                String updateDorm = "UPDATE dorm SET current_count = current_count + 1 WHERE name = '" + dormName + "'";
                db.getConnection().createStatement().executeUpdate(updateDorm);
            }
            studentResult.close();
        }
        resultSet.close();
    }
}
