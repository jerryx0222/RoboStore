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
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@SpringBootApplication
public class RobostoreApplication {

	public static void main(String[] args) {
		// http://127.0.0.1:8080/
		SpringApplication.run(RobostoreApplication.class, args);

		// 載入MySQL Driver/Connector
		try{
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	// 處理來自路徑"/"的HTTP GET請求
	@GetMapping("/")
	public String index(){
		return "Hello World Spring boot!";
	}
	

	private String[] executeSQL(){
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			// 建立連線
			con=DriverManager.getConnection("jdbc:mysql://localhost/website?user=root&password=12345");
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

	// 處理來自路徑"/execute"的HTTP GET請求
	@GetMapping("/execute")
	public String[] execute(){
		String[] data=executeSQL();
		return data;	// 回傳JSON格式的字串
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

	// 處理來自路徑"/test"的HTTP GET請求
	@GetMapping("/test")
	public int[] test(){
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
