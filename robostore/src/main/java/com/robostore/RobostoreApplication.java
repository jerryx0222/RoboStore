package com.robostore;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import org.springframework.web.bind.annotation.RequestBody;


@RestController
@SpringBootApplication
public class RobostoreApplication {

	public static void main(String[] args) {
		// http://127.0.0.1:8080/
		SpringApplication.run(RobostoreApplication.class, args);

		// 載入MySQL Driver/Connector
		try{
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			System.out.println("MySQL Driver 載入成功！");
		}catch(Exception e){
			System.out.println("MySQL驅動載入失敗: " + e.getMessage());
		}

		// 載入 PostgreSQL Driver
		try {
			// PostgreSQL 的驅動類別名稱
			Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
			System.out.println("PostgreSQL Driver 載入成功！");
		} catch (Exception e) {
			System.out.println("PostgreSQL驅動載入失敗: " + e.getMessage());
		}
	}


	private String[] executeMySQL(){
			Connection con=null;
			Statement stmt=null;
			ResultSet rs=null;
			try{
				// 建立連線
				con=DriverManager.getConnection("jdbc:mysql://localhost/website?user=root&password=password");
				stmt=con.createStatement();

				stmt.execute("select * from member");
				rs=stmt.getResultSet();
				ArrayList<String> data=new ArrayList<String>();
				while(rs.next()){
					data.add(rs.getString("name"));	
					//System.out.println(rs.getInt("id"));
					//System.out.println(rs.getString("name"));
				}
				return data.toArray(new String[0]);	// 轉成字串陣列回轉

				/* update data
				stmt.execute("update member set name='jack' where id=1");
				System.out.println(stmt.getUpdateCount());
				*/

			}catch(SQLException e){
				System.out.println(e.getMessage());
			}finally{
				// 確保關閉資料庫
				if(con!=null){
					try{
						con.close();
					}catch(SQLException e){
						System.out.println(e.getMessage());
					}
				}
			}
			return null;
		}

	private String[] executePostgreSQL() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// 1. 建立連線 (修改重點：URL 協定改為 postgresql，Port 改為 5432)
			// 格式：jdbc:postgresql://主機名:連接埠/資料庫名?user=帳號&password=密碼
			String url = "jdbc:postgresql://localhost:5432/JerryX?user=postgres&password=password&currentSchema=website";

			con = DriverManager.getConnection(url);
			
			stmt = con.createStatement();

			// 2. 執行查詢
			stmt.execute("select * from member");
			rs = stmt.getResultSet();
			
			ArrayList<String> data = new ArrayList<String>();
			while (rs.next()) {
				// 取得名字欄位 (假設欄位名稱仍為 name)
				data.add(rs.getString("name")); 
			}
			
			return data.toArray(new String[0]);

		} catch (SQLException e) {
			System.err.println("資料庫錯誤: " + e.getMessage());
		} finally {
			// 3. 確保關閉資源 (建議從 ResultSet 到 Connection 依序關閉)
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


	// 處理來自路徑"/execute"的HTTP GET請求
	@GetMapping("/execute")
	public String[] execute(){
		boolean bRet=false;
		if(bRet)
		{
			String[] data1 = executeMySQL();
			return data1;	// 回傳JSON格式的字串
		}
		else
		{
			String[] data2 = executePostgreSQL();
			return data2;	// 回傳JSON格式的字串
		}
	}

	// 處理來自路徑"hello?name=名字"的HTTP GET請求
	@GetMapping("/hello")
	public String hello(HttpSession session,@RequestParam String name){
		session.setAttribute("user-name", name);
		return "Hello World Spring boot-" + name + "!";
	}

	// 處理來自路徑"back"的HTTP GET請求
	@GetMapping("/back")
	public String back(HttpSession session){
		String name = (String)session.getAttribute("user-name");
		if(name==null)
			return "Who are yout?";
		else
			return "Welcome Back,"+name;
	}

	// 處理來自路徑"/test1"的HTTP GET請求
	@GetMapping("/test1")
	public int[] test1(){
		int[] data=new int[]{10,11,12,13};
		return data;
	}

	// 處理來自路徑"/testList"的HTTP GET請求
	@GetMapping("/testList")
	public List<Integer> testList(){
		List<Integer> data=List.of(10,11,12,13);
		return data;
	}

	// 處理來自路徑"/testMap"的HTTP GET請求
	@GetMapping("/testMap")
	public Map<Integer,Integer> testMap(){
		Map<Integer,Integer> data=Map.of(10,11,12,13);
		return data;
	}

		// 處理來自路徑"/testPoint"的HTTP GET請求
	@GetMapping("/testPoint")
	public List<Point> testPoint(){
		Point data1=new Point(10,11);
		Point data2=new Point(12,13);
		return List.of(data1,data2);
	}

	// 處理來自路徑"echo?name=名字"的HTTP GET請求
	@GetMapping("/echo")
	public String echo(@RequestParam String name){
		return "Hello World Spring boot-" + name + "!";
	}

	// 處理來自路徑"add1?n1=整數&n2=整數"的HTTP GET請求
	@GetMapping("/add1")
	public Map<String,Integer> add1(@RequestParam int n1, @RequestParam int n2){
		return Map.of("Add result:",(n1+n2));
	}

	// 處理來自路徑"add?n1=整數&n2=整數"的HTTP GET請求
	@GetMapping("/addG")
	public String addG(@RequestParam int n1, @RequestParam int n2){
		return "Answer is " + (n1+n2);
	}

	// 處理來自路徑"add?n1=整數&n2=整數"的HTTP POST請求
	@PostMapping("add")
	public String postMethodName(@RequestParam int n1, @RequestParam int n2){
		return "Answer is " + (n1+n2);
	}
	
	public String add(@RequestParam int n1, @RequestParam int n2){
		return "Answer is " + (n1+n2);
	}

	// 處理來自路徑"/user/字串"的HTTP GET請求
	@GetMapping("/user/{myString}")
	public String replyUser(@PathVariable String myString){
		return "Hello World Spring boot-" + myString + "!";
	}

	// 處理來自路徑"/square/整數"的HTTP GET請求
	@GetMapping("/square/{number}")
	public String replySquare(@PathVariable int number){
		return "Hello World Spring boot-" + number*number + "!";
	}
}
