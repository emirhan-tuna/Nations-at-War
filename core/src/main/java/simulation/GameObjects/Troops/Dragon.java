
package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;


public class Dragon extends Troop {
    final int type = 0;
    final int damage = 75;
    final int health = 50;
    final int cost = 75;
    final int range = 100;
    final boolean canAttackOtherLane = false;

    public Dragon(int x, int y, int team) {
        super(1, x, y,team);
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