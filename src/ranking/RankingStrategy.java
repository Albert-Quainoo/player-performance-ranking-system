package ranking;

import java.util.LinkedHashMap;
import model.Player;

/**
 * Interface for calculating a player's performance score.
 * Each position (Goalkeeper, Defender, etc.) has its own class that implements
 * this interface with different scoring weights — this is the Strategy pattern.
 */
public interface RankingStrategy {

    /**
     * Calculates and returns a score for the given player.
     *
     * @param player the player to score
     * @return the total score (higher is better; card penalties can make it negative)
     */
    double calculateScore(Player player);

    /**
     * Returns a breakdown of how the score was calculated.
     * Each entry maps a stat name to the points it contributed.
     *
     * @param player the player to break down
     * @return an ordered map of stat name to point value
     */
    LinkedHashMap<String, Double> getBreakdown(Player player);
}