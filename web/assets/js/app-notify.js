/**
 * AppNotify — thay thế alert() / confirm() kiểu trình duyệt.
 * AppNotify.confirm({ title, message, confirmText, cancelText }) => Promise<boolean>
 * AppNotify.alert({ title, message, type }) => Promise<void>
 */
(function(global) {
    'use strict';

    var overlay = null;
    var titleEl = null;
    var messageEl = null;
    var actionsEl = null;
    var resolveFn = null;
    var mode = 'alert';

    function ensureDom() {
        if (overlay) return;
        overlay = document.createElement('div');
        overlay.id = 'appNotifyOverlay';
        overlay.className = 'app-notify-overlay';
        overlay.innerHTML =
            '<div class="app-notify-box" role="dialog" aria-modal="true">'
            + '<h3 class="app-notify-title" id="appNotifyTitle"></h3>'
            + '<p class="app-notify-message" id="appNotifyMessage"></p>'
            + '<div class="app-notify-actions" id="appNotifyActions"></div>'
            + '</div>';
        document.body.appendChild(overlay);
        titleEl = document.getElementById('appNotifyTitle');
        messageEl = document.getElementById('appNotifyMessage');
        actionsEl = document.getElementById('appNotifyActions');
        overlay.addEventListener('click', function(e) {
            if (e.target === overlay && mode === 'confirm') {
                close(false);
            }
        });
    }

    function close(result) {
        if (!overlay) return;
        overlay.classList.remove('show');
        if (resolveFn) {
            var fn = resolveFn;
            resolveFn = null;
            fn(result);
        }
    }

    function setTitle(title, type) {
        var icon = 'fa-circle-exclamation';
        var cls = '';
        if (type === 'success') { icon = 'fa-circle-check'; cls = 'success'; }
        else if (type === 'error') { icon = 'fa-circle-xmark'; cls = 'error'; }
        else if (type === 'warning') { icon = 'fa-triangle-exclamation'; cls = 'warning'; }
        else if (type === 'confirm') { icon = 'fa-triangle-exclamation'; cls = 'confirm'; }
        else { icon = 'fa-triangle-exclamation'; }
        titleEl.className = 'app-notify-title' + (cls ? ' ' + cls : '');
        titleEl.innerHTML = '<i class="fa-solid ' + icon + '"></i> ' + (title || 'Thông báo');
    }

    function confirm(opts) {
        opts = opts || {};
        ensureDom();
        mode = 'confirm';
        setTitle(opts.title || 'Xác nhận', opts.type || 'confirm');
        messageEl.innerHTML = opts.message || opts.html || '';
        actionsEl.innerHTML =
            '<button type="button" class="app-notify-btn app-notify-btn-cancel" data-act="cancel">'
            + (opts.cancelText || 'Hủy') + '</button>'
            + '<button type="button" class="app-notify-btn app-notify-btn-confirm" data-act="confirm">'
            + (opts.confirmText || 'Đồng ý') + '</button>';
        actionsEl.querySelector('[data-act="cancel"]').onclick = function() { close(false); };
        actionsEl.querySelector('[data-act="confirm"]').onclick = function() { close(true); };
        return new Promise(function(resolve) {
            resolveFn = resolve;
            overlay.classList.add('show');
        });
    }

    function alert(opts) {
        opts = opts || {};
        if (typeof opts === 'string') opts = { message: opts };
        ensureDom();
        mode = 'alert';
        setTitle(opts.title || 'Thông báo', opts.type || 'info');
        messageEl.innerHTML = opts.message || opts.html || '';
        actionsEl.innerHTML =
            '<button type="button" class="app-notify-btn app-notify-btn-ok" data-act="ok">'
            + (opts.okText || 'Đóng') + '</button>';
        actionsEl.querySelector('[data-act="ok"]').onclick = function() { close(true); };
        return new Promise(function(resolve) {
            resolveFn = function() { resolve(); };
            overlay.classList.add('show');
        });
    }

    function success(message, title) {
        return alert({ type: 'success', title: title || 'Thành công', message: message });
    }

    function error(message, title) {
        return alert({ type: 'error', title: title || 'Lỗi', message: message });
    }

    function warn(message, title) {
        return alert({ type: 'warning', title: title || 'Cảnh báo', message: message });
    }

    function doLogoutWithConfirm() {
        var home = (typeof global.HOME_URL === 'string' && global.HOME_URL)
            ? global.HOME_URL
            : ((global.CONTEXT_PATH || '') + '/');
        return confirm({ message: 'Bạn có chắc muốn đăng xuất?' }).then(function(ok) {
            if (ok) {
                global.location.href = home + (home.indexOf('?') >= 0 ? '&' : '?') + 'logout=true';
            }
        });
    }

    global.AppNotify = {
        confirm: confirm,
        alert: alert,
        success: success,
        error: error,
        warn: warn,
        doLogoutWithConfirm: doLogoutWithConfirm
    };
})(typeof window !== 'undefined' ? window : this);
