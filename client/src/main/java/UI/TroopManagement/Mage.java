
package UI.TroopManagement;


public class Mage extends Troop {
    private int rangeAttr;
    private int AoE;

    public Mage(float x, float y, int ownerID, int range, int AoE) {
        super(x, y, 60, 30, (float) range, ownerID);
        this.rangeAttr = range;
        this.AoE = AoE;
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