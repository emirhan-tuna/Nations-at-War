package simulation.GameObjects;

import io.netty.buffer.ByteBuf;

public abstract class GameObject {
    protected int id;
    protected int type;
    protected int x;
    protected int y;
    protected int size;

    public GameObject(int type, int x, int y) {
        this.id = 0;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public abstract void update();

    public abstract void write(ByteBuf buf);

    public int getId() {return this.id;}
    public int getType() {return this.type;}
    public int getX() {return this.x;}
    public int getY() {return this.y;}

    public void setId(int id) {this.id = id;}
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
}
