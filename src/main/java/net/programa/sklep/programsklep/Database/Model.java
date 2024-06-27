package net.programa.sklep.programsklep.Database;
import javafx.scene.control.Alert;
import net.programa.sklep.programsklep.utils;

import java.sql.*;

public class Model {
    public static Connection con = null;
    public int id;
    public static void connect () throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:main.db");
        con.setAutoCommit(true);
    }

    public static void disconnect () throws SQLException {
        con.close();
    }

    public static void init_connection(){
        try {
            connect();
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR,  "Error", "Error while connecting to database, try again later.", e.getMessage(), rs -> null);
            System.exit(-1);
        }
        try{
            User.create_table();
            Product.create_table();
            Setting.create_table();
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR,  "Error", "Error while connecting to database, try again later.", e.getMessage(), rs -> null);
            System.exit(-1);
        }

    }

    public void update() throws SQLException{}

    public static void create_table() throws SQLException{}

    public void delete() throws SQLException{}

    public void reload() throws SQLException{}

    public void insert() throws SQLException{}
    // Read methods are implemented in the subclasses
}
