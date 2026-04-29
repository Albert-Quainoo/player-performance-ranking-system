import engine.RankingEngine;
import java.util.ArrayList;
import model.Player;
import model.Position;
import model.Team;

/**
 * Entry point for the console-mode demo of the Player Performance Ranking System.
 * Demonstrates team/player creation, performance record updates, and ranking output
 * without the GUI layer.
 */
public class Main {

    /**
     * Builds sample teams and players, populates stats, and prints rankings to stdout.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // --- Build teams ---
        Team heartsOfOak = new Team("Hearts of Oak", "Ghana");
        Team kotoko      = new Team("Asante Kotoko",  "Ghana");

        // --- Build players ---
        Player kwame  = new Player("Kwame Mensah",  27, 1,  Position.GOALKEEPER);
        Player bright = new Player("Bright Ofori",  24, 5,  Position.DEFENDER);
        Player kofi   = new Player("Kofi Asante",   26, 8,  Position.MIDFIELDER);
        Player eric   = new Player("Eric Boateng",  23, 9,  Position.FORWARD);
        Player yaw    = new Player("Yaw Darko",     25, 11, Position.FORWARD);
        Player ato    = new Player("Ato Mensah",    28, 4,  Position.DEFENDER);

        // addPlayer also calls player.setTeam() internally
        heartsOfOak.addPlayer(kwame);
        heartsOfOak.addPlayer(bright);
        heartsOfOak.addPlayer(kofi);
        kotoko.addPlayer(eric);
        kotoko.addPlayer(yaw);
        kotoko.addPlayer(ato);

        // --- Populate performance records ---
        kwame.getPerformanceRecord().updateSaves(95);
        kwame.getPerformanceRecord().updateCleanSheets(12);
        kwame.getPerformanceRecord().updateGoalsConceded(22);

        bright.getPerformanceRecord().updateTacklesWon(60);
        bright.getPerformanceRecord().updateInterceptions(45);
        bright.getPerformanceRecord().updateClearances(80);

        kofi.getPerformanceRecord().updateGoals(8);
        kofi.getPerformanceRecord().updateAssists(12);
        kofi.getPerformanceRecord().updateKeyPasses(55);
        kofi.getPerformanceRecord().updatePassesCompleted(300);

        eric.getPerformanceRecord().updateGoals(22);
        eric.getPerformanceRecord().updateAssists(9);
        eric.getPerformanceRecord().updateShotsOnTarget(68);

        yaw.getPerformanceRecord().updateGoals(15);
        yaw.getPerformanceRecord().updateAssists(5);
        yaw.getPerformanceRecord().updateShotsOnTarget(45);

        ato.getPerformanceRecord().updateTacklesWon(45);
        ato.getPerformanceRecord().updateClearances(60);
        ato.getPerformanceRecord().updateBlocks(20);

        // --- Display team info ---
        System.out.println("=== TEAM INFO ===");
        System.out.println(heartsOfOak.displayTeam());
        System.out.println(kotoko.displayTeam());

        // --- Display individual player info ---
        System.out.println("=== PLAYER INFO ===");
        for (Player p : heartsOfOak.getPlayers()) {
            System.out.println(p.displayPlayerInfo());
        }
        for (Player p : kotoko.getPlayers()) {
            System.out.println(p.displayPlayerInfo());
        }

        // --- Rank all players ---
        // getPlayers() returns an unmodifiable view, so copy to ArrayList for the engine
        ArrayList<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(heartsOfOak.getPlayers());
        allPlayers.addAll(kotoko.getPlayers());

        RankingEngine engine = new RankingEngine();

        System.out.println("=== FULL RANKINGS ===");
        System.out.println(engine.displayRankings(allPlayers));

        // --- Top 3 players ---
        System.out.println("=== TOP 3 PLAYERS ===");
        ArrayList<Player> topThree = engine.getTopNPlayers(allPlayers, 3);
        for (int i = 0; i < topThree.size(); i++) {
            Player p = topThree.get(i);
            System.out.printf("%d. %s (%.2f)%n",
                i + 1, p.getName(), engine.rank(p));
        }

        // --- Test resetStats ---
        System.out.println("\n=== AFTER RESET (Kofi Asante) ===");
        System.out.println("Score before reset: " + engine.rank(kofi));
        kofi.getPerformanceRecord().resetStats();
        System.out.println("Score after reset:  " + engine.rank(kofi));
        System.out.println(kofi.getPerformanceRecord().display());
    }
}