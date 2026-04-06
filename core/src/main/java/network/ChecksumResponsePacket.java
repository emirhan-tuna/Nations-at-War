package network;
import io.netty.buffer.ByteBuf;
import simulation.Simulation.Snapshot;
public class ChecksumResponsePacket extends ClientBoundPacket {
    private Snapshot snapshot;

    public ChecksumResponsePacket(ByteBuf data) {
        super(PACKET_CHECKSUM_RESPONSE, data);
    }

    public ChecksumResponsePacket(Snapshot snapshot) {
        super(PACKET_CHECKSUM_RESPONSE);
        this.snapshot = snapshot;
    }

    public void decodeData(ByteBuf data) {
        this.snapshot = new Snapshot(data);
    }

    public void write(ByteBuf buf) {
        this.snapshot.write(buf);
    }

    public Snapshot getSnapshot() {return this.snapshot;}
}
