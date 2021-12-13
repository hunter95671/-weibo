package com.hunter95.utils;

import com.hunter95.constants.constants;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 1、创建命名空间
 * 2、判断表是否存在
 * 3、创建表(三张表)
 * 4、向表插入数据
 * 5、数据删除
 */
public class HBaseUtil {

    //1、创建命名空间
    public static void createNameSpace(String nameSpace) throws IOException {

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //2.获取admin对象
        Admin admin = connection.getAdmin();

        //3.构建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(nameSpace).build();

        //4.创建命名空间
        admin.createNamespace(namespaceDescriptor);

        //5.关闭资源
        admin.close();
        connection.close();
    }

    //2、判断表是否存在
    private static boolean isTableExist(String tableName) throws IOException {

        //1.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //2.获取admin对象
        Admin admin = connection.getAdmin();

        //3.判断是否存在
        boolean exists = admin.tableExists(TableName.valueOf(tableName));

        //4.关闭资源
        admin.close();
        connection.close();

        //5.返回结果
        return exists;
    }

    //3、创建表
    public static void createTable(String tableName,int versions,String... cfs) throws IOException {

        //1.判断是否传入了列族信息
        if (cfs.length<=0){
            System.out.println("请输入列族信息！");
            return;
        }

        //2.判断表是否存在
        if(isTableExist(tableName)){
            System.out.println(tableName+"已存在");
        }

        //3.获取connection对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);

        //4.获取admin对象
        Admin admin = connection.getAdmin();

        //5.创建表描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        //6.循环添加列族信息
        for (String cf : cfs) {

            //7创建列族描述器
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);

            //8.设置版本
            hColumnDescriptor.setMaxVersions(versions);

            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        //9.创建表操作
        admin.createTable(hTableDescriptor);

        //10.关闭资源
        admin.close();
        connection.close();
    }
    //4、向表插入数据
    public static void putData(String tableName,String rowKey,String cf,String cn,String value) throws IOException {

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(tableName));

        //2.创建put对象
        Put put = new Put(Bytes.toBytes(rowKey));

        //3.给put对象赋值
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));

        //4.插入数据
        table.put(put);

        //5.关闭连接
        table.close();

    }
    //5、数据删除
    public static void deleteData(String tableName,String rowKey,String cf,String cn) throws IOException {

        //1.获取表对象
        Connection connection = ConnectionFactory.createConnection(constants.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(tableName));

        //2.构建删除对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));

        //2.1设置删除的列
        //delete.addColumns(Bytes.toBytes(cf),Bytes.toBytes(cn));

        //2.2删除指定的列族
        //delete.addFamily(Bytes.toBytes(cf));

        //3.执行删除操作
        table.delete(delete);

        //4.关闭连接
        table.close();
    }
}
