<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>广告业主登录 - AD Site</title>
    <style>
        :root { font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif; background: #f5f7fb; }
        body { margin: 0; display: flex; min-height: 100vh; align-items: center; justify-content: center; }
        .card { background: #fff; border-radius: 16px; box-shadow: 0 20px 60px rgba(15, 35, 95, .12); padding: 56px 72px; width: min(420px, 90%); }
        h1 { margin: 0 0 8px; font-size: 28px; color: #1b2440; }
        p.subtitle { margin: 0 0 32px; color: #5b6275; }
        label { color: #1b2440; font-weight: 600; display: block; margin-bottom: 6px; }
        input { width: 100%; border: 1px solid #d0d5e6; border-radius: 10px; padding: 14px 16px; font-size: 16px; margin-bottom: 18px; transition: border-color .2s; }
        input:focus { outline: none; border-color: #3f6df6; box-shadow: 0 0 0 3px rgba(63,109,246,.2); }
        button { width: 100%; border: none; border-radius: 12px; background: linear-gradient(135deg,#4e7df3,#7f5af0); color: #fff; font-size: 17px; padding: 14px 18px; cursor: pointer; margin-top: 10px; }
        button:hover { opacity: .92; }
        .hint { margin-top: 20px; color: #6c728a; font-size: 14px; text-align: center; }
        .hint a { color: #4e7df3; text-decoration: none; }
        .error { background: #ffe6e6; border-left: 4px solid #f44336; color: #d8000c; padding: 10px; margin-bottom: 20px; border-radius: 4px; }
    </style>
</head>
<body>
<div class="card">
    <h1>广告控制台</h1>
    <p class="subtitle">登录以管理您的广告素材与投放</p>
    <c:if test="${param.error eq 'invalid'}">
        <div class="error">账号或密码错误，请重试</div>
    </c:if>
    <form method="post" action="<c:url value='/auth/login'/>">
        <label for="username">账号</label>
        <input id="username" name="username" type="text" autocomplete="username" required>

        <label for="password">密码</label>
        <input id="password" name="password" type="password" autocomplete="current-password" required>

        <button type="submit">登录控制台</button>
    </form>
    <div class="hint">没有账号？请联系广告站管理员开通。</div>
</div>
</body>
</html>
