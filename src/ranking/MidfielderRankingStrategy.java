package ranking;

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

}
