package simulation.GameObjects.Troops;

import simulation.GameObjects.GameObject;
import simulation.GameObjects.Movable;

public abstract class Troop extends GameObject implements Movable {
    protected int size;
    protected int damage;
    protected int health;
    protected Troop target;
    protected int cost;
    protected int range;

    public Troop(int type, int x, int y, int damage, int health, int cost, int range, int team) {
        super(type, x, y, team);
        this.damage = damage;
        this.health = health;
        this.cost = cost;
        this.range = range;
    }

    public void move() {
        int speed = 50;

        if (this.team == 0) {
            this.x += speed;
        } else {
            this.x -= speed;
        }
    }

    public void attack(Troop target) {
        target.takeDamage(damage);
    }

    public void takeDamage(int damage) {
        this.health = Math.min(health - damage, health);
    } 

    public abstract void update();
}
