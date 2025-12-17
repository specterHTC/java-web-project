package org.example.hospital_demo.service;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.dto.SlotResponse;
import org.example.hospital_demo.entity.AppointmentSlot;
import org.example.hospital_demo.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 号源服务层
 * 管理医生的可预约时间段和号源数量
 */
@Service
public class SlotService {

    @Autowired
    private AppointmentSlotRepository slotRepo;

    // 获取医生某天的所有号源
    public Result<List<SlotResponse>> getDoctorSlots(Long doctorId, LocalDate date) {
        List<AppointmentSlot> slots = slotRepo.findByDoctorIdAndDateOrderByTimeSlot(doctorId, date);
        List<SlotResponse> responses = slots.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    // 获取可用号源
    public Result<List<SlotResponse>> getAvailableSlots(Long doctorId, LocalDate date) {
        List<AppointmentSlot> slots = slotRepo.findAvailableSlots(doctorId, date);
        List<SlotResponse> responses = slots.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return Result.success(responses);
    }

    // 转换为响应DTO
    private SlotResponse convertToResponse(AppointmentSlot slot) {
        SlotResponse response = new SlotResponse();
        response.setSlotId(slot.getId());
        response.setDoctorId(slot.getDoctor().getId());
        response.setDoctorName(slot.getDoctor().getName());
        response.setDoctorTitle(slot.getDoctor().getTitle());
        response.setDepartmentName(slot.getDoctor().getDepartment() != null ? 
                slot.getDoctor().getDepartment().getName() : "");
        response.setDate(slot.getDate());
        response.setTimeSlot(slot.getTimeSlot());
        response.setTotalSlots(slot.getTotalSlots());
        response.setUsedSlots(slot.getUsedSlots());
        response.setAvailableSlots(slot.getAvailableSlots());
        response.setEmergencyReserved(slot.getEmergencyReserved());
        response.setAvailableEmergencySlots(slot.getAvailableEmergencySlots());
        response.setStatus(slot.getStatus().name());
        response.setStatusDescription(slot.getStatus().getDescription());
        response.setCanBookNormal(slot.canBookNormal());
        response.setCanBookEmergency(slot.canBookEmergency());
        return response;
    }
}
