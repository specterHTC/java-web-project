package org.example.hospital_demo.repository;

import org.example.hospital_demo.entity.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 号源管理数据访问层
 * 提供号源的动态管理和查询功能
 */
@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    
    /**
     * 根据医生ID和日期查询号源
     */
    List<AppointmentSlot> findByDoctorIdAndDateOrderByTimeSlot(Long doctorId, LocalDate date);
    
    /**
     * 根据医生ID、日期和时间段查询号源
     */
    Optional<AppointmentSlot> findByDoctorIdAndDateAndTimeSlot(Long doctorId, LocalDate date, String timeSlot);
    
    /**
     * 查询指定日期范围内的号源
     */
    List<AppointmentSlot> findByDateBetweenOrderByDateAscTimeSlot(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询可用的号源（普通挂号）
     */
    @Query("SELECT s FROM AppointmentSlot s WHERE s.doctor.id = :doctorId AND s.date = :date " +
           "AND s.status = '可用' AND (s.totalSlots - s.usedSlots - s.emergencyReserved) > 0 " +
           "ORDER BY s.timeSlot")
    List<AppointmentSlot> findAvailableSlots(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 查询可用的急诊号源
     */
    @Query("SELECT s FROM AppointmentSlot s WHERE s.doctor.id = :doctorId AND s.date = :date " +
           "AND s.status = '可用' AND (s.totalSlots - s.usedSlots) > 0 " +
           "ORDER BY s.timeSlot")
    List<AppointmentSlot> findAvailableEmergencySlots(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 查询指定科室在指定日期的所有可用号源
     */
    @Query("SELECT s FROM AppointmentSlot s WHERE s.doctor.department.id = :departmentId " +
           "AND s.date = :date AND s.status = '可用' " +
           "AND (s.totalSlots - s.usedSlots - s.emergencyReserved) > 0 " +
           "ORDER BY s.doctor.name, s.timeSlot")
    List<AppointmentSlot> findAvailableSlotsByDepartment(@Param("departmentId") Long departmentId, 
                                                        @Param("date") LocalDate date);
    
    /**
     * 统计医生在指定日期的总号源数
     */
    @Query("SELECT COALESCE(SUM(s.totalSlots), 0) FROM AppointmentSlot s " +
           "WHERE s.doctor.id = :doctorId AND s.date = :date")
    Integer getTotalSlotsByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 统计医生在指定日期的已用号源数
     */
    @Query("SELECT COALESCE(SUM(s.usedSlots), 0) FROM AppointmentSlot s " +
           "WHERE s.doctor.id = :doctorId AND s.date = :date")
    Integer getUsedSlotsByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 查询需要清理的过期号源
     */
    @Query("SELECT s FROM AppointmentSlot s WHERE s.date < :currentDate")
    List<AppointmentSlot> findExpiredSlots(@Param("currentDate") LocalDate currentDate);
    
    /**
     * 检查是否存在重复的号源配置
     */
    boolean existsByDoctorIdAndDateAndTimeSlot(Long doctorId, LocalDate date, String timeSlot);
}