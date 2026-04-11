package simulation.GameObjects;

import io.netty.buffer.ByteBuf;

public abstract class GameObject {
    protected int id;
    protected int type;
    protected int x;
    protected int y;
    protected int lastX;
    protected int lastY;
    protected int size;
    protected int team;

    public GameObject(int type, int x, int y, int team) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.lastX = x;
        this.lastY = y;
        this.team = team;
    }

    public GameObject(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.team = buf.readByte();
    }

    public abstract void update();

    public void write(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeByte(team);
    }

    public void savePreviousState() {
        this.lastX = this.x;
        this.lastY = this.y;
    }

    public int getId() {return this.id;}
    public int getType() {return this.type;}
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getLastX() {return this.lastX;}
    public int getLastY() {return this.lastY;}
    public int getTeam() {return this.team;}

    public void setId(int id) {this.id = id;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
}
