package ranking;

import java.util.LinkedHashMap;
import model.PerformanceRecord;
import model.Player;

/**
 * Scoring strategy for goalkeepers.
 *
 * <p>Rewards saves, clean sheets, blocks, and clearances.
 * Penalises goals conceded and disciplinary cards.
 */
public class GoalkeeperRankingStrategy implements RankingStrategy {
    @Override
    public double calculateScore(Player player) {
        PerformanceRecord pr = player.getPerformanceRecord();

        double saveScore = pr.getSaves() * 3.0;
        double cleanSheetScore = pr.getCleanSheets() * 6.0;
        double blockScore = pr.getBlocks() * 2.0;
        double clearanceScore = pr.getClearances() * 1.5;
        double concededPenalty = pr.getGoalsConceded() * 2.5;
        double yellowCardPenalty = pr.getYellowCards() * 1.0;
        double redCardPenalty = pr.getRedCards() * 3.0;

        return saveScore + cleanSheetScore + blockScore + clearanceScore 
            - concededPenalty - yellowCardPenalty - redCardPenalty;
    }

    @Override
    public LinkedHashMap<String, Double> getBreakdown(Player player) {
        PerformanceRecord r = player.getPerformanceRecord();
        LinkedHashMap<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Saves",          r.getSaves()         * 3.0);
        breakdown.put("Clean Sheets",   r.getCleanSheets()   * 6.0);
        breakdown.put("Blocks",         r.getBlocks()        * 2.0);
        breakdown.put("Clearances",     r.getClearances()    * 1.5);
        breakdown.put("Goals Conceded", r.getGoalsConceded() * -2.5);
        breakdown.put("Yellow Cards",   r.getYellowCards()   * -1.0);
        breakdown.put("Red Cards",      r.getRedCards()      * -3.0);
        return breakdown;
    }

}
