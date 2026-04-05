package UI.TroopManagement;


public class Archer extends Troop {
    private int rangeAttr;

    public Archer(float x, float y, int ownerID, int range) {
        super(x, y, 70, 12, (float) range, ownerID);
        this.rangeAttr = range;
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