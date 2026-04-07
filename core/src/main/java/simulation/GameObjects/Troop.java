package simulation.GameObjects;

public abstract class Troop extends GameObject implements Movable {
    private int size;
    private int damage;
    private int health;

    public Troop(int type, int x, int y, int damage, int health, int size) {
        super(type, x, y);
        this.damage = damage;
        this.health = health;
        this.size = size;
    }
}
