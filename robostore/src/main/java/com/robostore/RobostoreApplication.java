package com.robostore;

import java.util.List;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@SpringBootApplication
public class RobostoreApplication {

	public static void main(String[] args) {
		// http://127.0.0.1:8080/
		SpringApplication.run(RobostoreApplication.class, args);

	}

	// 處理來自路徑"/"的HTTP GET請求
	@GetMapping("/")
	public String index(){
		return "Hello World Spring boot!";
	}
	
	// 處理來自路徑"/test"的HTTP GET請求
	@GetMapping("/test")
	public int[] test(){
		int[] data=new int[]{10,11,12,13};
		return data;
	}

	// 處理來自路徑"/testList"的HTTP GET請求
	@GetMapping("/testList")
	public List testList(){
		List data=List.of(10,11,12,13);
		return data;
	}

	// 處理來自路徑"/testMap"的HTTP GET請求
	@GetMapping("/testMap")
	public Map testMap(){
		Map data=Map.of(10,11,12,13);
		return data;
	}

		// 處理來自路徑"/testPoint"的HTTP GET請求
	@GetMapping("/testPoint")
	public Point testPoint(){
		Point data=new Point(10,11);
		return data;
	}

	// 處理來自路徑"echo?name=名字"的HTTP GET請求
	@GetMapping("/echo")
	public String echo(@RequestParam String name){
		return "Hello World Spring boot-" + name + "!";
	}

	// 處理來自路徑"add?n1=整數&n2=整數"的HTTP GET請求
	@GetMapping("/add")
	public String add(@RequestParam int n1, @RequestParam int n2){
		return "Hello World Spring boot-" + (n1+n2) + "!";
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
