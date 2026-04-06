
package UI.TroopManagement;

import com.badlogic.gdx.graphics.Texture;

public class Dragon extends Troop {
    public Dragon(Texture texture, float x, float y, int ownerID) {
        super(texture, x, y, 400, 100, 100f, ownerID, true, true, 500);
        this.isFlying = true;
    }

    @Override
    public void update(float delta) {
        if (target != null && target.health > 0) {
            float dist = (float) target.x - x;
            if (dist <= range) attack(target);
            else moveTo(target.x, target.y, delta);
        }
    }
}