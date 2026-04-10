package network;

import io.netty.buffer.ByteBuf;

public class DisconnectPacket extends ClientBoundPacket {
    public DisconnectPacket() {super(PACKET_DISCONNECT);};

    public DisconnectPacket(ByteBuf buf) {super(PACKET_DISCONNECT);}

    @Override
    protected void decodeData(ByteBuf data) {}

    @Override
    public void write(ByteBuf buf) {}
}
