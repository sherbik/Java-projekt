package net.programa.sklep.programsklep;

import com.sun.javafx.scene.control.DoubleField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.Product;

public class EditProductViewController {
    public static Product product;
    @FXML
    public TextField Name;
    @FXML
    public CheckBox isByWeight;
    @FXML
    public Spinner Remains;
    @FXML
    public Spinner Price;

    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("edit-product-view.fxml"));

        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 260);
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while creating form", e.getMessage(), rs -> null);
            System.exit(-1);
            return;
        }
        stage.setTitle("Product editor");
        stage.setScene(scene);
        EditProductViewController c;
        c = fxmlLoader.getController();
        SpinnerValueFactory.DoubleSpinnerValueFactory RemainsValueFactory =
                (SpinnerValueFactory.DoubleSpinnerValueFactory) c.Remains.getValueFactory();
        SpinnerValueFactory.DoubleSpinnerValueFactory PriceValueFactory =
                (SpinnerValueFactory.DoubleSpinnerValueFactory) c.Price.getValueFactory();
        RemainsValueFactory.setValue(product.remains);
        PriceValueFactory.setValue(product.price);
        c.isByWeight.setSelected(product.isByWeight);
        c.onIsByWeightChecked(null);
        c.Name.setText(product.name);
        EditProductViewController.addTextLimiter(c.Name, 255);
    }

    public void goBack(ActionEvent actionEvent) {
        product = null;
        ProductsManagerController.open_form(Application.currentStage);
    }

    public void onIsByWeightChecked(ActionEvent actionEvent) {
        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory = (SpinnerValueFactory.DoubleSpinnerValueFactory) Remains.getValueFactory();

        if (isByWeight.isSelected()){

            valueFactory.setAmountToStepBy(0.1);
        } else {

            valueFactory.setAmountToStepBy(1);
            valueFactory.setValue((double)valueFactory.getValue().intValue());
        }
    }

    public void Apply(ActionEvent actionEvent) {
        product.name = Name.getText();
        product.remains = (double)Remains.getValue();
        product.price = (double) Price.getValue();
        product.isByWeight = isByWeight.isSelected();
        try {
            product.update();
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while applying changes", e.getMessage(), rs -> null);
        }
        goBack(null);
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
