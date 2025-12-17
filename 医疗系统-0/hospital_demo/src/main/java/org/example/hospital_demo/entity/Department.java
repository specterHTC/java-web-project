package org.example.hospital_demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室实体类
 * 负责存储医院科室信息
 */
@Entity
@Table(name = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "科室名称不能为空")
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    // 与医生的关联关系（JSON序列化时忽略，避免循环引用）
    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors;
    
    // 与挂号记录的关联关系
    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;
    
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}