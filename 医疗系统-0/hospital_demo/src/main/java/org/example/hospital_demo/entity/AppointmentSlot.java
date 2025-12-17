package org.example.hospital_demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * 号源管理实体类
 * 负责管理医生的可预约时间段和号源数量
 * 这是挂号系统的创新功能，支持动态号源管理
 */
@Entity
@Table(name = "appointment_slot",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "date", "time_slot"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 关联医生
    @NotNull(message = "医生信息不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @NotNull(message = "日期不能为空")
    @Column(nullable = false)
    private LocalDate date;
    
    @NotNull(message = "时间段不能为空")
    @Column(name = "time_slot", nullable = false, length = 20)
    private String timeSlot; // 如：09:00-09:30, 14:00-14:30
    
    // 总号源数量
    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots = 1;
    
    // 已使用的号源数量
    @Column(name = "used_slots", nullable = false)
    private Integer usedSlots = 0;
    
    // 急诊预留数量
    @Column(name = "emergency_reserved", nullable = false)
    private Integer emergencyReserved = 2;
    
    // 号源状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status = SlotStatus.可用;
    
    /**
     * 号源状态枚举
     */
    public enum SlotStatus {
        可用("号源充足，可以预约"),
        已满("号源已满，无法预约"),
        停诊("医生停诊，暂停预约");
        
        private final String description;
        
        SlotStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 获取剩余普通号源数量
     */
    public Integer getAvailableSlots() {
        return Math.max(0, totalSlots - usedSlots - emergencyReserved);
    }
    
    /**
     * 获取剩余急诊号源数量
     */
    public Integer getAvailableEmergencySlots() {
        return Math.max(0, totalSlots - usedSlots);
    }
    
    /**
     * 判断是否可以预约普通号
     */
    public boolean canBookNormal() {
        return status == SlotStatus.可用 && getAvailableSlots() > 0;
    }
    
    /**
     * 判断是否可以预约急诊号
     */
    public boolean canBookEmergency() {
        return status == SlotStatus.可用 && getAvailableEmergencySlots() > 0;
    }
    
    /**
     * 使用一个号源
     */
    public void useSlot() {
        if (usedSlots < totalSlots) {
            usedSlots++;
            updateStatus();
        }
    }
    
    /**
     * 释放一个号源
     */
    public void releaseSlot() {
        if (usedSlots > 0) {
            usedSlots--;
            updateStatus();
        }
    }
    
    /**
     * 更新号源状态
     */
    private void updateStatus() {
        if (usedSlots >= totalSlots) {
            status = SlotStatus.已满;
        } else {
            status = SlotStatus.可用;
        }
    }
}