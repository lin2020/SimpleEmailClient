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
import javafx.scene.Parent;
import javafx.fxml.*;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;
import com.lin.view.*;

public class Main extends Application {

    // database
    private List<User> users;
    private EmailClientDB emailClientDB;

    private Scene scene;
    // root node
    private VBox vBox;
    // menu node
    private MenuBar menuBar;
    private Menu setupMenu;
    private Menu recvMenu;
    private Menu sendMenu;
    // content node
    private SplitPane splitPane;
    private AnchorPane userPane;
    private Button btn;
    private AnchorPane emailPane;
    private AnchorPane contentPane;


    @Override
    public void start(Stage primaryStage) {
        initEmailClientDB(primaryStage);
        initComponents(primaryStage);
        initEvents(primaryStage);
    }

    private void initEmailClientDB(Stage primaryStage) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        emailClientDB = EmailClientDB.getInstance();
        users = emailClientDB.loadUsers();
    }

    private void initComponents(Stage primaryStage) {
        vBox = new VBox();

        menuBar = new MenuBar();
        setupMenu = new Menu("设置");
        recvMenu = new Menu("收邮件");
        sendMenu = new Menu("写邮件");
        menuBar.getMenus().addAll(setupMenu, recvMenu, sendMenu);

        splitPane = new SplitPane();
        splitPane.setDividerPositions(0.25, 0.5);
        userPane = new AnchorPane();
        emailPane = new AnchorPane();
        contentPane = new AnchorPane();
        splitPane.getItems().addAll(userPane, emailPane, contentPane);

        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(splitPane);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        scene = new Scene(vBox, 900, 600);

        primaryStage.setTitle("Email Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initEvents(Stage primaryStage) {

    }

}
