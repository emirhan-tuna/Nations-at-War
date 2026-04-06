package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;

public class ActionEncoder {

    public static void encode(ByteBuf buf, ScheduledAction action) {
        buf.writeByte(action.id);
        buf.writeInt(action.getTick());
        action.write(buf);
   }
}
