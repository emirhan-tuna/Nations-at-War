package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;


public class Tower extends Troop {

    public Tower(int x, int y, int team) {
        super(4, x, y, 75, 1000, 75, 100, team);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) Math.hypot(target.getX() - x, target.getY() - y);
            if (dist <= range) {
                attack(target);
            }
        }
    }
}