import io.netty.channel.Channel;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerManager {
    private final Map<Channel, Player> activePlayers = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Player addPlayer(Channel channel) {
        Player newPlayer = new Player(idCounter.getAndIncrement(), channel);
        activePlayers.put(channel, newPlayer);
        System.out.println("player " + newPlayer.getId() + " connected");
        return newPlayer;
    }

    public Player getPlayer(Channel channel) {
        return activePlayers.get(channel);
    }

    public Player removePlayer(Channel channel) {
        Player removed = activePlayers.remove(channel);
        if (removed != null) {
            System.out.println("player " + removed.getId() + " removed");
        }

        return removed;
    }

    public Collection<Player> getAllPlayers() {
        return activePlayers.values();
    }
}