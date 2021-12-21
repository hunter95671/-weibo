package com.hunter95.WeiBoGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static com.hunter95.dao.HBaseDao.attendList;
import static com.hunter95.dao.HBaseDao.notAttendRandomPush;

public class HomepageFrame extends JFrame {
    public Container container = this.getContentPane();

    private JScrollPane jScrollPane ;
    private JPanel fatherPanel ;
    private JPanel[] jPanels = null ;

    private int weiboNum = 0;
    private String[] weibo={""};
    private String[] weiboTime={""} ;
    private String[] weiBoName={""} ;

    private int getWeiboNum() {
        //获得随机推送发布微博数目
        if (weiboNum == 0) {
            weiboNum = 3;
        }
        return weiboNum;
    }

    private String[] getWeibo() throws IOException {
        //获得随机微博内容
            weibo = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = notAttendRandomPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getWeiboNum(); i++) {
                weibo[i] = arrayList.get(i).get(2);
        }
        return weibo;
    }

    private String[] getWeiboTime() throws IOException {
        //获得当前用户发布微博时间

            weiboTime = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = notAttendRandomPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getWeiboNum(); i++) {
                weiboTime[i] = arrayList.get(i).get(1);
            }

        return weiboTime;
    }

    private String[] getWeiBoName() throws IOException {
        //获得当前用户关注用户的发布过微博的用户名

            weiBoName = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = notAttendRandomPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getWeiboNum(); i++) {
                weiBoName[i] = arrayList.get(i).get(0);

        }

        return weiBoName;
    }

    private JScrollPane getJScrollPane() throws IOException {

            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(40, 80, 520, 400);
            jScrollPane.setViewportView(getFatherPanel(getJPanels()));

        return jScrollPane;

    }

    private JPanel[] getJPanels() throws IOException {

            jPanels = new JPanel[getWeiboNum()];
            for (int i = 0; i < getWeiboNum(); i++) {
                jPanels[i] = new JPanel();
                jPanels[i].setLayout(null);
                jPanels[i].setBounds(0, 0, 520, 200);

                JButton button = new JButton(getWeiBoName()[i]);
                button.setBounds(0, 0, 150, 20);
                int finalI = i;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new OtherFrame(getWeiBoName()[finalI]);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        dispose();
                    }
                });
                JLabel weiboLabel = new JLabel(getWeibo()[i]);
                weiboLabel.setBounds(0, 30, 500, 100);
                JLabel timeLabel = new JLabel(getWeiboTime()[i]);
                timeLabel.setBounds(0, 150, 200, 20);

                jPanels[i].add(button);
                jPanels[i].add(weiboLabel);
                jPanels[i].add(timeLabel);


            }



        return jPanels;

    }


    private JPanel getFatherPanel(JPanel[] weibo) {


            fatherPanel = new JPanel();
            fatherPanel.setLayout(new GridLayout(weibo.length, 1));

            for (int i = 0; i < weibo.length; i++) {

                fatherPanel.add(weibo[i]);



            fatherPanel.setPreferredSize(new Dimension(520, 200 * weibo.length));


        }

        return fatherPanel;
    }


    public HomepageFrame() throws IOException {

        //用户界面
        Container container = this.getContentPane();
        container.setLayout(null);

        //欢迎框
        String userName = LoginFrame.userName + "欢迎来到微微博";
        JLabel userJLabel = new JLabel(userName);
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0, 0, 600, 40);
        userPanel.add(userJLabel);

        //刷新按钮
        JButton flushButton = new JButton("刷新");
        flushButton.setBounds(40,40,100,40);
        flushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new HomepageFrame();
                } catch (IOException ioException) {
                    //ioException.printStackTrace();
                }
                dispose();
            }
        });

        //返回登录页面按钮
        JButton returnButton = new JButton("返回登录页面");
        returnButton.setBounds(220,40,140,40);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new HomepageFrame();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                dispose();
            }
        });

        //个人中心按钮
        JButton ownButton = new JButton("个人中心");
        ownButton.setBounds(440, 40, 100, 40);
        ownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new UserFrame(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                dispose();
            }
        });

        container.add(returnButton);
        container.add(flushButton);
        container.add(ownButton);
        container.add(userPanel);
        container.add(getJScrollPane());


        //界面参数
        pack();
        setBounds(500, 150, 600, 500);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {

        new HomepageFrame();
    }
}
