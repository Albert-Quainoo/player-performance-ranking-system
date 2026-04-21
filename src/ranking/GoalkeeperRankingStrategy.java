package ranking;

import model.PerformanceRecord;
import model.Player;

public class GoalkeeperRankingStrategy implements RankingStrategy{
    @Override
    public double calculateScore(Player player){
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

}
