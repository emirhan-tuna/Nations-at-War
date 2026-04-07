package Network;
import com.badlogic.gdx.Screen;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import network.ChecksumPacket;
import network.ServerBoundPacket;
import network.SpawnPacket;

public class NetworkManager {
    public static boolean debug = true;
    private String host;
    private int port;
    private Channel channel;
    private final EventLoopGroup workerGroup;

    public NetworkManager() {
        this.workerGroup = new NioEventLoopGroup();
    }

    public void connect(String host, int port, long code, Screen screen) {

        if (isConnected()) {
            System.out.println("already connected");
            disconnect();
        }

        this.host = host;
        this.port = port;


        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    
                    //clientbound
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));

                    //serverbound
                    ch.pipeline().addLast(new PacketEncoder());
                    ch.pipeline().addLast(new PacketHandler());
                    
                    //clientbound
                    ch.pipeline().addLast(new ClientPacketHandler(code, screen)); 
                }
            });

            System.out.println("Connecting to server at " + host + ":" + port);
            
            ChannelFuture f = b.connect(host, port).sync();
            f.addListener((ChannelFuture future) -> {
                if (future.isSuccess()) {
                    this.channel = future.channel();
                    System.out.println("Successfully connected!");
                } else {
                    System.err.println("Failed to connect!");
                    future.cause().printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(ServerBoundPacket packet) {
        if(isConnected()) {
            channel.writeAndFlush(packet);
        } else {
            System.out.println("error: packet couldnt be send with type: " + packet.getType());
        }
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

    public void disconnect() {
        if (isConnected()) {
            channel.close().addListener(future -> {
                System.out.println("disconnected");
            });
        }
    }

    public void dispose() {
        disconnect();
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public void sendChecksum(long checksum, int tick) {
        ChecksumPacket checksumPacket = new ChecksumPacket(checksum, tick);
        sendPacket(checksumPacket);
    }

    public void sendSpawn(int type, int lane) {
        SpawnPacket spawnPacket = new SpawnPacket(type, lane);
        sendPacket(spawnPacket);
    }
}