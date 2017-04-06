package com.lin.view;

import java.util.*;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.event.*;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.stage.*;
import javafx.geometry.*;

import com.lin.view.*;
import com.lin.util.*;
import com.lin.protocol.*;
import com.lin.model.*;
import com.lin.database.*;


public class Login extends Application {

    private EmailClientDB emailClientDB;
    private List<User> all_users;
    private GridPane grid;
    private Text title;
    private Label addr;
    private TextField addrField;
    private Label pswd;
    private TextField pswdField;
    private Button login;
    private HBox hbBtn;
    private Text hint;
    private Scene scene;
    private ListView<String> hint_list;
    private ObservableList<String> hint_data;
    private static String[] hint_text = {"@qq.com", "@163.com", "@sohu.com", "@foxmail.com", "@126.com"};
    private ContextMenu contextMenu;
    private CustomMenuItem item;

    @Override
    public void start(Stage primaryStage) {
        initEmailClientDB();
        initComponents(primaryStage);
        initEvents(primaryStage);
    }

    private void initEmailClientDB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        emailClientDB = EmailClientDB.getInstance();
        all_users = emailClientDB.loadUsers();
    }

    private void initComponents(Stage primaryStage) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        title = new Text("Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        grid.add(title, 0, 0, 2, 1);

        addr = new Label("Email addr:");
        grid.add(addr, 0, 1);

        addrField = new TextField();
        grid.add(addrField, 1, 1);

        pswd = new Label("Password:");
        grid.add(pswd, 0, 2);

        pswdField = new PasswordField();
        grid.add(pswdField, 1, 2);

        login = new Button("Login");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(login);
        grid.add(hbBtn, 1, 4);

        hint = new Text();
        grid.add(hint, 1, 6, 2, 1);

        hint_data = FXCollections.observableArrayList();
        hint_list = new ListView<String>();
        hint_list.setItems(hint_data);
        hint_list.setVisible(false);
        grid.add(hint_list, 1, 2, 1, 5);

        scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Email Client");
        primaryStage.show();
    }

    private void initEvents(Stage primaryStage) {

        addrField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                String addr = addrField.getText();
                if ("".equals(addr) || addr == null || addr.contains("@")) {
                    hint_list.setVisible(false);
                } else {
                    hint_data.clear();
                    for (int i = 0; i < hint_text.length; ++i) {
                        hint_data.add(addr + hint_text[i]);
                    }
                    hint_list.setItems(hint_data);
                    hint_list.setVisible(true);
                }
            }
        });

        login.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                if (hasUser(addrField.getText(), pswdField.getText())) {
                    primaryStage.close();
                    new MainStage().show();
                } else {
                    hint.setFill(Color.FIREBRICK);
                    hint.setText("Email addr or Password error!");
                }
            }
        });

        hint_list.getSelectionModel().selectedItemProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                addrField.setText(newValue);
                hint_list.setVisible(false);
        });

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hint_list.setVisible(false);
            }
        });
    }

    private boolean hasUser(String addr, String pass) {
        for (User u : all_users) {
            if (u.getEmail_addr().equals(addr) && u.getEmail_pass().equals(pass)) {
                return true;
            }
        }
        String[] str = addr.split("@");
        String server = "pop." + str[1];
        LogUtil.i(server);
        Pop3 pop = new Pop3(server, addr, pass);
        LogUtil.i("status" + pop.getStatus());
        if (pop.getStatus()) {
            User user = new User();
            user.setEmail_addr(addr);
            user.setEmail_pass(pass);
            emailClientDB.insertUser(user);
            return true;
        }
        return false;
    }

}
