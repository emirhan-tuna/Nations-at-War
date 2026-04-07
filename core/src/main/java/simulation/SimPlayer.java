package simulation;

public class SimPlayer {
    private int gold;
    private int team;

    public SimPlayer(int team) {
        this.team = team;
    }

    public int getGold() {return this.gold;}

    public void addGold(int amount) {this.gold += amount;}

    public int getTeam() {return team;}
}
