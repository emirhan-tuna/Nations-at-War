
import java.util.Map;

import io.netty.channel.Channel;
import network.DisconnectPacket;
import network.GameOverPacket;
import network.Packet;
import network.StartGamePacket;
import simulation.Simulation;

public class GameRoom {
    private Map<Integer, GameRoom> rooms;
    private StartServer server;
    private Player[] players = new Player[2];
    private int playerCount = 0;

    private GameSimulation simulation;
    private NotifyApi api;
    private int roomId;

    //0 s 1 started 2 over
    private int gameState;

    public GameRoom(StartServer server, int id) {
        this.server = server;
        this.rooms = server.getRooms();
        this.roomId = id;
        this.api = new NotifyApi(this);

        this.api.reserveGameFromApi();
        this.api.startHeartbeat();
    }

    public void addPlayer(Player player) {
        if(playerCount > 1) {
            kickPlayer(player, "room with id " + roomId + " full", new DisconnectPacket());
            return;
        }

        player.setRoom(this);
        player.setTeam(playerCount);
        players[playerCount] = player;
        playerCount++;

        if(playerCount >= 2) {
            start();
        }
    }

    public void start() {
        System.out.println("game starting... id: " + this.roomId);
        simulation = new GameSimulation(this);
        broadcast(new StartGamePacket());
        gameState = 1;
        
        server.readyGame();

        Thread simThread = new Thread(simulation);
        simThread.start();
    }

    public void endGame(int winner) {
        //todo: db add points

        System.out.println("ending game");

        gameState = 2;

        broadcast(new GameOverPacket(winner));
        destroy();
    }

    public void destroy() {
        if(this.simulation != null) {
            this.simulation.stop();
        }

        api.stopHeartbeat();

        api.endGame(roomId);

        rooms.remove(this.getId());
        kickAll("room destroyed");
    }

    public void kickAll(String reason) {
        for(int i = 0; i < playerCount; i++) {
            kickPlayer(players[i], reason, new DisconnectPacket());
        }
    }

    public void sendPacket(Player player, Packet packet) {
        if (player != null && player.getChannel() != null && player.getChannel().isActive()) {
            player.getChannel().writeAndFlush(packet);
        }
    }

    public void broadcast(Packet packet) {
        for (int i = 0; i < playerCount; i++) {
            sendPacket(players[i], packet);
        }
    }

    public void kickPlayer(Player player, String reason, Packet kickPacket) {
        System.out.println("kicking player " + player.getId() + " from room " + roomId + ". reason: " + reason);
        
        Channel channel = player.getChannel();
        server.kickClient(channel, reason, kickPacket);
    }

    public void playerQuit(int team) {
        if(gameState == 1) {
            endGame(1 - team);
        } else if(gameState == 0) {
            server.readyGame();
            destroy();
        }
    }

    public int getId() {return this.roomId;}

    public int getGameState() {return this.gameState;}

    public Simulation getSimulation() {

        if(simulation == null) {
            return null;
        }
        
        return this.simulation.getSimulation();
    }
}
