package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Player;
import model.Position;
import ranking.DefenderRankingStrategy;
import ranking.ForwardRankingStrategy;
import ranking.GoalkeeperRankingStrategy;
import ranking.MidfielderRankingStrategy;
import ranking.RankingStrategy;

public class RankingEngine {
    private final Map<Position, RankingStrategy>  strategies;

    public RankingEngine(){
        strategies = new EnumMap<>(Position.class);
        strategies.put(Position.GOALKEEPER, new GoalkeeperRankingStrategy());
        strategies.put(Position.DEFENDER, new DefenderRankingStrategy());
        strategies.put(Position.MIDFIELDER, new MidfielderRankingStrategy());
        strategies.put(Position.FORWARD, new ForwardRankingStrategy());
    }

    public double rank(Player player){
        RankingStrategy strategy = strategies.get(player.getPosition());

        if (strategy == null){
            throw new IllegalArgumentException("No ranking strategy registered for position: " + player.getPosition());
        }

        return strategy.calculateScore(player);
    }

    public void setStrategy(Position position, RankingStrategy strategy){
        strategies.put(position, strategy);
    }

    public ArrayList<Player> rankPlayers(ArrayList<Player> players){
        Comparator <Player> byScore = Comparator.comparingDouble(this::rank).reversed();
        ArrayList <Player> sorted = new ArrayList<>(players);
        sorted.sort(byScore);
        return sorted;
    }

    public ArrayList<Player> getTopNPlayers(ArrayList<Player> players, int n){
        if (n < 0){
            throw new IllegalArgumentException("n cannot be negative");
        }
        ArrayList <Player> rankedPlayersPerNList = rankPlayers(players);
        return new ArrayList<>(rankedPlayersPerNList.subList(0, Math.min(n, rankedPlayersPerNList.size()))); 
    }

    public ArrayList<Player> filterByPosition(ArrayList<Player> players, Position position){
        ArrayList<Player> filtered = new ArrayList<>();
        for (Player p : players){
            if (p.getPosition() == position){
                filtered.add(p);
            }
        }
        return filtered;
    }

    public LinkedHashMap<String, Double> getScoreBreakdown(Player player) {
    RankingStrategy strategy = strategies.get(player.getPosition());
    if (strategy == null) {
        throw new IllegalArgumentException(
            "No ranking strategy registered for position: " + player.getPosition()
        );
    }
    return strategy.getBreakdown(player);
}

    public String displayRankings(ArrayList<Player> players){
        Map<Player, Double> sortedCache = new LinkedHashMap<>();
        for (Player p : players){
            sortedCache.put(p, rank(p));
        }
        List<Player> ranked = new ArrayList<>(sortedCache.keySet());
        ranked.sort((a,b) -> Double.compare(sortedCache.get(b),sortedCache.get(a))); 

        StringBuilder sb = new StringBuilder(); 
        int rank = 1;
         for (Player player : ranked){
            sb.append(String.format("%d. %-20s | %-12s | Score: %.2f%n",
            rank++, player.getName(), player.getPosition(), sortedCache.get(player)));
        }
        return sb.toString();
    }
    

}

