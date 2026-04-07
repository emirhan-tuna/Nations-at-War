package network;

import io.netty.buffer.ByteBuf;

public class SpawnPacket extends ServerBoundPacket {
    private int objType;
    private int lane;

    public SpawnPacket(int type, int lane) {
        super(PACKET_SPAWN);
        this.objType = type;
        this.lane = lane;
    }

    public SpawnPacket(ByteBuf data) {
        super(PACKET_SPAWN, data);
    }

    public int getObjType() {return this.objType;}
    public int getLane() {return this.lane;}

    @Override
    protected void decodeData(ByteBuf data) {
        this.objType = data.readByte();
        this.lane = data.readByte();
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte(objType);
        buf.writeByte(lane);
    }
}
