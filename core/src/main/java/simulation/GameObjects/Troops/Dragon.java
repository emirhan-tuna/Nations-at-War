
package simulation.GameObjects.Troops;


import io.netty.buffer.ByteBuf;


public class Dragon extends Troop {
    

    public Dragon(int x, int y, int team) {
        super(1, x, y, team);
        damage = 150;
        health = 200;
        cost = 250;
        range = 100;
        canAttackOtherLane = true;
    }

    public Dragon(ByteBuf buf) {
        super(buf);
        this.type = DRAGON;
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