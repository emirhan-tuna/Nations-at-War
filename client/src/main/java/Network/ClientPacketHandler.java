package Network;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.Packet;

public class ClientPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private int code;

    public ClientPacketHandler(int code) {
        this.code = code;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        AuthPacket authPacket = new AuthPacket(code);
        ctx.writeAndFlush(authPacket);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {

        if(msg instanceof AuthResponsePacket) {
            AuthResponsePacket authPacket = (AuthResponsePacket) msg;

            if(authPacket.getResponse()) {
                System.out.println("auth successful");
            } else {
                System.out.println("auth failed");
            }
        } else if(msg instanceof ChecksumResponsePacket) {
            ChecksumResponsePacket checksum = (ChecksumResponsePacket) msg;
            System.out.printf("server corrected pos to x: %d, y: %d, at tick: %d%n", checksum.getX(), checksum.getY(), checksum.getTick());
            System.out.printf("checksum: x:%d y:%d tick:%d", checksum.getX(), checksum.getY(), checksum.getTick());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        System.out.println("Connection error: " + err.getMessage());
        ctx.close();
    }
}