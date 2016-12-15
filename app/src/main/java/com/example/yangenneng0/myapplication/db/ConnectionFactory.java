package com.example.yangenneng0.myapplication.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: yangenneng
 * DateTime: 2016/12/15 18:38
 * Description:数据库连接工厂类
 * 单例模式
 */
public class ConnectionFactory {
    private static final String DB_DRIVER="com.mysql.jdbc.Driver";      //数据库驱动
    private static final String DB_URL = "jdbc:mysql://119.29.89.224:3306/qqdb?useUnicode=true&characterEncoding=UTF-8";//连接数据库的URL地址
    private static final String DB_USERNAME = "root";                   //数据库的用户名
    private static final String DB_PASSWORD ="";                   //数据库的密码

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DB_DRIVER);//反射机制
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
