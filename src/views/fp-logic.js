(function() {
    // 防重复初始化
    if (window.__fpInitialized) {
        return;
    }

    function initWithLib(FingerprintJS) {
        if (!FingerprintJS || typeof FingerprintJS.load !== 'function') {
            console.error('FingerprintJS 不可用');
            return;
        }
        window.__fpInitialized = true;
        FingerprintJS.load()
            .then(fp => fp.get())
            .then(result => {
                window.visitorId = result.visitorId;
                console.log('Fingerprint 生成成功，已设置 window.visitorId');
            })
            .catch(error => {
                console.error('Fingerprint 生成失败:', error);
            });
    }

    // 优先使用全局对象
    if (typeof window !== 'undefined' && window.FingerprintJS) {
        initWithLib(window.FingerprintJS);
        return;
    }

    // 尝试动态导入 ESM 包（适用于打包后或未挂全局的情况）
    try {
        import('@fingerprintjs/fingerprintjs')
            .then(mod => {
                const lib = mod && (mod.default || mod);
                // 备份到全局，供其它脚本使用
                window.FingerprintJS = lib;
                initWithLib(lib);
            })
            .catch(err => {
                console.error('FingerprintJS 动态导入失败:', err);
            });
    } catch (e) {
        console.error('FingerprintJS library not found.', e);
    }
})();