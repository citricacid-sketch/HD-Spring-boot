# 部署检查清单

在部署到Railway之前，请确保完成以下检查：

## 代码配置检查

### ✅ 数据库配置
- [x] 已添加PostgreSQL驱动依赖
- [x] 已创建application-production.properties
- [x] 已配置PostgreSQL连接（使用DATABASE_URL环境变量）
- [x] 已配置正确的数据库方言（PostgreSQLDialect）
- [x] 已设置ddl-auto为update（生产环境）

### ✅ API配置
- [x] DeepSeek API密钥使用环境变量（DEEPSEEK_API_KEY）
- [x] 已配置重试机制
- [x] 已配置超时设置

### ✅ 安全配置
- [x] 生产环境禁用H2控制台
- [x] 已配置CORS（支持环境变量CORS_ALLOWED_ORIGINS）
- [x] 日志级别设置为INFO（生产环境）

### ✅ 端口配置
- [x] 支持通过PORT环境变量配置端口
- [x] 默认端口为8080

## 部署前准备

### 1. 环境变量准备

在Railway上需要配置以下环境变量：

```
DEEPSEEK_API_KEY=your_deepseek_api_key
SPRING_PROFILES_ACTIVE=production
PORT=8080
CORS_ALLOWED_ORIGINS=https://your-frontend.netlify.app
```

注意：`DATABASE_URL`会由Railway自动提供，无需手动配置。

### 2. GitHub仓库检查

- [ ] 代码已推送到GitHub
- [ ] .gitignore已配置（忽略敏感文件）
- [ ] README.md已更新（包含部署说明）

### 3. 测试检查

- [ ] 本地测试通过
- [ ] API调用正常
- [ ] 数据库操作正常
- [ ] CORS配置正确

## Railway部署步骤

### 1. 创建项目
1. 登录Railway
2. 点击 "New Project"
3. 选择 "Deploy from GitHub repo"
4. 选择后端仓库

### 2. 添加数据库
1. 在项目页面点击 "New Service"
2. 选择 "Database"
3. 选择 "PostgreSQL"

### 3. 配置环境变量
在后端服务的 "Variables" 标签页添加：
- DEEPSEEK_API_KEY
- SPRING_PROFILES_ACTIVE=production
- PORT=8080
- CORS_ALLOWED_ORIGINS

### 4. 部署
1. 点击 "Deploy" 按钮
2. 等待部署完成
3. 检查部署日志

### 5. 验证
1. 测试API端点
2. 检查数据库连接
3. 测试CORS配置

## 常见问题排查

### 问题1：部署失败
- 检查构建日志
- 确认依赖是否完整
- 检查Java版本（需要17+）

### 问题2：数据库连接失败
- 检查DATABASE_URL是否正确
- 确认数据库服务正在运行
- 查看后端日志

### 问题3：API调用失败
- 检查DEEPSEEK_API_KEY是否正确
- 确认API配额是否充足
- 查看后端日志

### 问题4：CORS错误
- 检查CORS_ALLOWED_ORIGINS配置
- 确认前端URL是否正确
- 检查浏览器控制台错误

## 部署后维护

### 1. 监控
- 定期查看Railway日志
- 监控API调用次数
- 检查数据库使用情况

### 2. 更新
- 推送代码到GitHub后自动部署
- 或手动触发重新部署

### 3. 备份
- 定期备份数据库
- 保留重要配置

## 成本监控

Railway免费额度：
- 后端服务：约$2-3/月
- PostgreSQL：约$2-3/月
- 总计：约$5/月

定期检查使用情况，避免超出免费额度。

## 联系支持

如遇到问题：
- 查看Railway文档：https://docs.railway.app
- 查看Spring Boot文档：https://spring.io/projects/spring-boot
- 提交GitHub Issue
