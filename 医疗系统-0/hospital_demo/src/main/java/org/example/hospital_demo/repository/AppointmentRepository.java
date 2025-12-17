package org.example.hospital_demo.repository;

import org.example.hospital_demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 挂号记录数据访问层
 * 提供挂号相关的复杂查询和业务操作
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /**
     * 根据病人ID查询挂号记录
     */
    List<Appointment> findByPatientIdOrderByCreatedTimeDesc(Long patientId);
    
    /**
     * 根据医生ID和日期查询挂号记录
     */
    List<Appointment> findByDoctorIdAndAppointmentDateOrderByAppointmentTime(Long doctorId, LocalDate date);
    
    /**
     * 根据科室ID查询挂号记录
     */
    List<Appointment> findByDepartmentIdOrderByCreatedTimeDesc(Long departmentId);
    
    /**
     * 根据挂号状态查询
     */
    List<Appointment> findByStatusOrderByCreatedTimeDesc(Appointment.AppointmentStatus status);
    
    /**
     * 根据挂号类型查询
     */
    List<Appointment> findByTypeOrderByPriorityDescCreatedTimeDesc(Appointment.AppointmentType type);
    
    /**
     * 检查是否存在重复挂号（同一病人同一医生同一天）
     */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patient.id = :patientId " +
           "AND a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status = 'SCHEDULED'")
    boolean existsDuplicateAppointment(@Param("patientId") Long patientId, 
                                     @Param("doctorId") Long doctorId, 
                                     @Param("date") LocalDate date);
    
    /**
     * 查询指定医生指定日期的挂号数量
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate = :date AND a.status IN ('SCHEDULED', 'COMPLETED')")
    Long countByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 查询急诊挂号列表（按优先级排序）
     */
    @Query("SELECT a FROM Appointment a WHERE a.type = 'EMERGENCY' AND a.status = 'SCHEDULED' " +
           "ORDER BY a.priority DESC, a.createdTime ASC")
    List<Appointment> findEmergencyAppointments();
    
    /**
     * 查询指定医生的排队列表（按优先级和时间排序）
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date " +
           "AND a.status = 'SCHEDULED' ORDER BY a.priority DESC, a.appointmentTime ASC")
    List<Appointment> findQueueByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);
    
    /**
     * 查询过期的挂号记录（用于自动清理）
     */
    @Query("SELECT a FROM Appointment a WHERE a.status = 'SCHEDULED' " +
           "AND a.appointmentDate < :currentDate")
    List<Appointment> findExpiredAppointments(@Param("currentDate") LocalDate currentDate);
    
    /**
     * 统计各科室挂号数量（用于图表展示）
     */
    @Query("SELECT d.name, COUNT(a) FROM Appointment a JOIN a.department d " +
           "WHERE a.createdTime >= :startTime GROUP BY d.id, d.name")
    List<Object[]> countAppointmentsByDepartment(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 统计每日挂号趋势（最近30天）
     */
    @Query("SELECT DATE(a.createdTime), COUNT(a) FROM Appointment a " +
           "WHERE a.createdTime >= :startTime GROUP BY DATE(a.createdTime) ORDER BY DATE(a.createdTime)")
    List<Object[]> getDailyAppointmentTrend(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 查询病人的最新挂号记录
     */
    Optional<Appointment> findFirstByPatientIdOrderByCreatedTimeDesc(Long patientId);
    
    /**
     * 根据排队号码查询挂号记录
     */
    Optional<Appointment> findByQueueNumber(Integer queueNumber);
}