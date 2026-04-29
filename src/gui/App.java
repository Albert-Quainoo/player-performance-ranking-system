package gui;

import gui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.DataManager;
import persistence.DataManager.LoadResult;

/**
 * JavaFX application entry point for the Player Performance Ranking System.
 *
 * <p>Loads the main FXML view, restores persisted data via {@link persistence.DataManager},
 * and registers an auto-save hook that fires when the window is closed.
 */
public class App extends Application {

    /**
     * Initialises and displays the primary stage.
     * Persisted data is loaded and injected into the controller before the window appears.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/gui/views/main-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 960, 660);

        MainController controller = loader.getController();

        // Load persisted data and pass it to the controller
        // before the window is shown — zero latency on display
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