package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;


public class Tower extends Troop{
    protected int size;
    protected int damage;
    protected int health;
    protected Troop target;
    protected int cost;
    protected int range;

    public Tower(int type, int x, int y, int damage, int health, int cost, int range, int team) {
        super(5, x, y, 100, 500, 0, 100, team, true);
        this.damage = damage;
        this.health = health;
        this.cost = cost;
        this.range = range;

        if (team == 0) {
            this.x = 50;
        } else {
            this.x = 900;
        }
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) Math.hypot(target.getX() - x, target.getY() - y);
            if (dist <= range) {
                attack(target);
            }
        }
    }

    public void attack(Troop target) {
        target.takeDamage(damage);
    }

    @Override
    public void write(ByteBuf buf) {

    } 
}