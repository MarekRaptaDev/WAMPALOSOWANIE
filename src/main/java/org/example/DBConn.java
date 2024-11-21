package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class DBConn {
    //    DBConn - klasę do obsługi bazy danych (SQLite),
//    powinna udostępniać następujące metody: connect, createTables, insertRow, dropTables, disconnect.
    private Connection conn;
    private final String link;

    public DBConn(String link) {
        this.link = "jdbc:sqlite:" + link;
    }

    public void createTest(String name) {
        String createString = "CREATE TABLE IF NOT EXISTS " + name + " (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    content VARCHAR(255) NOT NULL,\n" +
                "    quantity INT NOT NULL,\n" +
                "    prem DECIMAL(10, 2)\n" +
                ");";
        String DataString = "INSERT INTO " + name + " (content, quantity, prem) VALUES\n" +
                "('Apple', 10, 1.50),\n" +
                "('Banana', 5, 0.75),\n" +
                "('Orange', 0, 0.85),\n" +
                "('Grapes', 12, 2.20),\n" +
                "('Pineapple', 7, 1.80);\n";
        try {
            Statement s = conn.createStatement();
            s.executeUpdate("Drop table " + name);
            s.executeUpdate(createString);
            s.executeUpdate(DataString);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void showRows(String name, int premium) {
        try {
            if (conn == null) {
                System.out.println("Something went wrong please try again");
                return;
            }
            Statement s = conn.createStatement();
            s.setQueryTimeout(30);
            ArrayList<String> results = new ArrayList<>();
            ResultSet rs;
            if (premium == 0) {
                rs = s.executeQuery("Select * from " + name + " Where prem == 0");
            } else {
                rs = s.executeQuery("Select * from " + name + " Where id != -1");
            }

            while (rs.next()) {
                results.add(rs.getString("content"));
            }
            if (!results.isEmpty()) {
                Random r = new Random();
                boolean accepted = false;
                while (!accepted) {
                    String result = results.get(r.nextInt(results.size()));
                    JOptionPane op = new JOptionPane();
                    int response = JOptionPane.showConfirmDialog(null, "Twoja nagroda to " + result + " Czy chcesz ją odebrać?", "pytanie", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        String updateQuery = "UPDATE " + name + " SET quantity = quantity - 1 WHERE content = ? ;";
                        String deleteQuery = "DELETE FROM " + name + " WHERE content = ? AND quantity = 0;";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

                            updateStmt.setString(1, result);
                            deleteStmt.setString(1, result);

                            System.out.println(updateStmt.executeUpdate());
                            System.out.println(deleteStmt.executeUpdate());

                            accepted = true;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (response == JOptionPane.NO_OPTION) {

                        JOptionPane.showMessageDialog(null, "Skoro przedmiot się nie spodobał spróbujemy jeszce raz.");

                    }
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");
        }

    }

    public void connect() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(link);
                System.out.println("Connection with database is open\n");

            } else {
                System.out.println("Something went wrong, try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");
        }
    }

    public void NZS(String name) {
        Random r = new Random();
        boolean finished = false;
        while (!finished) {
            String response = JOptionPane.showInputDialog("Co byś chciał od Mikołaja?");

            try {
                if (conn == null) {
                    System.out.println("Something went wrong please try again");
                    return;
                }
                Statement s = conn.createStatement();
                ResultSet rs;
                rs = s.executeQuery("SELECT * FROM " + name + " WHERE LOWER(content) LIKE LOWER('%" + response + "%')");
                ArrayList<String> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rs.getString("content"));
                }
                if(results.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Wygląda na to że już tego nie mamy spróbuj jeszcze raz :(");
                    break;
                }else if (results.size()==1){
                    JOptionPane.showMessageDialog(null,"Gratulacje twoja nagroda to "+ results.get(0));
                    String updateQuery = "UPDATE " + name + " SET quantity = quantity - 1 WHERE content = ? ;";
                    String deleteQuery = "DELETE FROM " + name + " WHERE content = ? AND quantity = 0;";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                         PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

                        updateStmt.setString(1, results.get(0));
                        deleteStmt.setString(1, results.get(0));

                        System.out.println(updateStmt.executeUpdate());
                        System.out.println(deleteStmt.executeUpdate());

                        finished = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Wygląda na to że więcej zestawów ma to co byś chciał, dlatego wylosujemy ci który dostaniesz :)");
                    String result = results.get(r.nextInt(results.size()));
                    String updateQuery = "UPDATE " + name + " SET quantity = quantity - 1 WHERE content = ? ;";
                    String deleteQuery = "DELETE FROM " + name + " WHERE content = ? AND quantity = 0;";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                         PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

                        updateStmt.setString(1, result);
                        deleteStmt.setString(1, result);

                        System.out.println(updateStmt.executeUpdate());
                        System.out.println(deleteStmt.executeUpdate());

                        finished = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    public void createTables(String name) {
        try {

            PreparedStatement s = conn.prepareStatement("CREATE TABLE  " + name + " (content,quantity,prem);");

            s.setQueryTimeout(30);
            s.executeUpdate();
            System.out.println("Tabela została dodana \n");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");
        }
    }

    public void insertRow(String q, String name) {
        try {

            PreparedStatement s2 = conn.prepareStatement("Insert into " + name + " (content,quantity,prem) values(?);");
            s2.setString(1, q);
            s2.executeUpdate();
            System.out.println("row added to database\n");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");
        }
    }

    public void dropTables(String name) {
        try {
            PreparedStatement s = conn.prepareStatement("DROP TABLE " + name + ";");

            s.setQueryTimeout(30);
            s.executeUpdate();
            System.out.println("Table " + name + " dropped\n");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");

        }
    }

    public void disconect() {
        try {
            if (conn != null && !(conn.isClosed())) {
                conn.close();
                System.out.println("Database connection closed\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something went wrong, try again.");

        }
    }
}

