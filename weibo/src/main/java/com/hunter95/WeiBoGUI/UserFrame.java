package com.hunter95.WeiBoGUI;

import com.hunter95.dao.HBaseDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static com.hunter95.dao.HBaseDao.*;

public class UserFrame extends JFrame {

    public Container container = this.getContentPane();

    private JScrollPane jScrollPane = null;
    private JPanel fatherPanel = null;
    private JPanel[] jPanels = null;
    private JPanel[] otherJPanels = null;
    private String[] followNameArray = null;

    private int weiboNum = 0;
    private int otherWeiboNum = 0;
    private int followNum = 0;
    private String[] weibo = null;
    private String[] weiboTime = null;
    private String[] otherWeibo = null;
    private String[] otherWeiboTime = null;
    private String[] otherWeiBoName = null;
    private boolean flag = false;

    private int getWeiboNum() throws IOException {
        //获得当前用户发布微博数目
        if (weiboNum == 0) {
            weiboNum = userWeiBoNum(LoginFrame.userName);
        }
        return weiboNum;
    }

    private int getFollowNum() throws IOException {
        //获得当前用户关注用户数目
        if (followNum == 0) {
            ArrayList<String> arrayList = attendList(LoginFrame.userName);
            followNum = arrayList.size();
        }
        return followNum;
    }


    private int getOtherWeiboNum() throws IOException {
        //获得当前用户的关注用户发布微博数目
        if (otherWeiboNum == 0) {
            ArrayList<String> arrayList = attendList(LoginFrame.userName);
            for (String s : arrayList) {
                otherWeiboNum+=userWeiBoNum(s);
            }
        }
        return otherWeiboNum;
    }


    private String[] getWeibo() throws IOException {
        //获得当前用户发布微博内容
        if (weibo == null) {
            weibo = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = HBaseDao.getWeiBo(LoginFrame.userName);
            for (int i = 0; i < getWeiboNum(); i++) {
                weibo[i] = arrayList.get(i).get(2);
            }
        }
        return weibo;
    }

    private String[] getOtherWeibo() throws IOException {
        //获得当前用户关注的用户发布微博内容
        if (otherWeibo == null) {
            otherWeibo = new String[getOtherWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = attendPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getOtherWeiboNum(); i++) {
                otherWeibo[i] = arrayList.get(i).get(2);
            }
        }

        return otherWeibo;
    }

    private String[] getWeiboTime() throws IOException {
        //获得当前用户发布微博时间
        if (weiboTime == null) {
            weiboTime = new String[getWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = HBaseDao.getWeiBo(LoginFrame.userName);
            for (int i = 0; i < getWeiboNum(); i++) {
                weiboTime[i] = arrayList.get(i).get(1);
            }
        }
        return weiboTime;
    }

    private String[] getFollowNameArray() throws IOException {
        //获得当前用户关注用户列表
        if (followNameArray == null) {
            followNameArray = new String[getFollowNum()];
            ArrayList<String> arrayList = attendList(LoginFrame.userName);
            for (int i = 0; i < getFollowNum(); i++) {
                followNameArray[i] = arrayList.get(i);
            }
        }
        return followNameArray;
    }

    private String[] getOtherWeiBoName() throws IOException {
        //获得当前用户关注用户的发布过微博的用户名
        if (otherWeiBoName == null) {
            otherWeiBoName = new String[getOtherWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = attendPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getOtherWeiboNum(); i++) {
                otherWeiBoName[i] = arrayList.get(i).get(0);
            }
        }
        return otherWeiBoName;
    }

    private String[] getOtherWeiboTime() throws IOException {
        //获得当前用户关注用户的发布微博时间
        if (otherWeiboTime == null) {
            otherWeiboTime = new String[getOtherWeiboNum()];
            ArrayList<ArrayList<String>> arrayList = attendPush(attendList(LoginFrame.userName));
            for (int i = 0; i < getOtherWeiboNum(); i++) {
                otherWeiboTime[i] = arrayList.get(i).get(1);
            }
        }
        return otherWeiboTime;
    }

    private JScrollPane getJScrollPane() throws IOException {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();

            if (flag) {
                jScrollPane.setBounds(160, 80, 420, 420);
                jScrollPane.setViewportView(getFatherPanel(getJPanels()));

            } else {
                jScrollPane.setBounds(160, 80, 420, 400);
                jScrollPane.setViewportView(getFatherPanel(getOtherJPanels()));

            }

        }

        return jScrollPane;

    }

    private JPanel[] getOtherJPanels() throws IOException {

        if (otherJPanels == null) {
            otherJPanels = new JPanel[getOtherWeiboNum()];
            for (int i = 0; i < getOtherWeiboNum(); i++) {
                otherJPanels[i] = new JPanel();
                otherJPanels[i].setLayout(null);
                otherJPanels[i].setBounds(0, 0, 420, 200);

                JButton button = new JButton(getOtherWeiBoName()[i]);
                button.setBounds(0, 0, 100, 20);
                int finalI = i;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new FollowFrame(getOtherWeiBoName()[finalI]);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        dispose();
                    }
                });
                JLabel weiboLabel = new JLabel(getOtherWeibo()[i]);
                weiboLabel.setBounds(0, 30, 400, 100);
                JLabel timeLabel = new JLabel(getOtherWeiboTime()[i]);
                timeLabel.setBounds(0, 150, 200, 20);


