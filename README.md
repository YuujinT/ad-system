# 视频分享平台 (VideoSharePlatform)

一个基于Java Servlet和JSP技术构建的Web视频分享平台，支持用户注册、登录、视频上传、播放、分类管理等功能。

## 项目概述

视频分享平台是一个功能丰富的Web应用程序，允许用户上传、观看和管理视频内容。项目采用经典的MVC架构模式，使用Servlet处理业务逻辑，JSP作为视图层，MySQL作为数据库。

## 技术栈

- **后端**: Java Servlet, JSP
- **前端**: HTML, CSS, JavaScript
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **服务器**: 支持Servlet容器 (如Tomcat 10+)
- **其他技术**: JSTL, Apache Commons, BCrypt

## 功能特性

### 用户功能
- 用户注册与登录
- 个人资料管理
- 密码安全加密 (BCrypt)
- 用户会话管理

### 视频功能
- 视频上传
- 视频播放
- 视频编辑
- 视频删除
- 视频分类浏览
- 视频推荐
- 视频广告功能：支持播放前广告，广告结束后自动播放主视频

### 管理功能
- 视频管理
- 分类管理
- 用户权限控制

## 系统架构

### 项目结构
```
src/
├── main/
│   ├── java/
│   │   ├── controller/      # Servlet控制器
│   │   ├── dao/            # 数据访问层
│   │   ├── model/          # 数据模型
│   │   ├── service/        # 业务逻辑层
│   │   └── util/           # 工具类
│   ├── resources/          # 配置文件
│   └── webapp/             # Web资源
│       ├── WEB-INF/
│       ├── css/            # 样式文件
│       └── js/             # JavaScript文件
```

### 核心模块

#### 控制器层 (Controller)
- [CategoriesServlet](src/main/java/org/example/videoshareplatform01/controller/CategoriesServlet.java) - 分类列表
- [CategoryServlet](src/main/java/org/example/videoshareplatform01/controller/CategoryServlet.java) - 分类详情
- [DeleteVideoServlet](src/main/java/org/example/videoshareplatform01/controller/DeleteVideoServlet.java) - 删除视频
- [EditVideoServlet](src/main/java/org/example/videoshareplatform01/controller/EditVideoServlet.java) - 编辑视频
- [HomeServlet](src/main/java/org/example/videoshareplatform01/controller/HomeServlet.java) - 首页
- [LoginServlet](src/main/java/org/example/videoshareplatform01/controller/LoginServlet.java) - 用户登录
- [LogoutServlet](src/main/java/org/example/videoshareplatform01/controller/LogoutServlet.java) - 用户登出
- [ProfileServlet](src/main/java/org/example/videoshareplatform01/controller/ProfileServlet.java) - 个人资料
- [RegisterServlet](src/main/java/org/example/videoshareplatform01/controller/RegisterServlet.java) - 用户注册
- [UploadServlet](src/main/java/org/example/videoshareplatform01/controller/UploadServlet.java) - 视频上传
- [VideoServlet](src/main/java/org/example/videoshareplatform01/controller/VideoServlet.java) - 视频播放

#### 数据访问层 (DAO)
- [UserDao](src/main/java/org/example/videoshareplatform01/dao/UserDao.java) / [UserDaoImpl](src/main/java/org/example/videoshareplatform01/dao/UserDaoImpl.java) - 用户数据访问
- [VideoDao](src/main/java/org/example/videoshareplatform01/dao/VideoDao.java) / [VideoDaoImpl](src/main/java/org/example/videoshareplatform01/dao/VideoDaoImpl.java) - 视频数据访问

#### 业务逻辑层 (Service)
- [UserService](src/main/java/org/example/videoshareplatform01/service/UserService.java) - 用户业务逻辑
- [VideoService](src/main/java/org/example/videoshareplatform01/service/VideoService.java) - 视频业务逻辑

#### 实体模型 (Model)
- [User](src/main/java/org/example/videoshareplatform01/model/User.java) - 用户实体
- [Video](src/main/java/org/example/videoshareplatform01/model/Video.java) - 视频实体

## 环境要求

- Java 11 或更高版本
- Maven 3.6 或更高版本
- MySQL 5.7 或更高版本
- Servlet容器 (如Tomcat 10+)

## 安装与部署

### 1. 克隆项目
```bash
git clone <repository-url>
cd VideoSharePlatform01
```

### 2. 创建数据库
执行 [database.sql](src/main/resources/database.sql) 创建数据库表结构：

```sql
CREATE DATABASE video_share CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE video_share;
SOURCE src/main/resources/database.sql;
```

### 3. 配置数据库
修改 [application.properties](src/main/resources/application.properties) 文件中的数据库连接信息：

```properties
# 数据库配置
db.url=jdbc:mysql://localhost:3306/video_share?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=your_username
db.password=your_password
```

### 4. 配置视频上传路径
在 [application.properties](src/main/resources/application.properties) 中设置视频上传目录：

```properties
# 视频存储目录配置
video.upload.dir=/path/to/video_uploads
```

### 5. 构建项目
```bash
mvn clean package
```

### 6. 部署到服务器
将生成的WAR文件部署到Servlet容器中，或直接在IDE中运行。

## 使用说明

### 用户功能
1. 访问首页查看推荐视频
2. 注册新账户或登录现有账户
3. 上传视频并填写相关信息
4. 浏览和播放视频
5. 管理个人资料和上传的视频

### 管理员功能
- 管理所有视频内容
- 管理用户账户
- 设置视频分类

## 依赖库

- **Jakarta Servlet API**: Web框架基础
- **Jakarta JSP API**: JSP页面支持
- **Jakarta JSTL**: JSP标签库
- **MySQL Connector**: 数据库连接
- **Apache Commons DBCP2**: 数据库连接池
- **BCrypt**: 密码加密
- **Apache Commons FileUpload**: 文件上传处理
- **Apache Commons IO**: 文件操作工具

## 项目特点

- **安全性**: 使用BCrypt加密用户密码，防止密码泄露
- **可扩展性**: 采用MVC架构，便于功能扩展
- **用户友好**: 直观的界面设计，易于使用
- **性能优化**: 使用数据库连接池提高性能
- **广告集成**: 集成视频广告功能，支持个性化广告投放

## 贡献

欢迎提交Issue和Pull Request来改进项目。

## 许可证

[MIT License](LICENSE)