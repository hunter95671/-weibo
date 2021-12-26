package com.hunter95.dao;

import com.hunter95.constants.constants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static com.hunter95.constants.constants.*;
import static com.hunter95.utils.HBaseUtil.deleteData;
import static com.hunter95.utils.HBaseUtil.putData;

/**
 * 1、发布微博
 * 2、删除微博
 * 3、关注用户
 * 4、取关用户
 * 5、用户注册
 * 6、判断用户名是否重复
 * 7、判断密码是否正确
 * 8、获取某个人所有微博详情
 * 9、获取用户的关注列表
 * 10、推送(关注用户)
 * 11、随机推送(未关注用户)
 * 12、获得当前用户发布微博数目
 * 13、获得当前用户关注用户数目
 */
public class HBaseDao {

    //1、发布微博
    public static void publishWeiBo(String uid, String content) throws IOException {

        //获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //操作微博内容表
        //1.获取微博内容表对象
        Table contTable = connection.getTable(TableName.valueOf(CONTENT_TABLE));

        //2.获取当前时间
        Long ts = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long date_temp = Long.valueOf(ts);
        String date_string = sdf.format(new Date(date_temp));

        //3.获取rowKey
        String rowKey = uid + "_" + date_string;

        //4.创建put对象
        Put contPut = new Put(Bytes.toBytes(rowKey));

        //5.给put对象赋值
        contPut.addColumn(Bytes.toBytes(constants.CONTENT_TABLE_CF), Bytes.toBytes("content"), Bytes.toBytes(content));

        //6.执行插入数据操作
        contTable.put(contPut);

        //关闭资源
        //contTable.close();
        //connection.close();
    }

    //2、删除微博
    public static void deleteWeiBo(String uidAndTime) throws IOException {
        deleteData(CONTENT_TABLE, uidAndTime);
    }

    //3、关注用户
    public static void addAttends(String uid, String attend) throws IOException {

        //获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //操作用户关系表
        //1.获取用户关系表对象
        Table relaTable = connection.getTable(TableName.valueOf(RELATION_TABLE));

        //2.创建一个集合用于存放用户关系表的Put对象
        ArrayList<Put> relaPuts = new ArrayList<>();

        //3.创建操作者的put对象
        Put uidPut = new Put(Bytes.toBytes(uid));

        //4.创建被关注者的put对象
        //5.给操作者的put对象赋值
        uidPut.addColumn(Bytes.toBytes(constants.RELATION_TABLE_CF1), Bytes.toBytes(attend), Bytes.toBytes(attend));

        //6.创建被关注者的put对象
        Put attendPut = new Put(Bytes.toBytes(attend));

        //7.给被关注者的put对象赋值
        attendPut.addColumn(Bytes.toBytes(constants.RELATION_TABLE_CF2), Bytes.toBytes(uid), Bytes.toBytes(uid));

        //8.将被关注者的put对象放入集合
        relaPuts.add(attendPut);

        //9.将操作者的put对象放入集合
        relaPuts.add(uidPut);

        //10.执行用户关系表的插入数据操作
        relaTable.put(relaPuts);

        //关闭资源
        relaTable.close();
        connection.close();
    }

    //4、取关用户
    public static void deleteAttends(String uid, String del) throws IOException {

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //第一部分：操作用户关系表

        //1.获取用户关系表对象
        Table relaTable = connection.getTable(TableName.valueOf(RELATION_TABLE));

        //2.创建一个集合，用于存放用户关系表的delete对象
        ArrayList<Delete> relaDeletes = new ArrayList<>();

        //3.创建操作者delete对象
        Delete uidDelete = new Delete(Bytes.toBytes(uid));

        //4.创建被取关者的delete对象
        //5.给操作者的delete对象赋值
        uidDelete.addColumns(Bytes.toBytes(constants.RELATION_TABLE_CF1), Bytes.toBytes(del));

        //6.创建被取关者的delete对象
        Delete delDelete = new Delete(Bytes.toBytes(del));

        //7.给被取关者的delete对象赋值
        delDelete.addColumns(Bytes.toBytes(constants.RELATION_TABLE_CF2), Bytes.toBytes(uid));

        //8.将被取关者的delete对象添加至集合
        relaDeletes.add(delDelete);

        //9.将操作者的delete对象添加至集合
        relaDeletes.add(uidDelete);

        //10.执行用户关系表的删除操作
        relaTable.delete(relaDeletes);

        //关闭资源
        relaTable.close();
        connection.close();
    }

    //5、用户注册
    public static void userRegister(String uid, String psw) throws IOException {
        putData(USER_TABLE, uid, "info", "password", psw);
    }

    //6、判断用户名是否重复
    public static boolean ifRepeat(String userName) throws IOException {

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(USER_TABLE));

        //2.构建scan对象
        Scan scan = new Scan();

        //3.扫描表
        ResultScanner resultScanner = table.getScanner(scan);

