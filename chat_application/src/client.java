import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static final int PORT_NUMBER = 6666;

    public static void main(String[] args) throws Exception {
        // Connect to the server
        Socket socket = new Socket("localhost", PORT_NUMBER);
        // Waiting for the server acknowledgement
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(in.readLine());

        readMessageFromUserAndSendToServer(socket);
        // Close the connection
        socket.close();
    }

    static void sendMessageToServer(Socket socket, String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readMessageFromUserAndSendToServer(Socket socket) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            while (true) {
                message = br.readLine();
                if (message.equals(String.valueOf("exit"))) {
                    sendMessageToServer(socket, message);
                    break;
                }
                // System.out.println("Sending message to server: " + message);
                sendMessageToServer(socket, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
