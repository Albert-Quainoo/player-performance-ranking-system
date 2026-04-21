package ranking;

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
    
}
