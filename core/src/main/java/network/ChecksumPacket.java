package network;
import io.netty.buffer.ByteBuf;

public class ChecksumPacket extends ServerBoundPacket {
    private int tick;
    private long checksum;

    public ChecksumPacket(long checksum, int tick) {
        super(PACKET_CHECKSUM);
        this.tick = tick;
        this.checksum = checksum;
    }

    public ChecksumPacket(ByteBuf data) {
        super(PACKET_CHECKSUM, data);
    }

    public void decodeData(ByteBuf data) {
        this.tick = data.readInt();
        this.checksum = data.readLong();
    }

    public void write(ByteBuf buf) {
        buf.writeInt(tick);
        buf.writeLong(checksum);
    }

    public int getTick() {return tick;}
    public long getChecksum() {return checksum;}
}
