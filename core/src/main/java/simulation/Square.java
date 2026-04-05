package simulation;

public class Square {
    public int id = 0;
    public int x;
    public int y;
    public int size;
    public int speed;

    public Square(int x, int y, int size, int speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public void update() {
        this.x += speed;
        if(x > 500) {
            x = 0;
        }
    }
}
