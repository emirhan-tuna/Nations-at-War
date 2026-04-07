
package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Dragon extends Troop {
    public Dragon(int x, int y, int team) {
        super(1, x, y, 100, 250, 500, 100, team, true);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            int dist = (int) Math.hypot(target.getX() - x, target.getY() - y);
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