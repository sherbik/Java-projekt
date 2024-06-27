package net.programa.sklep.programsklep.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Product extends Model {
    public int id;
    public String name;
    public double price;
    public boolean isByWeight;
    public double remains;


    public static void create_table() throws SQLException {
        Statement stm = con.createStatement();
        stm.executeUpdate("""
CREATE TABLE IF NOT EXISTS product(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name varchar(255),
    price double,
    isByWeight boolean,
    remains double
)
""");
    }
    public boolean is_exists() throws SQLException{
        PreparedStatement stm = con.prepareStatement("SELECT * FROM product WHERE id = ?");
        stm.setInt(1, id);
        return stm.executeQuery().next();
    }
    @Override
    public void update() throws SQLException{
        if (!is_exists()){
            create_product(name, price, isByWeight, remains);
            return;
        }
        PreparedStatement stm = con.prepareStatement("UPDATE product SET name = ?, price = ?, isByWeight = ?, remains = ? where id = ?");
        stm.setString(1, name);
        stm.setDouble(2, price);
        stm.setBoolean(3, isByWeight);
        stm.setDouble(4, remains);
        stm.setInt(5, id);
        stm.executeUpdate();
    }
    public static void create_product(String name, double price, boolean isByWeight, double remains) throws SQLException {
        PreparedStatement stm = con.prepareStatement("""
    INSERT INTO product (name, price, isByWeight, remains) VALUES (?, ?, ?, ?)
""");
        stm.setString(1, name);
        stm.setDouble(2, price);
        stm.setBoolean(3, isByWeight);
        stm.setDouble(4, remains);
        stm.executeUpdate();
    }

    private static Product get_from_set(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.id = rs.getInt(1);
        p.name = rs.getString(2);
        p.price = rs.getDouble(3);
        p.isByWeight = rs.getBoolean(4);
        p.remains = rs.getDouble(5);
        return p;
    }

    public static Product[] get_all() throws SQLException {
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM product");;
        List<Product> products = new ArrayList<>(){};
        while(rs.next()) {
            products.add(get_from_set(rs));
        }
        Object[] productsArr = products.toArray();
        return Arrays.copyOf(productsArr, productsArr.length, Product[].class);
    }

    public static Product  get_by_id(int id) throws SQLException {
        PreparedStatement stm = con.prepareStatement("SELECT * FROM product WHERE id = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return get_from_set(rs);
        }
        return null;
    }

    @Override
    public void delete() throws SQLException{
        PreparedStatement stm = con.prepareStatement("DELETE FROM product where id = ?");
        stm.setInt(1, id);
        stm.executeUpdate();
    }

}
