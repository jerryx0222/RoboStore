package com.robostore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * 系統啟動時自動檢查並初始化資料庫：
 *   1. 確認資料庫是否存在，不存在則建立
 *   2. 確認 Schema 是否存在（PostgreSQL），不存在則建立
 *   3. 確認資料表 member 是否存在，不存在則建立
 */
@Component
@Order(1)   // 確保在其他 CommandLineRunner 之前執行
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    private MyUserRepository myUserRepository;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) {
        boolean isMySQL = Arrays.asList(environment.getActiveProfiles()).contains("mysql");
        System.out.println("\n========== 資料庫初始化檢查 ==========");
        if (isMySQL) {
            checkAndInitMySQL();
        } else {
            checkAndInitPostgreSQL();
        }
        seedDefaultUsers();
        System.out.println("=======================================\n");
    }

    // ─── 預設帳號 ─────────────────────────────────────────────────────────────

    private void seedDefaultUsers() {
        if (!myUserRepository.existsByLevel(myUser.LEVEL_SYSTEM_DEV)) {
            myUser superUser = new myUser(
                "supernan",
                "jerryx0222@gmail.com",
                "hcs09871234",
                "0952666742",
                myUser.LEVEL_SYSTEM_DEV
            );
            myUserRepository.save(superUser);
            System.out.println("✔ 預設系統開發者帳號 [supernan] 建立成功");
        } else {
            System.out.println("✔ 預設系統開發者帳號已存在");
        }
    }

    // ─── PostgreSQL ───────────────────────────────────────────────────────────

    private void checkAndInitPostgreSQL() {
        // 解析 URL：jdbc:postgresql://host:port/dbName?currentSchema=schema
        String urlBody = datasourceUrl.replace("jdbc:postgresql://", "");
        String hostPort = urlBody.split("/")[0];
        String dbAndQuery = urlBody.substring(hostPort.length() + 1);
        String dbName = dbAndQuery.contains("?") ? dbAndQuery.split("\\?")[0] : dbAndQuery;
        String schema = "public";
        if (dbAndQuery.contains("currentSchema=")) {
            schema = dbAndQuery.split("currentSchema=")[1].split("&")[0];
        }
        String host = hostPort.contains(":") ? hostPort.split(":")[0] : hostPort;
        int port = hostPort.contains(":") ? Integer.parseInt(hostPort.split(":")[1]) : 5432;

        // 步驟一：確認資料庫是否存在
        if (!pgDatabaseExists(host, port, dbName)) {
            pgCreateDatabase(host, port, dbName);
        }

        // 步驟二：確認 Schema 是否存在
        try (Connection con = DriverManager.getConnection(datasourceUrl, username, password);
             Statement stmt = con.createStatement()) {

            checkAndCreatePgSchema(stmt, schema);

            // 步驟三：確認資料表是否存在
            checkAndCreatePgTable(stmt, schema);

        } catch (SQLException e) {
            System.err.println("✘ PostgreSQL Schema/Table 檢查失敗: " + e.getMessage());
        }
    }

    private boolean pgDatabaseExists(String host, int port, String dbName) {
        try (Connection _ = DriverManager.getConnection(datasourceUrl, username, password)) {
            System.out.println("✔ 資料庫 [" + dbName + "] 已存在");
            return true;
        } catch (SQLException e) {
            System.out.println("✘ 資料庫 [" + dbName + "] 不存在");
            return false;
        }
    }

    private void pgCreateDatabase(String host, int port, String dbName) {
        String sysUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
        try (Connection con = DriverManager.getConnection(sysUrl, username, password);
             Statement stmt = con.createStatement()) {
            stmt.execute("CREATE DATABASE \"" + dbName + "\"");
            System.out.println("✔ 資料庫 [" + dbName + "] 建立成功");
        } catch (SQLException e) {
            System.err.println("✘ 資料庫建立失敗: " + e.getMessage());
        }
    }

    private void checkAndCreatePgSchema(Statement stmt, String schema) throws SQLException {
        ResultSet rs = stmt.executeQuery(
            "SELECT schema_name FROM information_schema.schemata WHERE schema_name = '" + schema + "'");
        if (rs.next()) {
            System.out.println("✔ Schema [" + schema + "] 已存在");
        } else {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");
            System.out.println("✔ Schema [" + schema + "] 建立成功");
        }
        rs.close();
    }

    private void checkAndCreatePgTable(Statement stmt, String schema) throws SQLException {
        ResultSet rs = stmt.executeQuery(
            "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = '" + schema + "' AND table_name = 'member'");
        if (rs.next()) {
            System.out.println("✔ 資料表 [" + schema + ".member] 已存在");
        } else {
            stmt.execute(
                "CREATE TABLE \"" + schema + "\".member (" +
                "  id       SERIAL       PRIMARY KEY," +
                "  name     VARCHAR(100) NOT NULL," +
                "  email    VARCHAR(150)," +
                "  password VARCHAR(255)" +
                ")");
            System.out.println("✔ 資料表 [" + schema + ".member] 建立成功");
        }
        rs.close();
    }

    // ─── MySQL ────────────────────────────────────────────────────────────────

    private void checkAndInitMySQL() {
        // 解析 URL：jdbc:mysql://host:port/dbName?params
        String urlNoParams = datasourceUrl.contains("?")
            ? datasourceUrl.substring(0, datasourceUrl.indexOf("?")) : datasourceUrl;
        String params = datasourceUrl.contains("?")
            ? datasourceUrl.substring(datasourceUrl.indexOf("?") + 1) : "";
        String urlBody = urlNoParams.replace("jdbc:mysql://", "");
        String hostPort = urlBody.split("/")[0];
        String dbName = urlBody.substring(hostPort.length() + 1);
        String host = hostPort.contains(":") ? hostPort.split(":")[0] : hostPort;
        int port = hostPort.contains(":") ? Integer.parseInt(hostPort.split(":")[1]) : 3306;

        // 步驟一：確認資料庫是否存在
        if (!mysqlDatabaseExists(dbName)) {
            mysqlCreateDatabase(host, port, dbName, params);
        }

        // 步驟二：確認資料表是否存在
        try (Connection con = DriverManager.getConnection(datasourceUrl, username, password);
             Statement stmt = con.createStatement()) {
            checkAndCreateMysqlTable(stmt, dbName);
        } catch (SQLException e) {
            System.err.println("✘ MySQL Table 檢查失敗: " + e.getMessage());
        }
    }

    private boolean mysqlDatabaseExists(String dbName) {
        try (Connection _ = DriverManager.getConnection(datasourceUrl, username, password)) {
            System.out.println("✔ 資料庫 [" + dbName + "] 已存在");
            return true;
        } catch (SQLException e) {
            System.out.println("✘ 資料庫 [" + dbName + "] 不存在");
            return false;
        }
    }

    private void mysqlCreateDatabase(String host, int port, String dbName, String params) {
        String sysUrl = "jdbc:mysql://" + host + ":" + port + "/?" + params;
        try (Connection con = DriverManager.getConnection(sysUrl, username, password);
             Statement stmt = con.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS `" + dbName + "`");
            System.out.println("✔ 資料庫 [" + dbName + "] 建立成功");
        } catch (SQLException e) {
            System.err.println("✘ 資料庫建立失敗: " + e.getMessage());
        }
    }

    private void checkAndCreateMysqlTable(Statement stmt, String dbName) throws SQLException {
        ResultSet rs = stmt.executeQuery(
            "SELECT table_name FROM information_schema.tables " +
            "WHERE table_schema = '" + dbName + "' AND table_name = 'member'");
        if (rs.next()) {
            System.out.println("✔ 資料表 [member] 已存在");
        } else {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS member (" +
                "  id       INT AUTO_INCREMENT PRIMARY KEY," +
                "  name     VARCHAR(100) NOT NULL," +
                "  email    VARCHAR(150)," +
                "  password VARCHAR(255)" +
                ")");
            System.out.println("✔ 資料表 [member] 建立成功");
        }
        rs.close();
    }
}
