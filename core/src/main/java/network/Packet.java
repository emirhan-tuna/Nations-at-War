package network;

public abstract class Packet {
    protected int type;

    public Packet(int type) {
        this.type = type;
    }

    public int getType() {return this.type;}
}
