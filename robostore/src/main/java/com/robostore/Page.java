package com.robostore;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Page {

    @GetMapping("/back-template")
    public String backtemplate(Model model,HttpSession session) {
        String name=(String)session.getAttribute("user-name");
        model.addAttribute("name",name);
        return "back";
    }
    

    @GetMapping("/multiply")
    public String multiply(Model model,@RequestParam int n1,@RequestParam int n2) {
        int result=n1*n2;
        model.addAttribute("result",result);
        return "multiply";          // 對應樣板的檔案名稱
    }
    
    
}
