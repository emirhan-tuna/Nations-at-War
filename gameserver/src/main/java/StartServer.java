import java.util.Random;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import network.GameOverPacket;
import network.Packet;
import network.Routes;
import network.StartGamePacket;

class StartServer {
    public static int port = 9000;

    
    private PlayerManager playerManager;
    private GameSimulation simulation;
    private NotifyApi api;

    private long currentGameId = 0;
    private Random rng = new Random();

    public StartServer() {
        this.playerManager = new PlayerManager();
        this.api = new NotifyApi(this);
        api.startHeartbeat();
    }

    public static void main(String[] args) throws Exception {
        new StartServer().run();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new ServerPacketEncoder());
                    ch.pipeline().addLast(new ServerPacketDecoder());
                    ch.pipeline().addLast(new ServerPacketHandler(StartServer.this));

                    System.out.println("new connection from ip: " + ch.remoteAddress());
                }
            });

            System.out.println("starting gameserver(port: " + port + ")");
            ChannelFuture f = b.bind(port).sync();
            System.out.println("server (should be) running!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            readyGame();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void readyGame() {
        this.simulation = new GameSimulation(this);
        this.currentGameId = rng.nextLong();
        api.reserveGameFromApi();
    }

    public void startGame() {
        new Thread(this.simulation).start();
        broadcast(new StartGamePacket());
        
        this.currentGameId = rng.nextLong();
        api.reserveGameFromApi();
    }

    public void endGame(int winnerId) {
        broadcast(new GameOverPacket(winnerId));

        for (Player p : playerManager.getAllPlayers()) {
            p.getChannel().close(); 
        }

        playerManager.clearPlayers();

        this.readyGame();
    }

    public long getCurrentGameId() {return currentGameId;}


    public void sendPacket(Channel channel, Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void broadcast(Packet packet) {
        for (Player p : playerManager.getAllPlayers()) {
            p.getChannel().writeAndFlush(packet);
        }
    }

    public void kickClient(Channel channel, String reason, Packet packet) {
        System.out.println("kicked client: " + channel.remoteAddress() + " for reason: " + reason);
        channel.writeAndFlush(packet).addListener(ChannelFutureListener.CLOSE);
    }

    public GameSimulation getSimulation() {return simulation;}
    public PlayerManager getPlayerManager() {return playerManager;}
}