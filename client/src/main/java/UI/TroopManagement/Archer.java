package UI.TroopManagement;


public class Archer extends Troop {

    public Archer(float x, float y, String ownerID, float range) {
        super(x, y, 100, 25, 70f, ownerID, true, false, 100);
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