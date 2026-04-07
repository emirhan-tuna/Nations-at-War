
package simulation.GameObjects.Troops;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;


public class Knight extends Troop {
    
    public Knight(int x, int y, int team) {
        super(2, x, y, 50, 200, 50, 5, team);
    }

    public void update() {
        if (this.target != null && target.health > 0) {
            float dist = (float) (target.getX() - x);
            if (dist <= range) attack(target);
            else move();
        }
    }
}