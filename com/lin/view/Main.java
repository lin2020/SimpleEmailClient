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
import javafx.concurrent.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.lin.database.*;
import com.lin.model.*;
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
    private CheckMenuItem onlyDownloadTop;
    // recv menu
    private Menu recvMenu;
    private MenuItem allMailboxs;
    private MenuItem specificMailbox;
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
    private HBox topBox;
    private Label subjectLabel;
    private HBox attachmentBox;
    private Label attachmentLabel;
    private Separator separator1;
    private GridPane gridPane;
    private Label fromLabel;
    private Label fromText;
    private Label toLabel;
    private Label toText;
    private Label ccLabel;
    private Label ccText;
    private Label dateLabel;
    private Label dateText;
    private Separator separator2;
    private Label contentLabel;
    private Separator separator3;
    private HBox refwBox;
    private Button reButton;
    private Button fwButton;

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
        // *** setupMenu
        setupMenu = new Menu("设置");
        onlyDownloadTop = new CheckMenuItem("只下载邮件头");
        setupMenu.getItems().addAll(onlyDownloadTop);
        // *** recvMenu
        recvMenu = new Menu("收邮件");
        allMailboxs = new MenuItem("收取所有邮箱");
        specificMailbox = new MenuItem("收取特定邮箱");
        recvMenu.getItems().addAll(allMailboxs, specificMailbox);
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
                Image image = new Image(getClass().getResourceAsStream(s + ".png"));
                TreeItem<String> boxleaf = new TreeItem<> (s, new ImageView(image));
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
        contentBox.setSpacing(8);
        contentBox.setPadding(new Insets(8, 8, 8, 8));

        // **** email subject
        topBox = new HBox();
        subjectLabel = new Label("主题");
        subjectLabel.setWrapText(true);
        subjectLabel.setFont(new Font("Arial", 24));
        topBox.getChildren().add(subjectLabel);
        attachmentBox = new HBox();
        attachmentBox.setAlignment(Pos.BOTTOM_RIGHT);
        Image attachmentImage = new Image(getClass().getResourceAsStream("附件.png"));
        attachmentLabel = new Label("", new ImageView(attachmentImage));
        attachmentLabel.setWrapText(true);
        attachmentLabel.setFont(new Font("Arial", 18));
        attachmentBox.getChildren().add(attachmentLabel);
        attachmentBox.setVisible(false);
        HBox.setHgrow(attachmentBox, Priority.ALWAYS);
        topBox.getChildren().add(attachmentBox);
        contentBox.getChildren().add(topBox);

        separator1 = new Separator();
        contentBox.getChildren().add(separator1);

        // **** email info
        gridPane = new GridPane();
        gridPane.setVgap(8);
        fromLabel = new Label("发件人: ");
        fromLabel.setAlignment(Pos.TOP_LEFT);
        fromLabel.setTextFill(Color.DARKGRAY);
        GridPane.setConstraints(fromLabel, 0, 0);
        fromText = new Label("1780615543@qq.com");
        GridPane.setConstraints(fromText, 1, 0);
        toLabel = new Label("收件人: ");
        toLabel.setAlignment(Pos.TOP_LEFT);
        toLabel.setTextFill(Color.DARKGRAY);
        GridPane.setConstraints(toLabel, 0, 1);
        toText = new Label("abc_2020@sohu.com");
        toText.setWrapText(true);
        GridPane.setConstraints(toText, 1, 1);
        ccLabel = new Label("抄送: ");
        ccLabel.setAlignment(Pos.TOP_LEFT);
        ccLabel.setTextFill(Color.DARKGRAY);
        GridPane.setConstraints(ccLabel, 0, 2);
        ccText = new Label("15172323141@163.com");
        ccText.setWrapText(true);
        GridPane.setConstraints(ccText, 1, 2);
        dateLabel = new Label("时间: ");
        dateLabel.setTextFill(Color.DARKGRAY);
        GridPane.setConstraints(dateLabel, 0, 3);
        dateText = new Label("2017年4月17日 19:55");
        GridPane.setConstraints(dateText, 1, 3);
        gridPane.getChildren().addAll(fromLabel, fromText, toLabel, toText, ccLabel, ccText, dateLabel, dateText);
        contentBox.getChildren().add(gridPane);

        separator2 = new Separator();
        contentBox.getChildren().add(separator2);

        // **** email content
        contentLabel = new Label("正文");
        contentLabel.setWrapText(true);
        contentLabel.setPadding(new Insets(0, 0, 8, 8));
        contentBox.getChildren().add(contentLabel);

        separator3 = new Separator();
        contentBox.getChildren().add(separator3);

        // **** re and fw
        refwBox = new HBox();
        refwBox.setSpacing(8);
        refwBox.setAlignment(Pos.BOTTOM_RIGHT);
        reButton = new Button("回复");
        fwButton  = new Button("转发");
        refwBox.getChildren().addAll(reButton, fwButton);
        contentBox.getChildren().add(refwBox);
        contentBox.setVisible(false);

        splitPane.getItems().addAll(userBox, emailBox, contentBox);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // add menuBar and splitPane to root pane
        vBox.getChildren().addAll(menuBar, splitPane);

        // add root pane to scene
        scene = new Scene(vBox, 900, 600);

        // set and show primary stage
        primaryStage.setTitle("Email Client");
        primaryStage.setScene(scene);
        if (users.isEmpty()) {
            new UserEdit(primaryStage, treeView, rootNode);
        } else {
            primaryStage.show();
        }
    }

    private void initEvents(Stage primaryStage) {

        // 下载所有邮箱的邮件
        allMailboxs.setOnAction((ActionEvent t)->{
            LogUtil.i("allMailboxs has been click");

            if(users.isEmpty()) {
                return ;
            }

            ProgressDialog progressDialog = new ProgressDialog("所有邮箱");

            Task<Void> progressTask = new Task<Void>(){

                @Override
                protected void succeeded() {
                    super.succeeded();
                    progressDialog.getCancelButton().setText("结束");
                }

                @Override
                protected Void call() throws Exception {
                    users = emailClientDB.loadUsers();
                    for (User u : users) {
                        PopUtil.retrEmails(u, onlyDownloadTop.isSelected(), new PopCallbackListener() {
                            // 连接服务器
                            @Override
                            public void onConnect() {
                                LogUtil.i("on connect");
                                updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, ProgressIndicator.INDETERMINATE_PROGRESS);
                                updateTitle("正在连接");
                                updateMessage(u.getEmail_addr());
                            }
                            // 检查邮件列表
                            @Override
                            public void onCheck() {
                                LogUtil.i("on check");
                                updateTitle("正在检查");
                                updateMessage(u.getEmail_addr());
                            }
                            // 下载邮件
                            @Override
                            public void onDownLoad(long total_email_size, long download_email_size,
                                                   int total_email_count, int download_email_count,
                                                   long current_email_size, long current_download_size) {
                                LogUtil.i("on download");
                                if (onlyDownloadTop.isSelected()) {
                                    updateProgress(download_email_size, total_email_size);
                                    updateTitle("正在下载");
                                    updateMessage(u.getEmail_addr() + " 待下载: " + total_email_count + "  已下载: " + download_email_count);
                                } else {
                                    updateProgress(download_email_size, total_email_size);
                                    updateTitle("正在下载");
                                    updateMessage(u.getEmail_addr() + " 待下载: " + total_email_count + "  已下载: " + download_email_count);
                                }
                            }
                            // 下载完成
                            @Override
                            public void onFinish() {
                                LogUtil.i("on finish");
                                updateProgress(100, 100);
                                updateTitle("下载完成");
                            }
                            // 下载出错
                            @Override
                            public void onError() {
                                LogUtil.i("on error");
                                updateTitle("下载失败");
                            }
                            // 取消下载
                            @Override
                            public boolean onCancel() {
                                LogUtil.i("on cancel");
                                updateTitle("下载取消");
                                return progressDialog.getCancel();
                            }
                        });
                    }
                    return null;
                }
            };
            progressDialog.getProgress().progressProperty().bind(progressTask.progressProperty());
            progressDialog.getMessageLabel().textProperty().bind(progressTask.messageProperty());
            progressDialog.getTitleLabel().textProperty().bind(progressTask.titleProperty());
            new Thread(progressTask).start();
        });

        // 下载指定邮箱的邮件
        specificMailbox.setOnAction((ActionEvent t)->{
            LogUtil.i("specificMailbox has been click");
            List<User> users = emailClientDB.loadUsers();
            if (!users.isEmpty()) {
                final ContextMenu contextMenu = new ContextMenu();
                for (User u : users) {
                    MenuItem item = new MenuItem(u.getEmail_addr());
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            ProgressDialog progressDialog = new ProgressDialog(u.getEmail_addr());

                            Task<Void> progressTask = new Task<Void>(){

                                @Override
                                protected void succeeded() {
                                    super.succeeded();
                                    progressDialog.getCancelButton().setText("结束");
                                }

                                @Override
                                protected Void call() throws Exception {
                                    PopUtil.retrEmails(u, onlyDownloadTop.isSelected(), new PopCallbackListener() {
                                        // 连接服务器
                                        @Override
                                        public void onConnect() {
                                            LogUtil.i("on connect");
                                            updateTitle("正在连接");
                                            updateMessage("");
                                        }
                                        // 检查邮件列表
                                        @Override
                                        public void onCheck() {
                                            LogUtil.i("on check");
                                            updateTitle("正在检查");
                                            updateMessage("");
                                        }
                                        // 下载邮件
                                        @Override
                                        public void onDownLoad(long total_email_size, long download_email_size,
                                                               int total_email_count, int download_email_count,
                                                               long current_email_size, long current_download_size) {
                                            LogUtil.i("on download");
                                            if (onlyDownloadTop.isSelected()) {
                                                updateProgress(download_email_count, total_email_count);
                                                updateTitle("正在下载");
                                                updateMessage("待下载: " + total_email_count + "  已下载: " + download_email_count);
                                            } else {
                                                updateProgress(download_email_size, total_email_size);
                                                updateTitle("正在下载");
                                                updateMessage("待下载: " + total_email_count + "  已下载: " + download_email_count);
                                            }
                                        }
                                        // 下载完成
                                        @Override
                                        public void onFinish() {
                                            LogUtil.i("on finish");
                                            updateProgress(100, 100);
                                            updateTitle("下载完成");
                                        }
                                        // 下载出错
                                        @Override
                                        public void onError() {
                                            LogUtil.i("on error");
                                            updateTitle("下载失败");
                                        }
                                        // 取消下载
                                        @Override
                                        public boolean onCancel() {
                                            LogUtil.i("on cancel");
                                            updateTitle("下载取消");
                                            return progressDialog.getCancel();
                                        }
                                    });
                                    return null;
                                }
                            };
                            progressDialog.getProgress().progressProperty().bind(progressTask.progressProperty());
                            progressDialog.getMessageLabel().textProperty().bind(progressTask.messageProperty());
                            progressDialog.getTitleLabel().textProperty().bind(progressTask.titleProperty());
                            new Thread(progressTask).start();
                        }
                    });
                    contextMenu.getItems().add(item);
                }
                contextMenu.show(menuBar, primaryStage.getX() + 60, primaryStage.getY() + menuBar.getScene().getY() + menuBar.localToScene(0, 0).getX() + menuBar.getHeight());
                contextMenu.setAutoHide(true);
            }
        });

        // 编写txt邮件
        commonMenuItem.setOnAction((ActionEvent t)->{
            LogUtil.i("commonMenuItem has been click");
            if(users.isEmpty()) {
                return ;
            }
            SimpleDateFormat df = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);//设置日期格式
            LogUtil.i(df.format(new Date()));// new Date()为获取当前系统时间
            new EmailEdit(df.format(new Date()).toString(), "txt");
        });

        // 编写html邮件
        htmlMenuItem.setOnAction((ActionEvent t)->{
            LogUtil.i("htmlMenuItem has been click");
            if(users.isEmpty()) {
                return ;
            }
            SimpleDateFormat df = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);//设置日期格式
            LogUtil.i(df.format(new Date()));// new Date()为获取当前系统时间
            new EmailEdit(df.format(new Date()).toString(), "html");
        });

        // 显示邮箱以及配置邮箱
        treeView.setOnMouseClicked((MouseEvent me)->{
            if (me.getButton() == MouseButton.SECONDARY) {
                LogUtil.i("Mouse Click");
                final ContextMenu contextMenu = new ContextMenu();
                MenuItem item1 = new MenuItem("删除邮箱");
                item1.setOnAction((ActionEvent ae)->{
                    if (treeView.getSelectionModel().getSelectedItem() == null) {
                        return;
                    }
                    String email_regex = "\\w+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
                    String treeItemValue = treeView.getSelectionModel().getSelectedItem().getValue();
                    Pattern p = Pattern.compile(email_regex);
                    Matcher m = p.matcher(treeItemValue);
                    if (m == null) {
                        return ;
                    }
                    if (!m.find()) {
                        LogUtil.i("Click box");
                        treeItemValue = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
                    }
                    users = emailClientDB.loadUsers();
                    User user = null;
                    for (User u : users) {
                        if (u.getEmail_addr().equals(treeItemValue)) {
                            user = u;
                            break;
                        }
                    }
                    if (user == null) {
                        LogUtil.i("can't find user");
                    } else {
                        emailClientDB.deleteUser(user);
                        users = emailClientDB.loadUsers();
                        rootNode = new TreeItem<> ("user@example.com");
                        rootNode.setExpanded(true);
                        for (User u : users) {
                            TreeItem<String> userNode = new TreeItem<> (u.getEmail_addr());
                            for(String s : boxName) {
                                Image image = new Image(getClass().getResourceAsStream(s + ".png"));
                                TreeItem<String> boxleaf = new TreeItem<> (s, new ImageView(image));
                                userNode.getChildren().add(boxleaf);
                            }
                            rootNode.getChildren().add(userNode);
                        }
                        treeView.setRoot(rootNode);
                    }
                });
                MenuItem item2 = new MenuItem("添加邮箱");
                item2.setOnAction((ActionEvent ae)->{
                    new UserEdit(primaryStage, treeView, rootNode);
                });
                contextMenu.getItems().addAll(item1, item2);
                contextMenu.show(menuBar, me.getScreenX(), me.getScreenY());
                contextMenu.setAutoHide(true);
            } else {
                if (treeView.getSelectionModel().getSelectedItem() == null) {
                    return ;
                }
                emails = new ArrayList<Email>();
                String email_regex = "\\w+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
                String treeItemValue = treeView.getSelectionModel().getSelectedItem().getValue();
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(treeItemValue);
                if (m == null) {
                    return ;
                }
                if (!m.find()) {
                    LogUtil.i("Click box");
                    User user = null;
                    String treeNodeValue = treeView.getSelectionModel().getSelectedItem().getParent().getValue();
                    users = emailClientDB.loadUsers();
                    for (User u : users) {
                        if (u.getEmail_addr().equals(treeNodeValue)) {
                            user = u;
                            break;
                        }
                    }
                    if (user == null) {
                        LogUtil.i("can't find user");
                    } else {
                        LogUtil.i("find user");
                        emails = emailClientDB.loadEmails(user.getId(), treeItemValue);
                    }
                }
                Collections.sort(emails, new Comparator<Email>() {
                    public int compare(Email email0, Email email1) {
                        SimpleDateFormat df_old = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);  //原来日期格式
                        SimpleDateFormat df_new = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);  //新的日期格式
                        String d0 = null;
                        String d1 = null;
                        try {
                            d0 = df_new.format(df_old.parse(email0.getDate()));
                            d1 = df_new.format(df_old.parse(email1.getDate()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return d1.compareTo(d0);
                    }
                });
                listData = FXCollections.observableArrayList(emails);
                listView.setItems(listData);
                listView.setCellFactory((ListView<Email> l) -> new MyListCell());
                contentBox.setVisible(false);
            }
        });

        // 显示邮件
        listView.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends Email> ov, Email old_val, Email new_val) -> {
            LogUtil.i("happen");
            Email email = listView.getSelectionModel().getSelectedItem();
            if (email == null) {
                return ;
            }
            if (email.getContent().equals("onlyDownloadTop")) {
                for (User user : users) {
                    if (user.getId() == email.getUserid()) {
                        LogUtil.i("download email detail");
                        PopUtil.sendRetrRequest(user, email);
                    }
                }
            }
            subjectLabel.setText(email.getSubject());
            fromText.setText(email.getFrom());
            if (email.getAttachment_num() != 0) {
                attachmentLabel.setText(email.getAttachmentString());
                attachmentBox.setVisible(true);
            } else {
                attachmentLabel.setText("");
                attachmentBox.setVisible(false);
            }
            String to = "";
            for (String s : email.getTo_list()) {
                to += s + ";";
            }
            toText.setText(to);
            String cc = "";
            for (String s : email.getCc_list()) {
                cc += s + ";";
            }
            ccText.setText(cc);
            String date = "";
            SimpleDateFormat df_old = new SimpleDateFormat("EE, dd MMM yyyy kk:mm:ss Z", Locale.US);
            SimpleDateFormat df_new = new SimpleDateFormat("yyyy年MM月dd日 kk:mm", Locale.US);
            try {
                date = df_new.format(df_old.parse(email.getDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dateText.setText(date);
            contentLabel.setText(email.getContent());
            contentBox.setVisible(true);
        });

        // 配置邮件
        listView.setOnMouseClicked((MouseEvent me)->{
            if (me.getButton() == MouseButton.SECONDARY) {
                LogUtil.i("Mouse Click");
                final ContextMenu contextMenu = new ContextMenu();
                MenuItem item1 = new MenuItem("删除");
                item1.setOnAction((ActionEvent ae)->{
                    Email email = listView.getSelectionModel().getSelectedItem();
                    listData.remove(email);
                    listView.setItems(listData);
                    listView.setCellFactory((ListView<Email> l) -> new MyListCell());
                    emailClientDB.deleteEmail(email);
                    LogUtil.i("删除");
                    contentBox.setVisible(false);
                });
                MenuItem item2 = new MenuItem("彻底删除");
                item2.setOnAction((ActionEvent ae)->{
                    Email email = listView.getSelectionModel().getSelectedItem();
                    for (User user : users) {
                        if (user.getId() == email.getUserid()) {
                            PopUtil.sendDeleRequest(user, email);
                        }
                    }
                    listData.remove(email);
                    listView.setItems(listData);
                    listView.setCellFactory((ListView<Email> l) -> new MyListCell());
                    emailClientDB.deleteEmail(email);
                    LogUtil.i("彻底删除");
                    contentBox.setVisible(false);
                });
                MenuItem item3 = new MenuItem("移到垃圾箱");
                item3.setOnAction((ActionEvent ae)->{
                    Email email = listView.getSelectionModel().getSelectedItem();
                    listData.remove(email);
                    listView.setItems(listData);
                    listView.setCellFactory((ListView<Email> l) -> new MyListCell());
                    emailClientDB.deleteEmail(email);
                    email.setInbox("垃圾箱");
                    emailClientDB.insertEmail(email);
                    LogUtil.i("移到垃圾箱");
                    contentBox.setVisible(false);
                });
                contextMenu.getItems().addAll(item1, item2, item3);
                contextMenu.show(menuBar, me.getScreenX(), me.getScreenY());
                contextMenu.setAutoHide(true);
            }
        });

        // 回复邮件
        reButton.setOnAction((ActionEvent ae)->{
            SimpleDateFormat df = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);//设置日期格式
            LogUtil.i(df.format(new Date()));// new Date()为获取当前系统时间
            Email email = listView.getSelectionModel().getSelectedItem();
            new EmailEdit(df.format(new Date()).toString(), email, "回复: ", "txt");
        });

        // 转发邮件
        fwButton.setOnAction((ActionEvent ae)->{
            SimpleDateFormat df = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);//设置日期格式
            LogUtil.i(df.format(new Date()));// new Date()为获取当前系统时间
            Email email = listView.getSelectionModel().getSelectedItem();
            new EmailEdit(df.format(new Date()).toString(), email, "转发: ", "txt");
        });

    }

    // 定义邮件列表的显示格式
    class MyListCell extends ListCell<Email> {
       @Override
       public void updateItem(Email item, boolean empty) {
           super.updateItem(item, empty);
           if (item != null) {
               SimpleDateFormat df_old = new SimpleDateFormat("EE, dd MMM yyyy hh:mm:ss Z", Locale.US);  //原来日期格式
               SimpleDateFormat df_new = new SimpleDateFormat("MM-dd", Locale.US);  //新的日期格式

               String from = item.getFrom();
               String date = item.getDate();
               String subject = item.getSubject();

               from = from.split("@")[0];

               try {
                   date = df_new.format(df_old.parse(item.getDate()));
               } catch (Exception e) {
                   e.printStackTrace();
               }

               VBox root = new VBox();
               root.setSpacing(4);
               HBox hbox = new HBox();
               Label fromLabel = new Label(from);
               if (item.getAttachment_num() != 0) {
                   Image image = new Image(getClass().getResourceAsStream("附件_1.png"));
                   fromLabel.setGraphic(new ImageView(image));
                   fromLabel.setContentDisplay(ContentDisplay.RIGHT);
               }
               // fromLabel.setFont(new Font("System Bold", 16));
               fromLabel.setFont(new Font("Arial Bold", 16));
               fromLabel.setPrefWidth(140);
               Label dateLabel = new Label(date);
               dateLabel.setFont(new Font(16));
               dateLabel.setPrefWidth(80);
               hbox.getChildren().addAll(fromLabel, dateLabel);
               Label subjectLabel = new Label(subject);
               subjectLabel.setFont(new Font(14));
               root.getChildren().add(hbox);
               root.getChildren().add(subjectLabel);
               setGraphic(root);
           }
       }
    }

}
