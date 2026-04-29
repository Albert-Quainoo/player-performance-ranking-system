package gui.controllers;

import engine.RankingEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.PerformanceRecord;
import model.Player;
import model.Position;
import model.Team;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    // Toolbar
    @FXML private ComboBox<String> positionFilter;
    @FXML private VBox breakdownBox;
    @FXML private Spinner<Integer> topNSpinner;

    // Table
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> positionColumn;
    @FXML private TableColumn<Player, String> teamColumn;
    @FXML private TableColumn<Player, Integer> ageColumn;
    @FXML private TableColumn<Player, Double> scoreColumn;

    // Detail panel
    @FXML private VBox detailPanel;
    @FXML private Label playerNameLabel;
    @FXML private Label positionLabel;
    @FXML private Label teamLabel;
    @FXML private Label  ageLabel;
    @FXML private VBox statsBox;
    @FXML private Label  scoreLabel;

    // Update form
    @FXML private GridPane primaryGrid;
    @FXML private GridPane  additionalGrid;
    @FXML private TitledPane additionalPane;
    @FXML private Label errorLabel;

    // Status bar
    @FXML private Label statusLabel;

    private final RankingEngine engine = new RankingEngine();
    private final ObservableList<Player> playerData = FXCollections.observableArrayList();
    private final ArrayList<Player> masterList = new ArrayList<>();

    private Player selectedPlayer;
    private final Map<String, TextField> primaryFields    = new LinkedHashMap<>();
    private final Map<String, TextField> additionalFields = new LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Wire table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        teamColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTeam() != null
                    ? data.getValue().getTeam().getTeamName()
                    : "Unassigned"
            )
        );

        positionColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                normalise(data.getValue().getPosition().name())
    )
);

        scoreColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(
                engine.rank(data.getValue())
            ).asObject()
        );

        // Make columns scale with window width
        playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Position filter
        // Normalised display labels
        positionFilter.setItems(FXCollections.observableArrayList(
            "All", "Goalkeeper", "Defender", "Midfielder", "Forward"
        ));
        positionFilter.setValue("All");

        // Auto-rank when filter changes
        positionFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) autoRank();
        });
        // Row selection updates the detail panel
        playerTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectedPlayer = newVal;
                    refreshDetailPanel();
                }
            }
        );

        playerTable.setItems(playerData);
        loadSampleData();
    }

    private String normalise(String enumName) {
    return enumName.substring(0, 1).toUpperCase()
         + enumName.substring(1).toLowerCase();
}

    // ===== TOOLBAR HANDLERS =====

    @FXML
    private void handleRankPlayers() {
        ArrayList<Player> ranked = engine.rankPlayers(resolveSource());
        playerData.clear();
        playerData.addAll(ranked);
        String filter = positionFilter.getValue();
        String playerWord = ranked.size() == 1 ? "player" : "players";
        statusLabel.setText("Ranked " + ranked.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    @FXML
    private void handleResetStats() {
        if (selectedPlayer == null) {
            statusLabel.setText("Select a player first.");
            return;
        }
        selectedPlayer.getPerformanceRecord().resetStats();
        playerTable.refresh();
        refreshDetailPanel();
        statusLabel.setText(selectedPlayer.getName() + "'s stats have been reset.");
    }

    @FXML
    private void handleTopN() {
        int n = topNSpinner.getValue();
        ArrayList<Player> topN = engine.getTopNPlayers(resolveSource(), n);
        playerData.clear();
        playerData.addAll(topN);
        String filter = positionFilter.getValue();
        String playerWord = topN.size() == 1 ? "player" : "players";
        statusLabel.setText("Showing top " + topN.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    @FXML
    private void handleShowAll() {
        ArrayList<Player> ranked = engine.rankPlayers(resolveSource());
        playerData.clear();
        playerData.addAll(ranked);
        String filter = positionFilter.getValue();
        String playerWord = ranked.size() == 1 ? "player" : "players";
        statusLabel.setText("Showing all " + ranked.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    // ===== UPDATE STATS HANDLER =====

    @FXML
    private void handleSubmitUpdate() {
        if (selectedPlayer == null) return;

        errorLabel.setText("");

        Map<String, Integer> values = new LinkedHashMap<>();

        for (Map.Entry<String, TextField> entry : primaryFields.entrySet()) {
            Integer value = parseField(entry.getKey(), entry.getValue());
            if (value == null) return;
            values.put(entry.getKey(), value);
        }

        for (Map.Entry<String, TextField> entry : additionalFields.entrySet()) {
            Integer value = parseField(entry.getKey(), entry.getValue());
            if (value == null) return;
            values.put(entry.getKey(), value);
        }

        applyUpdates(values);
        refreshDetailPanel();
        rerankTable();
        statusLabel.setText(selectedPlayer.getName() + "'s stats updated.");

        // Reset all fields back to 0 after submit
        primaryFields.values().forEach(f -> f.setText("0"));
        additionalFields.values().forEach(f -> f.setText("0"));
    }

    // ===== DETAIL PANEL =====

    private void refreshDetailPanel() {
        if (selectedPlayer == null) return;

        detailPanel.setVisible(true);
        playerNameLabel.setText(selectedPlayer.getName());
        positionLabel.setText("Position: " + normalise(selectedPlayer.getPosition().name()));
        teamLabel.setText("Team: " + (
            selectedPlayer.getTeam() != null
                ? selectedPlayer.getTeam().getTeamName()
                : "Unassigned"
        ));
        ageLabel.setText("Age: " + selectedPlayer.getAge());
        refreshStatsBox();
        
        // Score breakdown
        breakdownBox.getChildren().clear();
        LinkedHashMap<String, Double> breakdown = engine.getScoreBreakdown(selectedPlayer);
        double total = 0;
        for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
            if (entry.getValue() == 0) continue; // Skip zero contributions
                String sign = entry.getValue() < 0 ? "" : "+";
                Label line = new Label(String.format("%-20s %s%.2f",
                    entry.getKey() + ":", sign, entry.getValue()));
                line.setStyle("-fx-font-family: monospace;");
                breakdownBox.getChildren().add(line);
                total += entry.getValue();
        }
        scoreLabel.setText(String.format("Total Score: %.2f", total));

        rebuildUpdateForm();
    }

    private void refreshStatsBox() {
        statsBox.getChildren().clear();
        PerformanceRecord r = selectedPlayer.getPerformanceRecord();

        addStatRow("Goals",            r.getGoals());
        addStatRow("Assists",          r.getAssists());
        addStatRow("Key Passes",       r.getKeyPasses());
        addStatRow("Passes Completed", r.getPassesCompleted());
        addStatRow("Shots on Target",  r.getShotsOnTarget());
        addStatRow("Tackles Won",      r.getTacklesWon());
        addStatRow("Interceptions",    r.getInterceptions());
        addStatRow("Blocks",           r.getBlocks());
        addStatRow("Clearances",       r.getClearances());
        addStatRow("Ball Recoveries",  r.getBallRecoveries());
        addStatRow("Saves",            r.getSaves());
        addStatRow("Clean Sheets",     r.getCleanSheets());
        addStatRow("Goals Conceded",   r.getGoalsConceded());
        addStatRow("Yellow Cards",     r.getYellowCards());
        addStatRow("Red Cards",        r.getRedCards());

        if (statsBox.getChildren().isEmpty()) {
            Label empty = new Label("No stats recorded yet.");
            empty.setStyle("-fx-text-fill: #aab2bd; -fx-font-style: italic; -fx-font-size: 12;");
            statsBox.getChildren().add(empty);
        }
    }

    private void addStatRow(String label, int value) {
        if (value == 0) return;
        HBox row = new HBox();
        Label name = new Label(label);
        name.setStyle("-fx-text-fill: #555; -fx-font-size: 12;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label val = new Label(String.valueOf(value));
        val.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 12;");
        row.getChildren().addAll(name, spacer, val);
        statsBox.getChildren().add(row);
    }

    private void rebuildUpdateForm() {
        primaryGrid.getChildren().clear();
        additionalGrid.getChildren().clear();
        primaryFields.clear();
        additionalFields.clear();

        Position position = selectedPlayer.getPosition();

        switch (position) {
            case GOALKEEPER -> {
                addPrimary("Saves",          "saves");
                addPrimary("Clean Sheets",   "cleanSheets");
                addPrimary("Goals Conceded", "goalsConceded");
                addPrimary("Blocks",         "blocks");
                addPrimary("Clearances",     "clearances");
            }
            case DEFENDER -> {
                addPrimary("Tackles Won",      "tacklesWon");
                addPrimary("Interceptions",    "interceptions");
                addPrimary("Clearances",       "clearances");
                addPrimary("Blocks",           "blocks");
                addPrimary("Ball Recoveries",  "ballRecoveries");
                addPrimary("Passes Completed", "passesCompleted");
            }
            case MIDFIELDER -> {
                addPrimary("Key Passes",       "keyPasses");
                addPrimary("Passes Completed", "passesCompleted");
                addPrimary("Assists",          "assists");
                addPrimary("Goals",            "goals");
                addPrimary("Ball Recoveries",  "ballRecoveries");
                addPrimary("Interceptions",    "interceptions");
            }
            case FORWARD -> {
                addPrimary("Goals",           "goals");
                addPrimary("Assists",         "assists");
                addPrimary("Shots on Target", "shotsOnTarget");
                addPrimary("Key Passes",      "keyPasses");
            }
        }

        if (position != Position.FORWARD && position != Position.MIDFIELDER) {
            addAdditional("Goals",           "goals");
            addAdditional("Assists",         "assists");
            addAdditional("Shots on Target", "shotsOnTarget");
        }
        if (position != Position.MIDFIELDER && position != Position.DEFENDER) {
            addAdditional("Key Passes", "keyPasses");
        }
        if (position != Position.DEFENDER && position != Position.MIDFIELDER) {
            addAdditional("Passes Completed", "passesCompleted");
            addAdditional("Ball Recoveries",  "ballRecoveries");
        }
        if (position != Position.GOALKEEPER) {
            addAdditional("Saves",         "saves");
            addAdditional("Clean Sheets",  "cleanSheets");
            addAdditional("Goals Conceded","goalsConceded");
        }
        if (position != Position.DEFENDER && position != Position.GOALKEEPER) {
            addAdditional("Tackles Won",   "tacklesWon");
            addAdditional("Interceptions", "interceptions");
            addAdditional("Clearances",    "clearances");
            addAdditional("Blocks",        "blocks");
        }

        addAdditional("Yellow Cards", "yellowCards");
        addAdditional("Red Cards",    "redCards");

        renderGrid(primaryGrid,    primaryFields);
        renderGrid(additionalGrid, additionalFields);
    }

    // ===== HELPERS =====

    private ArrayList<Player> resolveSource() {
        String filter = positionFilter.getValue();
        if (filter.equals("All")) {
            return new ArrayList<>(masterList);
        }
        return engine.filterByPosition(
            new ArrayList<>(masterList), Position.valueOf(filter.toUpperCase())
        );
    }

    private void autoRank(){
        ArrayList<Player> ranked = engine.rankPlayers(resolveSource());
        playerData.clear();
        playerData.addAll(ranked);
        String filter = positionFilter.getValue();
        String playerWord = ranked.size() == 1 ? "player" : "players";
        statusLabel.setText("Showing " + ranked.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    private void rerankTable() {
        ArrayList<Player> ranked = engine.rankPlayers(new ArrayList<>(masterList));
        playerData.clear();
        playerData.addAll(ranked);
        playerTable.refresh();
    }

    private void addPrimary(String label, String key) {
        primaryFields.put(key, createField(label));
    }

    private void addAdditional(String label, String key) {
        if (!primaryFields.containsKey(key)) {
            additionalFields.put(key, createField(label));
        }
    }

    private TextField createField(String label) {
        TextField field = new TextField("0");
        field.setPromptText(label);
        field.setPrefWidth(100);
        return field;
    }

    private void renderGrid(GridPane grid, Map<String, TextField> fields) {
        int row = 0;
        for (Map.Entry<String, TextField> entry : fields.entrySet()) {
            grid.add(new Label(camelToLabel(entry.getKey()) + ":"), 0, row);
            grid.add(entry.getValue(), 1, row);
            row++;
        }
    }

    private Integer parseField(String key, TextField field) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) {
                errorLabel.setText("'" + camelToLabel(key) + "' cannot be negative.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            errorLabel.setText("'" + camelToLabel(key) + "' must be a whole number.");
            return null;
        }
    }

    private void applyUpdates(Map<String, Integer> values) {
        PerformanceRecord r = selectedPlayer.getPerformanceRecord();
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            int val = entry.getValue();
            if (val == 0) continue;
            switch (entry.getKey()) {
                case "goals"           -> r.updateGoals(val);
                case "assists"         -> r.updateAssists(val);
                case "keyPasses"       -> r.updateKeyPasses(val);
                case "passesCompleted" -> r.updatePassesCompleted(val);
                case "shotsOnTarget"   -> r.updateShotsOnTarget(val);
                case "tacklesWon"      -> r.updateTacklesWon(val);
                case "interceptions"   -> r.updateInterceptions(val);
                case "blocks"          -> r.updateBlocks(val);
                case "clearances"      -> r.updateClearances(val);
                case "ballRecoveries"  -> r.updateBallRecoveries(val);
                case "saves"           -> r.updateSaves(val);
                case "cleanSheets"     -> r.updateCleanSheets(val);
                case "goalsConceded"   -> r.updateGoalsConceded(val);
                case "yellowCards"     -> r.updateYellowCards(val);
                case "redCards"        -> r.updateRedCards(val);
            }
        }
    }

    private String camelToLabel(String camel) {
        return camel
            .replaceAll("([A-Z])", " $1")
            .substring(0, 1).toUpperCase()
            + camel.replaceAll("([A-Z])", " $1").substring(1);
    }

    private void loadSampleData() {
        Team heartsOfOak = new Team("Hearts of Oak");
        Team kotoko      = new Team("Asante Kotoko");

        Player kwame  = new Player("Kwame Mensah",  27, 1,  Position.GOALKEEPER);
        Player bright = new Player("Bright Ofori",  24, 5,  Position.DEFENDER);
        Player kofi   = new Player("Kofi Asante",   26, 8,  Position.MIDFIELDER);
        Player eric   = new Player("Eric Boateng",  23, 9,  Position.FORWARD);
        Player yaw    = new Player("Yaw Darko",     25, 11, Position.FORWARD);

        heartsOfOak.addPlayer(kwame);
        heartsOfOak.addPlayer(bright);
        heartsOfOak.addPlayer(kofi);
        kotoko.addPlayer(eric);
        kotoko.addPlayer(yaw);

        kwame.getPerformanceRecord().updateSaves(95);
        kwame.getPerformanceRecord().updateCleanSheets(12);
        kwame.getPerformanceRecord().updateGoalsConceded(22);
        bright.getPerformanceRecord().updateTacklesWon(60);
        bright.getPerformanceRecord().updateInterceptions(45);
        kofi.getPerformanceRecord().updateGoals(8);
        kofi.getPerformanceRecord().updateAssists(12);
        kofi.getPerformanceRecord().updateKeyPasses(55);
        eric.getPerformanceRecord().updateGoals(22);
        eric.getPerformanceRecord().updateAssists(9);
        yaw.getPerformanceRecord().updateGoals(15);
        yaw.getPerformanceRecord().updateAssists(5);

        playerData.addAll(kwame, bright, kofi, eric, yaw);
        masterList.addAll(playerData);
    }
}