/**
 * Đọc dữ liệu bootstrap từ <script type="application/json" id="...">
 */
(function (global) {
    function readJsonScript(id, fallback) {
        var el = document.getElementById(id);
        if (!el || !el.textContent || !String(el.textContent).trim()) {
            return fallback;
        }
        try {
            return JSON.parse(el.textContent);
        } catch (e) {
            console.error('[bootstrap] parse failed:', id, e);
            return fallback;
        }
    }

    function getMetaContent(name) {
        var el = document.querySelector('meta[name="' + name + '"]');
        return el ? (el.getAttribute('content') || '') : '';
    }

    global.AppBootstrap = {
        readJsonScript: readJsonScript,
        getMetaContent: getMetaContent
    };
})(window);
