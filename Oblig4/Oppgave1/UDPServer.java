package Oppgave1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class UDPServer{
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(4445);
        boolean running = true;

        while (running) {

            byte[] buffer = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);
            String recievedData = new String(buffer, 0, buffer.length);
            recievedData=recievedData.trim();
            System.out.println("Recieved: " + recievedData);

            if (recievedData.equals("close")){
                System.out.println("Closing server");
                running = false;
                break;
            }

            StringTokenizer tokenizer = new StringTokenizer(recievedData);
            int firstNumber = Integer.parseInt(tokenizer.nextToken());
            String operation = tokenizer.nextToken();
            int secondNumber = Integer.parseInt(tokenizer.nextToken());

            int answer = switch (operation){
                case "+" -> firstNumber + secondNumber;
                case "-" -> firstNumber - secondNumber;
                case "*" -> firstNumber * secondNumber;
                case "/" -> firstNumber / secondNumber;
                default -> 0;
            };

            System.out.println("Sending back: " +answer);
            buffer = Integer.toString(answer).getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), receivePacket.getPort());
            socket.send(sendPacket);
        }
        socket.close();
    }
}
