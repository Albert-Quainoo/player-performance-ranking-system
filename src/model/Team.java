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
     * Constructs a Team with the specified name and initializes an empty player list.
     *
     * @param teamName the name of the team
     */
    public Team(String teamName, String country) {
        this.teamName = teamName;
        this.country = country;
        this.players = new ArrayList<>();
    }

    /**
     * Returns the name of the team.
     *
     * @return the team's name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Returns the country the team represents.
     *
     * @return the team's country
     */
    public String getTeamCountry() {
        return country;
    }

    /**
     * Sets the team's name.
     *
     * @param teamName the new team name
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Sets the country the team represents.
     *
     * @param country the new country
     */
    public void setTeamCountry(String country) {
        this.country = country;
    }

    /**
     * Returns an unmodifiable list of all players on the team.
     *
     * @return an unmodifiable list of players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Adds a player to the team if they are not already a member.
     * Sets the player's team affiliation to this team.
     *
     * @param player the player to add (null values are ignored)
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
     * Removes a player from the team.
     * Clears the player's team affiliation.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        if (players.remove(player)) {
            player.setTeam(null);
        }
    }

    /**
     * Generates a formatted string representation of the team.
     * Displays the team name and a list of all players with their positions.
     *
     * @return a formatted string containing team and player information
     */
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