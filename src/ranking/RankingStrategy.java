package ranking;
import java.util.LinkedHashMap;
import model.Player;

public interface RankingStrategy{
    double calculateScore(Player player);
    LinkedHashMap<String, Double> getBreakdown(Player player);


}