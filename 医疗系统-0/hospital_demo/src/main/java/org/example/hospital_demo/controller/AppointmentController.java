package org.example.hospital_demo.controller;

import org.example.hospital_demo.dto.*;
import org.example.hospital_demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 挂号控制器（核心模块）
 * 提供挂号相关的所有REST API接口
 * 功能亮点：支持急诊优先级队列、号源管理、智能排队
 */
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 创建挂号（核心功能）
     * POST /api/appointments
     */
    @PostMapping
    public Result<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    /**
     * 取消挂号
     * DELETE /api/appointments/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    /**
     * 获取所有挂号记录
     * GET /api/appointments
     */
    @GetMapping
    public Result<List<AppointmentResponse>> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }


    /**
     * 根据状态筛选挂号记录
     * GET /api/appointments/status/{status}
     */
    @GetMapping("/status/{status}")
    public Result<List<AppointmentResponse>> getAppointmentsByStatus(@PathVariable String status) {
        return appointmentService.getAppointmentsByStatus(status);
    }

    /**
     * 获取病人的挂号记录
     * GET /api/appointments/patient/{patientId}
     */
    @GetMapping("/patient/{patientId}")
    public Result<List<AppointmentResponse>> getPatientAppointments(@PathVariable Long patientId) {
        return appointmentService.getPatientAppointments(patientId);
    }

    /**
     * 获取医生的排队列表（按优先级排序，急诊优先）
     * GET /api/appointments/queue/{doctorId}?date=2024-01-01
     */
    @GetMapping("/queue/{doctorId}")
    public Result<List<AppointmentResponse>> getDoctorQueue(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.getDoctorQueue(doctorId, date);
    }

    /**
     * 完成就诊
     * PUT /api/appointments/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public Result<Void> completeAppointment(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }
}
