package com.lin.view;

import java.util.*;
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
import javafx.scene.web.HTMLEditor;
import java.util.Date;
import java.text.SimpleDateFormat;
import javafx.concurrent.*;
import java.io.File;

import com.lin.database.*;
import com.lin.model.*;
import com.lin.util.*;
import com.lin.view.*;

public class EmailEdit extends Stage {
    // database
    private List<User> users;
    private List<Email> emails;
    private EmailClientDB emailClientDB;
    private String uidl;
    private String kind;
    private List<File> attachFile;
    private boolean hasAttach;

    // scene
    private Scene scene;
    // root pane
    private VBox vbox;
    // grid pane
    private GridPane grid;
    private Label toLabel;
    private TextField toText;
    private Label fromLabel;
    private ComboBox<String> fromCombo;
    private Label ccLabel;
    private TextField ccText;
    private Label bccLabel;
    private TextField bccText;
    private Label subjectLabel;
    private TextField subjectText;
    // html editor
    private HTMLEditor htmlEditor;
    // txt editor
    private TextArea txtEditor;
    // hbox pane
    private HBox hbox;
    private Button sendButton;
    private Button saveButton;
    private Button attachButton;
    private Label attachLabel;
    private HBox phbox;
    private Label progressLabel;
    private ProgressBar progressBar;

    public EmailEdit(String uidl, String kind) {
        // 通过时间来辨别编辑邮件的唯一性
        this.uidl = uidl;
        // 通过输入的类型来判断要编辑的邮件的类型
        this.kind = kind;
        initEmailClientDB();
        initComponents();
        initEvents();
    }

    public EmailEdit(String uidl, Email email, String type, String kind) {
        this.uidl = uidl;
        this.kind = kind;
        initEmailClientDB();
        initComponents();
        initEvents();
        if (type.equals("回复: ")) {
            toText.setText(email.getFrom());
        }
        subjectText.setText(type + email.getSubject());
        if (kind.equals("txt")) {
            txtEditor.setText(getOriginalText(email));
        } else if (kind.equals("html")) {
            htmlEditor.setHtmlText(getOriginalText(email));
        }
    }

    private void initEmailClientDB() {
        emailClientDB = EmailClientDB.getInstance();
        users = emailClientDB.loadUsers();
    }

    private void initComponents() {

        attachFile = new ArrayList<>();
        hasAttach = false;

        // * root pane
        vbox = new VBox();
        vbox.setPadding(new Insets(8, 8, 8, 8));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.BOTTOM_LEFT);

