package org.example.hospital_demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * 号源查询请求DTO
 * 用于查询可用的挂号时间段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotQueryRequest {
    
    private Long departmentId; // 科室ID（可选）
    
    private Long doctorId; // 医生ID（可选）
    
    @NotNull(message = "查询日期不能为空")
    private LocalDate date; // 查询日期
    
    private String appointmentType = "普通"; // 挂号类型：普通/急诊
}