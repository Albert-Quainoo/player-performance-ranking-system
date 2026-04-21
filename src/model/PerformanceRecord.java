package model;

/**
 * PerformanceRecord class that tracks and manages football/soccer player performance statistics.
 * It maintains various metrics such as goals, assists, defensive actions, and disciplinary records.
 * All statistics are tracked cumulatively and can be updated through dedicated update methods.
 */
public class PerformanceRecord {

    // Offensive statistics
    /** Number of goals scored by the player */
    private int goals;
    /** Number of assists provided by the player */
    private int assists;
    /** Number of key passes made by the player */
    private int keyPasses;
    /** Total number of passes completed by the player */
    private int passesCompleted;
    /** Number of shots on target made by the player */
    private int shotsOnTarget;

    // Defensive statistics
    /** Number of tackles won by the player */
    private int tacklesWon;
    /** Number of interceptions made by the player */
    private int interceptions;
    /** Number of blocked shots/passes by the player */
    private int blocks;
    /** Number of clearances made by the player */
    private int clearances;
    /** Number of times the player recovered the ball */
    private int ballRecoveries;
    /** Number of saves made by the goalkeeper */
    private int saves;
    /** Number of goals conceded by the goalkeeper */
    private int goalsConceded;
    /** Number of clean sheets (games with no goals conceded) */
    private int cleanSheets;

    // Disciplinary records
    /** Number of yellow cards received by the player */
    private int yellowCards;
    /** Number of red cards received by the player */
    private int redCards;

    /**
     * Constructor that initializes all performance statistics to zero.
     */
    public PerformanceRecord() {
        this.goals = 0;
        this.assists = 0;
        this.keyPasses = 0;
        this.passesCompleted = 0;
        this.tacklesWon = 0;
        this.interceptions = 0;
        this.blocks = 0;
        this.clearances = 0;
        this.ballRecoveries =0;
        this.shotsOnTarget = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.saves = 0;
        this.goalsConceded = 0;
        this.cleanSheets = 0;
    }

    /**
     * Updates the ball recoveries statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param ballRecoveries the number of ball recoveries to add (must be positive)
     */
    public void updateBallRecoveries(int ballRecoveries) {
        if (ballRecoveries > 0) {
            this.ballRecoveries += ballRecoveries;
        }
    }

    /**
     * Updates the shots on target statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param shotsOnTarget the number of shots on target to add (must be positive)
     */
    public void updateShotsOnTarget(int shotsOnTarget) {
        if (shotsOnTarget > 0) {
            this.shotsOnTarget += shotsOnTarget;
        }
    }

    /**
     * Updates the yellow cards statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param yellowCards the number of yellow cards to add (must be positive)
     */
    public void updateYellowCards(int yellowCards) {
        if (yellowCards > 0) {
            this.yellowCards += yellowCards;
        }
    }

    /**
     * Updates the red cards statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param redCards the number of red cards to add (must be positive)
     */
    public void updateRedCards(int redCards) {
        if (redCards > 0) {
            this.redCards += redCards;
        }
    }

    /**
     * Updates the goals statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param goals the number of goals to add (must be positive)
     */
    public void updateGoals(int goals) {
        if (goals > 0) {
            this.goals += goals;
        }
    }

    /**
     * Updates the assists statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param assists the number of assists to add (must be positive)
     */
    public void updateAssists(int assists) {
        if (assists > 0) {
            this.assists += assists;
        }
    }

    /**
     * Updates the key passes statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param keyPasses the number of key passes to add (must be positive)
     */
    public void updateKeyPasses(int keyPasses) {
        if (keyPasses > 0) {
            this.keyPasses += keyPasses;
        }
    }

    /**
     * Updates the passes completed statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param passesCompleted the number of passes completed to add (must be positive)
     */
    public void updatePassesCompleted(int passesCompleted) {
        if (passesCompleted > 0) {
            this.passesCompleted += passesCompleted;
        }
    }

    /**
     * Updates the tackles won statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param tacklesWon the number of tackles won to add (must be positive)
     */
    public void updateTacklesWon(int tacklesWon) {
        if (tacklesWon > 0) {
            this.tacklesWon += tacklesWon;
        }
    }

    /**
     * Updates the interceptions statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param interceptions the number of interceptions to add (must be positive)
     */
    public void updateInterceptions(int interceptions) {
        if (interceptions > 0) {
            this.interceptions += interceptions;
        }
    }

    /**
     * Updates the blocks statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param blocks the number of blocks to add (must be positive)
     */
    public void updateBlocks(int blocks) {
        if (blocks > 0) {
            this.blocks += blocks;
        }
    }

    /**
     * Updates the clearances statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param clearances the number of clearances to add (must be positive)
     */
    public void updateClearances(int clearances) {
        if (clearances > 0) {
            this.clearances += clearances;
        }
    }

