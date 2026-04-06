package Network;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import UI.NetworkTestUi;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.Packet;
import simulation.ScheduledActions.ScheduledAction;
import simulation.Simulation.Snapshot;

public class ClientPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private int code;
    private Screen screen;

    public ClientPacketHandler(int code, Screen screen) {
        this.code = code;
        this.screen = screen;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        AuthPacket authPacket = new AuthPacket(code);
        ctx.writeAndFlush(authPacket);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {

        if(msg instanceof AuthResponsePacket) {
            AuthResponsePacket authPacket = (AuthResponsePacket) msg;

            if(authPacket.getResponse()) {
                System.out.println("auth successful");

                //use this id elsewhere(0 for left, 1 for right side)
                authPacket.getId();
            } else {
                System.out.println("auth failed");
            }
        } else if(msg instanceof ChecksumResponsePacket) {
            ChecksumResponsePacket checksum = (ChecksumResponsePacket) msg;
            Snapshot snapshot = checksum.getSnapshot();

            //Gdx.app.postRunnable(() -> {
            //    send snapshot here...
            //});

        } else if(msg instanceof ActionPacket) {
            ActionPacket actionPacket = (ActionPacket) msg;
            ScheduledAction action = actionPacket.getAction();
            //...
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        System.out.println("Connection error: " + err.getMessage());
        ctx.close();
    }
}