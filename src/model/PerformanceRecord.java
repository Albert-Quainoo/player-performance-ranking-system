package model;

/**
 * Stores all performance statistics for a player.
 * Stats are added on top of each other over time and can be reset when needed.
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

    /** Sets all stats to zero when a new record is created. */
    public PerformanceRecord() {
        this.goals = 0;
        this.assists = 0;
        this.keyPasses = 0;
        this.passesCompleted = 0;
        this.tacklesWon = 0;
        this.interceptions = 0;
        this.blocks = 0;
        this.clearances = 0;
        this.ballRecoveries = 0;
        this.shotsOnTarget = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.saves = 0;
        this.goalsConceded = 0;
        this.cleanSheets = 0;
    }

    // ===== UPDATE METHODS =====
    // Each method adds the given value to the running total.
    // Zero or negative values are ignored to prevent bad input.

    public void updateGoals(int goals)                 { if (goals > 0)         this.goals           += goals;         }
    public void updateAssists(int assists)             { if (assists > 0)       this.assists         += assists;       }
    public void updateKeyPasses(int keyPasses)         { if (keyPasses > 0)     this.keyPasses       += keyPasses;     }
    public void updatePassesCompleted(int passes)      { if (passes > 0)        this.passesCompleted += passes;        }
    public void updateShotsOnTarget(int shots)         { if (shots > 0)         this.shotsOnTarget   += shots;         }
    public void updateTacklesWon(int tackles)          { if (tackles > 0)       this.tacklesWon      += tackles;       }
    public void updateInterceptions(int interceptions) { if (interceptions > 0) this.interceptions   += interceptions; }
    public void updateBlocks(int blocks)               { if (blocks > 0)        this.blocks          += blocks;        }
    public void updateClearances(int clearances)       { if (clearances > 0)    this.clearances      += clearances;    }
    public void updateBallRecoveries(int recoveries)   { if (recoveries > 0)    this.ballRecoveries  += recoveries;    }
    public void updateSaves(int saves)                 { if (saves > 0)         this.saves           += saves;         }
    public void updateCleanSheets(int sheets)          { if (sheets > 0)        this.cleanSheets     += sheets;        }
    public void updateGoalsConceded(int conceded)      { if (conceded > 0)      this.goalsConceded   += conceded;      }
    public void updateYellowCards(int cards)           { if (cards > 0)         this.yellowCards     += cards;         }
    public void updateRedCards(int cards)              { if (cards > 0)         this.redCards        += cards;         }

    /**
     * Resets all performance statistics to zero.
     * Intended for use at the start of a new season or evaluation period.
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

    public int getGoals()          { return goals;          }
    public int getAssists()        { return assists;        }
    public int getKeyPasses()      { return keyPasses;      }
    public int getPassesCompleted(){ return passesCompleted;}
    public int getShotsOnTarget()  { return shotsOnTarget;  }
    public int getTacklesWon()     { return tacklesWon;     }
    public int getInterceptions()  { return interceptions;  }
    public int getBlocks()         { return blocks;         }
    public int getClearances()     { return clearances;     }
    public int getBallRecoveries() { return ballRecoveries; }
    public int getSaves()          { return saves;          }
    public int getCleanSheets()    { return cleanSheets;    }
    public int getGoalsConceded()  { return goalsConceded;  }
    public int getYellowCards()    { return yellowCards;    }
    public int getRedCards()       { return redCards;       }

    /** Returns a formatted summary of all stats. */
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
