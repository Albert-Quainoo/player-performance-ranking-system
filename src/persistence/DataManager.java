package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.PerformanceRecord;
import model.Player;
import model.Position;
import model.Team;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads all application data to/from a JSON file on disk.
 * Data is stored in ~/player-ranking-system/data.json.
 *
 * Teams and players are saved separately to avoid storing duplicate nested objects.
 * When loading, teams are rebuilt first, then players are linked back to their team by name.
 */
public class DataManager {

    private static final String SAVE_DIR  = System.getProperty("user.home")
                                          + File.separator + "player-ranking-system";
    private static final String SAVE_FILE = SAVE_DIR + File.separator + "data.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // ===== HELPER CLASSES FOR JSON SERIALIZATION =====
    // Simple flat classes used only for reading/writing JSON — no game logic here

    private static class TeamDTO {
        String name;
        String country;

        TeamDTO(String name, String country) {
            this.name    = name;
            this.country = country;
        }
    }

    private static class PlayerDTO {
        String name;
        int    age;
        int    jerseyNumber;
        String position;
        String nationality;
        String teamName;       // stored by name to avoid nesting the full Team object

        // Performance stats stored directly in the player record
        int goals;
        int assists;
        int keyPasses;
        int passesCompleted;
        int shotsOnTarget;
        int tacklesWon;
        int interceptions;
        int blocks;
        int clearances;
        int ballRecoveries;
        int saves;
        int cleanSheets;
        int goalsConceded;
        int yellowCards;
        int redCards;
    }

    private static class SaveData {
        List<TeamDTO>   teams   = new ArrayList<>();
        List<PlayerDTO> players = new ArrayList<>();
    }

    // ===== SAVE =====

    /**
     * Saves all teams and players to the JSON file.
     *
     * @param teams   the list of teams to save
     * @param players the list of players to save
     */
    public void save(List<Team> teams, List<Player> players) {
        SaveData data = new SaveData();

        for (Team t : teams) {
            data.teams.add(new TeamDTO(t.getTeamName(), t.getTeamCountry()));
        }

        for (Player p : players) {
            PlayerDTO dto       = new PlayerDTO();
            dto.name            = p.getName();
            dto.age             = p.getAge();
            dto.jerseyNumber    = p.getJerseyNumber();
            dto.position        = p.getPosition().name();
            dto.nationality     = p.getNationality();
            dto.teamName        = p.getTeam() != null ? p.getTeam().getTeamName() : null;

            PerformanceRecord r = p.getPerformanceRecord();
            dto.goals           = r.getGoals();
            dto.assists         = r.getAssists();
            dto.keyPasses       = r.getKeyPasses();
            dto.passesCompleted = r.getPassesCompleted();
            dto.shotsOnTarget   = r.getShotsOnTarget();
            dto.tacklesWon      = r.getTacklesWon();
            dto.interceptions   = r.getInterceptions();
            dto.blocks          = r.getBlocks();
            dto.clearances      = r.getClearances();
            dto.ballRecoveries  = r.getBallRecoveries();
            dto.saves           = r.getSaves();
            dto.cleanSheets     = r.getCleanSheets();
            dto.goalsConceded   = r.getGoalsConceded();
            dto.yellowCards     = r.getYellowCards();
            dto.redCards        = r.getRedCards();

            data.players.add(dto);
        }

        try {
            // Create save directory if it does not exist
            Path dir = Paths.get(SAVE_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            try (Writer writer = new FileWriter(SAVE_FILE)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    // ===== LOAD =====

    /**
     * Loads teams and players from the save file.
     * Returns empty lists if the file does not exist yet (first launch).
     *
     * @return a LoadResult containing the restored teams and players
     */
    public LoadResult load() {
        Path path = Paths.get(SAVE_FILE);
        if (!Files.exists(path)) {
            // First run — no save file yet
            return new LoadResult(new ArrayList<>(), new ArrayList<>());
        }

        try (Reader reader = new FileReader(SAVE_FILE)) {
            Type type = new TypeToken<SaveData>() {}.getType();
            SaveData data = gson.fromJson(reader, type);

            // Rebuild teams first
            List<Team> teams = new ArrayList<>();
            for (TeamDTO dto : data.teams) {
                teams.add(new Team(dto.name, dto.country));
            }

            // Build a lookup map for team assignment
            java.util.Map<String, Team> teamMap = new java.util.HashMap<>();
            for (Team t : teams) {
                teamMap.put(t.getTeamName(), t);
            }

            // Rebuild players
            List<Player> players = new ArrayList<>();
            for (PlayerDTO dto : data.players) {
                Player player = new Player(
                    dto.name,
                    dto.age,
                    dto.jerseyNumber,
                    Position.valueOf(dto.position)
                );
                player.setNationality(dto.nationality != null ? dto.nationality : "");

                // Restore team assignment
                if (dto.teamName != null && teamMap.containsKey(dto.teamName)) {
                    teamMap.get(dto.teamName).addPlayer(player);
                }

                // Restore performance record
                PerformanceRecord r = player.getPerformanceRecord();
                r.updateGoals(dto.goals);
                r.updateAssists(dto.assists);
                r.updateKeyPasses(dto.keyPasses);
                r.updatePassesCompleted(dto.passesCompleted);
                r.updateShotsOnTarget(dto.shotsOnTarget);
                r.updateTacklesWon(dto.tacklesWon);
                r.updateInterceptions(dto.interceptions);
                r.updateBlocks(dto.blocks);
                r.updateClearances(dto.clearances);
                r.updateBallRecoveries(dto.ballRecoveries);
                r.updateSaves(dto.saves);
                r.updateCleanSheets(dto.cleanSheets);
                r.updateGoalsConceded(dto.goalsConceded);
                r.updateYellowCards(dto.yellowCards);
                r.updateRedCards(dto.redCards);

                players.add(player);
            }

            return new LoadResult(teams, players);

        } catch (IOException e) {
            System.err.println("Failed to load data: " + e.getMessage());
            return new LoadResult(new ArrayList<>(), new ArrayList<>());
        }
    }

    // ===== LOAD RESULT =====

    /** Holds the teams and players returned by load() so both can be passed at once. */
    public static class LoadResult {
        public final List<Team>   teams;
        public final List<Player> players;

        public LoadResult(List<Team> teams, List<Player> players) {
            this.teams   = teams;
            this.players = players;
        }
    }
}