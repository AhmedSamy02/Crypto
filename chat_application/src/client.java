import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static final int PORT_NUMBER = 6666;
    static Socket socket;
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static BufferedReader socketReader;
    static PrintWriter out;
    static Thread serverRead;
    static Thread userRead;
    private static int id;
    private static String names[] = { "Alice", "Bob" };
    private static boolean flag = false;

    public static void main(String[] args) throws Exception {

        System.out.println("Connecting to server ......");
        // Connect to the server
        socket = new Socket("localhost", PORT_NUMBER);
        System.out.println("Connected Successfully");

        // Waiting for the server acknowledgement
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Waiting for other user ........");
        System.out.println(socketReader.readLine());
        id = Integer.parseInt(socketReader.readLine());
        userRead = new Thread() {
            @Override
            public void run() {
                readMessageFromUserAndSendToServer();
            }

            @Override
            public void interrupt() {
                super.interrupt();
                try {
                    System.out.println("Disconnected from server Press enter to exit");
                    in.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        serverRead = new Thread() {
            @Override
            public void run() {
                readMessageFromServer();
            }

        };
        userRead.start();
        serverRead.start();
        userRead.join();
        serverRead.join();

    }

    static void readMessageFromUserAndSendToServer() {
        String message = null;
        try {
            while (socket.isConnected()) {
                message = in.readLine();
                if (message == null) {
                    break;
                }
                while (message == null || message.equals("")) {
                    // System.out.print(names[id] + " : ");
                    message = in.readLine();
                }
                // Send message to server
                out.println(message);
                if (message.equals(String.valueOf("exit"))) {
                    flag = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void readMessageFromServer() {
        try {
            String message;
            while (socket.isConnected()) {

                message = socketReader.readLine();
                if (message == null) {
                    break;
                }
                if (message.equals(String.valueOf("exit"))) {
                    if (!flag) {
                        userRead.interrupt();
                    }
                    break;
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
