package com.robostore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "myUser")
public class myUser {

    // ─── Level 權限定義（數值越小，權限越高）─────────────────────────────────
    // 系統層級
    public static final int LEVEL_SYSTEM_DEV   =  0;  // 系統開發者
    public static final int LEVEL_SYSTEM_ADMIN =  1;  // 系統管理者
    public static final int LEVEL_SYSTEM_USER  =  2;  // 系統使用者
    // 1.廠商管理

    // 廠商層級
    public static final int LEVEL_VENDOR_DEV   = 10;  // 廠商開發者

    public static final int LEVEL_VENDOR_ADMIN = 11;  // 廠商管理者
    // 1.分店管理 
    // 2.多店訂單管理

    public static final int LEVEL_VENDOR_USER  = 12;  // 廠商使用者
    // 3.單店訂單管理

    // 個人層級
    public static final int LEVEL_PERSONAL_DEV   = 20; // 個人開發者
    public static final int LEVEL_PERSONAL_ADMIN = 21; // 個人管理者
    
    public static final int LEVEL_PERSONAL_USER  = 22; // 個人使用者
    // 1.現場:訂購/繳費/取貨
    // 2.網路:訂購/繳費/取貨

    public static final int LEVEL_GUEST  = 99; // 訪客
    // 3.現場:訂購/繳費/取貨

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 20)
    private String phonenumber;

    @Column
    private int level;

    @Column(nullable = false)
    private boolean phoneVerified = false;

    @Column(nullable = false)
    private boolean emailVerified = false;

    // ─── Constructors ─────────────────────────────────────────────────────────

    public myUser() {}

    public myUser(String name, String email, String password, String phonenumber, int level) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phonenumber = phonenumber;
        this.level = level;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhonenumber() { return phonenumber; }
    public void setPhonenumber(String phonenumber) { this.phonenumber = phonenumber; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public boolean isPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(boolean phoneVerified) { this.phoneVerified = phoneVerified; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public boolean isVerified() { return phoneVerified && emailVerified; }
}
