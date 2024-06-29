package net.programa.sklep.programsklep;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import net.programa.sklep.programsklep.Database.Product;
import net.programa.sklep.programsklep.Database.Setting;
import net.programa.sklep.programsklep.Database.User;

import java.sql.SQLException;

public class ShopViewCell {
    public Label name;
    public Label pfor;
    public Label price;
    public Spinner count;
    public ShopViewController.shopping_cart cart;
    public Label total_price;
    public Label sale;
    public HBox sale_box;

    public void setValues(Product product, ShopViewController.shopping_cart cart) throws SQLException {
        name.setText(product.name);
        pfor.setText(product.isByWeight ? " kg " : " one ");
        this.cart = cart;
        Double pprice = product.price;
        if (User.currentUser.is_regular){
            pprice *= (100 - Double.parseDouble(Setting.get_value("regular_customer_discount", "0"))) / 100;
            sale.setText(String.format("%.2f", Double.parseDouble(Setting.get_value("regular_customer_discount", "0"))) + "%");
            sale_box.setVisible(true);
        }
        SpinnerValueFactory.DoubleSpinnerValueFactory cvf = (SpinnerValueFactory.DoubleSpinnerValueFactory) count.getValueFactory();
        cvf.setMax(product.remains);
        cvf.setAmountToStepBy(product.isByWeight ? 0.1 : 1);
        price.setText(String.valueOf(pprice));
        total_price.setText("0");
        Double finalPprice = pprice;
        count.valueProperty().addListener((observable, oldValue, newValue) -> {
            double total_price_v = finalPprice;
            try {
                if (Integer.parseInt(Setting.get_value("whole_sale_from", "9999")) <= ((Double)newValue)){
                    total_price_v *= ((double) (100 - Integer.parseInt(Setting.get_value("whole_sale_discount", "0")))) / 100;
                    Double d = (100 - Double.parseDouble(Setting.get_value("whole_sale_discount", "0"))) / 100;
                    Double d2 = ((((100 - Double.parseDouble(Setting.get_value("regular_customer_discount", "0"))) / 100)));
                    Double total_discount = 1.0;
                    if (User.currentUser.is_regular){
                        total_discount*=d;
                    }
                    total_discount *=d2;
                    sale.setText(String.format("%.2f",  (1 -total_discount)*100) + "%");
                    sale_box.setVisible(true);
                } else {
                    if (User.currentUser.is_regular){
                        sale.setText(String.format("%.2f", Double.parseDouble(Setting.get_value("regular_customer_discount", "0"))) + "%");
                        sale_box.setVisible(true);
                    } else {
                        sale_box.setVisible(false);
                    }
                }
            } catch (SQLException e) {
                utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while updating count", e.getMessage(), rs->null);
                cvf.setValue((Double)oldValue);
                return;
            }
            cart.count = (Double) newValue;
            total_price.setText(String.format("%.2f", total_price_v * cart.count));
        });

    }
}
