
package simulation.GameObjects.Troops;


import io.netty.buffer.ByteBuf;


public class Dragon extends Troop {
    

    public Dragon(int x, int y, int team) {
        super(1, x, y, team);
        this.damage = 150;
        this.health = getMaxHealth();
        this.cost = 250;
        this.range = 100;
        this.canAttackOtherLane = true;
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

    @Override
    public int getMaxHealth() {
        return 200;
    }
}