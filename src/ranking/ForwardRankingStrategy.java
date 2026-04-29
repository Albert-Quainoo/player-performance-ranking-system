package ranking;

import java.util.LinkedHashMap;
import model.PerformanceRecord;
import model.Player;

public class ForwardRankingStrategy implements RankingStrategy{
    @Override
    public double calculateScore(Player player){
        PerformanceRecord pr = player.getPerformanceRecord();

        double goalScore = pr.getGoals() * 5.0;
        double assistScore = pr.getAssists() * 3.0;
        double shotsOnTargetScore = pr.getShotsOnTarget() * 1.5;
        double keyPassScore = pr.getPassesCompleted() * 1.0;
        double yellowCardPenalty = pr.getYellowCards() * 1.0;
        double redCardPenalty = pr.getRedCards() * 3.0;

        return goalScore + assistScore + shotsOnTargetScore 
            + keyPassScore - yellowCardPenalty - redCardPenalty;
    }

     @Override
    public LinkedHashMap<String, Double> getBreakdown(Player player) {
        PerformanceRecord r = player.getPerformanceRecord();
        LinkedHashMap<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Goals",          r.getGoals()         * 5.0);
        breakdown.put("Assists",        r.getAssists()       * 3.0);
        breakdown.put("Shots on Target",r.getShotsOnTarget() * 1.5);
        breakdown.put("Key Passes",     r.getKeyPasses()     * 1.0);
        breakdown.put("Yellow Cards",   r.getYellowCards()   * -1.0);
        breakdown.put("Red Cards",      r.getRedCards()      * -3.0);
        return breakdown;
    }

    

}
