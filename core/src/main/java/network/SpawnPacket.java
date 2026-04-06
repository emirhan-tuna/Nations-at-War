package network;

import io.netty.buffer.ByteBuf;

public class SpawnPacket extends ServerBoundPacket {
    private int objType;

    public SpawnPacket(int type) {
        super(PACKET_SPAWN);
        this.objType = type;
    }

    public SpawnPacket(ByteBuf data) {
        super(PACKET_SPAWN, data);
    }

    public int getObjType() {return this.objType;}

    @Override
    protected void decodeData(ByteBuf data) {
        this.objType = data.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt(objType);
    }
}
