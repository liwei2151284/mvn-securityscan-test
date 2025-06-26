package com.example.jfrog.demo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConfigPasswordDemo {

    public static void connectUsingPlainPassword() {
        try {
            // 加载配置文件
            Properties props = new Properties();
            InputStream input = new FileInputStream("application.properties");
            props.load(input);

            // 获取数据库连接信息（包含明文密码）
            String url = props.getProperty("db.url");
            String username = props.getProperty("user");
            String password = props.getProperty("PASSWORD");

            // 使用明文密码连接数据库
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected with user: " + username);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
