<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>上传广告素材</title>
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; margin: 0; background: #f5f7fb; }
        header { background: #1f2a44; color: #fff; padding: 20px 40px; display: flex; justify-content: space-between; align-items: center; }
        main { padding: 32px 40px; max-width: 720px; margin: 0 auto; }
        form { background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 4px 20px rgba(0,0,0,0.04); }
        label { display: block; margin: 12px 0 6px; font-weight: 600; color: #1f2a44; }
        input, select { width: 100%; padding: 10px 12px; border-radius: 8px; border: 1px solid #d7deee; }
        button { margin-top: 18px; width: 100%; padding: 12px; border: none; background: #3f6df6; color: #fff; border-radius: 10px; cursor: pointer; }
        .error { color: #f45d48; margin-bottom: 10px; }
        a { color: #3f6df6; }
    </style>
</head>
<body>
<header>
    <div>
        <h1>上传广告素材</h1>
        <p>当前账号：<c:out value="${sessionScope.accountName}"/></p>
    </div>
    <a href="<c:url value='/console/assets'/>" style="color:#fff;">返回列表</a>
</header>
<main>
    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" enctype="multipart/form-data">
        <label for="asset">选择文件</label>
        <input type="file" id="asset" name="asset" accept="image/*,video/*" required>

        <label for="tag">兴趣标签</label>
        <select id="tag" name="tag" required>
            <c:forEach var="t" items="${tags}">
                <option value="${t}">${t}</option>
            </c:forEach>
        </select>

        <button type="submit">上传</button>
    </form>
</main>
</body>
</html>

