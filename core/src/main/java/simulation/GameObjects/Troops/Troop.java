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
    protected boolean canAttackOtherLane;

    public Troop(int type, int x, int y, int damage, int health, int cost, int range, int team, boolean canAttackOtherLane) {
        super(type, x, y, team);
        this.damage = damage;
        this.health = health;
        this.cost = cost;
        this.range = range;
        this.canAttackOtherLane = canAttackOtherLane;
    }

    @Override
    public void move() {
        int speed = 50;

        if (this.team == 0) {
            setX(x + speed);
        } else {
            setX(x - speed);;
        }
    }

    public void attack(Troop target) {
        target.takeDamage(damage);
    }

    public void takeDamage(int damage) {
        this.health = Math.min(health - damage, health);
    } 

    public int calculateDistance(Troop troop) {
        if (this.canAttackOtherLane) {
            return (int) Math.abs(troop.getX() - x);
        } else {
            return (int) Math.hypot(Math.abs(troop.getX() - x), Math.abs(troop.getY() - y));
        }
    }

    public boolean getAttack() {
        return this.canAttackOtherLane;
    }

    public abstract void update();
}
