package org.example.hospital_demo.service;

import org.example.hospital_demo.dto.Result;
import org.example.hospital_demo.entity.Department;
import org.example.hospital_demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科室服务层（基础CRUD）
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepo;

    // 获取所有科室
    public Result<List<Department>> getAllDepartments() {
        return Result.success(departmentRepo.findAll());
    }

    // 根据ID获取科室
    public Result<Department> getDepartmentById(Long id) {
        return departmentRepo.findById(id)
                .map(Result::success)
                .orElse(Result.notFound("科室不存在"));
    }

    // 创建科室
    public Result<Department> createDepartment(Department department) {
        return Result.success("创建成功", departmentRepo.save(department));
    }

    // 更新科室
    public Result<Department> updateDepartment(Long id, Department department) {
        if (!departmentRepo.existsById(id)) {
            return Result.notFound("科室不存在");
        }
        department.setId(id);
        return Result.success("更新成功", departmentRepo.save(department));
    }

    // 删除科室
    public Result<Void> deleteDepartment(Long id) {
        if (!departmentRepo.existsById(id)) {
            return Result.notFound("科室不存在");
        }
        departmentRepo.deleteById(id);
        return Result.success("删除成功", null);
    }
}
