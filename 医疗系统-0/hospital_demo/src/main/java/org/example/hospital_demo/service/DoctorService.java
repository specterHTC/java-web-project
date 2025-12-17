package org.example.hospital_demo.service;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Doctor;
import org.example.hospital_demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医生服务层（基础CRUD）
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    // 获取所有医生
    public Result<List<Doctor>> getAllDoctors() {
        return Result.success(doctorRepo.findAll());
    }

    // 根据ID获取医生
    public Result<Doctor> getDoctorById(Long id) {
        return doctorRepo.findById(id)
                .map(Result::success)
                .orElse(Result.notFound("医生不存在"));
    }

    // 根据科室获取医生列表
    public Result<List<Doctor>> getDoctorsByDepartment(Long departmentId) {
        return Result.success(doctorRepo.findByDepartmentId(departmentId));
    }

    // 创建医生
    public Result<Doctor> createDoctor(Doctor doctor) {
        return Result.success("创建成功", doctorRepo.save(doctor));
    }

    // 更新医生
    public Result<Doctor> updateDoctor(Long id, Doctor doctor) {
        if (!doctorRepo.existsById(id)) {
            return Result.notFound("医生不存在");
        }
        doctor.setId(id);
        return Result.success("更新成功", doctorRepo.save(doctor));
    }

    // 删除医生
    public Result<Void> deleteDoctor(Long id) {
        if (!doctorRepo.existsById(id)) {
            return Result.notFound("医生不存在");
        }
        doctorRepo.deleteById(id);
        return Result.success("删除成功", null);
    }
}
