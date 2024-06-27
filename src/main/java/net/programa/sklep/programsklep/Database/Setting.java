package net.programa.sklep.programsklep.Database;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Setting extends Model{
    public int id;
    public String key;
    public String value;

    public static void create_table() throws SQLException {
        Statement stm = con.createStatement();
        stm.executeUpdate("""
    CREATE TABLE IF NOT EXISTS setting(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        key varchar(255),
        value varchar(255)
    )
    """);
    }

    public static Setting from_key(String key) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM setting WHERE key = ?");
        stm.setString(1, key);
        ResultSet rs = stm.executeQuery();
        if(rs.next()) {
            Setting setting = new Setting();
            setting.id = rs.getInt("id");
            setting.key = key;
            setting.value = rs.getString("value");
            return setting;
        }
        return null;
    }

    public static Boolean is_exist(String key) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM setting WHERE key=?");
        stm.setString(1, key);
        return stm.executeQuery().next();
    }

    public void insert() throws SQLException {
        PreparedStatement stm = con.prepareStatement("INSERT INTO setting(key, value) VALUES(?,?)");
        stm.setString(1, key);
        stm.setString(2, value);
        stm.executeUpdate();
    }

    public void set_value(String value) throws SQLException {
        if (!is_exist(key)) {
            insert();
            return;
        }
        PreparedStatement stm = con.prepareStatement("UPDATE setting SET value=? WHERE key=?");
        stm.setString(1, value);
        stm.setString(2, key);
        stm.executeUpdate();
    }

    public static String get_value(String key, String Default) throws SQLException {
        if (!is_exist(key)) {
            Setting s = new Setting();
            s.key = key;
            s.value = Default;
            s.insert();
            return Default;
        }
        PreparedStatement stm = con.prepareStatement("SELECT * FROM setting WHERE key=?");
        stm.setString(1, key);
        ResultSet rs = stm.executeQuery();
        rs.next();
        return rs.getString("value");
    }

}
