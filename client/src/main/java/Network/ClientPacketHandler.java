package Network;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import UI.GameScreenUI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.GameOverPacket;
import network.Packet;
import network.StartGamePacket;
import simulation.Simulation;
import simulation.ScheduledActions.ScheduledAction;
import simulation.Simulation.Snapshot;

public class ClientPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private int code;
    private Screen screen;
    private Simulation simulation;

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
                authPacket.getId();
            } else {
                System.out.println("auth failed");
            }
        } else if(msg instanceof ChecksumResponsePacket) {
            ChecksumResponsePacket checksum = (ChecksumResponsePacket) msg;
            Snapshot snapshot = checksum.getSnapshot();

            simulation.scheduleFromNetwork(() -> {
                simulation.correct(snapshot);
            });

        } else if(msg instanceof ActionPacket) {
            ActionPacket actionPacket = (ActionPacket) msg;
            ScheduledAction action = actionPacket.getAction();
            Simulation sim = ((GameScreenUI)screen).getClientManager().getSimulation();
            sim.scheduleFromNetwork(() -> {
                sim.addAction(action, action.getTick());
            });
        } else if(msg instanceof GameOverPacket) {

        } else if(msg instanceof StartGamePacket) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (screen instanceof GameScreenUI) {
                        ((GameScreenUI)screen).getClientManager().start();
                    } else {
                        System.out.println("Warning: Received StartGamePacket but current screen is not GameScreenUI");
                    }
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        System.out.println("Connection error: " + err.getMessage());
        ctx.close();
    }
}