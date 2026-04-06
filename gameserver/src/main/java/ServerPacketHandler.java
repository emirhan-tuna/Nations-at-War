import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumPacket;
import network.ChecksumResponsePacket;
import network.Packet;
import network.SpawnPacket;
import simulation.ScheduledActions.ScheduledAction;

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
        Player player = server.getPlayerManager().getPlayer(ctx.channel());

        if(msg instanceof AuthPacket) {

            AuthPacket auth = (AuthPacket) msg;

            if(auth.getCode() == 69693131) {
                System.out.println("client at addr " + ctx.channel().remoteAddress() + " authenticated successfully");
                player = server.getPlayerManager().addPlayer(ctx.channel());
                System.out.println("got id " + player.getId());

                ctx.writeAndFlush(new AuthResponsePacket(player.getId(), true));
            } else {

                System.out.println("wrong code by " + ctx.channel().remoteAddress() + " kicking...");
                server.kickClient(ctx.channel(), "wrong auth", new AuthResponsePacket(0, false));
            }

            return;

        }

        if (player == null) {
            System.out.println("unauthenticated!");
            ctx.close();
            return;
        }
        
        if(msg instanceof ChecksumPacket) {

            ChecksumPacket checksumPacket = (ChecksumPacket) msg;
            int tick = checksumPacket.getTick();
            long checksum = checksumPacket.getChecksum();
            System.out.println("received checksum, tick: " + tick + ",checksum: " + checksum);

            if(server.getSimulation().getChecksum(tick) == checksum) {

                System.out.println("checksum correct");

            } else {

                System.out.println("desync");
                ctx.writeAndFlush(new ChecksumResponsePacket(
                    server.getSimulation().getSnapshot()
                ));

            }
        } else if(msg instanceof SpawnPacket) {
            //spawn

            SpawnPacket actionPacket = (SpawnPacket) msg;
            int type = actionPacket.getObjType();
            int team = player.getTeam();
            
            System.out.println("spawn request: " + type + " from: " + player.getId());

            ScheduledAction scheduledSpawn = server.getSimulation().scheduleSpawn(type, team);
            
            server.broadcast(new ActionPacket(scheduledSpawn));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Player removed = server.getPlayerManager().removePlayer(ctx.channel());
        
        if (removed != null) {
             System.out.println("player " + removed.getId() + " disconnected.");
        } else {
             System.out.println("anonymous socket disconnected.");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("error with client: " + ctx.channel().remoteAddress() + " ,cause: " + cause.getMessage());
        ctx.close();
    }
}
