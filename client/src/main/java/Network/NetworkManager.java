package Network;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import network.AuthPacket;
import network.ChecksumPacket;
import network.ServerBoundPacket;

public class NetworkManager {
    public static boolean debug = true;
    private String host;
    private int port;
    private Channel channel;

    public NetworkManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(int code) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                    //clientbound
                    //max size 1 mb i dont think exceeding that is physically possible
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));

                    //serverbound
                    ch.pipeline().addLast(new LengthFieldPrepender(4)); 
                    ch.pipeline().addLast(new PacketEncoder());

                    //clientbound
                    ch.pipeline().addLast(new PacketHandler());
                    ch.pipeline().addLast(new ClientPacketHandler(code));
                 }
             });

            System.out.println("Connecting to server at " + host + ":" + port);
            ChannelFuture f = b.connect(host, port).sync();
            this.channel = f.channel();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
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

    public void sendAuth(int code) {
        AuthPacket authPacket = new AuthPacket(code);
        sendPacket(authPacket);
    }

    public void sendChecksum(int x, int y, int tick) {
        ChecksumPacket checksumPacket = new ChecksumPacket(x, y, tick);
        sendPacket(checksumPacket);
    }
}