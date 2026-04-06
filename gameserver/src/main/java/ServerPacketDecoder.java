import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.AuthPacket;
import network.ChecksumPacket;
import network.Packet;
import network.SpawnPacket;

public class ServerPacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        int packetType = in.readByte();
        Packet packet = null;

        switch(packetType) {
            case 0:
                //auth
                packet = new AuthPacket(in);
                break;
            case 1:
                //checksum
                packet = new ChecksumPacket(in);
                break;
            case 2:
                //spawn
                packet = new SpawnPacket(in);
            default:
                System.out.println("unknown packet with type: " + packetType + " received");
                in.skipBytes(in.readableBytes());         
        }

        if (packet != null) {
            out.add(packet);
        }
    }
}
