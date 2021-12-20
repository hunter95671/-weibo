package com.hunter95.WeiBoGUI;

import com.hunter95.dao.HBaseDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static com.hunter95.dao.HBaseDao.deleteAttends;
import static com.hunter95.dao.HBaseDao.userWeiBoNum;

public class FollowFrame extends JFrame {
    public Container container = this.getContentPane();

    private JScrollPane jScrollPane = null;
    private JPanel fatherPanel = null;
    private JPanel[] jPanels = null;

    private int weiboNum = 0;
    private static String followName;
    private String[] weibo = null;
    private String[] weiboTime = null;
    private String weiBoName = null;


    private int getWeiboNum() throws IOException {
        //获得当前用户发布微博数目
        if (weiboNum == 0) {
            weiboNum = userWeiBoNum(followName);
        }
        return weiboNum;
    }


    private String[] getWeibo() throws IOException {
        //获得当前用户发布微博内容
        if (weibo == null) {
            weibo = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = HBaseDao.getWeiBo(followName);
            for (int i = 0; i < getWeiboNum(); i++) {
                weibo[i] = arrayList.get(i).get(2);
            }
        }
        return weibo;
    }


    private String[] getWeiboTime() throws IOException {
        //获得当前用户发布微博时间
        if (weiboTime == null) {
            weiboTime = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = HBaseDao.getWeiBo(followName);
            for (int i = 0; i < getWeiboNum(); i++) {
                weiboTime[i] = arrayList.get(i).get(1);
            }
        }
        return weiboTime;
    }


    private String getWeiBoName() {
        //获得当前用户关注用户的发布过微博的用户名
        if (weiBoName == null) {
            weiBoName = followName;
        }
        return weiBoName;
    }


    private JScrollPane getJScrollPane() throws IOException {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();

            jScrollPane.setBounds(40, 120, 520, 400);
            jScrollPane.setViewportView(getFatherPanel(getJPanels()));

        }

        return jScrollPane;

    }

    private JPanel[] getJPanels() throws IOException {

        if (jPanels == null) {
            jPanels = new JPanel[getWeiboNum()];
            for (int i = 0; i < getWeiboNum(); i++) {
                jPanels[i] = new JPanel();
                jPanels[i].setLayout(null);
                jPanels[i].setBounds(0, 0, 520, 200);

                JButton button = new JButton(getWeiBoName());
                button.setBounds(0, 0, 100, 20);
                JLabel weiboLabel = new JLabel(getWeibo()[i]);
                weiboLabel.setBounds(0, 30, 500, 100);
                JLabel timeLabel = new JLabel(getWeiboTime()[i]);
                timeLabel.setBounds(0, 150, 200, 20);

                jPanels[i].add(button);
                jPanels[i].add(weiboLabel);
                jPanels[i].add(timeLabel);

            }

        }

        return jPanels;

    }


    private JPanel getFatherPanel(JPanel[] weibo) {

        if (fatherPanel == null) {
            fatherPanel = new JPanel();
            fatherPanel.setLayout(new GridLayout(weibo.length, 1));

            for (int i = 0; i < weibo.length; i++) {

                fatherPanel.add(weibo[i]);

            }

            fatherPanel.setPreferredSize(new Dimension(520, 200 * weibo.length));


        }

        return fatherPanel;
    }

    public FollowFrame(String name) throws IOException {
        followName = name;
        //用户界面
        Container container = this.getContentPane();
        container.setLayout(null);

        //欢迎框
        String userName = LoginFrame.userName + ":欢迎来到" + name + "的个人中心";
        JLabel userJLabel = new JLabel(userName);
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0, 0, 600, 20);
        userPanel.add(userJLabel);

        JButton followButton = new JButton("取消关注");
        followButton.setBounds(440, 40, 100, 20);
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        container,
                        "成功取消关注该用户！",
                        "     提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                try {
                    deleteAttends(LoginFrame.userName,name);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                try {
                    new OtherFrame(name);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                dispose();
            }
        });

        JButton homepageButton = new JButton("返回首页");
        homepageButton.setBounds(40, 80, 120, 20);
        homepageButton.addActionListener(new ActionListener() {
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

        JButton ownButton = new JButton("返回个人中心");
        ownButton.setBounds(420, 80, 120, 20);
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

        container.add(followButton);
        container.add(homepageButton);
        container.add(ownButton);
        container.add(userPanel);
        container.add(getJScrollPane());


        //界面参数
        pack();
        setBounds(500, 150, 600, 520);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    static String k = "";

    public static void main(String[] args) throws IOException {
        new FollowFrame(k);
    }
}
