import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerStarter {
    public static void main(String[] args) throws IOException {
        final int STARTPORT = 1250;
        int port = STARTPORT;
        int numberOfConnections = 4;
        ArrayList<Server> servers = new ArrayList<>();

        for (int i = 0; i <= numberOfConnections; i++) {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server created on port: " +port);
            port++;
            Socket connection = serverSocket.accept();
            servers.add(new Server(connection,port));
            servers.get(i).start();
        }
    }
}
