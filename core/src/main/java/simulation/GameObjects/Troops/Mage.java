
package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;


public class Mage extends Troop {
    

    public Mage(int x, int y, int team) {
        super(3, x, y, team);
        this.damage = 100;
        this.health = getMaxHealth();
        this.cost = 150;
        this.range = 100;
        this.canAttackOtherLane = true;
    }

    public Mage(ByteBuf buf) {
        super(buf);
        this.type = MAGE;
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            int dist = (int) Math.abs((target.getX() - x));
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
        return 40;
    }
}