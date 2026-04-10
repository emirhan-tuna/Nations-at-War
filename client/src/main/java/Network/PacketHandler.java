package Network;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.ActionPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.ClientBoundPacket;
import network.GameOverPacket;
import network.Packet;
import network.StartGamePacket;
import network.DisconnectPacket;

import java.util.List;


public class PacketHandler extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.readableBytes() < 1) {return;}

        int packetType = in.readByte();
        Packet packet = null;

        switch(packetType) {
            case ClientBoundPacket.PACKET_AUTH_RESPONSE:
                packet = new AuthResponsePacket(in);
                break;
            case ClientBoundPacket.PACKET_CHECKSUM_RESPONSE:
                packet = new ChecksumResponsePacket(in);
                break;
            case ClientBoundPacket.PACKET_ACTION:
                packet = new ActionPacket(in);
                break;
            case ClientBoundPacket.PACKET_GAMEOVER:
                packet = new GameOverPacket(in);
                break;
            case ClientBoundPacket.PACKET_STARTGAME:
                packet = new StartGamePacket(in);
                break;
            case ClientBoundPacket.PACKET_DISCONNECT:
                packet = new DisconnectPacket(in);
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
