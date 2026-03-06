package com.robostore;

import java.util.ArrayList;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@SpringBootApplication
public class RobostoreApplication {

	// Spring 依 application.yaml 的 active profile 自動注入對應的 DataSource
	@Autowired
	private DataSource dataSource;

	// 讀取目前啟用的 profile（mysql 或 postgresql）
	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(RobostoreApplication.class, args);
	}

	// 使用 application-mysql.yaml 設定的連線參數
	private String[] executeMySQL() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			stmt = con.createStatement();
			stmt.execute("select * from member");
			rs = stmt.getResultSet();
			ArrayList<String> data = new ArrayList<String>();
			while (rs.next()) {
				data.add(rs.getString("name"));
			}
			return data.toArray(new String[0]);
		} catch (SQLException e) {
			System.err.println("MySQL 查詢失敗: " + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("關閉資源失敗: " + e.getMessage());
			}
		}
		return null;
	}

	// 使用 application-postgresql.yaml 設定的連線參數
	private String[] executePostgreSQL() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = dataSource.getConnection();
			stmt = con.createStatement();
			stmt.execute("select * from member");
			rs = stmt.getResultSet();
			ArrayList<String> data = new ArrayList<String>();
			while (rs.next()) {
				data.add(rs.getString("name"));
			}
			return data.toArray(new String[0]);
		} catch (SQLException e) {
			System.err.println("PostgreSQL 查詢失敗: " + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("關閉資源失敗: " + e.getMessage());
			}
		}
		return null;
	}

	// 依 application.yaml 的 active profile 自動選擇資料庫
	@GetMapping("/execute")
	public String[] execute() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			if (profile.equals("mysql")) {
				return executeMySQL();
			}
		}
		return executePostgreSQL();
	}

}