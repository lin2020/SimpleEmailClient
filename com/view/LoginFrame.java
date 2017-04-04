package com.lin.view;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;

import com.lin.util.*;

public class LoginFrame {

    private JFrame frame;
    private JTextField addr_text;
    private JTextField pass_text;
    private JButton login_button;
    private String addr_hint = "email address";
    private String pass_hint = "email password";
    private String login_hint = CoderUtil.toUTF8("登录");

    public LoginFrame() {
        initComponents();
        initEvents();
    }

    private void initComponents() {
        frame = new JFrame(login_hint);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(null);

        addr_text = new JTextField(addr_hint);
        addr_text.setBounds(100, 75, 200, 24);
        addr_text.setForeground(Color.LIGHT_GRAY);
        addr_text.setMargin(new Insets(3, 3, 3, 3));

        pass_text = new JTextField(pass_hint);
        pass_text.setBounds(100, 110, 200, 24);
        pass_text.setForeground(Color.LIGHT_GRAY);
        pass_text.setMargin(new Insets(3, 3, 3, 3));

        login_button = new JButton(login_hint);
        login_button.setBounds(100, 170, 200, 24);

        mainPanel.add(addr_text);
        mainPanel.add(pass_text);
        mainPanel.add(login_button);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initEvents() {

        addr_text.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                LogUtil.i("addr gain");
                addr_text.setForeground(Color.blue);
                if (addr_text.getText().equals(addr_hint)) {
                    addr_text.setText(null);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                LogUtil.i("addr lost");
                if (addr_text.getText().trim().isEmpty()) {
                    addr_text.setText(addr_hint);
                    addr_text.setForeground(Color.LIGHT_GRAY);
                }
            }
        });

        pass_text.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                LogUtil.i("pass gain");
                pass_text.setForeground(Color.blue);
                if (pass_text.getText().equals(pass_hint)) {
                    pass_text.setText(null);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (pass_text.getText().trim().isEmpty()) {
                    pass_text.setText(pass_hint);
                    pass_text.setForeground(Color.LIGHT_GRAY);
                }
            }

        });

        addr_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                LogUtil.i("insert");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                LogUtil.i("remove");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                LogUtil.i("change");
            }
        });
    }
}
