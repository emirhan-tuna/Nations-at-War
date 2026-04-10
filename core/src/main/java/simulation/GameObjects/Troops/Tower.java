package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;


public class Tower extends Troop{

    public Tower(int type, int x, int y, int team) {
        super(type, x, y, team);

        this.damage = 75;
        this.health = 1000; 
        this.cost = 0;
        this.range = 100;
        this.canAttackOtherLane = true;

    }

    public Tower(ByteBuf buf) {
        super(buf);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            int dist = (int) Math.hypot(target.getX() - x, target.getY() - y);
            if (dist <= range) {
                attack(target);
            }
        }
    }

    @Override
    public boolean canAttack(Troop target) {
        return this.team != target.getTeam();
    }
}