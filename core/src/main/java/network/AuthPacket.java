package network;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
public class AuthPacket extends ServerBoundPacket {
    private int code;
    private String token;

    public AuthPacket(int code, String token) {
        super(PACKET_AUTH);
        this.code = code;
        this.token = token;
    }

    public AuthPacket(ByteBuf data) {
        super(PACKET_AUTH, data);
    }

    public void decodeData(ByteBuf data) {
        this.code = data.readInt();

        int tokenLength = data.readInt();
        byte[] tokenBytes = new byte[tokenLength];
        data.readBytes(tokenBytes);
        
        this.token = new String(tokenBytes, StandardCharsets.UTF_8);
    }

    public void write(ByteBuf buf) {
        buf.writeInt(code);

        byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(tokenBytes.length);
        buf.writeBytes(tokenBytes);
    }

    public int getCode() {return code;}

    public String getToken() {return token;}
}
