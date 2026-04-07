package network;

import io.netty.buffer.ByteBuf;

public class StartGamePacket extends ClientBoundPacket {
    public StartGamePacket() {super(PACKET_STARTGAME);};

    public StartGamePacket(ByteBuf buf) {super(PACKET_STARTGAME);}

    @Override
    protected void decodeData(ByteBuf data) {}

    @Override
    public void write(ByteBuf buf) {}
}
