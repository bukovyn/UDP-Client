import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Server server = new Server(6556);
	}
}
