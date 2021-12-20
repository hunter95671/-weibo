package com.hunter95.WeiBoGUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.hunter95.dao.HBaseDao.pswIfRight;

public class LoginFrame extends JFrame {
    //用户名
    public static String userName = "";

    public LoginFrame() {
        //登录界面

        Container container = this.getContentPane();
        container.setLayout(null);

        //上部分面板
        ImageIcon backPicture = new ImageIcon("src/main/QQ截图20211220103816.png");
        JLabel upLabel = new JLabel(backPicture);
        upLabel.setBounds(0,0,600,200);
        upLabel.setBackground(new Color(0,0,0));

        //下部分面板
        JLabel downLabel = new JLabel();
        downLabel.setBounds(0,200,600,300);

        //用户名文本框
        JTextField userText = new JTextField("",20);
        userText.setBounds(140,0,360,40);
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
        JLabel userLabel = new JLabel("用户名");
        userLabel.setBounds(100,0,40,40);


        //登录密码框
        JPasswordField passwordText = new JPasswordField("",16);
        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setBounds(100,50,40,40);
        passwordText.setBounds(140,50,360,40);

        //协议选择框
        JCheckBox jCheckBox = new JCheckBox("已阅读且同意用户协议");
        jCheckBox.setBounds(240,100,400,20);

        //登录按钮
        JButton loginButton = new JButton("登录");
        //点击登录按钮的监听事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = "";
                password = String.valueOf(passwordText.getPassword());
                if (userText.getText().equals("")){
                    JOptionPane.showMessageDialog(
                            container,
                            "请输入用户名！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else  if (password.equals("")){
                    JOptionPane.showMessageDialog(
                            container,
                            "请输入密码名！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else if (!jCheckBox.isSelected()){
                    JOptionPane.showMessageDialog(
                            container,
                            "请勾选同意用户协议！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }else {
                    try {
                        if(pswIfRight(userText.getText(),password)){
                            userName = userText.getText();
                            JOptionPane.showMessageDialog(
                                    container,
                                    "登录成功",
                                    "消息标题",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            new HomepageFrame();
                            dispose();
                        }else {
                            JOptionPane.showMessageDialog(
                                    container,
                                    "用户名或密码错误！",
                                    "消息标题",
                                    JOptionPane.WARNING_MESSAGE
                            );

                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        loginButton.setBounds(160,130,300,40);

        //注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.setBounds(160,180,300,40);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame();
                dispose();
            }
        });

        //添加至面板
        downLabel.add(userText);
        downLabel.add(passwordText);
        downLabel.add(jCheckBox);
        downLabel.add(userLabel);
        downLabel.add(passwordLabel);
        downLabel.add(loginButton);
        downLabel.add(registerButton);

        //添加至界面
        container.add(upLabel);
        container.add(downLabel);

        //界面参数
        setBounds(500,150,600,540);
        setBackground(Color.black);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
