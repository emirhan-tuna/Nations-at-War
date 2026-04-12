import java.net.HttpURLConnection;
import java.net.URL;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumPacket;
import network.ChecksumResponsePacket;
import network.DisconnectPacket;
import network.Packet;
import network.Routes;
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

            new Thread(() -> {
                String uid = verifyToken(auth.getToken()); 

                //game crashes if 2 players join too fast so execute on netty, dont change this
                ctx.executor().execute(() -> {
                    if (uid != null) {
                        if(server.getRoom(auth.getCode()) != null) {
                            System.out.println("client authenticated successfully for room: " + auth.getCode() + " with user id: " + uid);

                            Player newPlayer = server.getPlayerManager().addPlayer(ctx.channel());
                            newPlayer.setUserId(uid);

                            server.getRoom(auth.getCode()).addPlayer(newPlayer);
                            ctx.writeAndFlush(new AuthResponsePacket(newPlayer.getTeam(), true));
                        } else {
                            System.out.println("room " + auth.getCode() + " does not exist!");
                            server.kickClient(ctx.channel(), "room doesnt exist", new DisconnectPacket());
                        }
                    } else {
                        System.out.println("client " + ctx.channel().remoteAddress() + " sent an invalid token.");
                        server.kickClient(ctx.channel(), "invalid token", new DisconnectPacket());
                    }
                });
            }).start();

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

    private String verifyToken(String token) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(Routes.API_HOST + ":" + Routes.API_PORT + "/my-profile");
            conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Connection", "close");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine;

                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                String json = response.toString();

                String uidMarker = "\"uid\":\"";
                int start = json.indexOf(uidMarker);
                if (start != -1) {
                    start += uidMarker.length();
                    int end = json.indexOf("\"", start);
                    return json.substring(start, end); //no more libraries it already compiles super slowly
                }

                return null;

            } else {
                if (conn.getErrorStream() != null) conn.getErrorStream().close();
                return null;
            }
        } catch (Exception e) {
            System.err.println("api token verification error: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
