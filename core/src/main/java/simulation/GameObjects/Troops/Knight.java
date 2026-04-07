
package simulation.GameObjects.Troops;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Knight extends Troop {
    final int type = 0;
    final int damage = 75;
    final int health = 50;
    final int cost = 75;
    final int range = 100;
    final boolean canAttackOtherLane = false;
    
    public Knight(int x, int y, int team) {
        super(2, x, y, team);
    }

    public Knight(ByteBuf buf) {
        super(buf);
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