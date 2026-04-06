package network;
import io.netty.buffer.ByteBuf;

public abstract class ClientBoundPacket extends Packet {
    static final int PACKET_AUTH_RESPONSE = 0;
    static final int PACKET_CHECKSUM_RESPONSE = 1;
    static final int PACKET_ACTION = 2;
    
    public ClientBoundPacket(int type, ByteBuf data) {
        super(type);
        decodeData(data);
    }

    public ClientBoundPacket(int type) {
        super(type);
    }

    protected abstract void decodeData(ByteBuf data);

    public abstract void write(ByteBuf buf);
}
