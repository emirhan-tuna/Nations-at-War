package network;
import io.netty.buffer.ByteBuf;
public class AuthPacket extends ServerBoundPacket {
    private int code;

    public AuthPacket(int code) {
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
        buf.writeInt(code);//31316969
    }

    public int getCode() {return code;}
}
