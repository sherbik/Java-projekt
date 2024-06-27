package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RootUserViewController {
    public static Scene currentScene = null;
    public Button manage_categories;


    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("root-user-view.fxml"));
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
        stage.setTitle("Sklep manager");
        stage.setScene(scene);
    }

    public void onManage(ActionEvent actionEvent) {
        ProductsManagerController.open_form(Application.currentStage);
    }

    public void WholeSaleSettings(ActionEvent actionEvent) {
        WholesaleSettingsViewController.open_form(Application.currentStage);
    }
}
