<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
    <script src="${pageContext.request.contextPath}/js/fp.js"></script>
    <script src="${pageContext.request.contextPath}/js/fp-logic.js"></script>
    <script src="${pageContext.request.contextPath}/js/advertisement.js"></script>
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
        <h2>猜你想看</h2>
        <div class="ad-image" id="adContainer">
            广告图片展示区 (预留用于动态广告推送)
        </div>
    </div>

    <!-- 商品展示区域 -->
    <div class="products-container">
        <c:choose>
            <c:when test="${empty products}">
                <p>暂无商品</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="product" items="${products}">
                    <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}" class="product-link">
                        <div class="product-card">
                            <h3>${product.name}</h3>
                            <span class="product-category">${product.category}</span>
                            <p class="product-description">${product.description}</p>
                            <p class="product-price">￥${product.price}</p>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>