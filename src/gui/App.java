package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // FXMLLoader reads your .fxml file and builds the UI from it
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/gui/views/main-view.fxml")
        );

        // The loader builds the scene graph from the FXML
        Scene scene = new Scene(loader.load(), 960, 660);

        stage.setTitle("Player Performance Ranking System");
        stage.setMinWidth(760);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // JavaFX apps launch through Application.launch, not a direct main call
        launch(args);
    }
}