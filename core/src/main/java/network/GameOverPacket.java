package network;

import io.netty.buffer.ByteBuf;

public class GameOverPacket extends ClientBoundPacket {
    private int winnerId;

    public GameOverPacket(ByteBuf buf) {
        super(PACKET_GAMEOVER);
    }

    public GameOverPacket(int winnerId) {
        super(PACKET_GAMEOVER);
        this.winnerId = winnerId;
    }

    @Override
    protected void decodeData(ByteBuf data) {
        this.winnerId = data.readInt();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(this.winnerId);
    }

    public int getID() {
        return winnerId;
    }
}
