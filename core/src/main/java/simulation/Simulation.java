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
    private long currentChecksum;
    private boolean isServer;

    public Simulation(boolean isServer) {
        square = new Square(0, 0, 50, 10);
        this.isServer = isServer;
    }

    public static class Snapshot {
        public int tick;
        public int x;
        public int y;
        public long checksum;
    }

    public void update() {
        this.tick++;
        square.update();
        long currChecksum = updateChecksum();
        this.currentChecksum = currChecksum;
        if(this.isServer) {
            checksumHistory[this.tick % HISTORY_SIZE] = currChecksum;
        }
    }

    public void correct(int x, int y, int tick) {
        int tickDiff = this.tick - tick;

        if(tickDiff < 0) {
            square.x = x;
            square.y = y;
            this.tick = tick;
            return;
        }

        square.x = x;
        square.y = y;
        this.tick = tick;

        for(int i = 0; i < tickDiff; i++) {
            this.update();
        }
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

    public long getCurrentChecksum() {
        return this.currentChecksum;
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
