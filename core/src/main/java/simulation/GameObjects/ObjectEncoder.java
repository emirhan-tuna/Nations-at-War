package simulation.GameObjects;

import io.netty.buffer.ByteBuf;

public class ObjectEncoder {
    public static void encode(ByteBuf buf, GameObject object) {
        buf.writeByte(object.getId());
        buf.writeByte(object.getType());
        object.write(buf);
   }
}
