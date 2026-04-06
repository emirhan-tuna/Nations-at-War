package simulation.GameObjects;

import com.badlogic.gdx.Game;

import io.netty.buffer.ByteBuf;

public class Square extends GameObject implements Movable {
    private static int speed = 10;
    private static int size = 10;

    public Square(int x, int y) {
        super(0, x, y);
        this.x = x;
        this.y = y;
    }

    public Square(ByteBuf buf) {
        super(0, buf.readInt(), buf.readInt());
    }

    public void write(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
    }

    public void update() {
        move();
    }

    public void move() {
        this.x += speed;
        if(x > 500) {
            x = 0;
        }
    }
}
