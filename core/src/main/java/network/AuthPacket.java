package network;
import io.netty.buffer.ByteBuf;
public class AuthPacket extends ServerBoundPacket {
    private long code;

    public AuthPacket(long code) {
        super(PACKET_AUTH);
        this.code = code;
    }

    public AuthPacket(ByteBuf data) {
        super(PACKET_AUTH, data);
    }

    public void decodeData(ByteBuf data) {
        this.code = data.readInt();
    }

    public void write(ByteBuf buf) {
        buf.writeLong(code);//31316969
    }

    public long getCode() {return code;}
}
