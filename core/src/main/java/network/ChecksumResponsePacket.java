package network;
import io.netty.buffer.ByteBuf;
public class ChecksumResponsePacket extends ClientBoundPacket {
    private int posX;
    private int posY;
    private int tick;

    public ChecksumResponsePacket(ByteBuf data) {
        super(PACKET_CHECKSUM_RESPONSE, data);
    }

    public ChecksumResponsePacket(int x, int y, int tick) {
        super(PACKET_CHECKSUM_RESPONSE);
        this.tick = tick;
        this.posX = x;
        this.posY = y;
    }

    public void decodeData(ByteBuf data) {
        posX = data.readInt();
        posY = data.readInt();
        tick = data.readInt();
    }

    public void write(ByteBuf buf) {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.tick);
    }

    public int getX() {return posX;}
    public int getY() {return posY;}
    public int getTick() {return tick;}
}
