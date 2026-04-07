
package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Mage extends Troop {

    public Mage(int x, int y, int team) {
        super(3, x, y, 75, 30, 100, 100, team, false);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) (target.getX() - x);
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

    @Override
    public void write(ByteBuf buf) {

    } 
}