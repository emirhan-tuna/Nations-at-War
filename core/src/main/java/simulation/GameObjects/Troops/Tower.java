package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;


public class Tower extends Troop{

    public Tower(int type, int x, int y, int team) {
        super(5, x, y, team);

        this.type = 0;
        this.damage = 75;
        this.health = 1000; 
        this.cost = 75;
        this.range = 100;
        this.canAttackOtherLane = false;

        if (team == 0) {
            this.x = 50;
        } else {
            this.x = 900;
        }
    }

    public Tower(ByteBuf buf) {
        super(buf);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            int dist = (int) Math.hypot(target.getX() - x, target.getY() - y);
            if (dist <= range) {
                attack(target);
            }
        }
    }

    public void attack(Troop target) {
        target.takeDamage(damage);
    }
}