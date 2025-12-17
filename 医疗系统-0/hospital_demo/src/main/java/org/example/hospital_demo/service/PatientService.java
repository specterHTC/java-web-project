package org.example.hospital_demo.service;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Patient;
import org.example.hospital_demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 病人服务层（基础CRUD）
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    // 获取所有病人
    public Result<List<Patient>> getAllPatients() {
        return Result.success(patientRepo.findAll());
    }

    // 根据ID获取病人
    public Result<Patient> getPatientById(Long id) {
        return patientRepo.findById(id)
                .map(Result::success)
                .orElse(Result.notFound("病人不存在"));
    }

    // 根据手机号查找病人
    public Result<Patient> getPatientByPhone(String phone) {
        return patientRepo.findByPhone(phone)
                .map(Result::success)
                .orElse(Result.notFound("病人不存在"));
    }

    // 创建病人
    public Result<Patient> createPatient(Patient patient) {
        if (patientRepo.findByPhone(patient.getPhone()).isPresent()) {
            return Result.badRequest("该手机号已注册");
        }
        return Result.success("创建成功", patientRepo.save(patient));
    }

    // 更新病人
    public Result<Patient> updatePatient(Long id, Patient patient) {
        if (!patientRepo.existsById(id)) {
            return Result.notFound("病人不存在");
        }
        patient.setId(id);
        return Result.success("更新成功", patientRepo.save(patient));
    }

    // 删除病人
    public Result<Void> deletePatient(Long id) {
        if (!patientRepo.existsById(id)) {
            return Result.notFound("病人不存在");
        }
        patientRepo.deleteById(id);
        return Result.success("删除成功", null);
    }
}
