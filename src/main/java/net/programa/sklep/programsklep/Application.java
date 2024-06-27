package net.programa.sklep.programsklep;

import javafx.stage.Stage;
import net.programa.sklep.programsklep.Database.Model;

import java.io.IOException;

public class Application extends javafx.application.Application {
    public static Stage currentStage;
    @Override
    public void start(Stage stage) throws IOException {
        currentStage = stage;
        stage.show();
        Controller c = new Controller();
        c.open_form(stage);
        Model.init_connection();
    }

    public static void main(String[] args) {
        launch();
    }
}