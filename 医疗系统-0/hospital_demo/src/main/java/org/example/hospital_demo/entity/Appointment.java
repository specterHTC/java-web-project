package org.example.hospital_demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 挂号记录实体类
 * 系统的核心业务实体，负责管理所有挂号相关信息
 */
@Entity
@Table(name = "appointment", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "doctor_id", "appointment_date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 病人信息
    @NotNull(message = "病人信息不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    // 医生信息
    @NotNull(message = "医生信息不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    // 科室信息
    @NotNull(message = "科室信息不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @NotNull(message = "挂号日期不能为空")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;
    
    @NotNull(message = "挂号时间不能为空")
    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;
    
    // 挂号类型：普通挂号 vs 急诊挂号
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type = AppointmentType.普通;
    
    // 优先级：急诊病人的优先级（数字越大优先级越高）
    @Column(nullable = false)
    private Integer priority = 0;
    
    // 挂号状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.已预约;
    
    // 排队号码
    @Column(name = "queue_number")
    private Integer queueNumber;
    
    // 症状描述
    @Column(columnDefinition = "TEXT")
    private String symptoms;
    
    // 挂号费用
    @Column(precision = 10, scale = 2)
    private BigDecimal fee = new BigDecimal("10.00");
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    /**
     * 挂号类型枚举
     */
    public enum AppointmentType {
        普通("普通挂号，按先来先服务原则"),
        急诊("急诊挂号，优先处理");
        
        private final String description;
        
        AppointmentType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 挂号状态枚举
     */
    public enum AppointmentStatus {
        已预约("已成功预约，等待就诊"),
        已就诊("已完成就诊"),
        已取消("用户主动取消挂号"),
        已过期("超时未就诊，自动过期");
        
        private final String description;
        
        AppointmentStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
        
        // 根据挂号类型设置优先级
        if (type == AppointmentType.急诊) {
            priority = 100; // 急诊默认高优先级
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
    
    /**
     * 判断是否为急诊挂号
     */
    public boolean isEmergency() {
        return type == AppointmentType.急诊;
    }
    
    /**
     * 判断是否可以取消
     */
    public boolean canCancel() {
        return status == AppointmentStatus.已预约;
    }
}