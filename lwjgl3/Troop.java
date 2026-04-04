public abstract class Troop {
    protected float x, y;
    protected int health;
    protected int damage;
    protected float range;
    protected int ownerID;
    protected Troop target;

    public void moveTowards(float targetX, float targetY, float delta) {
        float speed = 50f; 
        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (dist > 1) {
            x += (dx / dist) * speed * delta;
            y += (dy / dist) * speed * delta;
        }
    }

    public void takeDamage(int dmg) {
        this.health -= dmg;
        if (this.health < 0) this.health = 0;
    }

    public abstract void update(float delta);
    
    public void attack(Troop target) {
        if (target != null) {
            target.takeDamage(this.damage);
        }
    }
}