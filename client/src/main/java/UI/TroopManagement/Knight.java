
package UI.TroopManagement;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class Knight extends Troop {
    
    public Knight(Texture texture, float x, float y, int ownerID) {
        super(texture, x, y, 200, 25, 40f, ownerID, false, false, 75);
        this.isFlying = false;
    }

    @Override
    public void update(float delta) {
        if (target != null && target.health > 0) {
            float dist = (float) (target.x - x);
            if (dist <= range) attack(target);
            else moveTo(target.x, target.y, delta);
        }
    }
}