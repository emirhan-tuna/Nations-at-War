package simulation;

import simulation.GameObjects.Troops.Tower;

public class SimPlayer {
    private int gold;
    private int team;
    private Tower tower;

    public SimPlayer(int team) {
        this.team = team;
    }

    public void setTower(Tower tower) {this.tower = tower;}

    public void setGold(int gold) {this.gold = gold;}

    public Tower getTower() {return this.tower;}

    public int getGold() {return this.gold;}

    public void addGold(int amount) {this.gold += amount;}

    public int getTeam() {return team;}
}
