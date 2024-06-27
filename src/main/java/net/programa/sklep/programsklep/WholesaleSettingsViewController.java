package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.Setting;
import net.programa.sklep.programsklep.Database.User;

import java.util.Set;
import java.util.stream.Collectors;

public class WholesaleSettingsViewController {
    public Spinner wholeSaleFrom;
    public Spinner wholeSaleDiscount;
    public Text rcustomers;
    public TextField login;
    public CheckBox isreg;
    public Button sset;
    public Spinner rdiscount;

    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("wholesale-settings-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 320, 600);
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while creating form", e.getMessage(), rs -> null);
            System.exit(-1);
            return;
        }
        stage.setTitle("Wholesale settings");
        stage.setScene(scene);
        WholesaleSettingsViewController c;
        do {
            c = fxmlLoader.getController();
        } while (c == null);
        try {
            c.rcustomers.setText(c.rcustomers.getText() + User.getRegularCustomers().stream().map(a -> a.login).collect(Collectors.joining(", ")));
            SpinnerValueFactory.IntegerSpinnerValueFactory wdVf= (SpinnerValueFactory.IntegerSpinnerValueFactory)c.wholeSaleDiscount.getValueFactory();
            wdVf.setValue(Integer.parseInt(Setting.get_value("whole_sale_discount", "10")));
            SpinnerValueFactory.IntegerSpinnerValueFactory wfVf= (SpinnerValueFactory.IntegerSpinnerValueFactory)c.wholeSaleFrom.getValueFactory();
            wfVf.setValue(Integer.parseInt(Setting.get_value("whole_sale_from", "10")));
            SpinnerValueFactory.IntegerSpinnerValueFactory rguD = (SpinnerValueFactory.IntegerSpinnerValueFactory)c.rdiscount.getValueFactory();
            rguD.setValue(Integer.parseInt(Setting.get_value("regular_customer_discount", "10")));
            c.wholeSaleFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Setting st = Setting.from_key("whole_sale_from");
                    st.set_value(String.valueOf((int)(newValue)));
                } catch (Exception e){
                    utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while setting wholesale discount count", e.getMessage(), rs -> null);
                }
            });
            c.wholeSaleDiscount.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Setting st = Setting.from_key("whole_sale_discount");
                    st.set_value(String.valueOf((int)(newValue)));
                } catch (Exception e){
                    utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while setting wholesale discount", e.getMessage(), rs -> null);
                }
            });
            c.rdiscount.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Setting st = Setting.from_key("regular_customer_discount");
                    st.set_value(String.valueOf((int)(newValue)));
                } catch (Exception e){
                    utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while setting regular customer discount", e.getMessage(), rs -> null);
                }
            });
        } catch (Exception e) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while showing regular customers", e.getMessage(), rs -> null);

        }
    }

    public void goBack(ActionEvent actionEvent) {
        RootUserViewController.open_form(Application.currentStage);
    }

    public void onSset(ActionEvent actionEvent) {
        User usr;
        try {
            usr = User.get_by_login(login.getText());
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while getting user by login.", e.getMessage(), rs -> null);
            return;
        }
        if (usr == null) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "User not found", null, rs -> null);
            return;
        }
        usr.is_regular = isreg.isSelected();
        try {
            usr.update();
        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while updating user.", e.getMessage(), rs -> null);
        }
        open_form(Application.currentStage);
    }
}
