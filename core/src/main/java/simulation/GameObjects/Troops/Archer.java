package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;


public class Archer extends Troop {
    final int type = 0;
    final int damage = 75;
    final int health = 50;
    final int cost = 75;
    final int range = 100;
    final boolean canAttackOtherLane = false;

    public Archer(int x, int y, int team) {
        super(0, x, y, team);
    }

    public Archer(ByteBuf buf) {
        super(buf);
    }

    public void update() {
        if (this.target != null) {
            int dist = target.getX() - x;
            if (dist <= range) {
                attack(target);
            } else {
                move();
            }
        } else {
            move();
        }
    }
}