        // ** grid pane
        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);
        // *** to
        toLabel = new Label("收件人:");
        toLabel.setPrefWidth(50);
        GridPane.setConstraints(toLabel, 0, 0);
        grid.getChildren().add(toLabel);
        toText = new TextField();
        toText.setPrefWidth(250);
        GridPane.setConstraints(toText, 1, 0);
        grid.getChildren().add(toText);

        // *** from
        fromLabel = new Label("发件人:");
        fromLabel.setPrefWidth(50);
        GridPane.setConstraints(fromLabel, 2, 0);
        grid.getChildren().add(fromLabel);
        fromCombo = new ComboBox<>();
        for (User u : users) {
            fromCombo.getItems().add(u.getEmail_addr());
        }
        fromCombo.setValue(users.get(0).getEmail_addr());
        fromCombo.setPrefWidth(250);
        GridPane.setConstraints(fromCombo, 3, 0);
        grid.getChildren().add(fromCombo);

        // *** cc
        ccLabel = new Label("抄送:");
        ccLabel.setPrefWidth(50);
        GridPane.setConstraints(ccLabel, 0, 1);
        grid.getChildren().add(ccLabel);
        ccText = new TextField();
        ccText.setPrefWidth(250);
        GridPane.setConstraints(ccText, 1, 1);
        grid.getChildren().add(ccText);

        // *** bcc
        bccLabel = new Label("密送:");
        bccLabel.setPrefWidth(50);
        GridPane.setConstraints(bccLabel, 2, 1);
        grid.getChildren().add(bccLabel);
        bccText = new TextField();
        bccText.setPrefWidth(250);
        GridPane.setConstraints(bccText, 3, 1);
        grid.getChildren().add(bccText);

        // *** subject
        subjectLabel = new Label("主题:");
        subjectLabel.setPrefWidth(50);
        GridPane.setConstraints(subjectLabel, 0, 2);
        grid.getChildren().add(subjectLabel);
        subjectText = new TextField();
        subjectText.setPrefWidth(450);
        GridPane.setConstraints(subjectText, 1, 2, 3, 1);
        grid.getChildren().add(subjectText);

        // ** html editor
        htmlEditor = new HTMLEditor();
        // ** txt editor
        txtEditor = new TextArea();
        txtEditor.setPrefHeight(400);

        // ** select button
        hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        // *** sendButton
        sendButton = new Button("发送");
        hbox.getChildren().add(sendButton);
        // *** saveButton
        saveButton = new Button("保存");
        hbox.getChildren().add(saveButton);
        // *** attachButton
        attachButton = new Button("附件");
        hbox.getChildren().add(attachButton);
        // *** attachLabel
        attachLabel = new Label("附件: ");
        attachLabel.setAlignment(Pos.CENTER);
        hbox.getChildren().add(attachLabel);
        attachLabel.setVisible(false);
        // *** progressBar
        phbox = new HBox();
        phbox.setSpacing(8);
        phbox.setAlignment(Pos.CENTER_RIGHT);
        progressLabel = new Label("正在发送");
        phbox.getChildren().add(progressLabel);
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(120);
        progressBar.setProgress(0.5f);
        phbox.getChildren().add(progressBar);
        phbox.setVisible(false);
        HBox.setHgrow(phbox, Priority.ALWAYS);
        hbox.getChildren().add(phbox);

        vbox.getChildren().add(grid);
        if (kind.equals("txt")) {
            vbox.getChildren().add(txtEditor);
        } else if (kind.equals("html")) {
            vbox.getChildren().add(htmlEditor);
        }
        vbox.getChildren().add(hbox);

        scene = new Scene(vbox, 650, 500);

        setTitle("编辑邮件");
        setScene(scene);
        show();
    }

    private void initEvents() {

        // 添加附件
        attachButton.setOnAction((ActionEvent ae)->{
            final FileChooser fileChooser = new FileChooser();
            final File file = fileChooser.showOpenDialog(this);
            if (file != null) {
                LogUtil.i("attachFile");
                attachFile.add(file);
                attachLabel.setText(attachLabel.getText() + file.getName() + "; ");
                attachLabel.setVisible(true);
                hasAttach = true;
            }
        });

        attachLabel.setOnMouseClicked((MouseEvent me)->{
            hasAttach = false;
            attachFile.clear();
            attachLabel.setText("附件 ");
            attachLabel.setVisible(false);
        });

        // 保存邮件
        saveButton.setOnAction((ActionEvent ev)->{
            String content = "";
            if (kind.equals("txt")) {
                content = txtEditor.getText();
            } else if (kind.equals("html")) {
                content = htmlEditor.getHtmlText();
            }
            Email email = new Email(fromCombo.getValue(), toText.getText(), ccText.getText(), bccText.getText(), subjectText.getText(), content);
            email.setUidl(uidl);
            SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss Z", Locale.US);
            String date = df.format(new Date());
            email.setDate(date);
            for (User u : users) {
                if (u.getEmail_addr().equals(fromCombo.getValue())) {
                    email.setUserid(u.getId());
                }
            }
            email.setInbox("草稿箱");
            emails = emailClientDB.queryEmails(uidl);
            if (emails.isEmpty()) {
                LogUtil.i("Empty");
            } else {
                for (Email e : emails) {
                    emailClientDB.deleteEmail(e);
                    LogUtil.i("delete");
                }
            }
            emailClientDB.insertEmail(email);
        });

        // 发送邮件
        sendButton.setOnAction((ActionEvent ev)->{
            if (isInputCorrect()) {
                String content = "";
                if (kind.equals("txt")) {
                    content = txtEditor.getText();
                } else if (kind.equals("html")) {
                    content = htmlEditor.getHtmlText();
                }
                LogUtil.i(subjectText.getText());
                Email email = new Email(fromCombo.getValue(), toText.getText(), ccText.getText(), bccText.getText(), subjectText.getText(), content);
                email.setUidl(uidl);
                SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss Z", Locale.US);
                String date = df.format(new Date());
                email.setDate(date);
                for (User u : users) {
                    if (u.getEmail_addr().equals(fromCombo.getValue())) {
                        email.setUserid(u.getId());
                    }
                }
                email.setInbox("发件箱");
                String html = content;

                phbox.setVisible(true);

                Task<Void> sendEmailTask = new Task<Void>() {

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                    }

                    @Override
                    protected Void call() throws Exception {
                        SmtpUtil.sendEmail(email, kind, hasAttach, attachFile, new SmtpCallbackListener() {
                            // 开始连接
                            public void onConnect() {
                                LogUtil.i("on connect");
                                updateMessage("正在连接");
                                updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, ProgressIndicator.INDETERMINATE_PROGRESS);
                            }
                            // 认证登录
                            public void onCheck() {
                                LogUtil.i("on check");
                                updateMessage("正在验证");
                            }
                            // 正在发送
                            public void onSend(long send_email_size, long total_email_size) {
                                updateMessage("正在发送");
                                updateProgress(send_email_size, total_email_size);
                            }
                            // 发送成功
                            public void onFinish() {
                                LogUtil.i("on finish");
                                updateMessage("发送成功");
                                emailClientDB.insertEmail(email);
                            }
                            // 发送失败
                            public void onError() {
                                LogUtil.i("on error");
                                updateMessage("发送失败");
                            }
                        });
                        return null;
                    }
                };
                progressBar.progressProperty().bind(sendEmailTask.progressProperty());
                progressLabel.textProperty().bind(sendEmailTask.messageProperty());
                new Thread(sendEmailTask).start();
            }
        });

    }

    private boolean isInputCorrect() {
        String to = toText.getText();
        String cc = ccText.getText();
        String bcc = bccText.getText();
        if (to.equals("") && cc.equals("") && bcc.equals("")) {
            LogUtil.i("to,cc,bcc empty");
            return false;
        }
        return true;
    }

    private String getOriginalText(Email email) {
        String txt = "\n\n\n" + "-- 原始邮件 --" + "\n";
        txt += "--------------------------------------\n";
        txt += "From: ";
        txt += email.getFrom() + "\n";
        txt += "Date: ";
        txt += email.getDate() + "\n";
        txt += "To: ";
        txt += email.getToString() + "\n";
        txt += "Cc: ";
        txt += email.getCcString() + "\n";
        txt += "Subject: ";
        txt += email.getSubject() + "\n";
        txt += "--------------------------------------\n";
        txt += "\n";
        txt += email.getContent();
        return txt;
    }
}
