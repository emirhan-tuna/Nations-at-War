import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumPacket;
import network.ChecksumResponsePacket;
import network.DisconnectPacket;
import network.Packet;
import network.SpawnPacket;
import simulation.SimPlayer;
import simulation.Simulation;
import simulation.Simulation.Snapshot;
import simulation.GameObjects.Troops.Troop;
import simulation.ScheduledActions.ScheduledAction;
import simulation.ScheduledActions.SpawnAction;

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

            if(player != null) {
                System.out.println("double auth by " + player.getId());
                return;
            }

            AuthPacket auth = (AuthPacket) msg;

            if(server.getRoom(auth.getCode()) != null) {
                System.out.println("client at addr " + ctx.channel().remoteAddress() + " attempting to join room with id: " + server.getCurrentGameId());

                player = server.getPlayerManager().addPlayer(ctx.channel());
                server.getRoom(auth.getCode()).addPlayer(player);

                System.out.println("got team " + player.getTeam());

                ctx.writeAndFlush(new AuthResponsePacket(player.getTeam(), true));
            } else {
                System.out.println("room " + auth.getCode() + " does not exist! this address tried to connect: " + ctx.channel().remoteAddress() + " (" + auth.getCode() + ") " + " kicking...");
                server.kickClient(ctx.channel(), "room doesnt exist", new DisconnectPacket());
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
            GameRoom room = player.getRoom();
            Simulation simulation = room.getSimulation();
            int playerId = player.getId();

            if(simulation == null) {
                System.out.println("no sim! cant check the sum");
                return;
            }

            System.out.println("checking sum from player: " + playerId);

            simulation.scheduleFromNetwork(() -> {
                long serverChecksum = simulation.getChecksum(tick);
                
                if (serverChecksum != checksum) {
                    System.out.println("wrong checksum by player: " + playerId + " with checksum " + checksum + "! sending correction...");
                    
                    Snapshot snapshot = simulation.getSnapshot();
                    ChecksumResponsePacket response = new ChecksumResponsePacket(snapshot);
                    
                    ctx.writeAndFlush(response); 
                } else {
                    System.out.println("checksum from player: " + playerId + " is correct!");
                    System.out.println("server checksum: " + serverChecksum + " player checksum: " + checksum + " at tick: " + tick);
                }
            });
        } else if(msg instanceof SpawnPacket) {
            //spawn

            SpawnPacket actionPacket = (SpawnPacket) msg;
            int type = actionPacket.getObjType();
            int team = player.getTeam();
            int lane = actionPacket.getLane();
            GameRoom room = player.getRoom();
            Simulation simulation = room.getSimulation();

            if (simulation == null) {return;}

            System.out.println("spawn request: " + type + " from: " + player.getId());

            simulation.scheduleFromNetwork(() -> {
                SpawnAction spawn = new SpawnAction(type, team, 0, lane);
                ScheduledAction scheduledSpawn = simulation.scheduleAction(spawn);
                room.broadcast(new ActionPacket(scheduledSpawn));
            });
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Player removed = server.getPlayerManager().removePlayer(ctx.channel());
        
        if (removed != null) {
             System.out.println("player " + removed.getId() + " disconnected.");

             if(removed.getRoom() != null) {
                removed.getRoom().playerQuit(removed.getTeam());
             }
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
