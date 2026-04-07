
package simulation.GameObjects.Troops;

import com.badlogic.gdx.graphics.Texture;


public class Mage extends Troop {

    public Mage(int x, int y, int team) {
        super(3, x, y, 75, 30, 100, 100, team);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) (target.getX() - x);
            if (dist <= range) attack(target);
            else move();
        }
    }

}