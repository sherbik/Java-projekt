package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.programa.sklep.programsklep.Database.Product;
import net.programa.sklep.programsklep.Database.Setting;
import net.programa.sklep.programsklep.Database.User;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.*;

public class ShopViewController {




    class shopping_cart {
         public int product_i;
         public double count;
         public shopping_cart(int product_i, double count) {
             this.product_i = product_i;
             this.count = count;

         }
     }
    public ListView Products;
    public Map<Integer, shopping_cart> shoppingCart;

    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("shop-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 340);
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR,  "Error","Error while creating form", e.getMessage(), rs -> null);
            System.exit(-1);
            return;
        }
        stage.setTitle(User.currentUser.login + " shop");
        stage.setScene(scene);
    }

    @FXML
    public void initialize() {
        Product[] products;
        try{
            products = Product.get_all();
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while getting all products set", e.getMessage(), rs -> null);
            return;
        }
        for (Product product : products) {
            Products.getItems().add(Integer.toString(product.id));
        }
        shoppingCart = new HashMap<Integer, shopping_cart>();

        Products.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("shop-view-cell.fxml"));
                            try {
                                setGraphic(loader.load());
                                ShopViewCell controller = loader.getController();
                                int id = Integer.parseInt(item);
                                Product product;
                                try {
                                    product = Product.get_by_id(id);
                                } catch (SQLException e){
                                    utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while getting product", e.getMessage(), rs -> null);
                                    return;
                                }
                                if (product != null) {
                                    shoppingCart.put(product.id, new shopping_cart(product.id, 0.0));
                                    controller.setValues(product, shoppingCart.get(product.id));
                                }
                            } catch (Exception e) {
                                utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while showing products", e.getMessage(), rs -> null);
                            }
                        }
                    }
                };
            }
        });
    }
    public void go_back(ActionEvent actionEvent) {
        User.currentUser.ShowForm();
    }
    public class buy_value {
        public int product_i;
        double count;
        double price;
        public buy_value(int product_i, double count, double price){
            this.product_i = product_i;
            this.count = count;
            this.price = price;
        }
    }
    public void buy(ActionEvent actionEvent) {
        List<buy_value> buy_values = new ArrayList<>();
        double total_price = 0;
        for (shopping_cart cart : shoppingCart.values()) {
            try {
                if (cart.count == 0){
                    continue;
                }
                Product product = Product.get_by_id(cart.product_i);
                double price_per_one  = product.price;
                if (User.currentUser.is_regular){
                    price_per_one *= (100 - Double.parseDouble(Setting.get_value("regular_customer_discount", "0"))) / 100;
                }
                if (Integer.parseInt(Setting.get_value("whole_sale_from", "9999")) <= cart.count){
                    price_per_one *= ((double) (100 - Integer.parseInt(Setting.get_value("whole_sale_discount", "0")))) / 100;
                }
                double total_price_v = cart.count * price_per_one;
                buy_values.add(new buy_value(cart.product_i, cart.count, total_price_v));
                total_price += total_price_v;

            } catch (Exception e){
                utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while counting products prices.", e.getMessage(), rs -> null);
            }
        }
        if (buy_values.size() == 0){
            utils.showDialog(Alert.AlertType.ERROR, "ERROR", "You can not buy nothing!", "You should buy anything!", rs -> null);
            return;
        }
        if (total_price > User.currentUser.balance){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "You have not enough money", "You can not afford your shopping cart", rs->null);
            return;
        }
        for (buy_value buy_value : buy_values) {
            try {
                Product p = Product.get_by_id(buy_value.product_i);
                p.remains -= buy_value.count;
                User.currentUser.balance -= buy_value.price;
                System.out.println(buy_value.price);
                User.currentUser.update();
                p.update();
            } catch (Exception e){
                utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while finding product.", e.getMessage(), rs -> null);
                return;
            }
        }
        utils.showDialog(Alert.AlertType.INFORMATION, "SUCCESS!", "All product in shopping cart was bought!", "You will be returned to the main page", rs-> {User.currentUser.ShowForm();
            return null;
        });
    }
}
