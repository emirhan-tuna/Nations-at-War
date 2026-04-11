package simulation.GameObjects;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.Troops.Archer;
import simulation.GameObjects.Troops.Dragon;
import simulation.GameObjects.Troops.Knight;
import simulation.GameObjects.Troops.Mage;
import simulation.GameObjects.Troops.Tower;
import simulation.GameObjects.Troops.Troop;

public class ObjectDecoder {
    public static GameObject decode(ByteBuf buf) {
        GameObject object = null;
        int objId = buf.readInt();
        int objType = buf.readByte();

        switch(objType) {
            case Troop.ARCHER: object = new Archer(buf); break;
            case Troop.DRAGON: object = new Dragon(buf); break;
            case Troop.KNIGHT: object = new Knight(buf); break;
            case Troop.MAGE: object = new Mage(buf); break;
            case Troop.TOWER: object = new Tower(buf); break;
        }

        object.setId(objId);

        return object;
    }
}

