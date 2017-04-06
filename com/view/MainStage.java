package com.lin.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;
import com.lin.view.*;

public class MainStage extends Stage {

    private User user;
    private EmailClientDB emailClientDB;

    private Button btn;
    private StackPane root;
    private Scene scene;

    public MainStage(User user) {
        this.user = user;
        this.emailClientDB = EmailClientDB.getInstance();
        initComponents();
        initEvents();
    }

    private void initComponents() {
        btn = new Button();
        btn.setText("say'hello stage'");

        root = new StackPane();
        root.getChildren().add(btn);

        scene = new Scene(root, 300, 250);

        this.setTitle("MainStage");
        this.setScene(scene);
        this.show();
    }

    private void initEvents() {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                LogUtil.i("hello stage");
            }
        });
    }
}
