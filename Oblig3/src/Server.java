import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server extends Thread{
    Socket connection;
    int port;

    public Server(Socket socket, int port){
        connection = socket;
        this.port = port;
    }

    public void run(){
        try{
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);

            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);
            writer.println("Hello, this is a simple calculator\n");

            while (true){
                writer.println("Type the first number you want to use (If at any point you want to close connection write close): ");
                String first = reader.readLine();
                writer.println("Operator: ");
                String operator = reader.readLine();
                writer.println("Second number: ");
                String second = reader.readLine();

                if (operator.equalsIgnoreCase("close") || first.equalsIgnoreCase("close") || second.equalsIgnoreCase("close")){
                    writer.println("Close");
                    break;
                }

                int firstParsed;
                int secondParsed;
                try {
                    firstParsed = Integer.parseInt(first);
                    secondParsed = Integer.parseInt(second);
                    int answer;
                    if (!(operator.equals("+") || operator.equals("-"))){
                        writer.println("Use a proper operator");
                        System.out.println("improper operator");
                    }else {
                        switch (operator){
                            case "+":
                                answer = firstParsed + secondParsed;
                                writer.println(answer);
                                break;
                            case "-":
                                answer = firstParsed-secondParsed;
                                writer.println(answer);
                                break;
                            default:
                                writer.println("Something went wrong!");
                                System.out.println("Something went wrong");
                        }
                    }
                }catch (NumberFormatException e){
                    writer.println("Make sure to user integers");
                    System.out.println("Number fault");
                }
            }
            writer.println("Connection is closing, bye bye!");
            System.out.println("Closing connection on port: "+port);
            reader.close();
            writer.close();
            connection.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
