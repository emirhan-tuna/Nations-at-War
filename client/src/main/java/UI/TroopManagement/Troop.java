
package UI.TroopManagement;


public abstract class Troop {
    
    protected float x, y;
    protected int health;
    protected int damage;
    protected float range;
    protected int ownerID;
    protected Troop target;

    public Troop(float x, float y, int health, int damage, float range, int ownerID){
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.range = range;
        this.ownerID = ownerID;
        this.target = null;
    }

    public abstract void update(float delta);

    public void moveTo(float targetX, float targetY, float delta) {
        float speed = 50f;
        float dx = targetX - this.x;
        float dy = targetY - this.y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist > 1f) {
            this.x += (dx / dist) * speed * delta;
            this.y += (dy / dist) * speed * delta;
        }
    }

    public void attack(Troop target){
        if(target != null){
            target.takeDamage(this.damage);
        }
    }

    public void takeDamage(int amount){
        this.health = Math.max(0, this.health - amount);
    }

    public boolean isDead(){
        return this.health <= 0;
    }

    // getters
    public float getX(){ return x; }
    public float getY(){ return y; }
    public int getHealth(){ return health; }
    public int getDamage(){ return damage; }
    public float getRange(){ return range; }
    public int getOwnerID(){ return ownerID; }
    public Troop getTarget(){ return target; }

    // setters
    public void setTarget(Troop target){ this.target = target; }
}
