package Oppgave2;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimpleWebServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        final int PORT = 80;
        ServerSocket socket = new ServerSocket(PORT);
        System.out.println("Socket created");
        Socket connection = socket.accept();
        System.out.println("Connection established on port: "+PORT);

        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);

        PrintWriter writer = new PrintWriter(connection.getOutputStream());
        String st;
        ArrayList<String> listOfStrings = new ArrayList<>();

        while(!(st=reader.readLine()).equals("")){
            listOfStrings.add(st);
        }
        for (String s: listOfStrings){
            System.out.println(s);
        }
        connection.setKeepAlive(true);

        writer.write("HTTP/1.0 200 OK");
        System.out.println("HTTP/1.0 200 OK");
        writer.write("Content-Type: text/html; charset=utf-8\n");
        System.out.println("Content-Type: text/html; charset=utf-8\n");
        writer.write("<HTML><BODY><H1>Halla kompis<H1/>");
        writer.write("<UL>");

        for (String s: listOfStrings){
            writer.write("<LI>"+s+"<LI/>");
        }
        writer.write("<UL/>");
        writer.write("<BODY/><HTML/>");
        connection.setKeepAlive(true);

        reader.close();
        writer.close();
        connection.close();


    }
}

