package net.programa.sklep.programsklep;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.programa.sklep.programsklep.Database.Product;
import net.programa.sklep.programsklep.Database.User;

import java.io.IOException;
import java.sql.SQLException;

public class ShopViewController {
    public static Scene currentScene;
    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("shop-view.fxml"));
        Scene scene;
        if (currentScene == null){
            try {
                scene = new Scene(fxmlLoader.load(), 320, 340);
                currentScene = scene;
            } catch (Exception e){
                utils.showDialog(Alert.AlertType.ERROR,  "Error","Error while creating form", e.getMessage(), rs -> null);
                System.exit(-1);
                return;
            }
        } else {
            scene = currentScene;
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
            listView.getItems().add(Integer.toString(product.id));
        }

        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("product-manager-cell.fxml"));
                            try {
                                setGraphic(loader.load());
                                ProductManagerCellController controller = loader.getController();
                                int id = Integer.parseInt(item);
                                Product product;
                                try {
                                    product = Product.get_by_id(id);
                                } catch (SQLException e){
                                    utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while getting product", e.getMessage(), rs -> null);
                                    return;
                                }
                                if (product != null) {
                                    controller.setValues(product);
                                }
                            } catch (IOException e) {
                                utils.showDialog(Alert.AlertType.ERROR, "ERROR", "Error while showing products", e.getMessage(), rs -> null);
                            }
                        }
                    }
                };
            }
        });
    }
}
