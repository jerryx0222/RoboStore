package com.robostore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 將所有非 API、非靜態資源的請求轉交給 Vue SPA 的 index.html，
 * 讓 Vue Router (HTML5 history mode) 在 Spring Boot 下正常運作。
 */
@Controller
public class SpaController {

    // 第一層路由（不含 . 的路徑，非 api / assets 開頭）
    // 例：/news, /member, /cart, /login
    @GetMapping("/{path:^(?!api$|assets$)[^\\.]*$}")
    public String spa() {
        return "forward:/index.html";
    }

    // 第二層路由（僅 /products/:category）
    @GetMapping("/products/{category:[^\\.]*}")
    public String spaProducts() {
        return "forward:/index.html";
    }
}
