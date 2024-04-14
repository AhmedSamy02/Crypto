import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static final int PORT_NUMBER = 6666;
    public static PrintWriter out[];
    public static BufferedReader in[];
    public static Socket sockets[];
    private static ServerSocket socket;
    private static Chat clients[];
    private static final int MAX_CLIENTS = 3;
    public static void main(String[] args) throws Exception {
        socket = new ServerSocket(PORT_NUMBER);
        clients = new Chat[MAX_CLIENTS];
        sockets = new Socket[MAX_CLIENTS];
        in = new BufferedReader[MAX_CLIENTS];
        out = new PrintWriter[MAX_CLIENTS];
        System.out.println("Server is running on port " + PORT_NUMBER);
        // Wait for All clients to connect
        for (int i = 0; i < MAX_CLIENTS; i++) {
            sockets[i] = socket.accept();
            System.out.println("Client " + (i + 1) + " connected");
            out[i] = new PrintWriter(sockets[i].getOutputStream(), true);
            in[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
            clients[i] = new Chat(i);
        }
        //Start chatting
        for (int i = 0; i < MAX_CLIENTS; i++) {
            clients[i].start();
        }
        socket.close();
    }

    private static class Chat extends Thread {
        private int id;

        Chat(int ID) {
            this.id = ID;
            out[id].println("Connected Succesfully : You are client " + (id + 1));
        }

        @Override
        public void run() {
            String message;
            while (true) {
                try {
                    message = in[id].readLine();
                    if (message.equals(String.valueOf("exit"))) {
                        out[id].close();
                        in[id].close();
                        for (int i = 0; i < MAX_CLIENTS; i++) {
                            if (i != id) {
                                clients[i].interrupt();
                            }
                        }
                        sockets[id].close();
                        break;
                    } else {
                        for (int i = 0; i < MAX_CLIENTS; i++) {
                            if (i != id) {
                                out[i].println("Client " + (id + 1) + " : " + message);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        @Override
        public void interrupt() {
            out[id].close();
            
            try {
                in[id].close();
                sockets[id].close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.interrupt();
        }
    }
}
