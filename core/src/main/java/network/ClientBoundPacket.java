package network;
import io.netty.buffer.ByteBuf;

public abstract class ClientBoundPacket extends Packet {
    static public final int PACKET_AUTH_RESPONSE = 0;
    static public final int PACKET_CHECKSUM_RESPONSE = 1;
    static public final int PACKET_ACTION = 2;
    static public final int PACKET_GAMEOVER = 3;
    static public final int PACKET_STARTGAME = 4;
    static public final int PACKET_DISCONNECT = 5;
    
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
