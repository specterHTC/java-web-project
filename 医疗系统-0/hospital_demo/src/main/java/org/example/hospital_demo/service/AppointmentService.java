package org.example.hospital_demo.service;

import org.example.hospital_demo.dto.*;
import org.example.hospital_demo.entity.*;
import org.example.hospital_demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 挂号服务层
 * 核心业务逻辑：挂号、取消、急诊优先级队列等
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;
    
    @Autowired
    private AppointmentSlotRepository slotRepo;
    
    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private DoctorRepository doctorRepo;
    
    @Autowired
    private DepartmentRepository departmentRepo;
    
    // 排队号码生成器
    private final AtomicInteger queueNumberGenerator = new AtomicInteger(1000);

    /**
     * 创建挂号（核心功能）
     */
    @Transactional
    public Result<AppointmentResponse> createAppointment(AppointmentRequest request) {
        // 1. 验证病人是否存在
        Patient patient = patientRepo.findById(request.getPatientId())
                .orElse(null);
        if (patient == null) {
            return Result.notFound("病人不存在");
        }
        
        // 2. 验证医生是否存在
        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElse(null);
        if (doctor == null) {
            return Result.notFound("医生不存在");
        }
        
        // 3. 验证科室是否存在
        Department department = departmentRepo.findById(request.getDepartmentId())
                .orElse(null);
        if (department == null) {
            return Result.notFound("科室不存在");
        }
        
        // 4. 检查是否重复挂号
        boolean isDuplicate = appointmentRepo.findByPatientIdOrderByCreatedTimeDesc(request.getPatientId())
                .stream()
                .anyMatch(a -> a.getDoctor().getId().equals(request.getDoctorId()) 
                        && a.getAppointmentDate().equals(request.getAppointmentDate())
                        && a.getStatus() == Appointment.AppointmentStatus.已预约);
        if (isDuplicate) {
            return Result.badRequest("您已在该日期预约过该医生，请勿重复挂号");
        }
        
        // 5. 检查号源
        boolean isEmergency = "急诊".equals(request.getType());
        AppointmentSlot slot = slotRepo.findByDoctorIdAndDateAndTimeSlot(
                request.getDoctorId(), 
                request.getAppointmentDate(), 
                request.getTimeSlot()).orElse(null);
        
        if (slot == null) {
            return Result.notFound("该时间段暂无号源");
        }
        
        if (isEmergency) {
            if (!slot.canBookEmergency()) {
                return Result.badRequest("急诊号源已满");
            }
        } else {
            if (!slot.canBookNormal()) {
                return Result.badRequest("普通号源已满，可尝试急诊挂号");
            }
        }
        
        // 6. 创建挂号记录
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setType(isEmergency ? Appointment.AppointmentType.急诊 : Appointment.AppointmentType.普通);
        appointment.setPriority(isEmergency ? 100 : 0);
        appointment.setSymptoms(request.getSymptoms());
        appointment.setQueueNumber(queueNumberGenerator.incrementAndGet());
        appointment.setStatus(Appointment.AppointmentStatus.已预约);
        
        // 7. 扣减号源
        slot.useSlot();
        slotRepo.save(slot);
        
        // 8. 保存挂号记录
        appointment = appointmentRepo.save(appointment);
        
        return Result.success("挂号成功", convertToResponse(appointment));
    }

    /**
     * 取消挂号
     */
    @Transactional
    public Result<Void> cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElse(null);
        if (appointment == null) {
            return Result.notFound("挂号记录不存在");
        }
        
        if (!appointment.canCancel()) {
            return Result.badRequest("该挂号记录无法取消");
        }
        
        // 更新状态
        appointment.setStatus(Appointment.AppointmentStatus.已取消);
        appointmentRepo.save(appointment);
        
        // 释放号源
        AppointmentSlot slot = slotRepo.findByDoctorIdAndDateAndTimeSlot(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                formatTimeSlot(appointment.getAppointmentTime())).orElse(null);
        if (slot != null) {
            slot.releaseSlot();
            slotRepo.save(slot);
        }
        
        return Result.success("取消成功", null);
    }

    /**
     * 获取病人的挂号记录
     */
    public Result<List<AppointmentResponse>> getPatientAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepo.findByPatientIdOrderByCreatedTimeDesc(patientId);
        List<AppointmentResponse> responses = appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 获取医生的排队列表（按优先级排序）
     */
    public Result<List<AppointmentResponse>> getDoctorQueue(Long doctorId, LocalDate date) {
        List<Appointment> queue = appointmentRepo.findQueueByDoctorAndDate(doctorId, date);
        List<AppointmentResponse> responses = queue.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 完成就诊
     */
    @Transactional
    public Result<Void> completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElse(null);
        if (appointment == null) {
            return Result.notFound("挂号记录不存在");
        }
        
        appointment.setStatus(Appointment.AppointmentStatus.已就诊);
        appointmentRepo.save(appointment);
        
        return Result.success("就诊完成", null);
    }

    /**
     * 获取所有挂号记录
     */
    public Result<List<AppointmentResponse>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepo.findAll();
        List<AppointmentResponse> responses = appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 根据状态筛选挂号记录
     */
    public Result<List<AppointmentResponse>> getAppointmentsByStatus(String status) {
        Appointment.AppointmentStatus appointmentStatus;
        try {
            appointmentStatus = Appointment.AppointmentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return Result.badRequest("无效的状态值");
        }
        
        List<Appointment> appointments = appointmentRepo.findByStatusOrderByCreatedTimeDesc(appointmentStatus);
        List<AppointmentResponse> responses = appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    /**
     * 转换为响应DTO
     */
    private AppointmentResponse convertToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientName(appointment.getPatient().getName());
        response.setPatientPhone(appointment.getPatient().getPhone());
        response.setDoctorName(appointment.getDoctor().getName());
        response.setDepartmentName(appointment.getDepartment().getName());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setType(appointment.getType().name());
        response.setTypeDescription(appointment.getType().getDescription());
        response.setPriority(appointment.getPriority());
        response.setStatus(appointment.getStatus().name());
        response.setStatusDescription(appointment.getStatus().getDescription());
        response.setQueueNumber(appointment.getQueueNumber());
        response.setSymptoms(appointment.getSymptoms());
        response.setFee(appointment.getFee());
        response.setCreatedTime(appointment.getCreatedTime());
        response.setUpdatedTime(appointment.getUpdatedTime());
        response.setDoctorTitle(appointment.getDoctor().getTitle());
        response.setDoctorSpecialty(appointment.getDoctor().getSpecialty());
        response.setCanCancel(appointment.canCancel());
        response.setTimeSlot(formatTimeSlot(appointment.getAppointmentTime()));
        return response;
    }

    /**
     * 格式化时间段
     */
    private String formatTimeSlot(LocalTime time) {
        LocalTime endTime = time.plusMinutes(30);
        return String.format("%02d:%02d-%02d:%02d", 
                time.getHour(), time.getMinute(),
                endTime.getHour(), endTime.getMinute());
    }
}