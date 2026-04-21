package ranking;
import model.Player;

public interface RankingStrategy{
    public abstract double calculateScore(Player player);

}