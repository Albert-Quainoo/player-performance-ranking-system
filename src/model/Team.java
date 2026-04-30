package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a sports team that manages a collection of players.
 * Provides functionality to add, remove, and display players on the team.
 */
public class Team {
    /** The name of the team */
    private String teamName;
    /** The country the team represents */
    private String country;
    /** The list of players on the team */
    private List<Player> players;

    /**
     * Creates a team with the given name and country.
     *
     * @param teamName the name of the team
     * @param country  the country the team is from
     */
    public Team(String teamName, String country) {
        this.teamName = teamName;
        this.country  = country;
        this.players  = new ArrayList<>();
    }

    // ===== GETTERS =====

    /** Returns the team's name. */
    public String getTeamName() { return teamName; }

    /** Returns the country the team is from. */
    public String getTeamCountry() { return country; }

    /** Returns a read-only view of the players on this team. */
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }

    // ===== SETTERS =====

    public void setTeamName(String teamName)    { this.teamName = teamName; }
    public void setTeamCountry(String country)  { this.country  = country;  }

    /**
     * Adds a player to the team. Also sets the player's team to this team.
     * Does nothing if the player is null or already on this team.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        if (player == null) {
            return;
        }

        if (!players.contains(player)) {
            players.add(player);
            player.setTeam(this);
        }
    }

    /**
     * Removes a player from the team and clears their team reference.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        if (players.remove(player)) {
            player.setTeam(null);
        }
    }

    /** Returns a formatted summary of the team and its players. */
    public String displayTeam() {
        StringBuilder builder = new StringBuilder();
        builder.append("Team: ").append(teamName)
               .append(" | Country: ").append(country).append("\n");
        builder.append("Players:\n");
        for (Player player : players) {
            builder.append("- ")
                   .append(player.getName())
                   .append(" (")
                   .append(player.getPosition())
                   .append(")\n");
        }
        return builder.toString();
    }
}