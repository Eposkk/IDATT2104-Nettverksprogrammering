package Oppgave2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int PORT = 80;
        ServerSocket socket = new ServerSocket(PORT);
        System.out.println("Socket created");
        Socket connection = socket.accept();
        System.out.println("Connection established on port: "+PORT);

        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);

        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);
        String st;
        ArrayList<String> listOfStrings = new ArrayList<>();

        writer.println("HTTP/1.0 200 OK");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("");
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<h1>Hi!</h1>");
        writer.println("<h3>These are the returned headers:</h3>");
        writer.println("<ul>");

        while(!(st=reader.readLine()).equals("")){
            listOfStrings.add(st);
        }
        for (String s: listOfStrings){
            writer.println("<li>"+s+"</li>");
        }
        writer.println("</ul>");
        writer.println("</body></html>");

        reader.close();
        writer.close();
        connection.close();
        System.out.println("Connection on port "+PORT+" closed");
    }
}