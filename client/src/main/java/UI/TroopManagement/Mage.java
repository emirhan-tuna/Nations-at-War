
package UI.TroopManagement;


public class Mage extends Troop {
    private int areaOfEffect;

    public Mage(float x, float y, String ownerID, int range, int AoE) {
        super(x, y, 60, 45, 60f, ownerID, true, false, 150);
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
}