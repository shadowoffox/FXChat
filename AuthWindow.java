import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthWindow {
    private static chat Chat;
    String lonin;
    String pass;
    private static Network network;

    public static Network getNetwork() {
        return network;
    }
public boolean isAuthSuccessful(){
        return network !=null;
}
    public static void display(){
        network=null;
        Stage win = new Stage();
        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle("Authorization");

        HBox login = new HBox(30);
        Label lbLogin = new Label("Login:");
        TextField tfLogin = new TextField();
        login.getChildren().addAll(lbLogin,tfLogin);
        login.setAlignment(Pos.CENTER);

        HBox pass = new HBox(10);
        Label lbPass = new Label("Password:");
        PasswordField pfPass = new PasswordField();
        pass.getChildren().addAll(lbPass,pfPass);
        pass.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(50);
        Button logIn = new Button("Log in");
        logIn.setOnAction(e -> {
            try {
                network = new Network("127.0.0.1",8888,(sendMsg) Chat );
                network.authorise(tfLogin.getText(),pfPass.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            } catch (AuthException e1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!!!");
                alert.setHeaderText(null);
                alert.setContentText("Ошибка авторизации!!!");
                alert.showAndWait();
                return;
            }
          win.close();  //отправляем данные на сервер авторизации, если получаем "ok" то закрываем это окно и запускаем чат, если получаем "эта учетная уже используется" или "неверный логин/пароль"
            //то ниче не делаем, только выводим это на экран
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> {
          win.close();  //останавливаем потоки закрываем сокет и закрываем программу полностью
        });
        buttons.getChildren().addAll(logIn,cancel);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(5);
        layout.getChildren().addAll(login,pass,buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,300,300);
        win.setScene(scene);
        win.show();

    }
}
