package org.example.hospital_demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 挂号请求数据传输对象
 * 用于接收前端提交的挂号信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    
    @NotNull(message = "病人ID不能为空")
    private Long patientId;
    
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;
    
    @NotNull(message = "科室ID不能为空")
    private Long departmentId;
    
    @NotNull(message = "挂号日期不能为空")
    @Future(message = "挂号日期必须是未来日期")
    private LocalDate appointmentDate;
    
    @NotNull(message = "挂号时间不能为空")
    private LocalTime appointmentTime;
    
    private String type = "普通"; // 挂号类型：普通/急诊
    
    private Integer priority = 0; // 优先级（急诊时使用）
    
    private String symptoms; // 症状描述
    
    private String timeSlot; // 时间段（如：09:00-09:30）
}