package simulation.GameObjects.Troops;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;
import simulation.GameObjects.Movable;

public abstract class Troop extends GameObject implements Movable {
    public static final int ARCHER = 0;
    public static final int DRAGON = 1;
    public static final int KNIGHT = 2;
    public static final int MAGE = 3;
    public static final int TOWER = 4;

    protected int size;
    protected int damage;
    protected int health;
    protected int lastHealth;
    protected Troop target;
    protected int cost;
    protected int range;
    protected boolean canAttackOtherLane;
    protected int cooldown;

    public Troop(int type, int x, int y, int team) {
        super(type, x, y, team);
        cooldown = 20;
    }

    public Troop(ByteBuf buf) {
        super(buf);
        this.health = buf.readInt();
        this.lastHealth = this.health;
        this.cooldown = buf.readInt();
    }

    @Override
    public void write(ByteBuf buf) {
        super.write(buf);
        buf.writeInt(health);
        buf.writeInt(cooldown);
    }

    @Override
    public void move() {
        int speed = 2;

        if (this.team == 0) {
            setX(x + speed);
        } else {
            setX(x - speed);;
        }
    }

    public void attack(Troop target) {
        if (cooldown == 20) {
            target.takeDamage(damage);
            cooldown = 0;
        } else {
            cooldown++;
        }
    }

    public void takeDamage(int damage) {
        this.health = Math.min(health - damage, health);
    } 

    public int calculateDistance(Troop troop) {
        if (troop instanceof Tower) {
            return (int) Math.abs(troop.getX() - x);
        }

        if (!this.canAttackOtherLane) {
            return (int) Math.abs(troop.getX() - x);
        } else {
            return (int) Math.hypot(Math.abs(troop.getX() - x), Math.abs(troop.getY() - y));
        }
    }

    public boolean canAttack(Troop troop) {
        if(troop.team == this.team) {return false;}
        
        if (troop instanceof Tower) {
            return true;
        }
        if (this.y == troop.getY() || this.canAttackOtherLane) {
            return true;
        } else {
            return false;
        }
    }

    public static int getRequiredGold(int type) {
        int requiredGold = 0;

        if (type == 0) {
            requiredGold = 75;
        } else if (type == 1) {
            requiredGold = 250;
        } else if (type == 2) {
            requiredGold = 50;
        } else if (type == 3) {
            requiredGold = 150;
        }

        return requiredGold;
    }

    public boolean getAttack() {
        return this.canAttackOtherLane;
    }

    public void setTarget(Troop troop) {
        this.target = troop;
    }

    public Troop getTarget() {
        return this.target;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public abstract void update();

    public int getHealth() {
        return this.health;
    }

    public int getLastHealth() {
        return this.lastHealth;
    }

    public abstract int getMaxHealth();

    @Override
    public void savePreviousState() {
        super.savePreviousState();
        this.lastHealth = this.health;
    }

    public int getGold() {
        return this.cost;
    }
}
