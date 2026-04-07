import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumPacket;
import network.ChecksumResponsePacket;
import network.Packet;
import network.SpawnPacket;
import simulation.Simulation;
import simulation.Simulation.Snapshot;
import simulation.ScheduledActions.ScheduledAction;
import simulation.ScheduledActions.SpawnAction;

public class ServerPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private StartServer server;
    private Simulation simulation;

    protected ServerPacketHandler(StartServer server) {
        this.server = server;
        this.simulation = server.getSimulation().getSimulation();
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

            if(server.getCurrentGameId() != 0 && auth.getCode() == server.getCurrentGameId()) {
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

            simulation.scheduleFromNetwork(() -> {
                long serverChecksum = simulation.getChecksum(tick);
                
                if (serverChecksum != checksum) {
                    Snapshot snapshot = simulation.getSnapshot();
                    ChecksumResponsePacket response = new ChecksumResponsePacket(snapshot);
                    
                    ctx.writeAndFlush(response); 
                }
            });
        } else if(msg instanceof SpawnPacket) {
            //spawn

            SpawnPacket actionPacket = (SpawnPacket) msg;
            int type = actionPacket.getObjType();
            int team = player.getTeam();
            
            System.out.println("spawn request: " + type + " from: " + player.getId());

            simulation.scheduleFromNetwork(() -> {
            SpawnAction spawn = new SpawnAction(type, team, 0);
            ScheduledAction scheduledSpawn = simulation.scheduleAction(spawn);
            server.broadcast(new ActionPacket(scheduledSpawn));
            });
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
