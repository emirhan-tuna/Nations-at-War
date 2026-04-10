
package simulation.GameObjects.Troops;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Knight extends Troop {
    final int damage = 50;
    final int health = 100;
    final int cost = 50;
    final int range = 10;
    final boolean canAttackOtherLane = false;
    
    public Knight(int x, int y, int team) {
        super(2, x, y, team);
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