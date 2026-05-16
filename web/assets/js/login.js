function getLoginContextPath() {
    if (typeof AppBootstrap !== 'undefined' && AppBootstrap.resolveContextPath) {
        return AppBootstrap.resolveContextPath();
    }
    return window.CONTEXT_PATH || '';
}

function togglePass(id, btn) {
    var inp = document.getElementById(id);
    inp.type = inp.type === 'password' ? 'text' : 'password';
    btn.textContent = inp.type === 'password' ? '👁' : '🙈';
}

var LOGIN_CONFIG = {
    USE_MOCK: false,
    API_BASE: (window.CONTEXT_PATH || '') + '/api/auth',
    MOCK_DELAY_MS: 120
};

function loginDelay(ms) {
    return new Promise(function(resolve) { setTimeout(resolve, ms); });
}

async function loginRequest(path, options) {
    var opts = options || {};
    var method = opts.method || 'POST';
    var body = opts.body || null;
    var headers = opts.headers || {};
    var reqOptions = {
        method: method,
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== null && body !== undefined) reqOptions.body = JSON.stringify(body);

    var res = await fetch(LOGIN_CONFIG.API_BASE + path, reqOptions);
    var json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) ? json.message : ('Lỗi HTTP: ' + res.status));
    return json || {};
}

var loginMockApi = {
    login: async function(payload) {
        await loginDelay(LOGIN_CONFIG.MOCK_DELAY_MS);
        if (!payload.account || !payload.password) return { success: false, message: 'Thiếu tài khoản hoặc mật khẩu' };
        return { success: true, phone: payload.account };
    }
};

var loginApi = {
    login: function(payload) {
        return loginRequest('/login', { method: 'POST', body: payload });
    }
};

var loginDataSource = LOGIN_CONFIG.USE_MOCK ? loginMockApi : loginApi;

function showLoginError(message) {
    AppNotify.error(message || 'Đăng nhập thất bại');
}

async function handleLogin(e) {
    e.preventDefault();
    var ok = true;
    var acc = document.getElementById('loginAccount').value.trim();
    var pass = document.getElementById('loginPassword').value;
    var submitBtn = e.target.querySelector('button[type="submit"]');
    if (!acc) {
        document.getElementById('accountGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('accountGroup').classList.remove('error');
    }
    if (!pass) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('passGroup').classList.remove('error');
    }
    if (ok) {
        try {
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.dataset.originalText = submitBtn.innerHTML;
                submitBtn.innerHTML = 'Đang đăng nhập...';
            }

            var ctx = getLoginContextPath();
            var response = await fetch(ctx + '/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ account: acc, password: pass })
            });

            if (!response.ok) {
                throw new Error('Lỗi máy chủ: HTTP ' + response.status);
            }

            var res = await response.json();

            if (res && res.success) {
                var qp = new URLSearchParams(window.location.search);
                if (qp.get('redirect') === 'datlich') {
                    window.location.href = getLoginContextPath() + '/Infor/Schedule#datlich';
                } else {
                    window.location.href = res.redirectUrl;
                }
            } else {
                showLoginError((res && res.message) || 'Sai tài khoản hoặc mật khẩu');
            }
        } catch (error) {
            console.error('[login] error:', error);
            showLoginError(error.message || 'Lỗi kết nối đến máy chủ');
        } finally {
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.innerHTML = submitBtn.dataset.originalText || 'Đăng nhập';
            }
        }
    }
    return false;
}
document.getElementById('loginAccount').addEventListener('input', function() {
    document.getElementById('accountGroup').classList.remove('error');
});
document.getElementById('loginPassword').addEventListener('input', function() {
    document.getElementById('passGroup').classList.remove('error');
});

