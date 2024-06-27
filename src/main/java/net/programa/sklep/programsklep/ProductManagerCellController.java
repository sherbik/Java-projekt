package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import net.programa.sklep.programsklep.Database.Product;

public class ProductManagerCellController {

    @FXML
    public Text name;
    @FXML
    public Text price;
    @FXML
    public FlowPane root_flow;
    @FXML
    public Text remains;
    private Product _product;

    public void setValues(Product product) {
        _product = product;
        name.setText(product.name);
        price.setText("Price " + product.price + "$");
        if (product.isByWeight){
            remains.setText("Remains: " + product.remains  + " kg");
        }
        else {
            remains.setText("Remains: " + ((int) product.remains));
        }
    }

    public void edit(ActionEvent actionEvent) {
        EditProductViewController.product = _product;
        EditProductViewController.open_form(Application.currentStage);
    }

    public void remove(ActionEvent actionEvent) {
        utils.showDialog(Alert.AlertType.CONFIRMATION, "Are you sure?", "Do you really want to delete product named " + _product.name + "?", "THIS CAN NOT BE UNDONE!", rs -> {
            if (rs == ButtonType.OK){
                try {
                    _product.delete();
                } catch (Exception e){
                    utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while deleting product", e.getMessage(), rss -> null);
                    return null;
                }
                ProductsManagerController.open_form(Application.currentStage);
            }
            return null;
        });
    }
}
