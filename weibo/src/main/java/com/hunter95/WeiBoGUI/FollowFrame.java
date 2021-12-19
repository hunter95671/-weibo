package WeiBoGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FollowFrame extends JFrame {
    public Container container = this.getContentPane();

    private JScrollPane jScrollPane = null;
    private JPanel fatherPanel = null;
    private JPanel[] jPanels = null;

    private int weiboNum = 0;
    private String followName = "";
    private String[] weibo = null;
    private String[] weiboTime = null;
    private String[] weiBoName = null;



    private int getWeiboNum(){
        //获得当前用户发布微博数目
        if (weiboNum == 0)
        {
            weiboNum = 10;
        }
        return weiboNum;
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


    private String[] getWeiBoName(){
        //获得当前用户关注用户的发布过微博的用户名
        if (weiBoName == null)
        {
            weiBoName = new String[getWeiboNum()];
            String[] hereTime = {"Roswell", "Quincy", "Quincy","Godly",
                    "Joe",
                    "Ernest",
                    "Frank",
                    "Joe",
                    "Ernest",
                    "Frank"};
            System.arraycopy(hereTime, 0, weiBoName, 0, weiBoName.length);
        }
        return weiBoName;
    }



    private JScrollPane getJScrollPane()

    {
        if (jScrollPane == null)

        {
            jScrollPane = new JScrollPane();

            jScrollPane.setBounds(40,120,520,400);
            jScrollPane.setViewportView(getFatherPanel(getJPanels()));


        }

        return jScrollPane;

    }



    private JPanel[] getJPanels()

    {

        if (jPanels == null) {
            jPanels = new JPanel[getWeiboNum()];
            for (int i = 0; i < getWeiboNum(); i++) {
                jPanels[i] = new JPanel();
                jPanels[i].setLayout(null);
                jPanels[i].setBounds(0,0,520,200);

                JButton button = new JButton(getWeiBoName()[i]);
                button.setBounds(0,0,100,20);
                JLabel weiboLabel = new JLabel(getWeibo()[i]);
                weiboLabel.setBounds(0,30,500,100);
                JLabel timeLabel = new JLabel(getWeiboTime()[i]);
                timeLabel.setBounds(0,150,200,20);

                jPanels[i].add(button);
                jPanels[i].add(weiboLabel);
                jPanels[i].add(timeLabel);




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

            fatherPanel.setPreferredSize(new Dimension(520,200 * weibo.length));


        }

        return fatherPanel;
    }

    public FollowFrame(String name){
        followName = name;
        //用户界面
        Container container = this.getContentPane();
        container.setLayout(null);

        //欢迎框
        String userName = LoginFrame.userName + ":欢迎来到" + name + "的个人中心";
        JLabel userJLabel = new JLabel(userName);
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0,0,600,20);
        userPanel.add(userJLabel);

        JButton followButton = new JButton("取消关注");
        followButton.setBounds(440,40,100,20);
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        container,
                        "成功取消关注该用户！",
                        "     提示",
                        JOptionPane.INFORMATION_MESSAGE
                );
                new OtherFrame(name);
                dispose();
            }
        });

        JButton homepageButton = new JButton("返回首页");
        homepageButton.setBounds(40,80,120,20);
        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomepageFrame();
                dispose();
            }
        });

        JButton ownButton = new JButton("返回个人中心");
        ownButton.setBounds(420,80,120,20);
        ownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserFrame(true);
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
        setBounds(500,150,600,520);
        setVisible(true);
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new FollowFrame("zhe");
    }
}
