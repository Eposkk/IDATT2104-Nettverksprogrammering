import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int port;
        boolean keepConnection=true;

        Scanner sc = new Scanner(System.in);
        System.out.println("Give address to the server you would like to connect to");
        String address = sc.nextLine();
        System.out.println("Give portnumber: ");
        String portString = sc.nextLine();
        port = Integer.parseInt(portString);

        Socket connection = new Socket(address, port);
        if (connection.isConnected()){
            System.out.println("Connection acquired");
        }
        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        while(keepConnection){
            keepConnection=printOutputFromServer(reader.readLine());
            writer.println(sc.nextLine());
            keepConnection=printOutputFromServer(reader.readLine());
            writer.println(sc.nextLine());
            keepConnection=printOutputFromServer(reader.readLine());
            writer.println(sc.nextLine());
            keepConnection=printOutputFromServer(reader.readLine());
        }

        reader.close();
        writer.close();
        connection.close();
    }

    public static boolean printOutputFromServer(String s) throws IOException {
        if (!(s.equals(null) || s.equals(""))){
            System.out.println(s);
        }
        else if (s.equals("Close")){
            return false;
        }
        return true;
    }
}
