import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {

    private final Pattern AUTH_PATTERN = Pattern.compile("^/auth (.+) (.+)$");
    private AuthService authService = new AuthServiceImpl();
    private Map<String,ClientHander> clientHandlerMap= Collections.synchronizedMap(new HashMap<>());

    public ChatServer() {

        try (ServerSocket serverSocket = new ServerSocket(8888))

        {
            System.out.println("Server Start");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                try (DataInputStream in = new DataInputStream(socket.getInputStream()); DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    String authMessage = in.readUTF();
                    Matcher matcher = AUTH_PATTERN.matcher(authMessage);
                    if (matcher.matches()) {
                        String username = matcher.group(1);
                        String password = matcher.group(2);
                        if (authService.AuthUser(username, password)) {
                            clientHandlerMap.put(username, new ClientHander(socket));
                            out.writeUTF("/auth succesful");
                            out.flush();
                        } else {
                            System.out.println("Ошибка авторизации");
                            out.writeUTF("/auth fail");
                            out.flush();
                            socket.close();
                        }
                    } else {
                        System.out.println("Ошибка сети!");
                        out.writeUTF("/auth fail");
                        out.flush();
                        socket.close();
                    }
                }


            }
    } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
