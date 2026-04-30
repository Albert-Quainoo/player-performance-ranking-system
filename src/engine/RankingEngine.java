package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Player;
import model.Position;
import ranking.DefenderRankingStrategy;
import ranking.ForwardRankingStrategy;
import ranking.GoalkeeperRankingStrategy;
import ranking.MidfielderRankingStrategy;
import ranking.RankingStrategy;

/**
 * Ranks players based on their position and performance stats.
 * Uses the Strategy pattern — each position has its own scoring formula
 * stored in a map, so the right formula is picked automatically at runtime.
 */
public class RankingEngine {

    // Maps each position to its scoring strategy
    private final Map<Position, RankingStrategy> strategies;

    /** Sets up the default scoring strategy for each position. */
    public RankingEngine() {
        strategies = new EnumMap<>(Position.class);
        strategies.put(Position.GOALKEEPER, new GoalkeeperRankingStrategy());
        strategies.put(Position.DEFENDER, new DefenderRankingStrategy());
        strategies.put(Position.MIDFIELDER, new MidfielderRankingStrategy());
        strategies.put(Position.FORWARD, new ForwardRankingStrategy());
    }

    /**
     * Returns the performance score for a player using their position's scoring formula.
     *
     * @param player the player to score
     * @return the calculated score
     */
    public double rank(Player player) {
        RankingStrategy strategy = strategies.get(player.getPosition());
        if (strategy == null) {
            throw new IllegalArgumentException(
                "No ranking strategy registered for position: " + player.getPosition()
            );
        }
        return strategy.calculateScore(player);
    }

    /** Swaps in a different scoring strategy for a given position. */
    public void setStrategy(Position position, RankingStrategy strategy) {
        strategies.put(position, strategy);
    }

    /** Returns a new list of players sorted from highest to lowest score. */
    public ArrayList<Player> rankPlayers(ArrayList<Player> players) {
        Comparator<Player> byScore = Comparator.comparingDouble(this::rank).reversed();
        ArrayList<Player> sorted = new ArrayList<>(players);
        sorted.sort(byScore);
        return sorted;
    }

    /**
     * Returns the top n players by score.
     *
     * @param players the full player list
     * @param n       how many top players to return
     * @return a list of up to n players, highest score first
     */
    public ArrayList<Player> getTopNPlayers(ArrayList<Player> players, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be negative");
        }
        ArrayList<Player> ranked = rankPlayers(players);
        return new ArrayList<>(ranked.subList(0, Math.min(n, ranked.size())));
    }

    /** Returns only the players from the list who play at the given position. */
    public ArrayList<Player> filterByPosition(ArrayList<Player> players, Position position) {
        ArrayList<Player> filtered = new ArrayList<>();
        for (Player p : players) {
            if (p.getPosition() == position) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    /**
     * Returns a breakdown of how a player's score was calculated.
     * Each entry is a stat name and the points it contributed (negative = penalty).
     */
    public LinkedHashMap<String, Double> getScoreBreakdown(Player player) {
        RankingStrategy strategy = strategies.get(player.getPosition());
        if (strategy == null) {
            throw new IllegalArgumentException(
                "No ranking strategy registered for position: " + player.getPosition()
            );
        }
        return strategy.getBreakdown(player);
    }

    /** Returns a formatted ranking table for the given players, sorted by score. */
    public String displayRankings(ArrayList<Player> players) {
        Map<Player, Double> scoreCache = new LinkedHashMap<>();
        for (Player p : players) {
            scoreCache.put(p, rank(p));
        }
        List<Player> ranked = new ArrayList<>(scoreCache.keySet());
        ranked.sort((a, b) -> Double.compare(scoreCache.get(b), scoreCache.get(a)));

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (Player player : ranked) {
            sb.append(String.format("%d. %-20s | %-12s | Score: %.2f%n",
                rank++, player.getName(), player.getPosition(), scoreCache.get(player)));
        }
        return sb.toString();
    }
}

