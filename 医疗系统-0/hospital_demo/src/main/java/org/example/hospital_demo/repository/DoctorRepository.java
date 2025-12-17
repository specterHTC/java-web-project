package org.example.hospital_demo.repository;

import org.example.hospital_demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 医生数据访问层
 * 提供医生信息的数据库操作方法
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * 根据科室ID查找医生列表
     */
    List<Doctor> findByDepartmentId(Long departmentId);
    
    /**
     * 根据医生姓名模糊查询
     */
    List<Doctor> findByNameContaining(String name);
    
    /**
     * 根据职称查询医生
     */
    List<Doctor> findByTitle(String title);
    
    /**
     * 查询指定科室的医生数量
     */
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);
    
    /**
     * 查询有号源的医生（指定日期）
     */
    @Query("SELECT DISTINCT d FROM Doctor d WHERE EXISTS " +
           "(SELECT 1 FROM AppointmentSlot slot WHERE slot.doctor = d AND slot.date = :date AND slot.status = 'AVAILABLE')")
    List<Doctor> findDoctorsWithAvailableSlots(@Param("date") LocalDate date);
    
    /**
     * 根据科室和职称查询医生
     */
    @Query("SELECT d FROM Doctor d WHERE d.department.id = :departmentId AND (:title IS NULL OR d.title = :title)")
    List<Doctor> findByDepartmentIdAndTitle(@Param("departmentId") Long departmentId, @Param("title") String title);
}