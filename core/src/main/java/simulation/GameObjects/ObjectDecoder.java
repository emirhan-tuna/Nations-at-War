package simulation.GameObjects;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.Troops.Archer;

public class ObjectDecoder {
    public static GameObject decode(ByteBuf buf) {
        GameObject object = null;
        int objId = buf.readByte();
        int objType = buf.readByte();

        switch(objType) {
            case 0:
                object = new Archer(0, 0 ,0);
                break;
            default:
                throw new IllegalArgumentException("unknown object type: " + objType);
        }

        object.setId(objId);

        return object;
    }
}

