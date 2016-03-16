public class Packet {
    String packet;
    int player;

    public Packet(String packet, int player) {
        this.packet = packet;
        this.player = player;
    }

    public String packet() {
        return packet;
    }

    public int player() {
        return player;
    }
}
