
package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Mage extends Troop {
    

    public Mage(int x, int y, int team) {
        super(3, x, y, team);
        damage = 100;
        health = 40;
        cost = 150;
        range = 100;
        canAttackOtherLane = true;
    }

    public Mage(ByteBuf buf) {
        super(buf);
        this.type = MAGE;
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