        //4.解析resultScanner
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                //5.解析result并打印数据
                if (userName.equals(Bytes.toString(CellUtil.cloneRow(cell)))) {
                    table.close();
                    //System.out.println("false");
                    return false;
                }
            }
        }
        //关闭表连接
        table.close();
        //System.out.println("true");
        return true;
    }

    //7、判断密码是否正确
    public static boolean pswIfRight(String userName, String psw) throws IOException {

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(USER_TABLE));

        //2.构建scan对象
        Scan scan = new Scan();

        //3.扫描表
        ResultScanner resultScanner = table.getScanner(scan);

        //4.解析resultScanner
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                //5.解析result并打印数据
                if (userName.equals(Bytes.toString(CellUtil.cloneRow(cell))) && psw.equals(Bytes.toString(CellUtil.cloneValue(cell)))) {
                    table.close();
                    //System.out.println("true");
                    return true;
                }
            }
        }
        //关闭表连接
        table.close();
        //System.out.println("false");
        return false;
    }

    //8、获取某个人所有微博详情
    public static ArrayList<ArrayList<String>> getWeiBo(String uid) throws IOException {

        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(CONTENT_TABLE));

        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();

        Scan scan = new Scan();

        //添加过滤器
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(uid));
        scan.setFilter(filter);

        ResultScanner resultScanner = table.getScanner(scan);

        //解析resultScanner
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                ArrayList<String> arr = new ArrayList<>();
                arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[0]);
                arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[1]);
                arr.add(Bytes.toString(CellUtil.cloneValue(cell)));
                arrayLists.add(arr);
            }
        }
        //6.关闭资源
        //table.close();
        //connection.close();

        return arrayLists;
    }

    //9、获取用户的关注列表
    public static ArrayList<String> attendList(String user) throws IOException {

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(RELATION_TABLE));

        //2.构建scan对象
        Scan scan = new Scan();
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(user));
        scan.setFilter(filter);
        scan.addFamily(Bytes.toBytes("attends"));

        //3.扫描表
        ResultScanner resultScanner = table.getScanner(scan);

        ArrayList<String> arrayLists = new ArrayList<>();

        //4.解析resultScanner
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                arrayLists.add(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        //关闭表连接
        //table.close();
        //System.out.println(arrayLists);
        return arrayLists;
    }

    //10、推送(所有关注用户)
    public static ArrayList<ArrayList<String>> attendPush(ArrayList<String> attend) throws IOException {

        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(CONTENT_TABLE));

        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();

        //循环遍历关注的用户
        for (String s : attend) {

            Scan scan = new Scan();

            //添加过滤器
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(s));
            scan.setFilter(filter);

            ResultScanner resultScanner = table.getScanner(scan);

            //解析resultScanner
            for (Result result : resultScanner) {
                for (Cell cell : result.rawCells()) {
                    ArrayList<String> arr = new ArrayList<>();
                    arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[0]);
                    arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[1]);
                    arr.add(Bytes.toString(CellUtil.cloneValue(cell)));
                    arrayLists.add(arr);
                }
            }
        }
        return arrayLists;
    }

    //11、随机推送(未关注用户)
    public static ArrayList<ArrayList<String>> notAttendRandomPush(ArrayList<String> attend) throws IOException {

        //获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(CONTENT_TABLE));

        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();

        //循环遍历关注的用户
        if (attend.isEmpty()) {
            Scan scan = new Scan();
            ResultScanner resultScanner = table.getScanner(scan);

            //解析resultScanner
            for (Result result : resultScanner) {
                for (Cell cell : result.rawCells()) {
                    ArrayList<String> arr = new ArrayList<>();
                    arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[0]);
                    arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[1]);
                    arr.add(Bytes.toString(CellUtil.cloneValue(cell)));
                    arrayLists.add(arr);
                }
            }
        } else {
            for (String s : attend) {

                Scan scan = new Scan();
                Filter filter = new RowFilter(CompareFilter.CompareOp.NOT_EQUAL, new SubstringComparator(s));
                scan.setFilter(filter);

                ResultScanner resultScanner = table.getScanner(scan);

                //解析resultScanner
                for (Result result : resultScanner) {
                    for (Cell cell : result.rawCells()) {
                        ArrayList<String> arr = new ArrayList<>();
                        arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[0]);
                        arr.add((Bytes.toString(CellUtil.cloneRow(cell))).split("_")[1]);
                        arr.add(Bytes.toString(CellUtil.cloneValue(cell)));
                        arrayLists.add(arr);
                    }
                }
            }
        }

        ArrayList<Integer> numList = new ArrayList<>();
        Random random = new Random();
        int i = 0;
        while (i < arrayLists.size()) {
            int randInt = random.nextInt(arrayLists.size());
            if (numList.contains(randInt)) {
            } else {
                i += 1;
                numList.add(randInt);
            }
        }

        ArrayList<ArrayList<String>> newArrayLists = new ArrayList<>();

        for (Integer integer : numList) {
            newArrayLists.add(arrayLists.get(integer));
        }
        table.close();
        return newArrayLists;
    }

    //12、获得当前用户发布微博数目
    public static int userWeiBoNum(String username) throws IOException {
        int num = 0;

        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(CONTENT_TABLE));

        Scan scan = new Scan();

        //添加过滤器
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(username));
        scan.setFilter(filter);
        //scan.setFilter(new FirstKeyOnlyFilter());

        ResultScanner rs = table.getScanner(scan);
        for (Result result : rs) {
            num += 1;
        }

        //6.关闭资源
        table.close();
        connection.close();
        return num;
    }

    //13、获得当前用户关注用户数目
    public static int userAttendNum(String username) throws IOException {
        int num = 0;

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(RELATION_TABLE));

        //2.构建scan对象
        Scan scan = new Scan();
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(username));
        scan.setFilter(filter);
        scan.addFamily(Bytes.toBytes("attends"));

        //3.扫描表
        ResultScanner resultScanner = table.getScanner(scan);

        //4.解析resultScanner
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                num += 1;
            }
        }
        //关闭表连接
        table.close();
        return num;
    }
}
