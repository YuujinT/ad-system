# 购物模拟网站

## 项目概述

这是一个基于Java Servlet/JSP技术开发的购物平台模拟网站，采用了标准的MVC架构模式。项目包含商品展示、详情页等功能，并集成了个性化的图片广告展示系统。

## 项目结构

```
ShopPlatform/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/example/shopplatform/
│       │       ├── model/
│       │       │   └── Product.java          # 商品实体类
│       │       ├── service/
│       │       │   └── ProductService.java   # 商品业务逻辑服务
│       │       └── servlet/
│       │           ├── HomeServlet.java      # 首页控制器
│       │           └── ProductDetailServlet.java # 商品详情页控制器
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── views/
│           │   │   ├── home.jsp              # 首页视图
│           │   │   └── product-detail.jsp    # 商品详情页视图
│           │   └── web.xml                   # Web应用配置文件
│           ├── css/
│           │   └── style.css                 # 样式文件
│           └── js/
│               ├── advertisement.js          # 广告功能JavaScript
│               ├── fp-logic.js               # 用户指纹生成逻辑
│               └── fp.js                     # FingerprintJS库
├── README.md                               # 项目说明文档
├── pom.xml                                 # Maven项目配置文件
└── mvnw.cmd                                # Maven包装器脚本
```

## 架构设计

项目遵循标准的MVC（Model-View-Controller）分层架构：

- **Model层**：位于`model`包中，包含实体类，负责数据封装。
- **Service层**：位于`service`包中，包含业务逻辑处理。
- **Controller层**：位于`servlet`包中，处理HTTP请求和响应。
- **View层**：位于`webapp/WEB-INF/views`目录下，使用JSP技术结合JSTL和EL表达式进行数据渲染。

## 图片广告的获取与展示

### 功能概述

项目实现了智能图片广告展示系统，该系统能够根据用户的浏览行为展示个性化广告内容。广告系统主要通过以下步骤实现：

1. 生成用户唯一标识（指纹）
2. 分析用户兴趣标签
3. 向广告服务器请求个性化广告
4. 在页面上展示相关广告

### 技术实现

#### 1. 用户识别机制

- 使用FingerprintJS库（[fp.js](src/main/webapp/js/fp.js)）生成用户设备的唯一指纹
- 指纹信息存储在全局变量`window.visitorId`中供其他功能使用
- 实现文件：[fp-logic.js](src/main/webapp/js/fp-logic.js)

#### 2. 兴趣标签收集

- 系统会根据用户访问的页面类型自动收集兴趣标签：
  - 访问首页（带分类参数）时，将分类参数作为兴趣标签
  - 访问商品详情页时，从页面提取商品类别作为兴趣标签
- 兴趣标签上报至广告服务器，用于后续的个性化广告匹配

#### 3. 广告展示流程

广告展示的核心逻辑实现在[advertisement.js](src/main/webapp/js/advertisement.js)文件中：

- 页面加载完成后，获取用户指纹ID
- 解析当前页面的类别标签并上报用户兴趣
- 请求个性化图片广告
- 将广告图片动态插入到页面指定位置

具体实现步骤：

1. **初始化**：监听`DOMContentLoaded`事件，在页面加载完成后执行广告逻辑
2. **获取用户ID**：调用`getOrCreateUserId()`函数获取用户指纹ID
3. **上报兴趣**：分析页面URL或内容，确定用户兴趣标签并上报
4. **加载广告**：向广告服务器发送请求获取个性化广告图片
5. **展示广告**：将返回的广告图片显示在页面的广告区域

#### 4. API接口

- 兴趣上报接口：`http://8.136.38.185:8080/ad-site/api/collectInterest`
- 广告获取接口：`http://8.136.38.185:8080/ad-site/api/ad`

### 使用方法

要使广告功能正常工作，请确保：

1. 页面中包含了必要的JavaScript文件：
   ```html
   <script src="js/fp.js"></script>
   <script src="js/fp-logic.js"></script>
   <script src="js/advertisement.js"></script>
   ```

2. 在页面中设置广告容器：
   ```html
   <div id="adContainer"></div>
   ```

3. 在商品详情页中添加类别标识：
   ```html
   <div class="product-category">电子产品</div>
   ```

## 技术栈

- **后端**：Java Servlet, JSP
- **前端**：HTML, CSS, JavaScript
- **构建工具**：Maven
- **Java版本**：Java 11
- **广告技术**：用户指纹识别，个性化广告推荐

## 运行项目

1. 确保安装了Maven和Java 11+
2. 执行编译命令：`mvn clean compile`
3. 在支持Servlet的Web服务器（如Tomcat）中部署运行

## 注意事项

- 广告功能依赖外部广告服务器，需要网络连接
- 用户指纹生成是广告个性化展示的关键组件，不可移除
- 项目使用JSP作为视图技术，配合JSTL和EL表达式进行数据渲染