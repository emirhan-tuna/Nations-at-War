package Network;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.ActionPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.Packet;

import java.util.List;


public class PacketHandler extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.readableBytes() < 1) {return;}

        int packetType = in.readByte();
        Packet packet = null;

        switch(packetType) {
            case 0:
                //auth
                packet = new AuthResponsePacket(in);
                break;

            case 1:
                //checksum result
                packet = new ChecksumResponsePacket(in);
                break;

            case 2:
                //checksum result
                packet = new ActionPacket(in);
                break;

            default:
                System.out.println("unknown packet type: " + packetType);
                in.skipBytes(in.readableBytes());
        }

        if(packet != null) {
            out.add(packet);
        }
    }
}
