package Oppgave1;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        DatagramSocket socket = new DatagramSocket();

        InetAddress address = InetAddress.getLocalHost();
        byte[] buffer;
        boolean running = true;

        while (running){
            //Scanning input and sending first packet
            System.out.println("Write an equation (number operator number): ");
            String inputFraBruker = sc.nextLine();
            buffer=inputFraBruker.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, address, 4445);
            socket.send(sendPacket);

            if (inputFraBruker.equals("close")){
                System.out.println("Closing connection");
                running = false;
                break;
            }

            buffer = new byte[256];
            DatagramPacket recievePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(recievePacket);

            String output = new String(buffer, 0, buffer.length);
            output = output.trim();

            System.out.println("Answer from server: " + output);
        }
        socket.close();
    }
}
