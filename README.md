# 🏥 医院挂号系统

## Hospital Registration System · 2025 Web 框架编程期末大作业

🚀 **基于 Spring Boot 3 + H2 数据库的智能挂号管理系统**



## 📖 项目简介

本项目是一个现代化的医院挂号管理系统，采用 **Spring Boot 3** 框架开发，
适合作为 **《Web 框架编程》课程期末大作业**。

* **核心亮点**：急诊优先队列算法，智能号源管理
* **技术特色**：H2 内存数据库，零配置部署
* **用户体验**：响应式前端界面，实时数据更新
* **团队协作**：无需数据库安装，clone 即可运行



## 🛠️ 技术栈与版本

### 🔧 后端技术栈

| 模块     | 技术                | 版本要求           | 备注                    |
|--------|-------------------|----------------|---------------------|
| JDK    | Java              | **JDK 17+**    | 推荐 JDK 21 (LTS)      |
| 框架     | Spring Boot       | **3.2.0**      | 已在 `pom.xml` 中固定    |
| 数据库    | H2 Database       | **内置**         | 内存数据库，无需安装          |
| ORM    | Spring Data JPA   | **3.2.0**      | 数据持久层               |
| 构建工具   | Maven             | **3.8+**       | 依赖管理                |
| API文档  | 内置 REST 接口      | -              | RESTful 风格           |

### 🎨 前端技术栈

| 模块     | 技术              | 版本    | 备注           |
|--------|-----------------|-------|--------------|
| 基础技术   | HTML5 + CSS3    | 标准版   | 原生技术栈        |
| 脚本语言   | JavaScript ES6   | 现代标准  | 异步请求处理       |
| UI设计   | 响应式布局          | 自定义   | 卡片式现代界面      |
| 交互体验   | SPA 单页应用       | 原生实现  | 无刷新页面切换      |



## 🚀 快速启动（Quick Start）

### 1️⃣ 环境准备

确保本地已安装：
- **JDK 17+** （推荐 JDK 21）
- **Maven 3.8+**
- **Git**（用于克隆项目）

### 2️⃣ 启动项目


使用 IDE 运行
找到 HospitalDemoApplication.java 直接运行


### 3️⃣ 访问系统

启动成功后，在浏览器中访问：

```
🌐 挂号系统主页：http://localhost:8080
🔍 数据库控制台：http://localhost:8080/h2-console
```

**H2 数据库连接信息：**
```
JDBC URL: jdbc:h2:mem:hospital_system
用户名: sa
密码: (留空)
```



## 📂 项目结构

```plaintext
hospital_demo/
├── src/main/java/org/example/hospital_demo/
│   ├── config/                    # 配置类
│   │   └── DataInitializer.java   # 测试数据初始化
│   ├── controller/                # 控制器层 (REST API)
│   │   ├── AppointmentController.java  # 🌟 挂号控制器 (核心模块)
│   │   ├── PatientController.java      # 病人管理
│   │   ├── DoctorController.java       # 医生管理
│   │   ├── DepartmentController.java   # 科室管理
│   │   └── SlotController.java         # 号源管理
│   ├── service/                   # 业务逻辑层
│   │   ├── AppointmentService.java     # 🌟 挂号服务 (核心业务)
│   │   ├── PatientService.java         # 病人服务
│   │   ├── DoctorService.java          # 医生服务
│   │   ├── DepartmentService.java      # 科室服务
│   │   └── SlotService.java            # 号源服务
│   ├── repository/                # 数据访问层
│   │   ├── AppointmentRepository.java  # 挂号数据访问
│   │   ├── PatientRepository.java      # 病人数据访问
│   │   ├── DoctorRepository.java       # 医生数据访问
│   │   ├── DepartmentRepository.java   # 科室数据访问
│   │   └── AppointmentSlotRepository.java # 号源数据访问
│   ├── entity/                    # 实体类 (数据库映射)
│   │   ├── Appointment.java            # 🌟 挂号记录实体
│   │   ├── AppointmentSlot.java        # 🌟 号源管理实体
│   │   ├── Patient.java                # 病人实体
│   │   ├── Doctor.java                 # 医生实体
│   │   └── Department.java             # 科室实体
│   ├── dto/                       # 数据传输对象
│   │   ├── AppointmentRequest.java     # 挂号请求DTO
│   │   ├── AppointmentResponse.java    # 挂号响应DTO
│   │   ├── SlotResponse.java           # 号源响应DTO
│   │   └── Result.java                 # 统一响应格式
│   └── HospitalDemoApplication.java    # 启动类
├── src/main/resources/
│   ├── static/
│   │   └── index.html             # 🌟 前端单页应用
│   └── application.properties     # 配置文件
└── pom.xml                        # Maven 依赖配置
```



## 🌟 核心功能特色

### 🎯 1️⃣ 智能挂号系统（Member B 核心模块）

**功能亮点：**
- ✅ **急诊优先队列**：急诊患者自动排队到前面
- ✅ **防重复挂号**：同一患者同一医生同一天不能重复挂号
- ✅ **实时号源管理**：动态显示剩余号源数量
- ✅ **智能排队算法**：按优先级 + 时间排序

**技术实现：**
```java
// 急诊优先级算法
ORDER BY a.priority DESC, a.createdTime ASC

// 号源预留机制
普通号源 = 总号源 - 已用号源 - 急诊预留
急诊号源 = 总号源 - 已用号源
```

### 🏥 2️⃣ 号源管理系统

**创新设计：**
- 📅 **动态号源生成**：自动为每个医生生成未来7天号源
- ⏰ **时间段管理**：上午 8:00-11:30，下午 14:00-17:00
- 🚨 **急诊预留**：每个时间段预留急诊号源
- 📊 **实时统计**：可用号源实时计算和显示

