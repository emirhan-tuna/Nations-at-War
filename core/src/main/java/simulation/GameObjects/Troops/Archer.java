package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;


public class Archer extends Troop {

    public Archer(int x, int y, int team) {
        super(0, x, y, team);
        damage = 75;
        health = 50;
        cost = 75;
        range = 100;
        canAttackOtherLane = false;
    }

    public Archer(ByteBuf buf) {
        super(buf);
        this.type = ARCHER;
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