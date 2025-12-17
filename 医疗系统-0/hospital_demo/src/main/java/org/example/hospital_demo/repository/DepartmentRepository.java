package org.example.hospital_demo.repository;

import org.example.hospital_demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 科室数据访问层
 * 提供科室信息的数据库操作方法
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * 根据科室名称查找科室
     */
    Optional<Department> findByName(String name);
    
    /**
     * 根据科室名称模糊查询
     */
    List<Department> findByNameContaining(String name);
    
    /**
     * 查询有医生的科室列表
     */
    @Query("SELECT DISTINCT d FROM Department d WHERE EXISTS (SELECT 1 FROM Doctor doc WHERE doc.department = d)")
    List<Department> findDepartmentsWithDoctors();
    
    /**
     * 检查科室名称是否已存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
}