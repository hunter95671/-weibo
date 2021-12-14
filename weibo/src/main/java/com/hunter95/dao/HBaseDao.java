package com.hunter95.dao;

import com.hunter95.constants.constants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

import static com.hunter95.constants.constants.USER_TABLE;
import static com.hunter95.utils.HBaseUtil.putData;

/**
 * 1、发布微博
 * 2、删除微博
 * 3、关注用户
 * 4、取关用户
 * 5、获取微博用户详情
 * 6、获取用户的初始化页面
 * 7、用户注册
 * 8、判断用户名是否重复
 *
 */
public class HBaseDao {

    //1、发布微博
    public static void publishWeiBo(String uid,String content) throws IOException {

        //获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //第一部分：操作微博内容表
        //1.获取微博内容表对象
        Table contTable = connection.getTable(TableName.valueOf(constants.CONTENT_TABLE));

        //2.获取当前时间戳
        long ts=System.currentTimeMillis();

        //3.获取rowKey
        String rowKey=uid+"_"+ts;

        //4.创建put对象
        Put contPut = new Put(Bytes.toBytes(rowKey));

        //5.给put对象赋值
        contPut.addColumn(Bytes.toBytes(constants.CONTENT_TABLE_CF),Bytes.toBytes("content"),Bytes.toBytes(content));

        //6.执行插入数据操作
        contTable.put(contPut);

        //第二部分：操作微博收件箱表
        //1.获取关系表对象
        Table relaTable = connection.getTable(TableName.valueOf(constants.RELATION_TABLE));

        //2.获取当前发布微博人的fans列族数据
        Get get = new Get(Bytes.toBytes(uid));
        get.addFamily(Bytes.toBytes(constants.RELATION_TABLE_CF2));
        Result result = relaTable.get(get);

        //3.创建一个集合，用于存放微博内容表的put对象
        ArrayList<Put> inboxPuts = new ArrayList<>();

        //4.遍历粉丝
        for (Cell cell : result.rawCells()) {

            //5.构建微博收件箱表的put对象
            Put inboxPut = new Put(CellUtil.cloneQualifier(cell));

            //6.给微博收件箱表的put对象赋值
            inboxPut.addColumn(Bytes.toBytes(constants.INBOX_TABLE_CF),Bytes.toBytes(uid),Bytes.toBytes(rowKey));

            //7.将微博收件箱表的put对象存入集合
            inboxPuts.add(inboxPut);
        }

        //8.判断是否有粉丝
        if(inboxPuts.size()>0){

            //获取收件箱表对象
            Table inboxTable = connection.getTable(TableName.valueOf(constants.INBOX_TABLE));

            //执行收件箱表数据插入操作
            inboxTable.put(inboxPuts);

            //关闭收件箱表
            inboxTable.close();
        }

        //关闭资源
        relaTable.close();
        contTable.close();
        connection.close();

    }

    //2、关注用户
    public static void addAttends(String uid,String... attends) throws IOException {

        //校验是否添加了待关注的人
        if(attends.length<=0){
            System.out.println("请选择待关注的人！");
            return;
        }

        //获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //第一部分：操作用户关系表
        //1.获取用户关系表对象
        Table relaTable = connection.getTable(TableName.valueOf(constants.RELATION_TABLE));

        //2.创建一个集合用于存放用户关系表的Put对象
        ArrayList<Put> relaPuts = new ArrayList<>();

        //3.创建操作者的put对象
        Put uidPut = new Put(Bytes.toBytes(uid));

        //4.循环创建被关注者的put对象
        for (String attend : attends) {

            //5.给操作者的put对象赋值
            uidPut.addColumn(Bytes.toBytes(constants.RELATION_TABLE_CF1),Bytes.toBytes(attend),Bytes.toBytes(attend));
            
            //6.创建被关注者的put对象
            Put attendPut = new Put(Bytes.toBytes(attend));

            //7.给被关注者的put对象赋值
            attendPut.addColumn(Bytes.toBytes(constants.RELATION_TABLE_CF2),Bytes.toBytes(uid),Bytes.toBytes(uid));

            //8.将被关注者的put对象放入集合
            relaPuts.add(attendPut);

        }

        //9.将操作者的put对象放入集合
        relaPuts.add(uidPut);

        //10.执行用户关系表的插入数据操作
        relaTable.put(relaPuts);

        //第二部分：操作收件箱表
        //1.获取微博内容表对象
        Table contTable = connection.getTable(TableName.valueOf(constants.CONTENT_TABLE));

        //2.创建收件箱表的put对象
        Put inboxPut = new Put(Bytes.toBytes(uid));

        //3.循环attends，获取每个被关注者近期发布的微博
        for (String attend : attends) {

            //4.获取当前被关注者的近期发布的微博(scan)->集合ResultScanner
            Scan scan = new Scan(Bytes.toBytes(attend + "_"), Bytes.toBytes(attend + "|"));
            ResultScanner resultScanner = contTable.getScanner(scan);

            //定义一个时间戳
            long ts = System.currentTimeMillis();
            //5.对获取的值进行遍历
            for (Result result : resultScanner) {

                //6.给收件箱表的put对象赋值
                inboxPut.addColumn(Bytes.toBytes(constants.INBOX_TABLE_CF),Bytes.toBytes(attend),ts++,result.getRow());
                
            }

            //7.判断当前put对象是否为空
            if (inboxPut.isEmpty()){

            }

            //获取收件箱表对象
            Table inboxTable = connection.getTable(TableName.valueOf(constants.INBOX_TABLE));

            //插入数据
            inboxTable.put(inboxPut);

            //关闭收件箱表连接
            inboxTable.close();

        }
        //关闭资源
        relaTable.close();
        contTable.close();
        connection.close();
    }

