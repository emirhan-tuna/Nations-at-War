import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.ClientBoundPacket;

public class ServerPacketEncoder extends MessageToByteEncoder<ClientBoundPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ClientBoundPacket packet, ByteBuf buf) {
        buf.writeByte(packet.getType());
        packet.write(buf);
    }
    
}
