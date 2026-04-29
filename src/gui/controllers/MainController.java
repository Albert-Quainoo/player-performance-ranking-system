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

/**
 * Controller for the main application view (main-view.fxml).
 *
 * <p>Manages the player table, position filter, toolbar actions, and the right-hand
 * side panel stack (welcome, new-team form, new-player form, edit-player form,
 * and player detail view). All ranking calculations are delegated to
 * {@link engine.RankingEngine}.
 */
public class MainController implements Initializable {

    // Toolbar
    @FXML private ComboBox<String>  positionFilter;
    @FXML private Spinner<Integer>  topNSpinner;

    // Table
    @FXML private TableView<Player>            playerTable;
    @FXML private TableColumn<Player, String>  nameColumn;
    @FXML private TableColumn<Player, String>  positionColumn;
    @FXML private TableColumn<Player, String>  teamColumn;
    @FXML private TableColumn<Player, Integer> ageColumn;
    @FXML private TableColumn<Player, Double>  scoreColumn;

    // Right panel states
    @FXML private VBox welcomePanel;
    @FXML private VBox newTeamPanel;
    @FXML private VBox newPlayerPanel;
    @FXML private VBox editPlayerPanel;
    @FXML private VBox detailPanel;

    // New team form
    @FXML private TextField teamNameField;
    @FXML private TextField teamCountryField;
    @FXML private Label     teamErrorLabel;

    // New player form
    @FXML private TextField        playerNameField;
    @FXML private TextField        playerAgeField;
    @FXML private TextField        playerJerseyField;
    @FXML private TextField        playerNationalityField;
    @FXML private ComboBox<String> playerPositionField;
    @FXML private ComboBox<String> playerTeamField;
    @FXML private Label            playerErrorLabel;

    // Edit player form
    @FXML private TextField        editNameField;
    @FXML private TextField        editAgeField;
    @FXML private TextField        editJerseyField;
    @FXML private TextField        editNationalityField;
    @FXML private ComboBox<String> editPositionField;
    @FXML private ComboBox<String> editTeamField;
    @FXML private Label            editErrorLabel;

    // Detail panel
    @FXML private Label playerNameLabel;
    @FXML private Label positionLabel;
    @FXML private Label teamLabel;
    @FXML private Label ageLabel;
    @FXML private Label nationalityLabel;
    @FXML private VBox  breakdownBox;
    @FXML private VBox  statsBox;
    @FXML private Label scoreLabel;

    // Update form
    @FXML private GridPane   primaryGrid;
    @FXML private GridPane   additionalGrid;
    @FXML private TitledPane additionalPane;
    @FXML private Label      errorLabel;

    // Status bar
    @FXML private Label statusLabel;

    private final RankingEngine          engine     = new RankingEngine();
    private final ObservableList<Player> playerData = FXCollections.observableArrayList();
    private final ArrayList<Player>      masterList = new ArrayList<>();
    private final ArrayList<Team>        teamList   = new ArrayList<>();

