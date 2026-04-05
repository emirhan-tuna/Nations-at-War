package io.github.some_example_name.UI.TroopManagement;

public class Tower extends Troop {
    public Tower(int x, int y, String ownerID) {
        super(x, y, 5000, 100, 70f, ownerID, true, false, 0);
    }

    @Override
    public void update(float delta) {
        if (target != null && target.health > 0) {
            float dist = (float) Math.hypot(target.x - x, target.y - y);
            if (dist <= range) attack(target);
        }
    }
}