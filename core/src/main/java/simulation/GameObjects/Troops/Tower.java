package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;


public class Tower extends Troop{
    final int type = 0;
    final int damage = 75;
    final int health = 50;
    final int cost = 75;
    final int range = 100;
    final boolean canAttackOtherLane = false;

    public Tower(int type, int x, int y, int damage, int health, int cost, int range, int team) {
        super(5, x, y, team);

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