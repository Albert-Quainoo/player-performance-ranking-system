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
 * Coordinates player ranking by delegating score calculation to the appropriate
 * {@link ranking.RankingStrategy} for each field position.
 *
 * <p>This class applies the <em>Strategy</em> design pattern: each position
 * (Goalkeeper, Defender, Midfielder, Forward) has its own scoring formula
 * registered at construction time and swappable at runtime via
 * {@link #setStrategy(Position, RankingStrategy)}.
 */
public class RankingEngine {

    /** Maps each field position to its corresponding scoring strategy. */
    private final Map<Position, RankingStrategy> strategies;

    /**
     * Constructs a RankingEngine with the default scoring strategies for all four positions.
     */
    public RankingEngine() {
        strategies = new EnumMap<>(Position.class);
        strategies.put(Position.GOALKEEPER, new GoalkeeperRankingStrategy());
        strategies.put(Position.DEFENDER, new DefenderRankingStrategy());
        strategies.put(Position.MIDFIELDER, new MidfielderRankingStrategy());
        strategies.put(Position.FORWARD, new ForwardRankingStrategy());
    }

    /**
     * Calculates and returns the performance score for a given player using
     * the strategy registered for their position.
     *
     * @param player the player to score
     * @return the calculated performance score
     * @throws IllegalArgumentException if no strategy is registered for the player's position
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

    /**
     * Replaces the scoring strategy for the given position.
     *
     * @param position the position whose strategy to replace
     * @param strategy the new scoring strategy
     */
    public void setStrategy(Position position, RankingStrategy strategy) {
        strategies.put(position, strategy);
    }

    /**
     * Returns a copy of the player list sorted by descending performance score.
     *
     * @param players the list of players to sort
     * @return a new sorted list, highest score first
     */
    public ArrayList<Player> rankPlayers(ArrayList<Player> players) {
        Comparator<Player> byScore = Comparator.comparingDouble(this::rank).reversed();
        ArrayList<Player> sorted = new ArrayList<>(players);
        sorted.sort(byScore);
        return sorted;
    }

    /**
     * Returns the top {@code n} players by performance score.
     *
     * @param players the full list of players to consider
     * @param n       the maximum number of players to return
     * @return a list of up to {@code n} players, highest score first
     * @throws IllegalArgumentException if {@code n} is negative
     */
    public ArrayList<Player> getTopNPlayers(ArrayList<Player> players, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be negative");
        }
        ArrayList<Player> ranked = rankPlayers(players);
        return new ArrayList<>(ranked.subList(0, Math.min(n, ranked.size())));
    }

    /**
     * Returns only the players in the list who play at the specified position.
     *
     * @param players  the list to filter
     * @param position the position to filter by
     * @return a new list containing only players at that position
     */
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
     * Returns an ordered map of scoring components and their point values for a player.
     * Negative values represent penalties.
     *
     * @param player the player to break down
     * @return an ordered map of stat label to score contribution
     * @throws IllegalArgumentException if no strategy is registered for the player's position
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

    /**
     * Builds a formatted ranking table string for the given list of players,
     * sorted by descending score.
     *
     * @param players the list of players to display
     * @return a formatted multi-line ranking string
     */
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

