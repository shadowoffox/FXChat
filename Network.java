import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Network implements Closeable {
    DataOutputStream out;
    DataInputStream in;
    Scanner scanner = new Scanner(System.in);
    private final Thread rec;
    private final Socket socket;
    private sendMsg messageSender;
    private String AUTH_PATTERN= "/auth %s %s";


    public  Network(String hostname, int port, sendMsg messageSender) throws IOException {

        this.socket = new Socket(hostname,port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.messageSender = messageSender;
        this.in = new DataInputStream(socket.getInputStream());


       this.rec = new Thread(new Runnable() {
            @Override
            public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String msg = in.readUTF();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            messageSender.sendMsg("Server", msg);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        });

    }
    public void authorise(String username, String password) {
        try {
            out.writeUTF(String.format(AUTH_PATTERN, username, password));
            out.flush();
            String response = in.readUTF();
            if (response.equals("/auth succesful")){
                rec.start();
            } else {
                throw new AuthException(){};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void sendMessage(String msg){
        try {
        out.writeUTF(msg);
        out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
