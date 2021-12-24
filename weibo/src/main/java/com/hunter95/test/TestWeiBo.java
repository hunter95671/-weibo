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
            HBaseUtil.createNameSpace(constants.NAMESPACE);
            //创建微博内容表
            HBaseUtil.createTable(constants.CONTENT_TABLE, constants.CONTENT_TABLE_VERSIONS, constants.CONTENT_TABLE_CF);
            //创建用户关系表
            HBaseUtil.createTable(constants.RELATION_TABLE, constants.RELATION_TABLE_VERSIONS, constants.RELATION_TABLE_CF1, constants.RELATION_TABLE_CF2);
            //创建用户表
            HBaseUtil.createTable(constants.USER_TABLE, constants.USER_TABLE_VERSIONS, constants.USER_TABLE_CF);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        //初始化
        init();
        //HBaseUtil.createTable(constants.USER_TABLE,constants.USER_TABLE_VERSIONS,constants.USER_TABLE_CF);

        //全表查看
        //scanTable("weibo:content");
        //scanTable("weibo:user");
        //scanTable("weibo:relation");

        //用户注册测试
        //HBaseDao.userRegister("zhangsan","123456");

        //判断密码是否正确测试
        //pswIfRight("zhangsan","123456");

        //用户名重复测试
        //HBaseDao.ifRepeat("zhangsan");

        //发布微博测试
        //HBaseDao.publishWeiBo("lisi","1条微博~~~~~");
        //HBaseDao.publishWeiBo("lisi","2条微博~~~~~");
        //HBaseDao.publishWeiBo("lisi","3条微博~~~~~");

        //删除微博测试
        //deleteWeiBo("zhangsan_2021-12-19 11:06");

        //ArrayList<ArrayList<String>> zhangsan = getWeiBo("zhangsan");
        //System.out.println(zhangsan);

        //关注用户测试
        //zhangsan关注FDr6gkPfr,FAvM1fL1I
        //HBaseDao.addAttends("zhangsan","FDr6gkPfr");
        //HBaseDao.addAttends("zhangsan","FAvM1fL1I");

        //获取用户关注列表测试
        //ArrayList<String> zhangsanAttendList = attendList("index");
        //System.out.println(zhangsanAttendList);

        //ArrayList<ArrayList<String>> index = notAttendRandomPush(attendList("index"));
        //System.out.println(index);

        //取关测试
        //deleteAttends("zhangsan","FDr6gkPfr");

        //过滤查询测试
        //filterScan("FDr6gkPfr","zhangsan");

        //HBaseDao.getInit("FDr6gkPfr");

        //HBaseDao.getWeiBo("zhangsan");

        //获取某人微博
        //ArrayList<ArrayList<String>> L5wHU1jIe = getWeiBo("FAvM1fL1I");
        //System.out.println(L5wHU1jIe);

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

        //随机数组生成

        /*
        ArrayList<ArrayList<String>> lists = attendRandomPush(attendList("zhangsan"));

        ArrayList<Integer> numList = new ArrayList<>();
        Random random = new Random ();
        int i=0;
        while (i<lists.size()) {
            int randInt = random.nextInt(lists.size());
            if (numList.contains(randInt)){
            }else {
                i+=1;
                numList.add(randInt);
            }
        }

        System.out.println(numList);

        for (Integer integer : numList) {
            System.out.println(lists.get(integer).get(0));
        }
*/
        /*//未关注者推送
        ArrayList<ArrayList<String>> zhangsan = notAttendRandomPush(attendList("zhangsan"));
        String[] weiboCon = null;
        weiboCon = new String[zhangsan.size()];
        for (int i = 0; i < zhangsan.size(); i++) {
            weiboCon[i] =zhangsan.get(i).get(1);
        }
        for (String s : weiboCon) {
            System.out.println(s);
        }*/
        //System.out.println(zhangsan);

        //关注者推送
        //ArrayList<ArrayList<String>> zhangsan = attendRandomPush(attendList("zhangsan"));
        //System.out.println(zhangsan);

        //获得当前用户发布微博数目测试
        //System.out.println(userWeiBoNum("L5wHU1jIe"));

        //获得当前用户关注用户数目测试
        //System.out.println(userAttendNum("zhangsan"));

    }
}
