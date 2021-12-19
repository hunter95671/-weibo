package WeiBoGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private int getWeiboNum(){
        //获得当前用户发布微博数目
        if (weiboNum == 0)
        {
            weiboNum = 10;
        }
        return weiboNum;
    }

    private int getFollowNum(){
        //获得当前用户关注用户数目
        if (followNum == 0)
        {
            followNum = 10;
        }
        return followNum;
    }


    private int getOtherWeiboNum(){
        //获得当前用户的关注用户发布微博数目
        if (otherWeiboNum == 0)
        {
            otherWeiboNum = 3;
        }
        return otherWeiboNum;
    }



    private String[] getWeibo(){
        //获得当前用户发布微博内容
        if (weibo == null)
        {
            weibo = new String[getWeiboNum()];
            String[] hereWeiBo = {"<html>#AMX玻尿酸酸奶嘭弹上市# 靠近点～看我发现了什么——@安慕希 AMX「肌肤关系」玻尿酸酸奶！0蔗糖酸奶基底，搭配真实石榴樱桃果味酱，添加食品级玻尿酸。快来get我的同款酸奶吧?? Dear-迪丽热巴的微博视频 ?",
                    "<html>工作中追求一丝不苟，生活中追求一尘不染。莱克天狼星三合一全屋吸尘/洗地机，定点喷洗，洗过即干。给家带来从里到外的干净。和@LEXY莱克 一起，让世界更干净。#迪丽热巴代言莱克清洁电器# LEXY莱克的微博视频 ?",
                    "<html>剧美生活，无限热爱！跟我一起关注#电视剧大赏##年度好剧推荐#活动，为你推荐我的年度好剧《长歌行》和《你是我的荣耀》，感谢大家对李长歌和乔晶晶的喜爱！ ?[组图共2张]?原图?",
                    "<html>#电视剧烈火如歌# 破亿啦！下周烧饼铺就要开张啦，都来吃烧饼哟，买一...送...么么哒 ?原图?",
                    "<html>立冬和初雪一起来了?? 绿洲 ?[组图共4张]?原图?",
                    "<html>#路易200#旅行，是空间与时间的转换，世界各地的文化更为@路易威登 提供了无尽艺术灵感。点击视频，与我共启旅程。 Dear-迪丽热巴的微博视频 ?",
                    "Godly",
                    "Joe",
                    "Ernest",
                    "Frank"};
            if (weibo.length >= 0) System.arraycopy(hereWeiBo, 0, weibo, 0, weibo.length);
        }
        return weibo;
    }

    private String[] getOtherWeibo(){
        //获得当前用户关注的用户发布微博内容
        if (otherWeibo == null)
        {
            otherWeibo = new String[getOtherWeiboNum()];
            String[] hereWeiBo = {"1","2","3"};
            if (otherWeibo.length >= 0) System.arraycopy(hereWeiBo, 0, otherWeibo, 0, otherWeibo.length);
        }
        return otherWeibo;
    }

    private String[] getWeiboTime(){
        //获得当前用户发布微博时间
        if (weiboTime == null)
        {
            weiboTime = new String[getWeiboNum()];
            String[] hereTime = {"2021/12/11 11:00",
                    "2021/12/8 10:05" ,
                    "2021/12/3 12:00" ,
                    "2021/11/26 16:24" ,
                    "2021/11/18 14:38" ,
                    "2021/11/16 19:24" ,
                    "2021/11/16 14:01" ,
                    "2021/11/13 10:26" ,
                    "2021/11/10 13:26" ,
                    "2021/11/7 10:12"};
            System.arraycopy(hereTime, 0, weiboTime, 0, weibo.length);
        }
        return weiboTime;
    }

    private String[] getFollowNameArray(){
        //获得当前用户关注用户列表
        if (followNameArray == null)
        {
            followNameArray = new String[getFollowNum()];
            String[] hereTime = {"Roswell", "Quincy", "Vernon", "Ian", "Edmund", "Lucas","Godly", "Joe",
                    "Ernest", "Frank"};
            System.arraycopy(hereTime, 0, followNameArray, 0, followNameArray.length);
        }
        return followNameArray;
    }

    private String[] getOtherWeiBoName(){
        //获得当前用户关注用户的发布过微博的用户名
        if (otherWeiBoName == null)
        {
            otherWeiBoName = new String[getOtherWeiboNum()];
            String[] hereTime = {"Roswell", "Quincy", "Quincy"};
            System.arraycopy(hereTime, 0, otherWeiBoName, 0, otherWeiBoName.length);
        }
        return otherWeiBoName;
    }

    private String[] getOtherWeiboTime(){
        //获得当前用户发布微博时间
        if (otherWeiboTime == null)
        {
            otherWeiboTime = new String[getOtherWeiboNum()];
            String[] hereTime = {"2021/12/11 11:00", "2021/12/8 10:05", "2021/12/8 10:05" };
            System.arraycopy(hereTime, 0, otherWeiboTime, 0, otherWeiboTime.length);
        }
        return otherWeiboTime;
    }

    private JScrollPane getJScrollPane()

    {
        if (jScrollPane == null)

        {
            jScrollPane = new JScrollPane();

            if (flag){
                jScrollPane.setBounds(160,80,420,420);
                jScrollPane.setViewportView(getFatherPanel(getJPanels()));

            }
            else {
                jScrollPane.setBounds(160,80,420,400);
                jScrollPane.setViewportView(getFatherPanel(getOtherJPanels()));

            }

        }

        return jScrollPane;

    }

    private JPanel[] getOtherJPanels()

    {

        if (otherJPanels == null) {
            otherJPanels = new JPanel[getOtherWeiboNum()];
            for (int i = 0; i < getOtherWeiboNum(); i++) {
                otherJPanels[i] = new JPanel();
                otherJPanels[i].setLayout(null);
                otherJPanels[i].setBounds(0,0,420,200);

                JButton button = new JButton(getOtherWeiBoName()[i]);
                button.setBounds(0,0,100,20);
                int finalI = i;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new FollowFrame(getOtherWeiBoName()[finalI]);
                        dispose();
                    }
                });
                JLabel weiboLabel = new JLabel(getOtherWeibo()[i]);
                weiboLabel.setBounds(0,30,400,100);
                JLabel timeLabel = new JLabel(getOtherWeiboTime()[i]);
                timeLabel.setBounds(0,150,200,20);


                otherJPanels[i].add(button);
                otherJPanels[i].add(weiboLabel);
                otherJPanels[i].add(timeLabel);



            }

        }

        return otherJPanels;

    }

    private JPanel[] getJPanels()

    {

        if (jPanels == null) {
            jPanels = new JPanel[getWeiboNum()];
            for (int i = 0; i < getWeiboNum(); i++) {
                jPanels[i] = new JPanel();
                jPanels[i].setLayout(null);
                jPanels[i].setBounds(0,0,420,200);

                JButton button = new JButton(LoginFrame.userName);
                button.setBounds(0,0,100,20);
                JLabel weiboLabel = new JLabel(getWeibo()[i]);
                weiboLabel.setBounds(0,30,400,100);
                JLabel timeLabel = new JLabel(getWeiboTime()[i]);
                timeLabel.setBounds(0,150,200,20);
                JButton deleteButton= new JButton("删除");
                deleteButton.setBounds(320,150,80,20);
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
                        String deleteText = LoginFrame.userName + "_" + getWeiboTime()[finalI];
                        System.out.println(deleteText);
                        new UserFrame(true);
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


    private JPanel getFatherPanel(JPanel[] weibo){

        if (fatherPanel == null)
        {
            fatherPanel = new JPanel();
            fatherPanel.setLayout(new GridLayout(weibo.length,1));

            for (int i = 0; i < weibo.length; i++)
            {

                fatherPanel.add(weibo[i]);

            }
            if (flag){
                fatherPanel.setPreferredSize(new Dimension(420,200 * weibo.length));
            } else {
                fatherPanel.setPreferredSize(new Dimension(420,180 * weibo.length));
            }

        }

        return fatherPanel;
    }



    public UserFrame(boolean frameFlag) {
        flag = frameFlag;
        //用户界面
        Container container = this.getContentPane();
        container.setLayout(null);

        //用户名框
        String userName = "            " + LoginFrame.userName;
        JLabel userJLabel = new JLabel(userName);
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0,0,600,40);
        userPanel.add(userJLabel);

        //关注列表框
        JLabel followPanel = new JLabel("        关注列表");
        followPanel.setBounds(0,40,120,40);
        JPanel followListPanel = new JPanel();
        followListPanel.setLayout(new GridLayout(getFollowNameArray().length,1));
        followListPanel.setPreferredSize(new Dimension(80,40 * getFollowNameArray().length));
        JButton[] followNameButton = new JButton[getFollowNameArray().length];
        for (int i = 0; i < getFollowNameArray().length; i++) {
             followNameButton[i] = new JButton(getFollowNameArray()[i]);
             followNameButton[i].setBounds(0,0,80,40);
            int finalI = i;
            followNameButton[i].addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     new FollowFrame(getFollowNameArray()[finalI]);
                     dispose();
                 }
             });
             followListPanel.add(followNameButton[i]);
        }

        //页面切换按钮
        JButton userButton = new JButton("个人发布");
        userButton.setBounds(160,40,120,30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag){
                    JOptionPane.showMessageDialog(
                            container,
                            "已在当前页面！！",
                            "消息标题",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    flag = true;
                    new UserFrame(true);
                    dispose();
                }
            }
        });
        JButton followButton = new JButton("关注发布");
        followButton.setBounds(310,40,120,30);
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag){
                    new UserFrame(false);
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
        writeButton.setBounds(460,40,120,30);
        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WriteFrame();
                dispose();
            }
        });

        JScrollPane followScrollPane = new JScrollPane();
        followScrollPane.setBounds(0,80,120,380);
        followScrollPane.setViewportView(followListPanel);


        container.add(userPanel);
        container.add(followScrollPane);
        container.add(followPanel);
        container.add(userButton);
        container.add(followButton);
        container.add(writeButton);
        container.add(getJScrollPane());


        //界面参数
        pack();
        setBounds(500,150,600,520);
        setVisible(true);
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        new UserFrame(true);
    }
}
