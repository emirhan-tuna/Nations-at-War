package network;
import io.netty.buffer.ByteBuf;

public class AuthResponsePacket extends ClientBoundPacket {
    private boolean response;
    private int id;

    public AuthResponsePacket(ByteBuf data) {
        super(PACKET_AUTH_RESPONSE, data);
    }

    public AuthResponsePacket(int id, boolean response) {
        super(PACKET_AUTH_RESPONSE);
        this.response = response;
        this.id = id;
    }

    public void write(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeBoolean(response);
    }

    protected void decodeData(ByteBuf data) {
        this.id = data.readInt();
        this.response = data.readBoolean();
    }

    public boolean getResponse() {return this.response;}
    public int getId() {return this.id;}
}
