package org.example.hospital_demo.controller;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Department;
import org.example.hospital_demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室控制器（基础CRUD）
 */
@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public Result<List<Department>> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Result<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public Result<Department> createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping("/{id}")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
