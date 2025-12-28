------

# ad-system

Web应用开发实验大作业

- 本项目旨在设计一个实验性的跨站广告投放系统Web课程作业，实现匿名用户的个性化广告推荐。项目结构如下：

[[ad-site](https://github.com/YuujinT/ad-system)]：	广告提供服务器

[[news-site](https://github.com/YuujinT/ad-system/tree/news)]	新闻网站

[[shop-site](https://github.com/YuujinT/ad-system/tree/shop)]	购物网站

[[video-site](https://github.com/YuujinT/ad-system/tree/video)]	视频分享网站

------

# ad-site 开发文档

## 1. 总览

### 	1.1 项目概述：

- 目标：提供跨站匿名广告投放与兴趣上报服务API，并为广告业主提供素材管理后台。

- 部署形态：Tomcat + Servlet + JSP，WAR 包（`ad-site.war`），静态上传目录通过 Tomcat Context 映射到宿主机物理路径。

- 只聚焦 ad-site，本文件不包含其他业务站点。

- 架构：Servlet 仅做输入/输出与简单校验，业务落在 Service 层；DAO 负责持久化，JSP 负责展示。

  

  ### 1.2  任务分配：

  - **广告商部分** ： [YuujinT]（https://github.com/YuujinT/）

  - **3个社媒设计：** [GAODUAN11 (Duan Gao)] (https://github.com/GAODUAN11)

  - **开发周期：2025.11月底 ~ 2025.12月**

  

  ### 1.3 项目地址：

  ​	[YuujinT/ad-system: 团队Web课后作业]： https://github.com/YuujinT/ad-system

## 2. 技术栈
| 模块 | 技术 |
| --- | --- |
| Web框架 | Jakarta EE Servlet 6 + Tomcat + JSP + JSTL |
| 数据持久化 | MySQL（使用HikariCP 连接池，MySQL Connector/J 9.x） |
| JSON序列化 | Jackson 2.17（解析 JSON 请求/响应） |
| 前后端通信 | JavaScript Fetch |
| 用户识别 | 浏览器指纹 [FingerprintJS](https://github.com/fingerprintjs/fingerprintjs) |

## 3. 功能一览
- 广告业主登录：`/auth/login`（表单，`login.jsp`）。
- 素材管理（需登录）：
  - 上传：`/asset/upload`，生成 UUID 文件名，写入 Tomcat 映射的 `uploads` 物理目录，并记录 DB。
  - 列表/预览/删除：`/asset/list`（JSP `assets.jsp` 展示，点击文件名走 `/asset/file?name=...` 预览；删除走 `/asset/delete?id=...`，会同时删除磁盘文件和 DB 记录）。
- 兴趣上报 API：`/api/collectInterest`（POST JSON）。
- 广告下发 API：`/api/ad`（GET 流式返回图片/视频素材）。图片与视频统一由同一个 Servlet 处理，依据 `contentType` 选择素材。

## 4. 数据库设计（schema.sql）
- 库：`ad_site`
- 表：
- `ad_owner`：广告业主账号

| 字段        | 类型                 | 说明              |
| ----------- | -------------------- | ----------------- |
| id          | BIGINT PK AUTO_INCREMENT | 主键              |
| AdAccount   | VARCHAR(64) UNIQUE   | 账号名            |
| Password    | VARCHAR(128)         | 密码              |
| created_at  | TIMESTAMP            | 创建时间          |

- `ad_assets`：素材表

| 字段        | 类型             | 说明                                |
| ----------- | ---------------- | ----------------------------------- |
| id          | BIGINT PK AUTO_INCREMENT | 主键                           |
| owner_id    | VARCHAR(64)      | 业主账号（引用 `ad_owner.AdAccount`） |
| file_name   | VARCHAR(255)     | UUID 重命名后的文件名（含扩展名）    |
| content_type| VARCHAR(128)     | MIME 类型（如 image/png, video/mp4） |
| interest_tag| VARCHAR(32)      | 标签（五选一）                      |

- `user_tags`：用户兴趣计数

| 字段       | 类型        | 说明                  |
| ---------- | ----------- | --------------------- |
| id         | VARCHAR(64) PK | 用户 ID（fingerprint） |
| technology | INT DEFAULT 0 | 标签访问次数          |
| gaming     | INT DEFAULT 0 | 标签访问次数          |
| travel     | INT DEFAULT 0 | 标签访问次数          |
| sports     | INT DEFAULT 0 | 标签访问次数          |
| food       | INT DEFAULT 0 | 标签访问次数          |
- 预置账号：`demo_owner / demo123`。

## 5. 配置
- 数据源：`src/main/resources/application.properties`
  - `db.url=jdbc:mysql://127.0.0.1:3306/ad_site?useSSL=false&serverTimezone=UTC`
  - `db.driver=com.mysql.cj.jdbc.Driver`
  - `db.username=...`（按环境修改）
  - `db.password=...`（按环境修改）
  - `db.pool.size=20`
- 上传目录：通过 Tomcat `<Context>` 将 `/uploads` 映射到宿主机目录（例：`docBase="/data/uploads"`），Servlet 仅写入该目录，不写入 WAR 内部。
- 指纹js加载：`fp.js` `fp-logic.js` 放入`webapp` ，编写网站js实现指纹和用户兴趣。

## 6. API 规范说明
### 6.1 兴趣上报 API — `/api/collectInterest` (POST JSON)
- 请求头：`Content-Type: application/json`
- 请求体：
```json
{ "id": "<fingerprint>", "tag": "technology|gaming|travel|sports|food" }
```
- 行为：
  - 若 `id` 不存在则插入新行，目标标签计数 +1；存在则对该标签计数 +1。
  - `tag` 为空或不是预设`supportedTags`中的`TAG_COLUMNS` → 400 Bad Request。
  
- 响应：204 No Content。

- Example：

  ```js
  async function reportInterest(userId, tag) {
      if (!userId) {
          throw new Error('缺少用户 ID，已取消上报');
      }
      if (!tag) {
          return;
      }
      const res = await fetch('http://????:8080/ad-site/api/collectInterest', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: userId, tag })
      });
  
      if (!res.ok) {
          const text = await res.text();
          throw new Error(`上报失败 [${res.status}]: ${text}`);
      }
  }
  ```

### 6.2 广告下发 API — `/api/ad` (GET)
- 查询参数：`id=<fingerprint>&contentType=image|video`

- 逻辑：
  - 从 `user_tags` 找到该用户计数最高的标签；若有多项并列，随机挑一项；若全 0 或无记录，随机选一个标签。
  - 在 `ad_assets` 中按标签 + `content_type` 过滤，随机挑选一条，流式输出文件内容。
  - 未找到素材 → 404。
  
- 响应头：
  - `Content-Type` 与素材保持一致
  
- Example (Pictures):

  ```js
  async function loadImageAd(userId, contentType) {
      const res = await fetch('http://????:8080/ad-site/api/ad?id=' + encodeURIComponent(userId) + '&contentType=' + encodeURIComponent(contentType));
  
      if (!res.ok) {
          const text = await res.text();
          throw new Error(`广告加载失败 [${res.status}]: ${text}`);
      }
  
      const adContainer = document.getElementById('adContainer');
      adContainer.innerHTML = '';
  
      const blob = await res.blob();
      const imageUrl = URL.createObjectURL(blob);
  
      const img = document.createElement('img');
      img.className = 'ad-image';
      img.src = imageUrl;
      img.alt = '广告内容';
  
      adContainer.appendChild(img);
  }
  ```

## 7. 广告业主后台
- 登录页：`/login.jsp` → `AuthServlet`（成功跳转 `/WEB-INF/views/assets.jsp`）。
- 素材列表：`assets.jsp` 渲染 DB 数据，提供：
  - 预览：锚点链接 `/asset/file?name=...`，直接输出文件流。
  - 删除：`/asset/delete?id=...`，同时删除 DB 记录与物理文件。
- 上传页：`upload.jsp`，表单提交到 `/asset/upload`（multipart）。
  - 服务端生成 UUID 文件名，保留扩展名；写入映射目录；记录四个字段：文件名 / owner / contentType / interest_tag。

## 8. 构建与运行
1. 初始化

   数据库
```sql
SOURCE src/main/resources/schema.sql;
```
`pom.xml`修改上传素材的路径：upload.dir

`application.properties`中配置正确的mysql连接参数

2) 打包 WAR
```bash
.\mvnw.cmd clean package
```
3) 部署
- 将 `target/ad-site-1.0-SNAPSHOT.war` 部署至 Tomcat ，重命名为ad-site

4) 访问
- 业主后台：`http://<host>:<port>/ad-site/login.jsp`
- API：`/ad-site/api/...`

## 9. 行为细节与策略
- 标签集合设置为：`technology | gaming | travel | sports | food`。
- 并列最高计数标签随机挑选，避免偏向首项。
- 素材删除：先删磁盘，再删 DB，若磁盘缺失则记录日志但不阻塞。
- CORS：对 API 统一设置允许跨域（默认 `*`）。
- 上传重名规避：强制 UUID + 原扩展名。
- 无标签用户的广告返回：在可用素材中随机挑选符合请求 contentType 的素材。

## 10. 目录速览
- `src/main/java/com/example/adsite/controller`：Servlet（`AdApiServlet`、`InterestIngestServlet`、`UploadAssetServlet` 等）。
- `src/main/java/com/example/adsite/service`：`AdService` / `InterestService` 业务逻辑。
- `src/main/java/com/example/adsite/dao`：`AdAssetDao`、`UserTagDao`、`OwnerDao`。
- `src/main/java/com/example/adsite/config`：`DataSourceConfig`（HikariCP）。
- `src/main/webapp`：`login.jsp`、`error-invalid-login.jsp`、`WEB-INF/views/*.jsp`。
- `src/main/resources/schema.sql`：数据库建表与示例账号。

------