                otherJPanels[i].add(button);
                otherJPanels[i].add(weiboLabel);
                otherJPanels[i].add(timeLabel);


            }

        }

        return otherJPanels;

    }

    private JPanel[] getJPanels() throws IOException {

        if (jPanels == null) {
            jPanels = new JPanel[getWeiboNum()];
            for (int i = 0; i < getWeiboNum(); i++) {
                jPanels[i] = new JPanel();
                jPanels[i].setLayout(null);
                jPanels[i].setBounds(0, 0, 420, 200);

                JButton button = new JButton(LoginFrame.userName);
                button.setBounds(0, 0, 100, 20);
                JLabel weiboLabel = new JLabel(getWeibo()[i]);
                weiboLabel.setBounds(0, 30, 400, 100);
                JLabel timeLabel = new JLabel(getWeiboTime()[i]);
                timeLabel.setBounds(0, 150, 200, 20);
                JButton deleteButton = new JButton("删除");
                deleteButton.setBounds(320, 150, 80, 20);
                int finalI = i;
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(
                                container,
                                "                删除成功",
                                "消息标题",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        String deleteText = null;
                        try {
                            deleteText = LoginFrame.userName + "_" + getWeiboTime()[finalI];
                            deleteWeiBo(deleteText);
                        } catch (IOException ioException) {
                        }
                        try {
                            new UserFrame(true);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        dispose();
                    }
                });

                jPanels[i].add(button);
                jPanels[i].add(weiboLabel);
                jPanels[i].add(timeLabel);
                jPanels[i].add(deleteButton);

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
            if (flag) {
                fatherPanel.setPreferredSize(new Dimension(420, 200 * weibo.length));
            } else {
                fatherPanel.setPreferredSize(new Dimension(420, 180 * weibo.length));
            }

        }

        return fatherPanel;
    }


    public UserFrame(boolean frameFlag) throws IOException {
        flag = frameFlag;
        //用户界面
        Container container = this.getContentPane();
        container.setLayout(null);

        //用户名框
        String userName = "            " + LoginFrame.userName;
        JLabel userJLabel = new JLabel(userName);
        userJLabel.setFont(new Font("楷体", 1, 20));
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0, 0, 600, 40);
        userPanel.add(userJLabel);

        //返回首页按钮
        JButton homeButton = new JButton("返回首页");
        homeButton.setBounds(0,40,120,40);
        homeButton.addActionListener(new ActionListener() {
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

        //关注列表框
        JLabel followPanel = new JLabel("        关注列表");
        followPanel.setBounds(0, 80, 120, 40);
        JPanel followListPanel = new JPanel();
        followListPanel.setLayout(new GridLayout(getFollowNameArray().length, 1));
        followListPanel.setPreferredSize(new Dimension(80, 40 * getFollowNameArray().length));
        JButton[] followNameButton = new JButton[getFollowNameArray().length];
        for (int i = 0; i < getFollowNameArray().length; i++) {
            followNameButton[i] = new JButton(getFollowNameArray()[i]);
            followNameButton[i].setBounds(0, 0, 80, 40);
            int finalI = i;
            followNameButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new FollowFrame(getFollowNameArray()[finalI]);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    dispose();
                }
            });
            followListPanel.add(followNameButton[i]);
        }

        //页面切换按钮
        JButton userButton = new JButton("个人发布");
        userButton.setBounds(160, 40, 120, 30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag) {
                    JOptionPane.showMessageDialog(
                            container,
                            "已在当前页面！！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    flag = true;
                    try {
                        new UserFrame(true);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    dispose();
                }
            }
        });
        JButton followButton = new JButton("关注发布");
        followButton.setBounds(310, 40, 120, 30);
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag) {
                    try {
                        new UserFrame(false);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    dispose();
                } else {

                    JOptionPane.showMessageDialog(
                            container,
                            "已在当前页面！！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        JButton writeButton = new JButton("写微博");
        writeButton.setBounds(460, 40, 120, 30);
        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WriteFrame();
                dispose();
            }
        });

        JScrollPane followScrollPane = new JScrollPane();
        followScrollPane.setBounds(0, 120, 120, 50 * followNum);
        followScrollPane.setViewportView(followListPanel);


        container.add(userPanel);
        container.add(homeButton);
        container.add(followScrollPane);
        container.add(followPanel);
        container.add(userButton);
        container.add(followButton);
        container.add(writeButton);
        container.add(getJScrollPane());


        //界面参数
        pack();
        setBounds(500, 150, 600, 520);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {

        new UserFrame(true);
    }
}
