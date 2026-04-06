import io.netty.channel.Channel;

public class Player {
    private final int id;
    private final Channel channel;
    private final int team;

    public Player(int id, Channel channel) {
        this.id = id;
        this.team = id % 2;
        this.channel = channel;
    }

    public int getId() {return id;}
    public Channel getChannel() {return channel;}
    public int getTeam() {return team;}
}