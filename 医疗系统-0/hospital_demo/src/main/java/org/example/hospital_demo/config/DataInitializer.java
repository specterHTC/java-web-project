package org.example.hospital_demo.config;

import org.example.hospital_demo.entity.*;
import org.example.hospital_demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化配置类
 * 在项目启动时自动创建测试数据
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            DepartmentRepository departmentRepo,
            DoctorRepository doctorRepo,
            PatientRepository patientRepo,
            AppointmentSlotRepository slotRepo) {
        
        return args -> {
            System.out.println("========== 开始初始化测试数据 ==========");
            
            // 1. 创建科室
            List<Department> departments = createDepartments(departmentRepo);
            System.out.println("✓ 已创建 " + departments.size() + " 个科室");
            
            // 2. 创建医生
            List<Doctor> doctors = createDoctors(doctorRepo, departments);
            System.out.println("✓ 已创建 " + doctors.size() + " 名医生");
            
            // 3. 创建病人
            List<Patient> patients = createPatients(patientRepo);
            System.out.println("✓ 已创建 " + patients.size() + " 名病人");
            
            // 4. 创建号源
            createAppointmentSlots(slotRepo, doctors);
            System.out.println("✓ 已创建医生号源");
            
            System.out.println("========== 测试数据初始化完成 ==========");
            System.out.println("访问 http://localhost:8080 打开挂号系统");
            System.out.println("访问 http://localhost:8080/h2-console 可查看数据库");
            System.out.println("JDBC URL: jdbc:h2:mem:hospital_system");
        };
    }
    
    /**
     * 创建科室数据
     */
    private List<Department> createDepartments(DepartmentRepository repo) {
        Department d1 = new Department();
        d1.setName("内科");
        d1.setDescription("负责内脏器官疾病的诊断和治疗");
        
        Department d2 = new Department();
        d2.setName("外科");
        d2.setDescription("负责手术治疗和外伤处理");
        
        Department d3 = new Department();
        d3.setName("儿科");
        d3.setDescription("负责儿童疾病的诊断和治疗");
        
        Department d4 = new Department();
        d4.setName("急诊科");
        d4.setDescription("负责紧急情况和急症处理");
        
        Department d5 = new Department();
        d5.setName("骨科");
        d5.setDescription("负责骨骼和关节疾病的诊断和治疗");
        
        return repo.saveAll(Arrays.asList(d1, d2, d3, d4, d5));
    }
    
    /**
     * 创建医生数据
     */
    private List<Doctor> createDoctors(DoctorRepository repo, List<Department> departments) {
        // 内科医生
        Doctor doc1 = new Doctor();
        doc1.setName("张医生");
        doc1.setDepartment(departments.get(0));
        doc1.setTitle("主任医师");
        doc1.setSpecialty("心血管疾病、高血压");
        doc1.setSchedule("周一上午,周三上午,周五上午");
        doc1.setMaxPatientsPerDay(30);
        
        Doctor doc2 = new Doctor();
        doc2.setName("李医生");
        doc2.setDepartment(departments.get(0));
        doc2.setTitle("副主任医师");
        doc2.setSpecialty("呼吸系统疾病");
        doc2.setSchedule("周二上午,周四上午");
        doc2.setMaxPatientsPerDay(25);
        
        // 外科医生
        Doctor doc3 = new Doctor();
        doc3.setName("王医生");
        doc3.setDepartment(departments.get(1));
        doc3.setTitle("主任医师");
        doc3.setSpecialty("普外科手术");
        doc3.setSchedule("周一下午,周三下午");
        doc3.setMaxPatientsPerDay(20);
        
        // 儿科医生
        Doctor doc4 = new Doctor();
        doc4.setName("赵医生");
        doc4.setDepartment(departments.get(2));
        doc4.setTitle("主治医师");
        doc4.setSpecialty("儿童常见病");
        doc4.setSchedule("周一至周五上午");
        doc4.setMaxPatientsPerDay(40);
        
        // 急诊科医生
        Doctor doc5 = new Doctor();
        doc5.setName("刘医生");
        doc5.setDepartment(departments.get(3));
        doc5.setTitle("副主任医师");
        doc5.setSpecialty("急诊处理");
        doc5.setSchedule("全天候");
        doc5.setMaxPatientsPerDay(50);
        
        // 骨科医生
        Doctor doc6 = new Doctor();
        doc6.setName("陈医生");
        doc6.setDepartment(departments.get(4));
        doc6.setTitle("主任医师");
        doc6.setSpecialty("骨折、关节置换");
        doc6.setSchedule("周二下午,周四下午");
        doc6.setMaxPatientsPerDay(15);
        
        return repo.saveAll(Arrays.asList(doc1, doc2, doc3, doc4, doc5, doc6));
    }
    
    /**
     * 创建病人数据
     */
    private List<Patient> createPatients(PatientRepository repo) {
        Patient p1 = new Patient();
        p1.setName("测试病人A");
        p1.setPhone("13800138001");
        p1.setIdCard("110101199001011234");
        p1.setGender(Patient.Gender.男);
        p1.setAge(30);
        p1.setAddress("北京市朝阳区");
        
        Patient p2 = new Patient();
        p2.setName("测试病人B");
        p2.setPhone("13800138002");
        p2.setIdCard("110101199202022345");
        p2.setGender(Patient.Gender.女);
        p2.setAge(25);
        p2.setAddress("北京市海淀区");
        
        Patient p3 = new Patient();
        p3.setName("测试病人C");
        p3.setPhone("13800138003");
        p3.setIdCard("110101198503033456");
        p3.setGender(Patient.Gender.男);
        p3.setAge(38);
        p3.setAddress("北京市西城区");
        
        return repo.saveAll(Arrays.asList(p1, p2, p3));
    }
    
    /**
     * 创建号源数据（未来7天）
     */
    private void createAppointmentSlots(AppointmentSlotRepository repo, List<Doctor> doctors) {
        String[] timeSlots = {
            "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00",
            "10:00-10:30", "10:30-11:00", "11:00-11:30",
            "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00",
            "16:00-16:30", "16:30-17:00"
        };
        
        LocalDate today = LocalDate.now();
        
        for (Doctor doctor : doctors) {
            // 为每个医生创建未来7天的号源
            for (int day = 0; day < 7; day++) {
                LocalDate date = today.plusDays(day);
                
                for (String timeSlot : timeSlots) {
                    AppointmentSlot slot = new AppointmentSlot();
                    slot.setDoctor(doctor);
                    slot.setDate(date);
                    slot.setTimeSlot(timeSlot);
                    slot.setTotalSlots(3);  // 每个时间段3个号
                    slot.setUsedSlots(0);
                    slot.setEmergencyReserved(1);  // 预留1个急诊号
                    slot.setStatus(AppointmentSlot.SlotStatus.可用);
                    repo.save(slot);
                }
            }
        }
    }
}