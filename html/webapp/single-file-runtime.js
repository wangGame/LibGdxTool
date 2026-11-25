// Included inline by HtmlSingleFilePacker, before the TeaVM application starts.
(() => {
    "use strict";
    const files = /*__EMBEDDED_FILES__*/;
    const urls = new Map();
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
    for (const [path, [type, base64]] of Object.entries(files)) {
        const embedded = URL.createObjectURL(new Blob([decode(base64)], {type}));
        urls.set(normalize(path), embedded);
        // This TeaVM version removes only index.html from its base URL. Also
        // recognize concatenated paths so the single file can have any name.
        urls.set(normalize(page.href + path), embedded);
    }
    // TeaVM uses XMLHttpRequest for assets and its helper scripts. Blob URLs work
    // from file:// without a server and retain native XHR events and binary responses.
    const originalOpen = XMLHttpRequest.prototype.open;
    XMLHttpRequest.prototype.open = function(method, url, ...args) {
        return originalOpen.call(this, method, urls.get(normalize(url)) || url, ...args);
    };
    // Also support helper libraries that use fetch instead of XMLHttpRequest.
    const originalFetch = window.fetch;
    window.fetch = function(input, options) {
        const url = typeof input === "string" || input instanceof URL ? input : input.url;
        const embedded = urls.get(normalize(url));
        return originalFetch.call(this, embedded || input, options);
    };
    const application = document.createElement("script");
    application.text = new TextDecoder().decode(decode(/*__APPLICATION__*/));
    document.body.appendChild(application);
})();
