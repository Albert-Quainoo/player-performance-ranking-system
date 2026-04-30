package model;

/**
 * Represents a player in a sports team management system.
 * A player has personal details, position, team affiliation, and performance records.
 */
public class Player {
    /** The player's name */
    private String name;
    /** The player's age */
    private int age;
    /** The player's jersey number */
    private int jerseyNumber;
    /** The player's position on the field */
    private Position position;
    /** The team the player belongs to (can be null if not assigned) */
    private Team team;
    /** The player's performance record */
    private PerformanceRecord performanceRecord;
    /** The player's nationality */
    private String nationality;


    /**
     * Constructs a Player with the specified personal details and position.
     * Initializes an empty performance record and no team affiliation.
     *
     * @param name the player's name
     * @param age the player's age in years
     * @param jerseyNumber the player's jersey number
     * @param position the player's position on the field
     */
    public Player(String name, int age, int jerseyNumber, Position position) {
        this.name = name;
        this.age = age;
        this.jerseyNumber = jerseyNumber;
        this.nationality = "";
        this.position = position;
        this.performanceRecord = new PerformanceRecord();
        this.team = null;
    }

    // ===== GETTERS =====

    /** Returns the player's name. */
    public String getName() { return name; }

    /** Returns the player's age. */
    public int getAge() { return age; }

    /** Returns the player's jersey number. */
    public int getJerseyNumber() { return jerseyNumber; }

    /** Returns the player's position on the field. */
    public Position getPosition() { return position; }

    /** Returns the team the player belongs to, or null if unassigned. */
    public Team getTeam() { return team; }

    /** Returns the player's performance record. */
    public PerformanceRecord getPerformanceRecord() { return performanceRecord; }

    /** Returns the player's nationality. */
    public String getNationality() { return nationality; }

    // ===== SETTERS =====

    public void setTeam(Team team)               { this.team          = team;          }
    public void setName(String name)             { this.name          = name;          }
    public void setAge(int age)                  { this.age           = age;           }
    public void setJerseyNumber(int number)      { this.jerseyNumber  = number;        }
    public void setPosition(Position position)   { this.position      = position;      }
    public void setNationality(String nationality){ this.nationality   = nationality;   }

    /** Returns a formatted summary of the player's details and performance stats. */
    public String displayPlayerInfo() {
        String teamName = (team == null) ? "No Team" : team.getTeamName();
        return "Player Information:\n"
                + "Name: "         + name         + "\n"
                + "Age: "          + age          + "\n"
                + "Jersey Number: "+ jerseyNumber + "\n"
                + "Position: "     + position     + "\n"
                + "Nationality: "  + nationality  + "\n"
                + "Team: "         + teamName     + "\n"
                + performanceRecord.display();
    }
}
