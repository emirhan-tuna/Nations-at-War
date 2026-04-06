package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;

public class ActionDecoder {
    public static ScheduledAction decode(ByteBuf buf) {
        ScheduledAction action = null;
        int actionId = buf.readByte();
        int tick = buf.readInt();

        switch(actionId) {
            case 0:
                action = new SpawnAction(buf);
                break;
            default:
                throw new IllegalArgumentException("unknown scheduledaction id: " + actionId);
        }

        action.setTick(tick);

        return action;
    }
}
