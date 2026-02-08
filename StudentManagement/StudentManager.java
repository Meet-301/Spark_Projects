package Projects.StudentManagement;

import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    DBConfigStudent.URL,
                    DBConfigStudent.username,
                    DBConfigStudent.password
            );

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("\n----- STUDENT MANAGEMENT SYSTEM -----");
                System.out.println("1. Insert Student");
                System.out.println("2. Update Marks");
                System.out.println("3. Delete Student");
                System.out.println("4. Search Student");
                System.out.println("5. Exit");

                System.out.print("\nEnter your choice: ");
                int choice = scan.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter roll no: ");
                        int rno = scan.nextInt();

                        System.out.print("Enter name: ");
                        String name = scan.next();

                        System.out.print("Enter marks: ");
                        double marks = scan.nextInt();

                        // Insert Query
                        String query = "INSERT INTO student VALUES(?,?,?)";
                        PreparedStatement ps = con.prepareStatement(query);
                        ps.setInt(1, rno);
                        ps.setString(2, name);
                        ps.setDouble(3, marks);

                        int rows = ps.executeUpdate();
                        System.out.println(rows + " rows inserted");
                        break;
                    case 2:
                        System.out.print("Enter roll number to update marks: ");
                        int rollNo = scan.nextInt();

                        System.out.print("Enter marks: ");
                        double updatedMarks = scan.nextInt();

                        // Update Query
                        String updateQuery = "UPDATE student SET marks = ? WHERE rollno = ?";

                        PreparedStatement updatePreparedStmt = con.prepareStatement(updateQuery);
                        updatePreparedStmt.setDouble(3, updatedMarks);
                        updatePreparedStmt.setInt(1, rollNo);

                        int updatedRecords = updatePreparedStmt.executeUpdate();

                        if (updatedRecords > 0)
                            System.out.println(rollNo + "'s marks updated successfully");
                        else
                            System.err.println("Roll number " + rollNo + "not found!!!");

                        break;
                    case 3:
                        System.out.print("Enter roll number to delete the student: ");
                        int studentRollNo = scan.nextInt();

                        // Delete Query
                        String deleteQuery = "DELETE FROM student WHERE rollno = ?";

                        PreparedStatement deletePreparedStmt = con.prepareStatement(deleteQuery);
                        deletePreparedStmt.setInt(1, studentRollNo);

                        int deletedRecords = deletePreparedStmt.executeUpdate();

                        if (deletedRecords > 0)
                            System.out.println(studentRollNo + " deleted successfully");
                        else
                            System.err.println("Roll number " + studentRollNo + " not found!!!");

                        break;
                    case 4:
                        System.out.print("Enter roll number to search the student: ");
                        int rollNumber = scan.nextInt();

                        // Search(Select) Query
                        String searchQuery = "SELECT * FROM student WHERE rollno = ?";

                        PreparedStatement selectPreparedStmt = con.prepareStatement(searchQuery);
                        selectPreparedStmt.setInt(1, rollNumber);

                        ResultSet rs = selectPreparedStmt.executeQuery();

                        if (rs.next())
                            System.out.println("Roll no: " + rs.getInt(1) + " | "
                                    + "Name: " + rs.getString(2) + " | "
                            + "Marks: " + rs.getDouble(3));
                        else
                            System.err.println("Roll number " + rollNumber + "not found!!!");
                        break;
                    case 5:
                        con.close();

                        System.exit(0);
                    default:
                        System.err.println("Invalid choice!!!");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
