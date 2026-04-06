package UI.TroopManagement;

import com.badlogic.gdx.graphics.Texture;

public class Swordsman extends Troop {
    public Swordsman(Texture texture, float x, float y, int ownerID) {
        super(texture, x, y, 100, 50, 30f, ownerID, false, false, 50);
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