# 按需加载对比版

原版保持预加载行为：`html:singleHtml` → `html/build/dist/tripeaks.html`。

新增版本：运行项目根目录的 `pack-html-lazy.bat`，或执行
`gradlew.bat html:lazySingleHtml`，输出 `html/build/dist-lazy/tripeaks-lazy.html`。

新增版关闭 TeaVM 的全量素材预加载，首次请求素材时才解码 Base64 并创建
Blob URL；后续请求使用缓存。当前 Ashley 示例只在显示前异步加载 `000.png`。
TeaVM 自身的辅助脚本和必需的 classpath 资源仍会在启动时加载。

在浏览器控制台输入 `demoLazyAssets`，可以查看资源总数、已解码数量和字节数。
HTML 仍内嵌全部资源，所以文件大小基本相同；减少的是启动解码和运行内存。

当前示例经 HTTP 单文件预览验证：正常显示图片，启动只解码 12 / 1135 个资源，
资源解码字节数为 3,143,062（约 3 MB，不含游戏 JavaScript 本身）。

以后添加新画面，应先用 `AssetLoader.getInstance().loadAsset(...)` 异步加载画面所需
文件，在回调中创建使用该资源的对象。当前通用 `Asset.getTexture()` 是同步接口，
不能对尚未加载的资源直接调用；图集、字体等还需要加载对应的依赖文件。
