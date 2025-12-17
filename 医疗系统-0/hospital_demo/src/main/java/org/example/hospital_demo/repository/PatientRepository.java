package org.example.hospital_demo.repository;

import org.example.hospital_demo.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 病人数据访问层
 * 提供病人信息的数据库操作方法
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * 根据手机号查找病人
     */
    Optional<Patient> findByPhone(String phone);
    
    /**
     * 根据身份证号查找病人
     */
    Optional<Patient> findByIdCard(String idCard);
    
    /**
     * 根据姓名模糊查询病人
     */
    List<Patient> findByNameContaining(String name);
    
    /**
     * 检查手机号是否已存在（排除指定ID）
     */
    @Query("SELECT COUNT(p) > 0 FROM Patient p WHERE p.phone = :phone AND p.id != :id")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("id") Long id);
    
    /**
     * 检查身份证号是否已存在（排除指定ID）
     */
    @Query("SELECT COUNT(p) > 0 FROM Patient p WHERE p.idCard = :idCard AND p.id != :id")
    boolean existsByIdCardAndIdNot(@Param("idCard") String idCard, @Param("id") Long id);
}