package com.robostore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemberController {

    record LoginRequest(String account, String password) {}

    record UserInfo(String account, String name, String level, String joinDate) {}

    private static final Map<String, Map.Entry<String, UserInfo>> USERS = Map.of(
        "wang_ming@email.com", Map.entry("123456",
            new UserInfo("wang_ming@email.com", "王小明", "一般會員", "2022-05-01")),
        "admin@robostore.com", Map.entry("admin123",
            new UserInfo("admin@robostore.com", "管理員", "VIP 會員", "2020-01-01"))
    );

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var entry = USERS.get(req.account());
        if (entry != null && entry.getKey().equals(req.password())) {
            return ResponseEntity.ok(entry.getValue());
        }
        return ResponseEntity.status(401).body(Map.of("error", "帳號或密碼錯誤"));
    }
}
