import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static final int PORT_NUMBER = 6666;
    public static Socket sockets[] = new Socket[2];
    private static ServerSocket socket;
    private static Chat clients[] = new Chat[2];
    static private PrintWriter out[] = new PrintWriter[2];
    static private BufferedReader in[] = new BufferedReader[2];
    private static String names[] = { "Alice", "Bob" };

    public static void main(String[] args) throws Exception {
        socket = new ServerSocket(PORT_NUMBER);

        System.out.println("Server is running on port " + PORT_NUMBER);
        // Wait for All clients to connect
        for (int i = 0; i < 2; i++) {
            sockets[i] = socket.accept();
            System.out.println("Client " + (i + 1) + " connected");
        }
        // Output and Input streams for both clients initiallizations
        out[0] = new PrintWriter(sockets[0].getOutputStream(), true);
        out[1] = new PrintWriter(sockets[1].getOutputStream(), true);
        in[0] = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
        in[1] = new BufferedReader(new InputStreamReader(sockets[1].getInputStream()));
        out[0].println("Connected Succesfully : You are " + names[0]);
        out[0].println(0);
        out[1].println("Connected Succesfully : You are " + names[1]);
        out[1].println(1);
        System.out.println("Both clients connected and streams initialized.");
        boolean swappingDone = swappingPublicKeys();
        if (!swappingDone) {
            System.out.println("The session ended because of unauthorized user");
            socket.close();
            return;
        }
        // Create a chat object for each client
        clients[0] = new Chat(0);
        clients[1] = new Chat(1);
        // Start chatting
        clients[0].start();
        clients[1].start();
        // Wait for all clients to disconnect
        clients[0].join();
        clients[1].join();
        
    }

    private static boolean swappingPublicKeys() throws IOException {
        int yA = Integer.parseInt(in[0].readLine());
        out[1].println(yA);
        int ack = Integer.parseInt(in[1].readLine());
        if (ack == 0) {
            sockets[1].close();
            return false;
        }
        int yB = Integer.parseInt(in[1].readLine());
        out[0].println(yB);
        ack = Integer.parseInt(in[0].readLine());
        if (ack == 0) {
            sockets[0].close();
            return false;
        }
        return true;
    }

    private static class Chat extends Thread {
        private int id;
        Chat(int ID) throws IOException {
            this.id = ID;
        }
        @Override
        public void run() {
            try {
                String message = null;
                while (true) {
                    message = in[id].readLine();
                    if (message.equals(String.valueOf("exit"))) {
                        out[id].println("exit");
                        out[1 - id].println("exit");
                        out[id].close();
                        in[id].close();
                        clients[1 - id].interrupt();
                        sockets[id].close();
                        break;
                    } else {
                        System.out.println(names[id]+ " : " + message);
                        // otherOut.println("Client " + (id + 1) + " : " + message);
                        out[1 - id].println(message);
                    }
                }
            } catch (Exception e) {
                System.out.println("Session ended");
            }
        }
        @Override
        public void interrupt() {
            super.interrupt();
            try {
                out[id].println("exit");
                out[id].close();
                in[id].close();
                sockets[id].close();
            } catch (IOException e) {

            }

        }

    }
}
