package org.example.hospital_demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医生实体类
 * 负责存储医生的基本信息和排班信息
 */
@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "医生姓名不能为空")
    @Column(nullable = false, length = 50)
    private String name;
    
    // 所属科室（忽略科室中的doctors列表，避免循环）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"doctors", "appointments", "hibernateLazyInitializer"})
    private Department department;
    
    @Column(length = 50)
    private String title; // 职称：主任医师、副主任医师、主治医师等
    
    @Column(columnDefinition = "TEXT")
    private String specialty; // 专长描述
    
    @Column(length = 200)
    private String schedule; // 排班信息：如"周一上午,周三下午"
    
    @Column(name = "max_patients_per_day")
    private Integer maxPatientsPerDay = 50; // 每日最大接诊量
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    // 与挂号记录的关联关系
    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
    
    // 与号源管理的关联关系
    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentSlot> appointmentSlots;
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}