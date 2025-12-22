// D:\A-development-project\IDEA-Java\firstproject\src\views\ad.js

// 广告加载器初始化函数（作为主要导出函数）
export async function initAdLoader() {
    try {
        const uuid = await getOrCreateUserId();
        if (!uuid) {
            throw new Error('无法获取用户指纹 ID');
        }

        const tag = resolvePageTag();
        if (tag) {
            await reportInterest(uuid, tag);
        }
        await loadImageAd(uuid, 'image');

    } catch (error) {
        showError(error.message);
    }
}

// 解析页面标签函数
function resolvePageTag() {
    // 确保在浏览器环境中执行
    if (typeof window === 'undefined' || !window.location) {
        return null;
    }

    const url = new URL(window.location.href);
    // 去除末尾多余斜杠，并拆分路径段
    const pathname = url.pathname.replace(/\/+$/, '');
    const segments = pathname.split('/').filter(Boolean);

    // 路由模式：
    // 1) '/'           -> 首页，不上报
    // 2) '/category/:category' -> 从路径参数提取分类并上报
    // 3) '/news/:id'   -> 详情页，无法从URL直接判断分类，不上报

    // 首页
    if (segments.length === 0) {
        return null;
    }

    const root = segments[0];

    // 分类列表页：/category/:category
    if (root === 'category' && segments.length >= 2) {
        const category = segments[1];
        return category || null;
    }

    // 新闻详情页：/news/:id（此处不上报）
    if (root === 'news') {
        return null;
    }

    // 其他路径不上报
    return null;
}

// 上报兴趣函数
async function reportInterest(userId, tag) {
    if (!userId) {
        throw new Error('缺少用户 ID，已取消上报');
    }
    if (!tag) {
        return;
    }
    const res = await fetch('http://8.136.38.185:8080/ad-site/api/collectInterest', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: userId, tag })
    });

    if (!res.ok) {
        const text = await res.text();
        throw new Error(`上报失败 [${res.status}]: ${text}`);
    }
    console.log('兴趣数据上报成功');
}

// 加载广告函数
async function loadImageAd(userId, contentType) {
    const res = await fetch(`http://8.136.38.185:8080/ad-site/api/ad?id=${encodeURIComponent(userId)}&contentType=${encodeURIComponent(contentType)}`);

    if (!res.ok) {
        const text = await res.text();
        throw new Error(`广告加载失败 [${res.status}]: ${text}`);
    }

    // 创建图片容器
    const adContainer = document.getElementById('adContainer');
    if (!adContainer) {
        console.error('广告容器不存在');
        return;
    }
    adContainer.innerHTML = ''; // 清空旧内容

    // 处理二进制流
    const blob = await res.blob();
    const imageUrl = URL.createObjectURL(blob);

    // 创建图片元素
    const img = document.createElement('img');
    img.className = 'ad-image';
    img.src = imageUrl;
    img.alt = '广告内容';

    // 添加到页面
    adContainer.appendChild(img);

    // 资源释放处理
    img.addEventListener('load', () => {
        URL.revokeObjectURL(imageUrl);
    });

    console.log('广告加载成功');
}

// 错误提示函数
function showError(message) {
    console.error('广告系统错误:', message);

    // 在页面上显示错误信息
    const errorDiv = document.createElement('div');
    errorDiv.className = 'ad-error';
    errorDiv.textContent = `广告加载失败: ${message}`;
    errorDiv.style.cssText = `
    color: #ff0000;
    background: #ffe6e6;
    padding: 10px;
    border: 1px solid #ff0000;
    border-radius: 4px;
    margin: 10px 0;
  `;

    // 尝试添加到页面body中
    if (document.body) {
        document.body.appendChild(errorDiv);
    }
}

// 等待用户指纹ID函数
async function waitForVisitorId(timeoutMs = 5000, intervalMs = 100) {
    const start = Date.now();
    return new Promise((resolve, reject) => {
        const timer = setInterval(() => {
            if (window.visitorId) {
                clearInterval(timer);
                resolve(window.visitorId);
            } else if (Date.now() - start > timeoutMs) {
                clearInterval(timer);
                reject(new Error('无法获取用户指纹 ID'));
            }
        }, intervalMs);
    });
}

// 获取或创建用户ID函数
async function getOrCreateUserId() {
    try {
        // 只等待 FingerprintJS，失败则抛出错误
        return await waitForVisitorId();
    } catch (error) {
        console.error('获取用户ID失败:', error);
        return null;
    }
}
