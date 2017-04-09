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

import com.lin.database.*;
import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;
import com.lin.view.*;

public class EmailEdit extends Stage {
    // database
    private List<User> users;
    private List<Email> emails;
    private EmailClientDB emailClientDB;
    private String uidl;

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
    private HTMLEditor editor;
    // hbox pane
    private HBox hbox;
    private Button saveButton;
    private Button sendButton;
    private HBox phbox;
    private ProgressBar progressBar;

    public EmailEdit(String uidl) {
        // 通过时间来辨别编辑邮件的唯一性
        this.uidl = uidl;
        initEmailClientDB();
        initComponents();
        initEvents();
    }

    private void initEmailClientDB() {
        emailClientDB = EmailClientDB.getInstance();
        users = emailClientDB.loadUsers();
    }

    private void initComponents() {

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
        editor = new HTMLEditor();

        // ** select button
        hbox = new HBox();
        hbox.setSpacing(5);
        // *** saveButton
        saveButton = new Button("保存");
        hbox.getChildren().add(saveButton);
        // *** sendButton
        sendButton = new Button("发送");
        hbox.getChildren().add(sendButton);
        // *** progressBar
        phbox = new HBox();
        phbox.setAlignment(Pos.CENTER_RIGHT);
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(120);
        progressBar.setProgress(0.5f);
        phbox.getChildren().add(progressBar);
        HBox.setHgrow(phbox, Priority.ALWAYS);
        hbox.getChildren().add(phbox);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(editor);
        vbox.getChildren().add(hbox);

        scene = new Scene(vbox, 650, 500);

        setTitle("编辑邮件");
        setScene(scene);
        show();
    }

    private void initEvents() {

        // 保存邮件
        saveButton.setOnAction((ActionEvent ev)->{
            Email email = new Email(fromCombo.getValue(),
                                    toText.getText(), ccText.getText(), bccText.getText(),
                                    subjectText.getText(), editor.getHtmlText());
            email.setUidl(uidl);
            email.setDate("test");
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

        });

    }

    private boolean isInputCorrect() {
        String to = toText.getText();
        String cc = ccText.getText();
        String bcc = bccText.getText();
        String subject = Text.getText();
        String content = editor.getHtmlText();
        if (to.equals("") && cc.equals("") && bcc.equals("")) {
            LogUtil.i("to,cc,bcc empty");
            return false;
        }
    }

}
