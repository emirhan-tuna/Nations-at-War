package network;
import io.netty.buffer.ByteBuf;

public abstract class ServerBoundPacket extends Packet {
    static final int PACKET_AUTH = 0;
    static final int PACKET_CHECKSUM = 1;
    static final int PACKET_SPAWN = 2;

    public ServerBoundPacket(int type) {
        super(type);
    }

    public ServerBoundPacket(int type, ByteBuf data) {
        super(type);
        decodeData(data);
    }

    protected abstract void decodeData(ByteBuf data);

    public abstract void write(ByteBuf buf);
}
