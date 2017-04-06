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

import com.lin.view.*;
import com.lin.util.*;
import com.lin.protocol.*;
import com.lin.model.*;
import com.lin.database.*;


public class Login extends Application {

    private GridPane grid;
    private Text title;
    private Label addr;
    private TextField addrField;
    private Label pswd;
    private TextField pswdField;
    private Button btn;
    private HBox hbBtn;
    private Text hint;
    private Scene scene;
    private ListView<String> hint_list;
    private ObservableList<String> hint_data;
    private static String[] hint_text = {"@qq.com", "@163.com", "@sohu.com", "@foxmail.com"};

    @Override
    public void start(Stage primaryStage) {
        initComponents(primaryStage);
        initEvents(primaryStage);
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

        btn = new Button("Login");
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        hint = new Text();
        grid.add(hint, 1, 6, 2, 1);

        hint_data = FXCollections.observableArrayList();
        for (int i = 0; i < hint_text.length; ++i) {
            hint_data.add(addrField.getText() + hint_text[i]);
        }
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
                if ("".equals(addrField.getText()) || addrField.getText().contains("@")) {
                    hint_list.setVisible(false);
                } else {
                    hint_data.clear();
                    for (int i = 0; i < hint_text.length; ++i) {
                        hint_data.add(addrField.getText() + hint_text[i]);
                    }
                    hint_list.setVisible(true);
                }
            }
        });

        btn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                if ("abc_2020@sohu.com".equals(addrField.getText()) && "abc2020".equals(pswdField.getText())) {
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
        });

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                hint_list.setVisible(false);
            }
        });
    }
}
