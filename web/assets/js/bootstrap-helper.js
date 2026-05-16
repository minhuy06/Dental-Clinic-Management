/**
 * Bootstrap: đọc JSON seed + resolve context-path cho toàn app.
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

    function deriveContextPathFromAssets() {
        var link = document.querySelector('link[href*="/assets/"], script[src*="/assets/"]');
        if (!link) return '';
        var url = link.getAttribute('href') || link.getAttribute('src') || '';
        var idx = url.indexOf('/assets/');
        return idx > 0 ? url.substring(0, idx) : '';
    }

    function deriveContextPathFromLocation() {
        var path = global.location && global.location.pathname ? global.location.pathname : '';
        var markers = ['/account/', '/reception/', '/doctor/', '/admin/', '/patient/', '/Infor/'];
        for (var i = 0; i < markers.length; i++) {
            var pos = path.indexOf(markers[i]);
            if (pos > 0) return path.substring(0, pos);
        }
        return '';
    }

    /**
     * Trả về context-path (vd: /Dental_Clinic_Management). Luôn gán window.CONTEXT_PATH.
     */
    function resolveContextPath() {
        if (typeof global.CONTEXT_PATH === 'string' && global.CONTEXT_PATH.length > 0) {
            return global.CONTEXT_PATH;
        }

        var cp = getMetaContent('context-path');
        if (!cp) {
            var header = document.getElementById('header');
            if (header) {
                cp = header.getAttribute('data-context-path') || '';
            }
        }
        if (!cp) cp = deriveContextPathFromAssets();
        if (!cp) cp = deriveContextPathFromLocation();

        global.CONTEXT_PATH = cp || '';
        return global.CONTEXT_PATH;
    }

    global.AppBootstrap = {
        readJsonScript: readJsonScript,
        getMetaContent: getMetaContent,
        resolveContextPath: resolveContextPath
    };

    resolveContextPath();
})(window);
