package simulation.GameObjects;

import io.netty.buffer.ByteBuf;

public class ObjectDecoder {
    public static GameObject decode(ByteBuf buf) {
        GameObject object = null;
        int objId = buf.readByte();
        int objType = buf.readByte();

        switch(objType) {
            case 0:
                object = new Square(buf);
                break;
            default:
                throw new IllegalArgumentException("unknown object type: " + objType);
        }

        object.setId(objId);

        return object;
    }
}

