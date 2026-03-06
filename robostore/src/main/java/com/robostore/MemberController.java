package com.robostore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MyUserRepository myUserRepository;

    record LoginRequest(String account, String password) {}

    record UserInfo(String account, String name, int level) {}

    record MemberInfo(Long id, String name, String email, String phonenumber, int level,
                      boolean phoneVerified, boolean emailVerified) {}

    record MemberForm(String name, String email, String password, String phonenumber, int level,
                      boolean phoneVerified, boolean emailVerified) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // 依序用 email → name → phonenumber 查詢
        Optional<myUser> found = myUserRepository.findByEmail(req.account());
        if (found.isEmpty()) found = myUserRepository.findByName(req.account());
        if (found.isEmpty()) { List<myUser> byPhone = myUserRepository.findByPhonenumber(req.account()); if (!byPhone.isEmpty()) found = Optional.of(byPhone.get(0)); }
        if (found.isPresent() && found.get().getPassword().equals(req.password())) {
            myUser user = found.get();
            return ResponseEntity.ok(new UserInfo(user.getEmail(), user.getName(), user.getLevel()));
        }
        return ResponseEntity.status(401).body(Map.of("error", "帳號或密碼錯誤"));
    }

    @GetMapping("/members")
    public List<MemberInfo> getMembers() {
        return myUserRepository.findAll().stream()
            .map(this::toInfo)
            .toList();
    }

    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody MemberForm form) {
        if (form.name() == null || form.name().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "姓名不得為空"));
        if (form.email() == null || form.email().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Email 不得為空"));
        if (!form.email().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            return ResponseEntity.badRequest().body(Map.of("error", "Email 格式錯誤"));
        if (myUserRepository.findByName(form.name()).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error", "姓名已存在"));
        if (myUserRepository.findByEmail(form.email()).isPresent())
            return ResponseEntity.badRequest().body(Map.of("error", "Email 已存在"));
        if (form.phonenumber() == null || form.phonenumber().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "電話為必填"));
        if (!form.phonenumber().matches("^09\\d{8}$"))
            return ResponseEntity.badRequest().body(Map.of("error", "電話格式錯誤，須為09開頭共10碼"));
        if (!myUserRepository.findByPhonenumber(form.phonenumber()).isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "電話號碼已存在"));
        myUser u = new myUser(form.name(), form.email(),
                form.password() != null ? form.password() : "",
                form.phonenumber(), form.level());
        u.setPhoneVerified(form.phoneVerified());
        u.setEmailVerified(form.emailVerified());
        return ResponseEntity.ok(toInfo(myUserRepository.save(u)));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberForm form) {
        Optional<myUser> opt = myUserRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        myUser u = opt.get();
        if (form.name() != null && !form.name().isBlank()) {
            Optional<myUser> byName = myUserRepository.findByName(form.name());
            if (byName.isPresent() && !byName.get().getId().equals(id))
                return ResponseEntity.badRequest().body(Map.of("error", "姓名已存在"));
            u.setName(form.name());
        }
        if (form.email() != null && !form.email().isBlank()) {
            if (!form.email().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
                return ResponseEntity.badRequest().body(Map.of("error", "Email 格式錯誤"));
            Optional<myUser> byEmail = myUserRepository.findByEmail(form.email());
            if (byEmail.isPresent() && !byEmail.get().getId().equals(id))
                return ResponseEntity.badRequest().body(Map.of("error", "Email 已存在"));
            u.setEmail(form.email());
        }
        if (form.phonenumber() == null || form.phonenumber().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "電話為必填"));
        if (!form.phonenumber().matches("^09\\d{8}$"))
            return ResponseEntity.badRequest().body(Map.of("error", "電話格式錯誤，須為09開頭共10碼"));
        List<myUser> byPhone = myUserRepository.findByPhonenumber(form.phonenumber());
        if (!byPhone.isEmpty() && !byPhone.get(0).getId().equals(id))
            return ResponseEntity.badRequest().body(Map.of("error", "電話號碼已存在"));
        if (form.password() != null && !form.password().isBlank()) u.setPassword(form.password());
        u.setPhonenumber(form.phonenumber());
        u.setLevel(form.level());
        u.setPhoneVerified(form.phoneVerified());
        u.setEmailVerified(form.emailVerified());
        return ResponseEntity.ok(toInfo(myUserRepository.save(u)));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        if (!myUserRepository.existsById(id)) return ResponseEntity.notFound().build();
        myUserRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    private MemberInfo toInfo(myUser u) {
        return new MemberInfo(u.getId(), u.getName(), u.getEmail(), u.getPhonenumber(), u.getLevel(),
                u.isPhoneVerified(), u.isEmailVerified());
    }
}