    /**
     * Updates the saves statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param saves the number of saves to add (must be positive)
     */
    public void updateSaves(int saves) {
        if (saves > 0) {
            this.saves += saves;
        }
    }

    /**
     * Updates the goals conceded statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param goalsConceded the number of goals conceded to add (must be positive)
     */
    public void updateGoalsConceded(int goalsConceded) {
        if (goalsConceded > 0) {
            this.goalsConceded += goalsConceded;
        }
    }

    /**
     * Updates the clean sheets statistic by adding the provided value.
     * Only updates if the value is positive.
     *
     * @param cleanSheets the number of clean sheets to add (must be positive)
     */
    public void updateCleanSheets(int cleanSheets) {
        if (cleanSheets > 0) {
            this.cleanSheets += cleanSheets;
        }
    }

    /**
    * Resets all performance statistics back to zero.
    * Used to clear a player's record at the start of a new season or evaluation period.
    */
    public void resetStats() {
        this.goals = 0;
        this.assists = 0;
        this.keyPasses = 0;
        this.passesCompleted = 0;
        this.shotsOnTarget = 0;
        this.tacklesWon = 0;
        this.interceptions = 0;
        this.blocks = 0;
        this.clearances = 0;
        this.ballRecoveries = 0;
        this.saves = 0;
        this.goalsConceded = 0;
        this.cleanSheets = 0;
        this.yellowCards = 0;
        this.redCards = 0;
    }

    // ===== GETTER METHODS =====

    /**
     * Returns the number of ball recoveries.
     *
     * @return the ball recoveries count
     */
    public int getBallRecoveries() {
        return ballRecoveries;
    }

    /**
     * Returns the number of shots on target.
     *
     * @return the shots on target count
     */
    public int getShotsOnTarget() {
        return shotsOnTarget;
    }

    /**
     * Returns the number of yellow cards received.
     *
     * @return the yellow cards count
     */
    public int getYellowCards() {
        return yellowCards;
    }

    /**
     * Returns the number of red cards received.
     *
     * @return the red cards count
     */
    public int getRedCards() {
        return redCards;
    }

    /**
     * Returns the number of goals scored.
     *
     * @return the goals count
     */
    public int getGoals() {
        return goals;
    }

    /**
     * Returns the number of assists provided.
     *
     * @return the assists count
     */
    public int getAssists() {
        return assists;
    }

    /**
     * Returns the number of key passes made.
     *
     * @return the key passes count
     */
    public int getKeyPasses() {
        return keyPasses;
    }

    /**
     * Returns the number of passes completed.
     *
     * @return the passes completed count
     */
    public int getPassesCompleted() {
        return passesCompleted;
    }

    /**
     * Returns the number of tackles won.
     *
     * @return the tackles won count
     */
    public int getTacklesWon() {
        return tacklesWon;
    }

    /**
     * Returns the number of interceptions made.
     *
     * @return the interceptions count
     */
    public int getInterceptions() {
        return interceptions;
    }

    /**
     * Returns the number of blocks made.
     *
     * @return the blocks count
     */
    public int getBlocks() {
        return blocks;
    }

    /**
     * Returns the number of clearances made.
     *
     * @return the clearances count
     */
    public int getClearances() {
        return clearances;
    }

    /**
     * Returns the number of saves made.
     *
     * @return the saves count
     */
    public int getSaves() {
        return saves;
    }

    /**
     * Returns the number of goals conceded.
     *
     * @return the goals conceded count
     */
    public int getGoalsConceded() {
        return goalsConceded;
    }

    /**
     * Returns the number of clean sheets recorded.
     *
     * @return the clean sheets count
     */
    public int getCleanSheets() {
        return cleanSheets;
    }

    /**
     * Returns a formatted string representation of all performance statistics.
     * Displays all recorded metrics in a readable multi-line format.
     *
     * @return a string containing all performance statistics
     */
    public String display() {
        return "Performance Record:\n"
                + "Goals: " + goals + "\n"
                + "Assists: " + assists + "\n"
                + "Key Passes: " + keyPasses + "\n"
                + "Passes Completed: " + passesCompleted + "\n"
                + "Tackles Won: " + tacklesWon + "\n"
                + "Interceptions: " + interceptions + "\n"
                + "Blocks: " + blocks + "\n"
                + "Clearances: " + clearances + "\n"
                + "Saves: " + saves + "\n"
                + "Goals Conceded: " + goalsConceded + "\n"
                + "Ball Recoveries: " + ballRecoveries + "\n"
                + "Shots on Target: " + shotsOnTarget + "\n"
                + "Yellow Cards: " + yellowCards + "\n"
                + "Red Cards: " + redCards + "\n"
                + "Clean Sheets: " + cleanSheets + "\n";
    }
    
}
