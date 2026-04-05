import io.netty.channel.Channel;

public class Player {
    private Channel channel;
    private int id;
    private int gold;

    public Player(Channel channel, int id) {
        this.id = 0;
        this.channel = channel;
    }

    public int getId() {return id;}
    public Channel getChannel() {return channel;}
}
