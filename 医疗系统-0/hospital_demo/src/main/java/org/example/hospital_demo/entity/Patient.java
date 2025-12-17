package org.example.hospital_demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 病人实体类
 * 负责存储病人的基本信息
 */
@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "姓名不能为空")
    @Column(nullable = false, length = 50)
    private String name;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(unique = true, length = 20)
    private String phone;
    
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确")
    @Column(name = "id_card", length = 18)
    private String idCard;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    
    private Integer age;
    
    @Column(length = 200)
    private String address;
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    // 与挂号记录的关联关系（JSON序列化时忽略）
    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
    
    /**
     * 性别枚举
     */
    public enum Gender {
        男, 女
    }
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}