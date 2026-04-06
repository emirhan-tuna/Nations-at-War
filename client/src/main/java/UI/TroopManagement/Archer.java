package UI.TroopManagement;

import com.badlogic.gdx.graphics.Texture;

public class Archer extends Troop {

    public Archer(Texture texture, float x, float y, int ownerID, float range) {
        super(texture, x, y, 100, 25, 70f, ownerID, true, false, 100);
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