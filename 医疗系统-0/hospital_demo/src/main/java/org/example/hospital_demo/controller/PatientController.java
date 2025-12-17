package org.example.hospital_demo.controller;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Patient;
import org.example.hospital_demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病人控制器（基础CRUD）
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public Result<List<Patient>> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Result<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @GetMapping("/phone/{phone}")
    public Result<Patient> getPatientByPhone(@PathVariable String phone) {
        return patientService.getPatientByPhone(phone);
    }

    @PostMapping
    public Result<Patient> createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    @PutMapping("/{id}")
    public Result<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePatient(@PathVariable Long id) {
        return patientService.deletePatient(id);
    }
}
