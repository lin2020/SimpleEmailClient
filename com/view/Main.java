package com.lin.view;

import java.util.*;
import javax.swing.event.ChangeListener;
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
    private String[] boxName = {"收件箱", "发件箱", "草稿箱", "垃圾箱"};

    private Scene scene;
    // root node
    private VBox vBox;
    // menu node
    private MenuBar menuBar;
    private Menu setupMenu;
    private Menu recvMenu;
    private Menu sendMenu;
    // split node
    private SplitPane splitPane;
    // user pane
    private VBox userBox;
    private TreeView<String> treeView;
    private TreeItem<String> rootNode;
    // email pane
    private VBox emailBox;
    // content pane
    private VBox contentBox;


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
        // * root pane
        vBox = new VBox();

        // ** menu pane
        menuBar = new MenuBar();
        setupMenu = new Menu("设置");
        recvMenu = new Menu("收邮件");
        sendMenu = new Menu("写邮件");
        menuBar.getMenus().addAll(setupMenu, recvMenu, sendMenu);

        // ** split pane
        splitPane = new SplitPane();
        splitPane.setDividerPositions(0.25, 0.5);

        // *** userBox
        userBox = new VBox();
        rootNode = new TreeItem<> ("All Users");
        rootNode.setExpanded(true);
        for (User u : users) {
            TreeItem<String> userNode = new TreeItem<> (u.getEmail_addr());
            for(String s : boxName) {
                TreeItem<String> boxleaf = new TreeItem<> (s);
                userNode.getChildren().add(boxleaf);
            }
            rootNode.getChildren().add(userNode);
        }
        treeView = new TreeView<> (rootNode);
        treeView.setShowRoot(false);
        VBox.setVgrow(treeView, Priority.ALWAYS);
        userBox.getChildren().add(treeView);

        // ***emailBox
        emailBox = new VBox();
        // final ComboBox<String> emailComboBox = new ComboBox<>();
        // emailComboBox.getItems().addAll(
        //    "jacob.smith@example.com",
        //    "isabella.johnson@example.com",
        //    "ethan.williams@example.com",
        //    "emma.jones@example.com",
        //    "michael.brown@example.com"
        // );
        // emailComboBox.setPromptText("Email address");
        // emailComboBox.setEditable(true);
        // emailBox.getChildren().add(emailComboBox);

        contentBox = new VBox();


        splitPane.getItems().addAll(userBox, emailBox, contentBox);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // add menuBar and splitPane to root pane
        vBox.getChildren().addAll(menuBar, splitPane);

        // add root pane to scene
        scene = new Scene(vBox, 900, 600);

        // set and show primary stage
        primaryStage.setTitle("Email Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initEvents(Stage primaryStage) {

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String str = treeView.getSelectionModel().getSelectedItem().getValue();
                String user = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
                int dex = treeView.getSelectionModel().getSelectedIndex();
                LogUtil.i(str + user + dex);
            }
        });

    }

}
