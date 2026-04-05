import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumPacket;
import network.ChecksumResponsePacket;
import network.Packet;

public class ServerPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private StartServer server;

    protected ServerPacketHandler(StartServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("new client connected: " + ctx.channel().remoteAddress());
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
        if(msg instanceof AuthPacket) {
            AuthPacket auth = (AuthPacket) msg;

            if(auth.getCode() == 69693131) {
                System.out.println("client at addr " + ctx.channel().remoteAddress() + " authenticated successfully");
                ctx.writeAndFlush(new AuthResponsePacket(true));
            } else {
                System.out.println("wrong code by " + ctx.channel().remoteAddress() + " kicking...");
                server.kickClient(ctx.channel(), "wrong auth", new AuthResponsePacket(false));
            }
        } else if(msg instanceof ChecksumPacket) {
            ChecksumPacket checksumPacket = (ChecksumPacket) msg;
            //ctx.writeAndFlush(new ChecksumResponsePacket(0, 0, 0));
            int tick = checksumPacket.getTick();
            long checksum = checksumPacket.getChecksum();
            System.out.println("received checksum, tick: " + tick + ",checksum: " + checksum);

            if(server.getSimulation().getChecksum(tick) == checksum) {
                System.out.println("checksum correct");
            } else {
                System.out.println("desync");
                ctx.writeAndFlush(new ChecksumResponsePacket(server.getSimulation().getX(),
                server.getSimulation().getY(),
                server.getSimulation().getTick()));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("error with client: " + ctx.channel().remoteAddress() + " ,cause: " + cause.getMessage());
        ctx.close();
    }
}
