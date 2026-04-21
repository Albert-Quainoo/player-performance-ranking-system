package engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
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
        ArrayList <Player> rankedPlayersPerNList = rankPlayers(players);
        return new ArrayList<>(rankedPlayersPerNList.subList(0, Math.min(n, rankedPlayersPerNList.size()))); 
    }

    public String displayRankings(ArrayList<Player> players){
        ArrayList <Player> rankedPlayers = rankPlayers(players);
        StringBuilder sb = new StringBuilder();
        int rank = 1;
         for (Player player : rankedPlayers){
            sb.append(String.format("%d. %-20s | %-12s | Score: %.2f%n",
            rank++, player.getName(), player.getPosition(), rank(player)));
        }
        return sb.toString();
    }
    

}

