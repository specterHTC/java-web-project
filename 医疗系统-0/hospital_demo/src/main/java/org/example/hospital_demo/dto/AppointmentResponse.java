package org.example.hospital_demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 挂号响应数据传输对象
 * 用于向前端返回挂号信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    
    private Long id;
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String departmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String type;
    private String typeDescription;
    private Integer priority;
    private String status;
    private String statusDescription;
    private Integer queueNumber;
    private String symptoms;
    private BigDecimal fee;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    // 扩展信息
    private String doctorTitle;      // 医生职称
    private String doctorSpecialty;  // 医生专长
    private Boolean canCancel;       // 是否可以取消
    private String timeSlot;         // 时间段
}