package ranking;

import java.util.LinkedHashMap;
import model.Player;

/**
 * Strategy interface for computing a player's performance score.
 *
 * <p>Each concrete implementation encodes the scoring formula for one field position.
 * The {@link engine.RankingEngine} selects the correct strategy at runtime based on
 * the player's position, demonstrating the <em>Strategy</em> design pattern.
 */
public interface RankingStrategy {

    /**
     * Calculates a numeric performance score for the given player.
     *
     * @param player the player to evaluate
     * @return the calculated score (higher is better; penalties may produce negatives)
     */
    double calculateScore(Player player);

    /**
     * Returns an ordered breakdown of each scoring component and its contribution.
     * Negative values represent penalties (e.g. cards, goals conceded).
     *
     * @param player the player to break down
     * @return an ordered map of component name to point value
     */
    LinkedHashMap<String, Double> getBreakdown(Player player);
}