### 👥 3️⃣ 多角色管理

**病人管理：**
- 基础信息管理（姓名、手机、身份证等）
- 挂号历史查询
- 挂号状态跟踪

**医生管理：**
- 医生信息管理（姓名、职称、专长等）
- 科室关联管理
- 排班信息设置

**科室管理：**
- 科室基础信息
- 医生归属管理



## 🔧 API 接口文档

### 🌟 挂号模块 API（核心）

| 功能       | 请求方式   | 接口路径                              | 说明           |
|----------|--------|-----------------------------------|--------------|
| 创建挂号     | POST   | `/api/appointments`               | 🌟 核心挂号功能    |
| 取消挂号     | DELETE | `/api/appointments/{id}`          | 取消已预约挂号      |
| 获取排队列表   | GET    | `/api/appointments/queue/{doctorId}` | 🌟 急诊优先排队    |
| 病人挂号记录   | GET    | `/api/appointments/patient/{id}`  | 个人挂号历史       |
| 完成就诊     | PUT    | `/api/appointments/{id}/complete` | 更新就诊状态       |

### 📋 其他模块 API

| 模块   | 接口路径                    | 说明      |
|------|-------------------------|---------|
| 病人管理 | `/api/patients`         | 基础CRUD |
| 医生管理 | `/api/doctors`          | 基础CRUD |
| 科室管理 | `/api/departments`      | 基础CRUD |
| 号源查询 | `/api/slots/doctor/{id}` | 号源查询    |



## 💡 技术亮点

### 🔥 1️⃣ H2 内存数据库优势

```properties
# 零配置数据库
spring.datasource.url=jdbc:h2:mem:hospital_system
spring.h2.console.enabled=true
```

**优势：**
- ✅ **团队协作友好**：无需安装配置数据库
- ✅ **开发效率高**：clone 代码即可运行
- ✅ **调试方便**：内置 Web 控制台
- ✅ **数据持久**：程序运行期间数据保持

### 🔥 2️⃣ 智能业务逻辑

```java
// 急诊优先队列实现
@Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
       "AND a.appointmentDate = :date AND a.status = '已预约' " +
       "ORDER BY a.priority DESC, a.createdTime ASC")
List<Appointment> findQueueByDoctorAndDate(@Param("doctorId") Long doctorId, 
                                          @Param("date") LocalDate date);
```

### 🔥 3️⃣ 前端用户体验

- 📱 **响应式设计**：适配桌面和移动端
- ⚡ **异步交互**：Ajax 请求，无页面刷新
- 🎨 **现代UI**：卡片式布局，渐变色设计
- 🔄 **实时更新**：号源状态实时刷新



## ⚠️ 开发规范

### ✅ 1️⃣ 代码规范

**实体类设计：**
```java
// 解决 JSON 循环引用
@JsonIgnore
@OneToMany(mappedBy = "department")
private List<Doctor> doctors;

// 枚举增强可读性
public enum AppointmentType {
    普通("普通挂号，按先来先服务原则"),
    急诊("急诊挂号，优先处理");
}
```

**统一响应格式：**
```java
public class Result<T> {
    private Integer code;    // 状态码
    private String message;  // 响应消息
    private T data;         // 数据载荷
    private Long timestamp; // 时间戳
}
```

### ✅ 2️⃣ 数据库规范

**自动初始化测试数据：**
- 5个科室（内科、外科、儿科、急诊科、骨科）
- 6名医生（不同科室和职称）
- 3名测试病人
- 未来7天的号源数据

### ✅ 3️⃣ 前端规范

**页面结构：**
- 首页：系统概览和统计
- 挂号：完整挂号流程
- 记录：个人挂号历史
- 排队：医生排队查询
- 管理：系统数据管理



## 📅 任务分配

* [x] **基础架构搭建**（Member B）✅
* [x] **🌟 挂号核心模块**（Member B）✅ - 急诊优先队列
* [x] **号源管理系统**（Member B）✅ - 智能号源分配
* [x] **病人管理模块**（Member B）✅ - 基础CRUD
* [x] **医生管理模块**（Member B）✅ - 基础CRUD
* [x] **科室管理模块**（Member B）✅ - 基础CRUD
* [x] **前端界面系统**（Member B）✅ - 响应式单页应用
* [ ] 其他成员模块（Member A, C, D, E）🕒



## 🎯 项目演示

### 系统截图
1. **首页概览**：显示今日挂号统计和科室列表
2. **在线挂号**：支持普通挂号和急诊挂号
3. **排队查询**：急诊患者优先显示
4. **挂号记录**：个人挂号历史和状态跟踪
5. **系统管理**：数据管理和统计分析

### 核心业务流程
```
选择科室 → 选择医生 → 选择日期 → 查看可用号源 → 
选择挂号类型(普通/急诊) → 选择时间段 → 提交挂号 → 
获得排队号码 → 查看排队状态
```



## ✨ 特别说明

> 本项目专注于 **挂号模块** 的深度实现，展现了急诊优先队列、智能号源管理等
> 医疗行业核心业务逻辑，适合作为 Web 框架编程课程的优秀案例。

**技术价值：**
- 🏆 **业务复杂度高**：医疗挂号的真实业务场景
- 🏆 **算法应用**：优先级队列、动态资源分配
- 🏆 **工程实践**：分层架构、RESTful API、响应式前端
- 🏆 **团队协作**：零配置部署，便于团队开发

---

**开发者：Member B**  
**项目类型：Web 框架编程期末大作业**  
**完成时间：2025年**