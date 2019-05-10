import java.io.IOException;
import java.net.*;

public class PeerDiscovery extends Thread {
    private static final PeerDiscovery peerDiscoveryInstance = new PeerDiscovery();
    private DatagramSocket datagramSocket;

    {
        try {
            datagramSocket = new DatagramSocket(5555);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private PeerDiscovery() {
    }

    public static PeerDiscovery getInstance() {
        return peerDiscoveryInstance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ReceiveUDP(datagramSocket);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    private void ReceiveUDP(DatagramSocket socket) throws SocketException, UnknownHostException {
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data received");
        SendUDPPacket(packet.getAddress().getHostAddress(), packet.getPort(), "");
    }

    private void SendUDPPacket(String IP, int port, String message) throws UnknownHostException, SocketException {
        byte[] buffer = message.getBytes();
        InetAddress address = InetAddress.getByName(IP);
        DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length, address, port);
        DatagramSocket socket2 = new DatagramSocket();
        try {
            socket2.send(packet2);
        } catch (IOException e) {
            System.out.println("Failed To Send Message");
        }
    }
}
