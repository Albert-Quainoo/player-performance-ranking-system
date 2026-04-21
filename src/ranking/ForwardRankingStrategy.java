package ranking;

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

}
