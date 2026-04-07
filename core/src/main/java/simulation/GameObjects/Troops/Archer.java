package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;


public class Archer extends Troop {

    public Archer(int x, int y, int team) {
        super(0, x, y, 75, 50, 75, 100, team, false);
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

    @Override
    public void write(ByteBuf buf) {

    } 
}