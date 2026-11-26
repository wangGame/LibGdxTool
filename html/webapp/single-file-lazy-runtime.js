// The on-demand variant: build Blob URLs only when the game requests a resource.
(() => {
    "use strict";
    const files = /*__EMBEDDED_FILES__*/;
    const paths = new Map();
    const blobs = new Map();
    let decodedBytes = 0;
    const stats = document.documentElement.dataset;
    stats.lazyAssetTotal = String(Object.keys(files).length);
    stats.lazyAssetDecoded = "0";
    stats.lazyAssetBytes = "0";
    const decode = base64 => Uint8Array.from(atob(base64), char => char.charCodeAt(0));
    const normalize = value => {
        const url = new URL(String(value), document.baseURI);
        url.search = "";
        url.hash = "";
        return url.href;
    };
    const page = new URL(location.href);
    page.search = "";
    page.hash = "";
    for (const path of Object.keys(files)) {
        paths.set(normalize(path), path);
        paths.set(normalize(page.href + path), path);
    }
    const embeddedURL = input => {
        const path = paths.get(normalize(input));
        if (path === undefined) return input;
        if (!blobs.has(path)) {
            const [type, base64] = files[path];
            const bytes = decode(base64);
            blobs.set(path, URL.createObjectURL(new Blob([bytes], {type})));
            decodedBytes += bytes.byteLength;
            stats.lazyAssetDecoded = String(blobs.size);
            stats.lazyAssetBytes = String(decodedBytes);
            files[path][1] = null;
            console.debug("[Lazy assets] decoded " + path);
        }
        return blobs.get(path);
    };
    // Read-only counters for comparing this experiment with the preload version.
    window.demoLazyAssets = Object.freeze({
        total: Object.keys(files).length,
        get decoded() { return blobs.size; },
        get decodedBytes() { return decodedBytes; }
    });
    const originalOpen = XMLHttpRequest.prototype.open;
    XMLHttpRequest.prototype.open = function(method, url, ...args) {
        return originalOpen.call(this, method, embeddedURL(url), ...args);
    };
    const originalFetch = window.fetch;
    window.fetch = function(input, options) {
        const url = typeof input === "string" || input instanceof URL ? input : input.url;
        const embedded = embeddedURL(url);
        return originalFetch.call(this, embedded === url ? input : embedded, options);
    };
    const application = document.createElement("script");
    application.text = new TextDecoder().decode(decode(/*__APPLICATION__*/));
    document.body.appendChild(application);
})();
