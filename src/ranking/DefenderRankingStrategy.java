package ranking;

import java.util.LinkedHashMap;
import model.PerformanceRecord;
import model.Player;

public class DefenderRankingStrategy implements RankingStrategy{
    @Override
    public double calculateScore(Player player){
        PerformanceRecord pr = player.getPerformanceRecord();

        double tackleScore = pr.getTacklesWon() * 2.5;
        double interceptionScore = pr.getInterceptions() * 2.5;
        double clearanceScore = pr.getClearances() * 2.0;
        double blockScore = pr.getBlocks() * 2.0;
        double ballRecoveryScore = pr.getBallRecoveries() * 1.5;
        double passesScore = pr.getPassesCompleted() * 0.3;
        double yellowCardPenalty = pr.getYellowCards() * 1.5;
        double redCardPenalty = pr.getRedCards() * 1.5;

        return tackleScore + interceptionScore + clearanceScore + blockScore 
            + ballRecoveryScore + passesScore - yellowCardPenalty - redCardPenalty;
    }

    @Override
    public LinkedHashMap<String, Double> getBreakdown(Player player) {
        PerformanceRecord r = player.getPerformanceRecord();
        LinkedHashMap<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Tackles Won",      r.getTacklesWon()      * 2.5);
        breakdown.put("Interceptions",    r.getInterceptions()   * 2.5);
        breakdown.put("Clearances",       r.getClearances()      * 2.0);
        breakdown.put("Blocks",           r.getBlocks()          * 2.0);
        breakdown.put("Ball Recoveries",  r.getBallRecoveries()  * 1.5);
        breakdown.put("Passes Completed", r.getPassesCompleted() * 0.3);
        breakdown.put("Yellow Cards",     r.getYellowCards()     * -1.5);
        breakdown.put("Red Cards",        r.getRedCards()        * -4.0);
        return breakdown;
    }
}
