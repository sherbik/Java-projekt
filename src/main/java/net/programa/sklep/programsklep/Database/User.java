package net.programa.sklep.programsklep.Database;

import net.programa.sklep.programsklep.Application;
import net.programa.sklep.programsklep.RootUserViewController;
import net.programa.sklep.programsklep.UuserView;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User extends Model {
    public static User currentUser = null;
    public int id;
    public String login;
    public String password;
    public double balance;
    public double total_spend;
    public boolean is_regular;


    public boolean is_root(){
        return login.equals("root");
    }

    public void ShowForm(){
        if (is_root()){
            RootUserViewController.open_form(Application.currentStage);
        }
        else {
            UuserView.open_form(Application.currentStage);
        }
    }

    public static void create_table() throws SQLException {
        Statement stm = con.createStatement();
        stm.executeUpdate("""
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    login STRING,
    password STRING,
    balance DOUBLE,
    total_spend DOUBLE,
    is_regular BOOLEAN
)
""");
        if (!is_user_exist("root")){
            if (register("root", "root") == null){
                throw new RuntimeException("Could not register root user");
            }
        }
    }

    public static User get_by_login(String login) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM users where login = ?");
        stm.setString(1, login);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return get_from_set(rs);
        }
        return null;
    }

    public static User get_from_set(ResultSet rs) throws SQLException {
        User usr = new User();
        usr.id = rs.getInt("id");
        usr.login = rs.getString("login");
        usr.password = rs.getString("password");
        usr.balance = rs.getDouble("balance");
        usr.total_spend = rs.getDouble("total_spend");
        usr.is_regular = rs.getBoolean("is_regular");
        return usr;
    }

    public static boolean is_user_exist(String login) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM users WHERE login = ?");
        stm.setString(1, login);
        ResultSet rs = stm.executeQuery();
        return rs.next();
    }
    public static User register(String login, String password) throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO users (login, password, balance, total_spend, is_regular) VALUES (?, ?, 0, 0, false)");
        stm.setString(1, login);
        stm.setString(2, password);
        stm.executeUpdate();
        return login(login, password);
    }

    public static User login(String login, String password) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?");
        stm.setString(1, login);
        stm.setString(2, password);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            User usr = new User();
            usr.id = rs.getInt("id");
            usr.login = rs.getString("login");
            usr.password = rs.getString("password");
            usr.balance = rs.getDouble("balance");
            usr.total_spend = rs.getDouble("total_spend");
            usr.is_regular = rs.getBoolean("is_regular");
            return usr;
        }
        return null;
    }

    public void update() throws SQLException {
        PreparedStatement stm = con.prepareStatement("UPDATE users SET login = ?, password = ?, balance = ?, total_spend = ?, is_regular = ? WHERE id = ?");
        stm.setString(1, this.login);
        stm.setString(2, this.password);
        stm.setDouble(3, this.balance);
        stm.setDouble(4, this.total_spend);
        stm.setBoolean(5, this.is_regular);
        stm.setInt(6, this.id);
        stm.executeUpdate();
    }

    public void insert() throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO users (login, password, balance, total_spend, is_regular) VALUES(?,?,?,?,?,?)");
        stm.setString(1, this.login);
        stm.setString(2, this.password);
        stm.setDouble(3, this.balance);
        stm.setDouble(4, this.total_spend);
        stm.setBoolean(5, is_regular);
        stm.executeUpdate();
    }

    public void delete() throws SQLException {
        PreparedStatement stm = con.prepareStatement("DELETE FROM users WHERE id = ?");
        stm.setInt(1, this.id);
        stm.executeUpdate();
    }

    @Override
    public void reload() throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM users WHERE id = ?");
        stm.setInt(1, this.id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            this.login = rs.getString("login");
            this.password = rs.getString("password");
            this.balance = rs.getDouble("balance");
            this.total_spend = rs.getDouble("total_spend");
            this.is_regular = rs.getBoolean("is_regular");
        }
    }

    public static List<User> getRegularCustomers() throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT * FROM users WHERE is_regular = 1");
        ResultSet rs = stm.executeQuery();
        List<User> regularCustomers = new ArrayList<>();
        while (rs.next()) {
            regularCustomers.add(get_from_set(rs));
        }
        return regularCustomers;
    }
}
