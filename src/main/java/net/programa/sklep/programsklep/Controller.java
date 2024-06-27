package net.programa.sklep.programsklep;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.User;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    public static Scene currentScene = null;

    @FXML
    public TextField login;
    @FXML
    public TextField password;
    @FXML
    public Button confirm;
    @FXML
    public Hyperlink goto_register;


    public static void open_form(Stage stage) {
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene;
        if (currentScene == null) {
            try {
                scene = new Scene(fxmlLoader.load(), 320, 260);
                currentScene = scene;
            } catch (Exception e) {
                utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while creating form", e.getMessage(), rs -> null);
                System.exit(-1);
                return;
            }
        } else {
            scene = currentScene;
        }
        stage.setTitle("Please login");
        stage.setScene(scene);
    }

    public void goto_reg(ActionEvent actionEvent) {
        RegController r = new RegController();
        r.open_form(Application.currentStage);
    }

    public void Login(ActionEvent actionEvent) {
        try {
            User usr = User.login(login.getText(), password.getText());
            if (usr == null){
                throw new RuntimeException("User not found!");
            }
            User.currentUser = usr;
            usr.ShowForm();

        } catch (Exception e){
            utils.showDialog(Alert.AlertType.ERROR, "Error", "Error while logging in", e.getMessage(), rs -> null);
        }
    }
}