    //4、取关用户
    public static void deleteAttends(String uid,String... dels) throws IOException {

        if (dels.length<=0){
            System.out.println("请添加待取关的用户！");
            return;
        }

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //第一部分：操作用户关系表

        //1.获取用户关系表对象
        Table relaTable = connection.getTable(TableName.valueOf(constants.RELATION_TABLE));

        //2.创建一个集合，用于存放用户关系表的delete对象
        ArrayList<Delete> relaDeletes = new ArrayList<>();

        //3.创建操作者delete对象
        Delete uidDelete = new Delete(Bytes.toBytes(uid));

        //4.循环创建被取关者的delete对象

        for (String del : dels) {

            //5.给操作者的delete对象赋值
            uidDelete.addColumns(Bytes.toBytes(constants.RELATION_TABLE_CF1),Bytes.toBytes(del));

            //6.创建被取关者的delete对象
            Delete delDelete = new Delete(Bytes.toBytes(del));

            //7.给被取关者的delete对象赋值
            delDelete.addColumns(Bytes.toBytes(constants.RELATION_TABLE_CF2),Bytes.toBytes(uid));

            //8.将被取关者的delete对象添加至集合
            relaDeletes.add(delDelete);

        }

        //9.将操作者的delete对象添加至集合
        relaDeletes.add(uidDelete);

        //10.执行用户关系表的删除操作
        relaTable.delete(relaDeletes);

        //第二部分：操作收件箱表
        //1.获取收件箱表对象
        Table inboxTable = connection.getTable(TableName.valueOf(constants.INBOX_TABLE));

        //2.创建操作者的delete对象
        Delete inboxDelete = new Delete(Bytes.toBytes(uid));

        //3.给操作者的delete对象赋值
        for (String del : dels) {
            inboxDelete.addColumns(Bytes.toBytes(constants.INBOX_TABLE_CF),Bytes.toBytes(del));
        }

        //4.执行收件箱表删除操作
        inboxTable.delete(inboxDelete);

        //关闭资源
        relaTable.close();
        inboxTable.close();
        connection.close();
    }

    //5、获取用户的初始化页面数据
    public static void getInit(String uid) throws IOException {

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //2.获取收件箱表对象
        Table inboxTable = connection.getTable(TableName.valueOf(constants.INBOX_TABLE));
        
        //3.获取微博内容表对象
        Table contTable = connection.getTable(TableName.valueOf(constants.CONTENT_TABLE));

        //4.创建收件箱表get对象，并获取数据(设置最大版本)
        Get inboxGet = new Get(Bytes.toBytes(uid));
        inboxGet.setMaxVersions();
        Result result = inboxTable.get(inboxGet);

        //5.遍历获取数据
        for (Cell cell : result.rawCells()) {

            //6.构建微博内容表get对象
            Get contGet = new Get(CellUtil.cloneValue(cell));
            
            //7.获取该get对象的数据内容
            Result contResult = contTable.get(contGet);

            //8.解析内容并打印
            for (Cell contCell : contResult.rawCells()) {

                System.out.println("RK："+Bytes.toString(CellUtil.cloneRow(cell))+
                        "，CF："+Bytes.toString(CellUtil.cloneFamily(cell))+
                        "，CN："+Bytes.toString(CellUtil.cloneQualifier(cell))+
                        "，Value："+Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        //关闭资源
        inboxTable.close();
        contTable.close();
        connection.close();
    }

    //6、获取某个人所有微博详情
    public static void getWeiBo(String uid) throws IOException {

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //2.获取微博内容表对象
        Table table = connection.getTable(TableName.valueOf(constants.CONTENT_TABLE));

        //3.构建scan对象
        Scan scan = new Scan();

        //构建过滤器
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator(uid+"_"));

        scan.setFilter(rowFilter);

        //4.获取数据
        ResultScanner scanner = table.getScanner(scan);

        //5.解析数据并打印
        for (Result result : scanner) {

            for (Cell cell : result.rawCells()) {
                System.out.println("RK："+Bytes.toString(CellUtil.cloneRow(cell))+
                        "，CF："+Bytes.toString(CellUtil.cloneFamily(cell))+
                        "，CN："+Bytes.toString(CellUtil.cloneQualifier(cell))+
                        "，Value："+Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        //6.关闭资源
        table.close();
        connection.close();
    }
    //7、用户注册
    public static void userRegister(String uid,String psw) throws IOException {
        putData(USER_TABLE,uid,"info","password",psw);
    }

    //8、判断用户名是否重复
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
                if(userName.equals(Bytes.toString(CellUtil.cloneRow(cell)))){
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
}
