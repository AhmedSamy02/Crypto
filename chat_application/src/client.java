import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
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
    private static boolean flag = false;
    private static int privateKey = 0;
    private static int publicKey = 0;
    private static int otherPublicKey = 0;
    private static int secretSharedKey = 0;

    private static int modularExponent(int x, int y, int p) {
        int res = x%p;
        while (y>1) {
            res *= x;
            res %= p;
            y--;
        }
        return res;
    }

    public static void main(String[] args) throws Exception {

        int qDH = 0;
        int alphaDH = 0;
        int qGamal = 0;
        int alphaGamal = 0;
        // Read the file name from the user
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            line = br.readLine();
            qDH = Integer.parseInt(line.split(" ")[0]);
            alphaDH = Integer.parseInt(line.split(" ")[1]);
            line = br.readLine();
            qGamal = Integer.parseInt(line.split(" ")[0]);
            alphaGamal = Integer.parseInt(line.split(" ")[1]);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Deffie Helman Parameters : q = " + qDH + " alpha = " + alphaDH);
        System.out.println("Elgamal Parameters : q = " + qGamal + " alpha = " + alphaGamal);

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
        if (id == 0) {
            privateKey = (int) Math.floor(Math.random() * qDH + 1);
            publicKey = modularExponent(alphaDH, privateKey, qDH);
            // TODO : Add El Gamal signature
            out.println(publicKey);
            otherPublicKey = Integer.parseInt(socketReader.readLine());
            // TODO : Check El Gamal Signature
            boolean isValid = true;
            if (!isValid) {
                out.println(0);
                socket.close();
                socketReader.close();
                out.close();
                return;
            }
            out.println(1);
        } else {
            otherPublicKey = Integer.parseInt(socketReader.readLine());
            // TODO : Check El Gamal Signature
            boolean isValid = true;
            if (!isValid) {
                out.println(0);
                socket.close();
                socketReader.close();
                out.close();
                return;
            }
            out.println(1);
            privateKey = (int) Math.floor(Math.random() * qDH + 1);
            publicKey = modularExponent(alphaDH, privateKey, qDH);
            // TODO : Add El Gamal signature
            out.println(publicKey);
        }
        secretSharedKey = modularExponent(otherPublicKey, privateKey, qDH);
        System.out.println("----------------Status----------------");
        System.out.println("Connection Done Successfully");
        System.out.println("----------------Parameters----------------");
        System.out.println("Public key : " + publicKey);
        System.out.println("Private key : " + privateKey);
        System.out.println("Other Public Key : " + otherPublicKey);
        System.out.println("Secret Shared Key : " + secretSharedKey);
        System.out.println();
        System.out.println("You can start chatting now");
        System.out.println("----------------Chat----------------");

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
