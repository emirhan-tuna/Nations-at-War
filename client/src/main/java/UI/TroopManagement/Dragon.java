
package UI.TroopManagement;


public class Dragon extends Troop {
    private boolean isFlying;

    public Dragon(float x, float y, String ownerID) {
        super(x, y, 400, 100, 100f, ownerID, true, true, 500);
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