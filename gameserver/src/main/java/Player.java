import io.netty.channel.Channel;
import simulation.SimPlayer;

public class Player {
    private final int id;
    private final Channel channel;
    private int team;

    private GameRoom room;
    private SimPlayer simPlayer;

    public Player(int id, Channel channel) {
        this.id = id;
        this.team = id % 2;
        this.channel = channel;
    }

    public void setSimPlayer(SimPlayer simPlayer) {
        this.simPlayer = simPlayer;
    }

    public void setRoom(GameRoom room) {this.room = room;}
    public void setTeam(int team) {this.team = team;}

    public GameRoom getRoom() {return this.room;}
    public SimPlayer getSimPlayer() {return this.simPlayer;}
    public int getId() {return id;}
    public Channel getChannel() {return channel;}
    public int getTeam() {return team;}
}