import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;


public class chat extends Application implements sendMsg {
    Stage window;
private String username;
private String msg;
//public Network network;
public TextArea textArea;
    @Override
    public void start (Stage primaryStage) throws Exception {
     /*   try{
            network = new Network("127.0.0.1",8888,this );

        }catch (IOException e1) {
            e1.printStackTrace();}*/

        window = primaryStage;
        window.setTitle("Chat");

        HBox sendline = new HBox(5);
        TextField field = new TextField("send message");

        Button buttonSend = new Button("Send");

        sendline.setAlignment(Pos.CENTER_RIGHT);
        sendline.getChildren().addAll(field, buttonSend);

        HBox area = new HBox(5);
      textArea = new TextArea();
        textArea.setEditable(false);
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setContent(textArea);
        area.getChildren().addAll(textArea);

        BorderPane bp = new BorderPane();
        bp.setCenter(area);
        bp.setBottom(sendline);

        Scene scene = new Scene(bp, 300, 400);
        window.setScene(scene);

        window.show();
        AuthWindow.display();

        field.setPrefWidth(scene.getWidth()-buttonSend.getWidth()-5);
        buttonSend.setOnAction(e -> {
            sendMsg("user",field.getText());
            textArea.appendText(username + "\n" + msg + "\n");
            field.setText("");
        });
    }

    @Override
    public void sendMsg(String username, String msg) {
        this.username = username;
        this.msg = msg;
    }
}
