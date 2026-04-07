package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;


public class Archer extends Troop {

    public Archer(int x, int y, int team) {
        super(0, x, y, 75, 50, 75, 100, team);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) (target.getX() - x);
            if (dist <= range) attack(target);
            else move();
        }
    }
}