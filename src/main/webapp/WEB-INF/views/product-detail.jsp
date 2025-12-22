<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - 购物平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
    <script src="${pageContext.request.contextPath}/js/fp.js"></script>
    <script src="${pageContext.request.contextPath}/js/fp-logic.js"></script>
</head>
<body>
<header>
    <h1>欢迎来到购物平台</h1>
</header>

<nav>
    <ul>
        <li><a href="${pageContext.request.contextPath}/home"
               class="<c:if test='${empty selectedCategory}'>active</c:if>">全部商品</a></li>
        <c:forEach var="category" items="${categories}">
            <li><a href="${pageContext.request.contextPath}/home?category=${category}"
                   class="<c:if test='${selectedCategory eq category}'>active</c:if>">${category}</a></li>
        </c:forEach>
    </ul>
</nav>

<div class="container">
    <!-- 广告区域 -->
    <div class="advertisement">
        <h2>商品详情页广告</h2>
        <div id="adContainer" class="ad-image"></div>
    </div>

    <!-- 商品详情区域 -->
    <div class="product-detail-container">
        <div class="product-detail-card">
            <h2>${product.name}</h2>
            <div class="product-detail-info">
                <div class="product-detail-item">
                    <span class="detail-label">商品类别:</span>
                    <span class="detail-value product-category">${product.category}</span>
                </div>
                <div class="product-detail-item">
                    <span class="detail-label">商品价格:</span>
                    <span class="detail-value product-price">￥${product.price}</span>
                </div>
                <div class="product-detail-item">
                    <span class="detail-label">商品描述:</span>
                    <span class="detail-value">${product.description}</span>
                </div>
            </div>
            <div class="product-actions">
                <button class="btn btn-primary">加入购物车</button>
                <button class="btn btn-secondary">立即购买</button>
            </div>
        </div>
    </div>

    <div class="back-link">
        <a href="${pageContext.request.contextPath}/home">&laquo; 返回商品列表</a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/advertisement.js"></script>
</body>
</html>