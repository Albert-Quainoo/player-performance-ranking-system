package gui;

import gui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.DataManager;
import persistence.DataManager.LoadResult;

/**
 * Launches the JavaFX application window.
 * Loads saved data on startup and saves it automatically when the window is closed.
 */
public class App extends Application {

    /**
     * Builds and shows the main window.
     *
     * @param stage the primary window provided by JavaFX
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/gui/views/main-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 960, 660);

        MainController controller = loader.getController();

        // Load saved data and pass it to the controller before the window opens
        DataManager dataManager = new DataManager();
        LoadResult result = dataManager.load();
        controller.loadData(result.teams, result.players);

        // Auto-save when the window is closed
        stage.setOnCloseRequest(event ->
            dataManager.save(controller.getTeamList(), controller.getMasterList())
        );

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