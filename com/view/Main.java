package com.lin.view;

import java.util.*;
import java.text.SimpleDateFormat;
import java.awt.Rectangle;
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
import java.util.regex.*;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;
import com.lin.view.*;

public class Main extends Application {

    // database
    private List<User> users;
    private List<Email> emails;
    private EmailClientDB emailClientDB;
    private String[] boxName = {"收件箱", "发件箱", "草稿箱", "垃圾箱"};

    private Scene scene;
    // root node
    private VBox vBox;
    // menu node
    private MenuBar menuBar;
    private Menu setupMenu;
    private Menu recvMenu;
    // send menu
    private Menu sendMenu;
    private MenuItem commonMenuItem;
    private MenuItem htmlMenuItem;
    // split node
    private SplitPane splitPane;
    // user pane
    private VBox userBox;
    private TreeView<String> treeView;
    private TreeItem<String> rootNode;
    // email pane
    private VBox emailBox;
    private ListView<Email> listView;
    private ObservableList<Email> listData;
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
        emails = new ArrayList<> ();
        listData = FXCollections.observableArrayList(emails);
    }

    private void initComponents(Stage primaryStage) {
        // * root pane
        vBox = new VBox();


        // ** menu pane
        menuBar = new MenuBar();
        setupMenu = new Menu("设置");
        recvMenu = new Menu("收邮件");

        // *** sendMenu
        sendMenu = new Menu("写邮件");
        commonMenuItem = new MenuItem("普通邮件");
        htmlMenuItem = new MenuItem("HTML邮件");
        sendMenu.getItems().addAll(commonMenuItem, htmlMenuItem);
        menuBar.getMenus().addAll(setupMenu, recvMenu, sendMenu);


        // ** split pane
        splitPane = new SplitPane();
        splitPane.setDividerPositions(0.25, 0.5);

        // *** userBox
        userBox = new VBox();
        rootNode = new TreeItem<> ("user@example.com");
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
        listView = new ListView<> ();
        listView.setItems(listData);
        listView.setCellFactory((ListView<Email> l) -> new MyListCell());
        VBox.setVgrow(listView, Priority.ALWAYS);
        emailBox.getChildren().add(listView);

        // ***contentBox
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

        // sendMenu.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent e) {
        //         LogUtil.i("sendMenu has been click");
        //         (new EmailEdit()).show();
        //     }
        // });


        htmlMenuItem.setOnAction((ActionEvent t)->{
            LogUtil.i("htmlMenuItem has been click");
            SimpleDateFormat df = new SimpleDateFormat("EE, M MMM yyyy hh:mm:ss Z", Locale.US);//设置日期格式
            LogUtil.i(df.format(new Date()));// new Date()为获取当前系统时间
            new EmailEdit(df.format(new Date()).toString());
        });

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // String str = treeView.getSelectionModel().getSelectedItem().getValue();
                // String user = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
                // int dex = treeView.getSelectionModel().getSelectedIndex();
                // LogUtil.i(str + user + dex);
                emails = new ArrayList<Email>();
                String email_regex = "\\w+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
                String treeItemValue = treeView.getSelectionModel().getSelectedItem().getValue();
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(treeItemValue);
                if (!m.find()) {
                    LogUtil.i("Click box");
                    User user = null;
                    String treeNodeValue = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
                    for (User u : users) {
                        if (u.getEmail_addr().equals(treeNodeValue)) {
                            user = u;
                            break;
                        }
                    }
                    if (user == null) {
                        LogUtil.i("can't find user");
                    } else {
                        emails = emailClientDB.loadEmails(user.getId(), treeItemValue);
                    }
                }
                listData = FXCollections.observableArrayList(emails);
                listView.setItems(listData);
                listView.setCellFactory((ListView<Email> l) -> new MyListCell());
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends Email> ov, Email old_val, Email new_val) -> {
            LogUtil.i("happen");
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    LogUtil.i("Mouse Click");
                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem item1 = new MenuItem("删除");
                    item1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            listData.remove(listView.getSelectionModel().getSelectedItem());
                            listView.setItems(listData);
                            listView.setCellFactory((ListView<Email> l) -> new MyListCell());
                            LogUtil.i("删除");
                        }
                    });
                    MenuItem item2 = new MenuItem("移到垃圾箱");
                    item2.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            LogUtil.i("移到垃圾箱");
                        }
                    });
                    contextMenu.getItems().addAll(item1, item2);
                    contextMenu.show(menuBar, e.getScreenX(), e.getScreenY());
                    contextMenu.setAutoHide(true);
                }
            }
        });

    }

    class MyListCell extends ListCell<Email> {
       @Override
       public void updateItem(Email item, boolean empty) {
           super.updateItem(item, empty);
           if (item != null) {
               Text text = new Text(item.getSubject());
               // Button btn = new Button("test");
               setGraphic(text);
           }
       }
   }

}
