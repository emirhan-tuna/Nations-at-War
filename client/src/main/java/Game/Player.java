package Game;

public class Player {
    
    private int id;
    private int resources;
    private int health;

    public Player(int playerID){
        this.id = playerID;
        this.resources = 100; // placeholder
        this.health = 1000; // placeholder
    }

    public void takeBaseDamage(int damage){
        this.health = Math.max(this.health - damage, 0);
    }

    public boolean isDefeated(){
        return this.health <= 0;
    }

    public boolean spendResources(int cost){
        if(this.resources >= cost){
            this.resources -= cost;
            return true;
        }
        return false;
    }

    public void addIncome(int amount){
        this.resources += amount;
    }

    // getters
    public int getId(){ return this.id; }
    public int getHealth(){ return this.health; }
    public int getResources(){ return this.resources; }
}
