package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.User;

import java.io.IOException;
import java.sql.SQLException;

public class RegController {
    public static Scene currentScene = null;
    @FXML
    public TextField login;
    @FXML
    public TextField password1;
    @FXML
    public TextField password2;


    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("reg-view.fxml"));
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
        stage.setTitle("Registration");
        stage.setScene(scene);
    }

    public void register(ActionEvent actionEvent) throws SQLException {
        if (password1.getText().isEmpty() || password2.getText().isEmpty() || login.getText().isEmpty()) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Please fill all the fields", null, rs -> null);
            return;
        }
        if (!password1.getText().equals(password2.getText())) {
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Passwords do not match", null, rs -> null);
            return;
        }
        if (User.is_user_exist(login.getText())){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "User already exists", null, rs -> null);
        }
        User.currentUser = User.register(login.getText(), password1.getText());
        User.currentUser.ShowForm();
    }

    public void goto_login(ActionEvent actionEvent) {
        Controller.open_form(Application.currentStage);
    }
}
