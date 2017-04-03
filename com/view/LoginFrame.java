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
    private JTextField textField;
    private JTextField textField2;

    public LoginFrame() {
        initComponents();
        initEvents();
    }

    private void initComponents() {

        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(null);
        // mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Font font = new Font("宋体", Font.PLAIN, 14);
        textField = new JTextField();
        textField.setBounds(100, 100, 300, 24);
        // textField.setFont(font);

        textField2 = new JTextField();
        textField2.setBounds(100, 140, 300, 24);

        mainPanel.add(textField);
        mainPanel.add(textField2);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    private void initEvents() {

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                LogUtil.i("gain");
                // textField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.red,Color.blue));
            }
            @Override
            public void focusLost(FocusEvent e) {
                LogUtil.i("lost");
                textField.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 51)));
            }
        });

        // textField.addMouseListener(new MouseListener() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         LogUtil.i("Clicked");
        //     }
        //     @Override
        //     public void mousePressed(MouseEvent e) {
        //         LogUtil.i("Pressed");
        //     }
        //     @Override
        //     public void mouseReleased(MouseEvent e) {
        //         LogUtil.i("Released");
        //     }
        //     @Override
        //     public void mouseEntered(MouseEvent e) {
        //         LogUtil.i("Entered");
        //     }
        //     @Override
        //     public void mouseExited(MouseEvent e) {
        //         LogUtil.i("Exited");
        //     }
        // });

        textField.getDocument().addDocumentListener(new DocumentListener() {
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
