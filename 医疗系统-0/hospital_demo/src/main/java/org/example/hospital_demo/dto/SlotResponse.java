package org.example.hospital_demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * 号源响应DTO
 * 用于返回可用的挂号时间段信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponse {
    
    private Long slotId;
    private Long doctorId;
    private String doctorName;
    private String doctorTitle;
    private String departmentName;
    private LocalDate date;
    private String timeSlot;
    private Integer totalSlots;
    private Integer usedSlots;
    private Integer availableSlots;
    private Integer emergencyReserved;
    private Integer availableEmergencySlots;
    private String status;
    private String statusDescription;
    private Boolean canBookNormal;
    private Boolean canBookEmergency;
}