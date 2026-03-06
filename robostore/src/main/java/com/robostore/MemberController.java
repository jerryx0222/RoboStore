package com.robostore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MyUserRepository myUserRepository;

    record LoginRequest(String account, String password) {}

    record UserInfo(String account, String name, int level) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<myUser> found = myUserRepository.findByEmail(req.account());
        if (found.isPresent() && found.get().getPassword().equals(req.password())) {
            myUser user = found.get();
            return ResponseEntity.ok(new UserInfo(user.getEmail(), user.getName(), user.getLevel()));
        }
        return ResponseEntity.status(401).body(Map.of("error", "帳號或密碼錯誤"));
    }
}
