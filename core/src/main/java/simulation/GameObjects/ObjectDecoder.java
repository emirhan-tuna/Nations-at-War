package simulation.GameObjects;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.Troops.Archer;
import simulation.GameObjects.Troops.Dragon;
import simulation.GameObjects.Troops.Knight;
import simulation.GameObjects.Troops.Mage;
import simulation.GameObjects.Troops.Tower;

public class ObjectDecoder {
    public static GameObject decode(ByteBuf buf) {
        GameObject object = null;
        int objId = buf.readInt();
        int objType = buf.readByte();

        switch(objType) {
            case 0: object = new Archer(buf); break;
            case 1: object = new Dragon(buf); break;
            case 2: object = new Knight(buf); break;
            case 3: object = new Mage(buf); break;
            case 4: object = new Tower(buf); break;
        }

        object.setId(objId);

        return object;
    }
}