    private Player selectedPlayer;
    private final Map<String, TextField> primaryFields    = new LinkedHashMap<>();
    private final Map<String, TextField> additionalFields = new LinkedHashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        positionColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                normalise(data.getValue().getPosition().name())
            )
        );

        teamColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTeam() != null
                    ? data.getValue().getTeam().getTeamName()
                    : "No Team"
            )
        );

        scoreColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleDoubleProperty(
                engine.rank(data.getValue())
            ).asObject()
        );

        playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        positionFilter.setItems(FXCollections.observableArrayList(
            "All", "Goalkeeper", "Defender", "Midfielder", "Forward"
        ));
        positionFilter.setValue("All");

        positionFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) autoRank();
        });

        // Position options reused across new player and edit player forms
        ObservableList<String> positionOptions = FXCollections.observableArrayList(
            "Goalkeeper", "Defender", "Midfielder", "Forward"
        );
        playerPositionField.setItems(positionOptions);
        editPositionField.setItems(positionOptions);

        playerTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    selectedPlayer = newVal;
                    showPanel(detailPanel);
                    refreshDetailPanel();
                }
            }
        );

        playerTable.setItems(playerData);
    }

    // ===== PANEL STATE MANAGEMENT =====

    private void showPanel(VBox panel) {
        welcomePanel.setVisible(false);    welcomePanel.setManaged(false);
        newTeamPanel.setVisible(false);    newTeamPanel.setManaged(false);
        newPlayerPanel.setVisible(false);  newPlayerPanel.setManaged(false);
        editPlayerPanel.setVisible(false); editPlayerPanel.setManaged(false);
        detailPanel.setVisible(false);     detailPanel.setManaged(false);

        panel.setVisible(true);
        panel.setManaged(true);
    }

    // ===== TOOLBAR HANDLERS =====

    @FXML
    private void handleNewTeam() {
        teamNameField.clear();
        teamCountryField.clear();
        teamErrorLabel.setText("");
        showPanel(newTeamPanel);
    }

    @FXML
    private void handleNewPlayer() {
        playerNameField.clear();
        playerAgeField.clear();
        playerJerseyField.clear();
        playerNationalityField.clear();
        playerPositionField.setValue(null);
        playerErrorLabel.setText("");
        refreshTeamDropdown(playerTeamField, true);
        showPanel(newPlayerPanel);
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
        String filter     = positionFilter.getValue();
        String playerWord = topN.size() == 1 ? "player" : "players";
        statusLabel.setText("Showing top " + topN.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    @FXML
    private void handleShowAll() {
        ArrayList<Player> ranked = engine.rankPlayers(resolveSource());
        playerData.clear();
        playerData.addAll(ranked);
        String filter     = positionFilter.getValue();
        String playerWord = ranked.size() == 1 ? "player" : "players";
        statusLabel.setText("Showing all " + ranked.size() + " " + playerWord
            + (filter.equals("All") ? "" : " for: " + filter));
    }

    // ===== NEW TEAM FORM =====

    @FXML
    private void handleCreateTeam() {
        String name    = teamNameField.getText().trim();
        String country = teamCountryField.getText().trim();

        if (name.isEmpty()) {
            teamErrorLabel.setText("Team name cannot be empty.");
            return;
        }
        if (country.isEmpty()) {
            teamErrorLabel.setText("Country cannot be empty.");
            return;
        }

        for (Team t : teamList) {
            if (t.getTeamName().equalsIgnoreCase(name)) {
                teamErrorLabel.setText("A team with that name already exists.");
                return;
            }
        }

        teamList.add(new Team(name, country));
        statusLabel.setText("Team \"" + name + "\" created.");
        showPanel(welcomePanel);
    }

    @FXML
    private void handleCancelForm() {
        showPanel(selectedPlayer != null ? detailPanel : welcomePanel);
    }

    // ===== NEW PLAYER FORM =====

    @FXML
    private void handleCreatePlayer() {
        String name        = playerNameField.getText().trim();
        String ageStr      = playerAgeField.getText().trim();
        String jerseyStr   = playerJerseyField.getText().trim();
        String nationality = playerNationalityField.getText().trim();
        String posStr      = playerPositionField.getValue();

        if (name.isEmpty()) {
            playerErrorLabel.setText("Name cannot be empty.");
            return;
        }

        if (nationality.isEmpty()) {
            playerErrorLabel.setText("Nationality cannot be empty.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            playerErrorLabel.setText("Age must be a positive whole number.");
            return;
        }

        int jersey;
        try {
            jersey = Integer.parseInt(jerseyStr);
            if (jersey <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            playerErrorLabel.setText("Jersey number must be a positive whole number.");
            return;
        }

        if (posStr == null) {
            playerErrorLabel.setText("Please select a position.");
            return;
        }

        Player player = new Player(name, age, jersey, Position.valueOf(posStr.toUpperCase()));
        player.setNationality(nationality);

        // Assign team if selected
        String teamName = playerTeamField.getValue();
        if (teamName != null && !teamName.equals("No Team")) {
            for (Team t : teamList) {
                if (t.getTeamName().equals(teamName)) {
                    t.addPlayer(player);
                    break;
                }
            }
        }

        masterList.add(player);
        playerData.add(player);
        rerankTable();

        statusLabel.setText("Player \"" + name + "\" added.");
        showPanel(welcomePanel);
    }

    // ===== EDIT PLAYER FORM =====

    @FXML
    private void handleEditPlayer() {
        if (selectedPlayer == null) return;

        editNameField.setText(selectedPlayer.getName());
        editAgeField.setText(String.valueOf(selectedPlayer.getAge()));
        editJerseyField.setText(String.valueOf(selectedPlayer.getJerseyNumber()));
        editNationalityField.setText(selectedPlayer.getNationality());
        editPositionField.setValue(normalise(selectedPlayer.getPosition().name()));
        editErrorLabel.setText("");

        refreshTeamDropdown(editTeamField, false);
        editTeamField.setValue(
            selectedPlayer.getTeam() != null
                ? selectedPlayer.getTeam().getTeamName()
                : "No Team"
        );

        showPanel(editPlayerPanel);
    }

    @FXML
    private void handleSaveEdit() {
        String name        = editNameField.getText().trim();
        String ageStr      = editAgeField.getText().trim();
        String jerseyStr   = editJerseyField.getText().trim();
        String nationality = editNationalityField.getText().trim();
        String posStr      = editPositionField.getValue();

        if (name.isEmpty()) {
            editErrorLabel.setText("Name cannot be empty.");
            return;
        }

        if (nationality.isEmpty()) {
            editErrorLabel.setText("Nationality cannot be empty.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            editErrorLabel.setText("Age must be a positive whole number.");
            return;
        }

        int jersey;
        try {
            jersey = Integer.parseInt(jerseyStr);
            if (jersey <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            editErrorLabel.setText("Jersey number must be a positive whole number.");
            return;
        }

        if (posStr == null) {
            editErrorLabel.setText("Please select a position.");
            return;
        }

        selectedPlayer.setName(name);
        selectedPlayer.setAge(age);
        selectedPlayer.setJerseyNumber(jersey);
        selectedPlayer.setNationality(nationality);
        selectedPlayer.setPosition(Position.valueOf(posStr.toUpperCase()));

        // Handle team change
        String newTeamName = editTeamField.getValue();
        Team   currentTeam = selectedPlayer.getTeam();

        if (newTeamName == null || newTeamName.equals("No Team")) {
            if (currentTeam != null) currentTeam.removePlayer(selectedPlayer);
        } else if (currentTeam == null || !currentTeam.getTeamName().equals(newTeamName)) {
            if (currentTeam != null) currentTeam.removePlayer(selectedPlayer);
            for (Team t : teamList) {
                if (t.getTeamName().equals(newTeamName)) {
                    t.addPlayer(selectedPlayer);
                    break;
                }
            }
        }

        rerankTable();
        refreshDetailPanel();
        showPanel(detailPanel);
        statusLabel.setText(name + "'s details updated.");
    }

    @FXML
    private void handleCancelEdit() {
        showPanel(detailPanel);
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

        primaryFields.values().forEach(f -> f.setText("0"));
        additionalFields.values().forEach(f -> f.setText("0"));
    }

    // ===== DETAIL PANEL =====

    private void refreshDetailPanel() {
        if (selectedPlayer == null) return;

        playerNameLabel.setText(selectedPlayer.getName());
        positionLabel.setText(normalise(selectedPlayer.getPosition().name()));
        teamLabel.setText(selectedPlayer.getTeam() != null
            ? selectedPlayer.getTeam().getTeamName()
            : "No Team");
        ageLabel.setText("Age " + selectedPlayer.getAge());
        nationalityLabel.setText(selectedPlayer.getNationality().isEmpty()
            ? "Nationality unknown"
            : selectedPlayer.getNationality());

        // Score breakdown
        breakdownBox.getChildren().clear();
        LinkedHashMap<String, Double> breakdown = engine.getScoreBreakdown(selectedPlayer);
        double total = 0;
        for (Map.Entry<String, Double> entry : breakdown.entrySet()) {
            if (entry.getValue() == 0) continue;
            String sign = entry.getValue() < 0 ? "" : "+";
            Label line  = new Label(String.format("%-20s %s%.2f",
                entry.getKey() + ":", sign, entry.getValue()));
            line.setStyle("-fx-font-family: monospace;");
            breakdownBox.getChildren().add(line);
            total += entry.getValue();
        }
        scoreLabel.setText(String.format("Total Score: %.2f", total));

        refreshStatsBox();
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
        HBox   row    = new HBox();
        Label  lName  = new Label(label);
        lName.setStyle("-fx-text-fill: #555; -fx-font-size: 12;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label  lVal   = new Label(String.valueOf(value));
        lVal.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-size: 12;");
        row.getChildren().addAll(lName, spacer, lVal);
        statsBox.getChildren().add(row);
    }

    /**
     * Populates the controller with persisted data before the window is shown.
     * Called by {@link gui.App} immediately after the FXML controller is set.
     *
     * @param teams   the previously saved list of teams
     * @param players the previously saved list of players
     */
    public void loadData(java.util.List<Team> teams, java.util.List<Player> players) {
        teamList.addAll(teams);
        masterList.addAll(players);
        playerData.addAll(players);
        rerankTable();
    }



    // ===== HELPERS =====

    private void refreshTeamDropdown(ComboBox<String> dropdown, boolean includeNoTeam) {
        ObservableList<String> options = FXCollections.observableArrayList();
        if (includeNoTeam) options.add("No Team");
        for (Team t : teamList) options.add(t.getTeamName());
        dropdown.setItems(options);
        dropdown.setValue(includeNoTeam ? "No Team" : null);
    }

    private ArrayList<Player> resolveSource() {
        String filter = positionFilter.getValue();
        if (filter.equals("All")) return new ArrayList<>(masterList);
        return engine.filterByPosition(
            new ArrayList<>(masterList), Position.valueOf(filter.toUpperCase())
        );
    }

    private void autoRank() {
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
            addAdditional("Saves",          "saves");
            addAdditional("Clean Sheets",   "cleanSheets");
            addAdditional("Goals Conceded", "goalsConceded");
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

    private String normalise(String enumName) {
        return enumName.substring(0, 1).toUpperCase()
             + enumName.substring(1).toLowerCase();
    }

    /**
     * Returns the current list of teams. Called by {@link gui.App} on window close to save state.
     *
     * @return the list of all teams
     */
    public java.util.List<Team> getTeamList() {
        return teamList;
    }

    /**
     * Returns the master list of all players. Called by {@link gui.App} on window close to save state.
     *
     * @return the list of all players
     */
    public java.util.List<Player> getMasterList() {
        return masterList;
    }
}