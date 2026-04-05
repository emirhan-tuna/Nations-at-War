package Network;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.ServerBoundPacket;

public class PacketEncoder extends MessageToByteEncoder<ServerBoundPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerBoundPacket packet, ByteBuf buf) {
        buf.writeByte(packet.getType());
        packet.write(buf);
    }
}
