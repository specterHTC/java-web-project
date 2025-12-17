package org.example.hospital_demo.controller;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.dto.SlotResponse;
import org.example.hospital_demo.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 号源控制器
 * 查询医生的可预约时间段
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "*")
public class SlotController {

    @Autowired
    private SlotService slotService;

    /**
     * 获取医生某天的所有号源
     * GET /api/slots/doctor/{doctorId}?date=2024-01-01
     */
    @GetMapping("/doctor/{doctorId}")
    public Result<List<SlotResponse>> getDoctorSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return slotService.getDoctorSlots(doctorId, date);
    }

    /**
     * 获取可用号源
     * GET /api/slots/available/{doctorId}?date=2024-01-01
     */
    @GetMapping("/available/{doctorId}")
    public Result<List<SlotResponse>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return slotService.getAvailableSlots(doctorId, date);
    }
}
