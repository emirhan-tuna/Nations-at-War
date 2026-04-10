package Network;
import com.badlogic.gdx.Gdx;

import Game.ClientGameManager;
import Game.Main;
import UI.GameOverUI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.ActionPacket;
import network.AuthPacket;
import network.AuthResponsePacket;
import network.ChecksumResponsePacket;
import network.GameOverPacket;
import network.Packet;
import network.StartGamePacket;
import network.DisconnectPacket;
import simulation.Simulation;
import simulation.ScheduledActions.ScheduledAction;
import simulation.Simulation.Snapshot;

public class ClientPacketHandler extends SimpleChannelInboundHandler<Packet> {
    private int code;
    private ClientGameManager manager;
    private Simulation simulation;

    public ClientPacketHandler(int code, ClientGameManager manager) {
        this.code = code;
        this.manager = manager;
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
            Simulation sim = manager.getSimulation();
            sim.scheduleFromNetwork(() -> {
                sim.addAction(action, action.getTick());
            });
        } else if(msg instanceof GameOverPacket) {
            Main game = manager.getGame();

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    manager.stop();
                    game.setScreen(new GameOverUI(game));
                }
            });

        } else if(msg instanceof StartGamePacket) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    manager.start();
                }
            });
        } else if(msg instanceof DisconnectPacket) {
            //handle it here...
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        System.out.println("Connection error: " + err.getMessage());
        ctx.close();
    }
}