package network;

import io.netty.buffer.ByteBuf;
import simulation.ScheduledActions.ActionDecoder;
import simulation.ScheduledActions.ActionEncoder;
import simulation.ScheduledActions.ScheduledAction;

public class ActionPacket extends ClientBoundPacket {
    private ScheduledAction action;

    public ActionPacket(ScheduledAction action) {
        super(PACKET_ACTION);
        this.action = action;
    }

    public ActionPacket(ByteBuf data) {
        super(PACKET_ACTION, data);
    }

    @Override
    protected void decodeData(ByteBuf data) {
        action = ActionDecoder.decode(data);
    }

    @Override
    public void write(ByteBuf buf) {
        ActionEncoder.encode(buf, action);
    }

    public ScheduledAction getAction() {return action;}

}
