package com.robostore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "myManufacturers")
public class myManufacturers {

    // ─── 狀態常數 ──────────────────────────────────────────────────────────────
    public static final String STATUS_ACTIVE   = "啟用";
    public static final String STATUS_INACTIVE = "停用";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 廠商名稱 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 聯絡電話 */
    @Column(length = 30)
    private String phone;

    /** 狀態：啟用 / 停用 */
    @Column(nullable = false, length = 10)
    private String status = STATUS_ACTIVE;

    /** 統一編號（8碼） */
    @Column(unique = true, length = 8)
    private String taxId;

    /** 地址 */
    @Column(length = 200)
    private String address;

    /** 分店名稱 */
    @Column(length = 100)
    private String branchName;

    /** 集團名稱 */
    @Column(length = 100)
    private String groupName;

    // ─── Constructors ─────────────────────────────────────────────────────────

    public myManufacturers() {}

    public myManufacturers(String name, String phone, String status,
                           String taxId, String address,
                           String branchName, String groupName) {
        this.name       = name;
        this.phone      = phone;
        this.status     = status;
        this.taxId      = taxId;
        this.address    = address;
        this.branchName = branchName;
        this.groupName  = groupName;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
}
