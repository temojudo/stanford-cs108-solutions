package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyDB {

    private static final String SERVER = "localhost:3306/";
    private static final String SCHEME = "oop_hw05";

    private static final String USER = "root";
    private static final String PASS = "root";

    private static Connection con = null;

    public MyDB() {
        if (con == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + SERVER + SCHEME, USER, PASS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return con;
    }
}