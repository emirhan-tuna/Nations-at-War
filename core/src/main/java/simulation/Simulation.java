package simulation;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class Simulation {
    private Square square;
    private int tick = 0;
    private final CRC32 crc32 = new CRC32();
    private final ByteBuffer buffer = ByteBuffer.allocate(12);
    private static final int HISTORY_SIZE = 256;
    private final long[] checksumHistory = new long[HISTORY_SIZE];

    public Simulation() {
        square = new Square(0, 0, 50, 10);
    }

    public void update() {
        square.update();
        long currentChecksum = updateChecksum();
        checksumHistory[tick % HISTORY_SIZE] = currentChecksum;
        tick++;
    }

    public long updateChecksum() {
        crc32.reset();
        buffer.clear();

        buffer.putInt(square.id);
        buffer.putInt(square.x);
        buffer.putInt(square.y);

        crc32.update(buffer.array(), 0, buffer.position());
        return crc32.getValue();
    }

    public long getChecksum(int tick) {
        return checksumHistory[tick % HISTORY_SIZE];
    }

    public int getX() {
        return square.x;
    }

    public int getY() {
        return square.y;
    }

    public int getTick() {
        return tick;
    }
}
