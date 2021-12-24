package com.hunter95.WeiBoGUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.hunter95.dao.HBaseDao.publishWeiBo;

public class WriteFrame extends JFrame {

    public WriteFrame(){
        Container container = this.getContentPane();
        container.setLayout(null);

        //container.setBackground(new Color(155, 172, 151));

        JLabel welcomeLabel = new JLabel("                           微微博，记录你的生活");
        welcomeLabel.setBounds(0,0,300,40);

        // 创建一个 5 行 10 列的文本区域
        JTextArea textArea = new JTextArea();
        textArea.setBounds(45,50,200,200);
        //textArea.setBackground(new Color(155, 172, 151));
        // 设置自动换行
        textArea.setLineWrap(true);
        textArea.setFont(new Font("宋体", 1, 20));
        textArea.setDocument(new PlainDocument() {

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = textArea.getText();
                if (text.length() + str.length() > 50) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(container, "每条微微博最多50字符哦");
                    return;
                }

                super.insertString(offs, str, a);
            }
        });

        JButton commitButton = new JButton("发布");
        commitButton.setBounds(90,260,110,40);
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        //写微博成功后将信息写入数据库
                        container,
                        "发布成功！",
                        "提示信息",
                        JOptionPane.INFORMATION_MESSAGE
                );

                try {
                    publishWeiBo(LoginFrame.userName,textArea.getText());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                try {
                    new UserFrame(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                dispose();
            }
        });

        container.add(commitButton);
        container.add(welcomeLabel);
        container.add(textArea);
        setBounds(600, 250, 300, 360);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        new WriteFrame();
    }
}
