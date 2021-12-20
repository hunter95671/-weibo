package com.hunter95.WeiBoGUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class Test extends JFrame {

    public Test(){
        Container container = this.getContentPane();
        container.setLayout(null);

        JTextField textField = new JTextField("哲某人");
        textField.setBounds(0,0,120,20);
        //限制输入文本框的长度
        textField.setDocument(new PlainDocument() {

            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String text = textField.getText();
                if (text.length() + str.length() > 10) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(container, "Larger than 10 characters!");
                    return;
                }

                super.insertString(offs, str, a);
            }
        });

        container.add(textField);

        setBounds(500,150,600,540);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
    public static void main(String[] args) {
        new Test();
    }
}
