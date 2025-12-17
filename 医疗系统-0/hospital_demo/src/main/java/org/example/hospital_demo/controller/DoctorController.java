package org.example.hospital_demo.controller;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Doctor;
import org.example.hospital_demo.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生控制器（基础CRUD）
 */
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public Result<List<Doctor>> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Result<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @GetMapping("/department/{departmentId}")
    public Result<List<Doctor>> getDoctorsByDepartment(@PathVariable Long departmentId) {
        return doctorService.getDoctorsByDepartment(departmentId);
    }

    @PostMapping
    public Result<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @PutMapping("/{id}")
    public Result<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }
}
