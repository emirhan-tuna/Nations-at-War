import io.netty.channel.Channel;
import simulation.SimPlayer;

public class Player {
    private final int id;
    private final Channel channel;
    private final int team;
    private SimPlayer simPlayer;

    public Player(int id, Channel channel) {
        this.id = id;
        this.team = id % 2;
        this.channel = channel;
    }

    public void setSimPlayer(SimPlayer simPlayer) {
        this.simPlayer = simPlayer;
    }

    public int getId() {return id;}
    public Channel getChannel() {return channel;}
    public int getTeam() {return team;}
}