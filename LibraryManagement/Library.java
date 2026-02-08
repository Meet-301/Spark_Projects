package Projects.LibraryManagement;

import java.sql.*;
import java.util.Scanner;

public class Library {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    DBConfigLibrary.URL,
                    DBConfigLibrary.username,
                    DBConfigLibrary.password
            );

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("\n-------- 📖Library Management System📖 --------");

                System.out.println("\n1. ➕ Add book");
                System.out.println("2. ➖ Remove book");
                System.out.println("3. 🔍 Search book");
                System.out.println("4. 📚 Issue book");
                System.out.println("5. 🔖 Return book");
                System.out.println("6. 📑 Show all books");
                System.out.println("7. 🔙 Exit");

                System.out.print("\nEnter your choice: ");
                int choice = scan.nextInt();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter book title: ");
                        String title = scan.next();

                        System.out.print("Enter book author: ");
                        String author = scan.next();

                        System.out.print("Enter book quantity: ");
                        int quantity = scan.nextInt();

                        if (quantity <= 0 || title.isBlank() || author.isBlank())
                            throw new IllegalArgumentException("Please enter valid data!!!");

                        String insertSql = "INSERT INTO books(title,author,quantity,initialQuantity) VALUES(?,?,?,?)";

                        PreparedStatement ps = con.prepareStatement(insertSql);
                        ps.setString(1, title);
                        ps.setString(2, author);
                        ps.setInt(3, quantity);
                        ps.setInt(4, quantity);

                        int rows = ps.executeUpdate();

                        System.out.println(rows > 0 ? "Book added ✅" : "Something went wrong!! DB error ❌");
                    }

                    case 2 -> {
                        System.out.print("Enter book title to remove the book: ");
                        String removeTitle = scan.next();

                        if (removeTitle.isBlank())
                            throw new IllegalArgumentException("Please enter title!!!");

                        String removeSql = "DELETE FROM books WHERE title = ?";

                        PreparedStatement psRemove = con.prepareStatement(removeSql);
                        psRemove.setString(1, removeTitle);

                        int rowCount = psRemove.executeUpdate();

                        System.out.println(rowCount > 0 ? "Book removed ✅" : "Book not found ❌");
                    }

                    case 3 -> {
                        System.out.print("Enter book title to search the book: ");
                        String searchTitle = scan.next();

                        if (searchTitle.isBlank())
                            throw new IllegalArgumentException("Please enter title!!!");

                        String searchSql = "SELECT * FROM books WHERE title = ?";

                        PreparedStatement psSearch = con.prepareStatement(searchSql);
                        psSearch.setString(1, searchTitle);

                        ResultSet resultSet = psSearch.executeQuery();

                        if (resultSet.next()) {
                            int available = resultSet.getInt(4);
                            System.out.print("Title: " + resultSet.getString(2) + " | " +
                                    "Author: " + resultSet.getString(3) + " | " +
                                    "Quantity: " + resultSet.getInt(4) + " | ");
                            System.out.print(available > 0 ? "Available: Yes" : "Available : No");
                        } else {
                            System.out.println("Book not found ❌");
                        }
                    }

                    case 4 -> {
                        System.out.print("Enter book title to issue the book: ");
                        String issueTitle = scan.next();

                        if (issueTitle.isBlank())
                            throw new IllegalArgumentException("Please enter title!!!");

                        String issueSql = "SELECT * FROM books WHERE title = ?";

                        PreparedStatement psIssue = con.prepareStatement(issueSql);
                        psIssue.setString(1, issueTitle);

                        ResultSet issueResultSet = psIssue.executeQuery();

                        if (issueResultSet.next()) {
                            int availability = issueResultSet.getInt(4);
                            if (availability > 0) {
                                String updateIssueSql = "UPDATE books SET quantity = quantity-1 WHERE title = ?";

                                PreparedStatement psUpdateIssue = con.prepareStatement(updateIssueSql);
                                psUpdateIssue.setString(1, issueTitle);
                                psUpdateIssue.executeUpdate();

                                System.out.println("Book issued ✅");

                            } else {
                                System.out.println("Book not available ❌");
                            }
                        } else {
                            System.out.println("Book not found ❌");
                        }
                    }

                    case 5 -> {
                        System.out.print("Enter book title to return the book: ");
                        String returnTitle = scan.next();

                        if (returnTitle == null)
                            throw new IllegalArgumentException("Please enter valid title!!!");

                        String returnSql = "SELECT * FROM books WHERE title = ?";

                        PreparedStatement psReturn = con.prepareStatement(returnSql);
                        psReturn.setString(1, returnTitle);

                        ResultSet returnResultSet = psReturn.executeQuery();

                        if (returnResultSet.next()) {
                            int initialQuantity = returnResultSet.getInt(5);
                            int currentQuantity = returnResultSet.getInt(4);

                            if (currentQuantity < initialQuantity) {
                                String updateIssueSql = "UPDATE books SET quantity = quantity+1 WHERE title = ?";

                                PreparedStatement psUpdateIssue = con.prepareStatement(updateIssueSql);
                                psUpdateIssue.setString(1, returnTitle);
                                psUpdateIssue.executeUpdate();

                                System.out.println("Book returned ✅");
                            } else {
                                System.out.println("That book hasn't issued yet ❌");
                            }
                        } else {
                            System.out.println("Book not found ❌");
                        }
                    }

                    case 6 -> {
                        String searchSql = "SELECT * FROM books";

                        Statement stmt = con.createStatement();

                        ResultSet resultSet = stmt.executeQuery(searchSql);

                        boolean found = false;
                        while (resultSet.next()) {
                            found = true;
                            int available = resultSet.getInt(4);
                            System.out.print("Title: " + resultSet.getString(2) + " | " +
                                    "Author: " + resultSet.getString(3) + " | " +
                                    "Quantity: " + resultSet.getInt(4) + " | ");
                            System.out.println(available > 0 ? "Available: Yes" : "Available: No");
                        }

                        if (!found)
                            System.out.println("Books not available ❌");

                    }

                    case 7 -> {
                        con.close();
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice ❌");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
