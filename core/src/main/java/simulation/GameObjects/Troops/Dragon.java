
package simulation.GameObjects.Troops;


import io.netty.buffer.ByteBuf;


public class Dragon extends Troop {
    final int damage = 150;
    final int health = 200;
    final int cost = 250;
    final int range = 100;
    final boolean canAttackOtherLane = true;

    public Dragon(int x, int y, int team) {
        super(1, x, y, team);
    }

    public Dragon(ByteBuf buf) {
        super(buf);
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
}