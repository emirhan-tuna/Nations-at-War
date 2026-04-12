package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;


public class Archer extends Troop {

    public Archer(int x, int y, int team) {
        super(0, x, y, team);
        this.damage = 75;
        this.health = getMaxHealth();
        this.cost = 75;
        this.range = 100;
        this.canAttackOtherLane = false;
    }

    public Archer(ByteBuf buf) {
        super(buf);
        this.type = ARCHER;
    }

    public void update() {
        if (this.target != null && target.getHealth() > 0) {
            int dist = calculateDistance(target);
            if (dist <= range) {
                attack(target);
            } else {
                move();
            }
        } else {
            move();
        }
    }

    @Override
    public int getMaxHealth() {
        return 50;
    }
}