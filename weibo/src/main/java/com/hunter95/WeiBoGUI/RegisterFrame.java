package com.hunter95.WeiBoGUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.hunter95.dao.HBaseDao.ifRepeat;
import static com.hunter95.dao.HBaseDao.userRegister;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        //定义页面
        Container container = this.getContentPane();
        container.setLayout(null);

        JTextField userText = new JTextField("", 20);
        JLabel userLabel = new JLabel("用户名 （用户名不能多于10个字符）");
        userLabel.setBounds(120, 100, 240, 40);
        userText.setBounds(120, 140, 360, 40);
        userText.setDocument(new PlainDocument() {

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = userText.getText();
                if (text.length() + str.length() > 10) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(container, "用户名多于10个字符!");
                    return;
                }

                super.insertString(offs, str, a);
            }
        });
        JPasswordField passwordText = new JPasswordField("", 16);
        JLabel passwordLabel = new JLabel("密码 （密码不能少于6个字符且不多于16个字符）");
        passwordLabel.setBounds(120, 180, 360, 40);
        passwordText.setBounds(120, 220, 360, 40);
        JPasswordField rePasswordText = new JPasswordField("", 16);
        JLabel rePasswordLabel = new JLabel("确认密码");
        rePasswordLabel.setBounds(120, 260, 80, 40);
        rePasswordText.setBounds(120, 300, 360, 40);

        //协议选择框
        JCheckBox jCheckBox = new JCheckBox("已阅读且同意隐私协议");
        jCheckBox.setBounds(240, 350, 400, 20);

        //注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(120, 380, 360, 40);
        //按下注册按钮后的监听事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = "";
                password = String.valueOf(passwordText.getPassword());
                String rePassword = "";
                rePassword = String.valueOf(rePasswordText.getPassword());
                if (userText.getText().equals("")) {
                    JOptionPane.showMessageDialog(
                            container,
                            "请输入用户名！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else if (password.equals("")) {
                    JOptionPane.showMessageDialog(
                            container,
                            "请输入密码！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else if (rePassword.equals("")) {
                    JOptionPane.showMessageDialog(
                            container,
                            "请再次输入密码！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else if (!jCheckBox.isSelected()) {
                    JOptionPane.showMessageDialog(
                            container,
                            "请勾选同意隐私协议！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
                else if (userText.getText().length() == 0){
                    JOptionPane.showMessageDialog(
                            container,
                            "请输入用户名！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
                //判断用户名是否存在
                else {
                    try {
                        if (!ifRepeat(userText.getText())) {
                            JOptionPane.showMessageDialog(
                                    container,
                                    "用户名已存在！",
                                    "消息标题",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }else if(password.length() < 6 || password.length() > 16){
                            JOptionPane.showMessageDialog(
                                    container,
                                    "密码长度不符合要求！",
                                    "消息标题",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                        else if (!password.equals(rePassword)) {
                            JOptionPane.showMessageDialog(
                                    container,
                                    "两次输入密码不一致！",
                                    "消息标题",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    //注册成功后将用户信息写入数据库
                                    container,
                                    "注册成功",
                                    "消息标题",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            userRegister(userText.getText(),password);
                            new LoginFrame();
                            dispose();

                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        //返回登录界面按钮
        JButton returnButton = new JButton("返回登录界面");
        returnButton.setBounds(120, 440, 360, 40);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame();
                dispose();
            }
        });

        container.add(userLabel);
        container.add(userText);
        container.add(passwordLabel);
        container.add(passwordText);
        container.add(rePasswordLabel);
        container.add(rePasswordText);
        container.add(jCheckBox);
        container.add(registerButton);
        container.add(returnButton);


        setBounds(500, 150, 600, 540);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new RegisterFrame();
    }
}

