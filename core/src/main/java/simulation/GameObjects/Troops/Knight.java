
package simulation.GameObjects.Troops;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Knight extends Troop {
    
    
    public Knight(int x, int y, int team) {
        super(2, x, y, team);
        this.damage = 50;
        this.health = 100;
        this.cost = 50;
        this.range = 10;
        this.canAttackOtherLane = false;
    }

    public Knight(ByteBuf buf) {
        super(buf);
        this.type = KNIGHT;
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            int dist = (int) (target.getX() - x);
            if (dist <= range) {
                attack(target);
            }
            else {
                move();
            }
        } else {
            move();
        }
    }
}