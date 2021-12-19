package com.hunter95.test;

import com.hunter95.constants.constants;
import com.hunter95.dao.HBaseDao;
import com.hunter95.utils.HBaseUtil;
import org.junit.Test;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.hunter95.dao.HBaseDao.*;
import static com.hunter95.utils.HBaseUtil.scanTable;

public class TestWeiBo {

    public static void init() {

        try {
            //创建命名空间
            //HBaseUtil.createNameSpace(constants.NAMESPACE);
            //创建微博内容表
            HBaseUtil.createTable(constants.CONTENT_TABLE, constants.CONTENT_TABLE_VERSIONS, constants.CONTENT_TABLE_CF);
            //创建用户关系表
            HBaseUtil.createTable(constants.RELATION_TABLE, constants.RELATION_TABLE_VERSIONS, constants.RELATION_TABLE_CF1, constants.RELATION_TABLE_CF2);
            //创建收件箱表
            HBaseUtil.createTable(constants.INBOX_TABLE, constants.INBOX_TABLE_VERSIONS, constants.INBOX_TABLE_CF);
            //创建用户表
            HBaseUtil.createTable(constants.USER_TABLE, constants.USER_TABLE_VERSIONS, constants.USER_TABLE_CF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        //初始化
        //init();
        //HBaseUtil.createTable(constants.USER_TABLE,constants.USER_TABLE_VERSIONS,constants.USER_TABLE_CF);

        //全表查看
        //scanTable("weibo:content");
        //scanTable("weibo:user");
        //scanTable("weibo:relation");
        //scanTable("weibo:inbox");

        //用户注册测试
        //HBaseDao.userRegister("zhangsan","123456");

        //用户名重复测试
        //HBaseDao.ifRepeat("lisi");

        //发布微博测试
        //HBaseDao.publishWeiBo("zhangsan","一条微博~");

        //关注用户测试
        //zhangsan关注FDr6gkPfr,FAvM1fL1I
        //HBaseDao.addAttends("zhangsan","FDr6gkPfr");
        //HBaseDao.addAttends("zhangsan","FAvM1fL1I");

        //获取用户关注列表测试
        ArrayList<String> zhangsanAttendList = attendList("zhangsan");
        //System.out.println(zhangsanAttendList);

        //过滤查询测试
        //filterScan("FDr6gkPfr","zhangsan");

        //HBaseDao.getInit("FDr6gkPfr");

        //HBaseDao.getWeiBo("zhangsan");

        /*
        //随机推送(所有用户)测试
        ArrayList<ArrayList<String>> arr1 = normalRandomPush();
        System.out.println(arr1.get(0));
        System.out.println("用户：");
        System.out.println(arr1.get(0).get(0));
        System.out.println("发布日期：");
        System.out.println(arr1.get(0).get(1));
        System.out.println("微博内容：");
        System.out.println(arr1.get(0).get(2));
        //randomPush();
        */

        //ArrayList<ArrayList<String>> lists = attendRandomPush(attendList("zhangsan"));
        //System.out.println(lists.get(0).get(1));

       /* Random random = new Random ();
        boolean[]  bool = new boolean[14];
        int randInt = 0;
        for(int j = 0; j < 9 ; j++) {
            do {
                randInt = random.nextInt(14);
            } while (bool[randInt] = true);
        }
        */

        //随机数组生成
        ArrayList<Integer> numList = new ArrayList<>();
        Random random = new Random ();
        int k=10;
        int i=0;
        while (i<k) {
            int randInt = random.nextInt(88);
            if (numList.contains(randInt)){
            }else {
                i+=1;
                numList.add(randInt);
            }
        }

        System.out.println(numList);

       /* //1001发布微博
        HBaseDao.publishWeiBo("1001","好耶！");

        //1002关注1001和1003
        HBaseDao.addAttends("1002","1001","1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");

        System.out.println("+++++++++++++++++++++++++++");

        //1003发布三条微博，同时1001发布两条微博
        HBaseDao.publishWeiBo("1003","ok！");
        Thread.sleep(10);
        HBaseDao.publishWeiBo("1003","hi！");
        Thread.sleep(10);
        HBaseDao.publishWeiBo("1003","hello！");
        Thread.sleep(10);
        HBaseDao.publishWeiBo("1001","好的！");
        Thread.sleep(10);
        HBaseDao.publishWeiBo("1001","好呀！");

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("+++++++++++++++++++++++++++");

        //1002取关1003
        HBaseDao.deleteAttends("1002","1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("+++++++++++++++++++++++++++");

        //1002再次关注1003
        HBaseDao.addAttends("1002","1003");

        //获取1002初始化页面
        HBaseDao.getInit("1002");
        System.out.println("+++++++++++++++++++++++++++");

        //获取1001微博详情
        HBaseDao.getWeiBo("1001");*/
    }
}
