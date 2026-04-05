
package UI.TroopManagement;

import java.util.ArrayList;

public class Knight extends Troop {
    
    public Knight(float x, float y, String ownerID) {
        super(x, y, 200, 25, 40f, ownerID, false, false, 75);
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