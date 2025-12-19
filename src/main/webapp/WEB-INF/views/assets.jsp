<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>广告素材管理</title>
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; margin: 0; background: #f5f7fb; }
        header { background: #1f2a44; color: #fff; padding: 20px 40px; display: flex; justify-content: space-between; align-items: center; }
        main { padding: 32px 40px; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; background: #fff; border-radius: 12px; overflow: hidden; }
        th, td { padding: 14px 16px; border-bottom: 1px solid #eef2ff; }
        th { text-align: left; font-size: 14px; text-transform: uppercase; letter-spacing: .06em; color: #6c7a97; }
        tr:hover { background: #f8faff; }
        .tag { padding: 2px 8px; border-radius: 999px; font-size: 12px; background: #ecf2ff; color: #3f6df6; }
        .actions button { border: none; background: none; color: #f45d48; cursor: pointer; }
        .toolbar { display: flex; justify-content: flex-end; gap: 12px; margin-bottom: 16px; }
        .toolbar a { text-decoration: none; padding: 10px 20px; border-radius: 10px; background: #3f6df6; color: #fff; }
    </style>
</head>
<body>
<header>
    <div>
        <h1>广告素材控制台</h1>
        <p>当前账号：<c:out value="${accountName}"/></p>
    </div>
    <form method="post" action="<c:url value='/auth/logout'/>">
        <button type="submit">退出</button>
    </form>
</header>
<main>
    <div class="toolbar">
        <a href="<c:url value='/console/assets/upload'/>">上传素材</a>
    </div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>文件名</th>
            <th>Content-Type</th>
            <th>兴趣标签</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="asset" items="${assets}">
            <tr>
                <td><c:out value="${asset.id}"/></td>
                <td>
                    <a href="<c:url value='/uploads/${asset.fileName}'/>" target="_blank" rel="noopener">
                        <c:out value="${asset.fileName}"/>
                    </a>
                </td>
                <td><c:out value="${asset.contentType}"/></td>
                <td><span class="tag"><c:out value="${asset.interestTag}"/></span></td>
                <td class="actions">
                    <form method="post" action="<c:url value='/console/assets/delete'/>" onsubmit="return confirm('确认删除该素材吗？');">
                        <input type="hidden" name="assetId" value="${asset.id}"/>
                        <button type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty assets}">
            <tr>
                <td colspan="5">暂无素材，点击上方按钮上传。</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</main>
</body>
</html>
