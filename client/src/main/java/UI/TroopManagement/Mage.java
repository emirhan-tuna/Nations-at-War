
package UI.TroopManagement;

import com.badlogic.gdx.graphics.Texture;

public class Mage extends Troop {
    private int areaOfEffect;

    public Mage(Texture texture, float x, float y, int ownerID, int range, int AoE) {
        super(texture, x, y, 60, 45, 60f, ownerID, true, false, 150);
        this.areaOfEffect = AoE;
    }

    @Override
    public void update(float delta) {
        if (target != null && target.health > 0) {
            float dist = (float) Math.hypot(target.x - x, target.y - y);
            if (dist <= range) attack(target);
            else moveTo(target.x, target.y, delta);
        }
    }

    @Override
    public void attack(Troop target) {
        if (target != null) {
            target.takeDamage(this.damage);
        }
    }
}