document.addEventListener('DOMContentLoaded', async function() {
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
});

function resolvePageTag() {
    const url = new URL(window.location.href);
    const pathname = url.pathname.replace(/\/+$/, '');
    const segments = pathname.split('/').filter(Boolean);
    const ctxless = segments.length > 1 ? segments.slice(1) : segments; // 去掉可能的上下文前缀
    // 判断路径段数量是否大于1
    // 如果大于1，则使用slice(1)去掉第一个元素（通常是应用上下文路径）
    // 如果不大于1，则保持原样
    // 例如：['shop', 'home'] → ['home']

    // /home?category=xxx 上报，缺参则不上报
    if (ctxless[0] === 'home') {
        const category = url.searchParams.get('category');
        return category ? category : null;
    }

    // /product... 从页面读取
    if (ctxless[0] === 'product') {
        const elem = document.querySelector('.product-category');
        if (elem && elem.textContent && elem.textContent.trim()) {
            return elem.textContent.trim();
        }
        return null;
    }

    // 其他不上报
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
}

// 错误提示函数
function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error';
    errorDiv.textContent = message;
    document.querySelector('.container').appendChild(errorDiv);
}

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

async function getOrCreateUserId() {
    // 只等待 FingerprintJS，失败则抛出错误
    return waitForVisitorId();
}
