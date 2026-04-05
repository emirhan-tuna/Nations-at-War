package network;
import io.netty.buffer.ByteBuf;

public class AuthResponsePacket extends ClientBoundPacket {
    private boolean response;

    public AuthResponsePacket(ByteBuf data) {
        super(PACKET_AUTH_RESPONSE, data);
    }

    public AuthResponsePacket(boolean response) {
        super(PACKET_AUTH_RESPONSE);
        this.response = response;
    }

    public void write(ByteBuf buf) {
        buf.writeBoolean(response);
    }

    protected void decodeData(ByteBuf data) {
        response = data.readBoolean();
    }

    public boolean getResponse() {return response;}
}
