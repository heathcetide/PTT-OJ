# PTT Judge System

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.2-green.svg)
![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-5.7+-blue.svg)

## 项目介绍

PTT是一个功能完善的在线判题系统，基于 Spring Boot 开发的在线编程评测平台。本系统支持多种编程语言的代码在线运行、评测，并提供题目管理、用户管理、排行榜等功能。系统采用微服务架构设计，具有高可用性、可扩展性和安全性。

### 项目特点

- 🚀 高性能：采用分布式架构，支持高并发访问
- 🔒 安全性：代码沙箱隔离执行，防止恶意代码
- 🎯 准确性：严格的判题逻辑，支持多种测试用例
- 🔌 扩展性：支持多种编程语言，易于扩展新功能
- 📊 可视化：直观的数据统计和排行榜展示

## 技术栈

### 后端技术
- Spring Boot：核心框架
- Spring Security：认证和授权
- MyBatis：ORM框架
- MySQL：关系型数据库
- Redis：缓存和会话管理
- Elasticsearch：全文搜索引擎
- RabbitMQ：消息队列
- Docker：容器化部署
- Nginx：反向代理和负载均衡

### 开发工具
- IntelliJ IDEA：Java IDE
- Maven：依赖管理
- Git：版本控制
- Postman：接口测试
- JUnit：单元测试
- Swagger：接口文档

## 核心功能

### 1. 用户系统
- 账号管理：注册、登录、找回密码
- 用户认证：JWT token认证
- 权限控制：基于RBAC的权限管理
- 个人中心：用户信息管理

### 2. 题目系统
- 题目管理：创建、修改、删除题目
- 在线判题：实时评测、多语言支持
- 提交历史：详细的提交记录和运行结果
- 题目分类：标签管理、难度分级

### 3. 判题系统
- 代码沙箱：安全的代码执行环境
- 资源限制：内存、CPU使用限制
- 结果验证：多测试用例支持
- 评测反馈：详细的错误信息

### 4. 排行榜系统
- 实时排名：基于解题数和提交时间
- 做题统计：个人题目完成情况
- 竞赛排名：比赛专用排行榜
- 数据分析：解题趋势分析

## 项目结构
- oj-backend
- ├── common # 公共模块
- │ ├── common-core # 核心功能
- │ ├── common-util # 工具类
- │ └── common-security # 安全模块
- ├── controller # 控制器层
- ├── service # 业务逻辑层
- ├── mapper # 数据访问层
- ├── model # 数据模型
- │ ├── entity # 实体类
- │ ├── dto # 数据传输对象
- │ └── vo # 视图对象
- ├── config # 配置类
- └── utils # 工具类


## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+
- Elasticsearch 7.x
- Docker & Docker Compose

### 本地开发

1. 克隆项目

git clone https://github.com/Heath-Cetide/oj-backend.git
cd oj-backend


2. 配置环境

修改 application.yml 配置文件
vim src/main/resources/application.yml


3. 初始化数据库

执行SQL脚本
mysql -u username -p database_name < docs/sql/init.sql


4. 启动项目

mvn spring-boot:run


### Docker部署

1. 构建镜像

docker build -t oj-backend .


2. 启动服务

docker-compose up -d


## 接口文档

项目启动后访问：
- Swagger接口文档：`http://localhost:8080/swagger-ui.html`
- API文档：`http://localhost:8080/doc.html`

## 性能优化

- 使用Redis缓存热点数据
- Elasticsearch优化搜索性能
- 数据库索引优化
- 接口性能优化
- JVM调优建议

## 安全特性

- Spring Security 安全框架
- JWT 认证
- 代码沙箱隔离
- XSS防御
- SQL注入防护
- 接口限流

## 开发规范

- 遵循阿里巴巴Java开发规范
- 统一的代码格式化模板
- 规范的注释要求
- 统一的接口响应格式
- 详细的提交信息规范

## 贡献指南

1. Fork 本仓库
2. 创建新的分支 `git checkout -b feature/your-feature`
3. 提交更改 `git commit -m 'Add some feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 提交 Pull Request

## 版本历史

- v1.0.0 (2024-03-20)
  - 初始版本发布
  - 基础功能实现

## 开源协议

本项目使用 [MIT 许可证](LICENSE)

## 作者

Heath-Cetide

## 联系方式

- Email: heath@cetide.com
- GitHub: [@Heath-Cetide](https://github.com/Heath-Cetide)

## 鸣谢

感谢所有为这个项目做出贡献的开发者们！

## 常见问题

详见 [Wiki](../../wiki) 页面# PTT-OJ
# PTT-OJ
