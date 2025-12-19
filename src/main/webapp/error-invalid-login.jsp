<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>登录失败</title>
    <style>
        body { font-family: 'Segoe UI', Arial, sans-serif; margin: 0; background: #f6f7fb; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
        .card { background: #fff; padding: 32px 40px; border-radius: 14px; box-shadow: 0 8px 30px rgba(0,0,0,.08); text-align: center; width: min(420px, 90%); }
        h1 { margin: 0 0 12px; color: #d32f2f; }
        p { margin: 0 0 16px; color: #4a5568; }
        a { display: inline-block; padding: 10px 18px; background: #3f6df6; color: #fff; border-radius: 10px; text-decoration: none; }
    </style>
</head>
<body>
<div class="card">
    <h1>登录失败</h1>
    <p>账号或密码不正确，请返回重新输入。</p>
    <a href="login.jsp">返回登录</a>
</div>
</body>
</html>

