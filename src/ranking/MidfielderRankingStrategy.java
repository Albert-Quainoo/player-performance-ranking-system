package ranking;

import java.util.LinkedHashMap;
import model.PerformanceRecord;
import model.Player;

public class MidfielderRankingStrategy implements RankingStrategy{
    @Override
    public double calculateScore(Player player){
        PerformanceRecord pr = player.getPerformanceRecord();

        double keyPassScore = pr.getKeyPasses() * 3.0;
        double passesScore = pr.getPassesCompleted() * 0.5;
        double assistScore = pr.getAssists() * 3.5;
        double goalScore = pr.getGoals() * 4.0;
        double ballRecoveryScore = pr.getBallRecoveries() * 1.5;
        double interceptionScore = pr.getInterceptions() * 1.5;
        double yellowCardPenalty = pr.getYellowCards() * 1.0;
        double redCardPenalty = pr.getRedCards() * 3.0;

        return keyPassScore + passesScore + assistScore + goalScore 
            + ballRecoveryScore + interceptionScore 
            - yellowCardPenalty - redCardPenalty;

    }

    @Override
    public LinkedHashMap<String, Double> getBreakdown(Player player) {
        PerformanceRecord r = player.getPerformanceRecord();
        LinkedHashMap<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("Key Passes",       r.getKeyPasses()       * 3.0);
        breakdown.put("Passes Completed", r.getPassesCompleted() * 0.5);
        breakdown.put("Assists",          r.getAssists()         * 3.5);
        breakdown.put("Goals",            r.getGoals()           * 4.0);
        breakdown.put("Ball Recoveries",  r.getBallRecoveries()  * 1.5);
        breakdown.put("Interceptions",    r.getInterceptions()   * 1.5);
        breakdown.put("Yellow Cards",     r.getYellowCards()     * -1.0);
        breakdown.put("Red Cards",        r.getRedCards()        * -3.0);
        return breakdown;
    }

}
