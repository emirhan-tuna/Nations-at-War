package UI.TroopManagement;

import com.badlogic.gdx.graphics.Texture;

import Game.Player;

public class Tower extends Troop {
    private Player player;

    public Tower(Texture texture, int x, int y, int ownerID, Player player) {
        super(texture, x, y, 5000, 100, 70f, ownerID, true, false, 0);
        this.player = player;
    }

    @Override
    public void update(float delta) {
        if (target != null && target.health > 0) {
            float dist = (float) Math.hypot(target.x - x, target.y - y);
            if (dist <= range) attack(target);
        }
    }

    @Override
    public void takeDamage(int amount) {
        player.takeBaseDamage(amount);
    }
}