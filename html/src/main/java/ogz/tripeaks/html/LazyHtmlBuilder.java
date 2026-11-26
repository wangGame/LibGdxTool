package ogz.tripeaks.html;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import org.teavm.tooling.TeaVMTool;

/** Builds the separate on-demand variant without overwriting the existing build. */
@SkipClass
public final class LazyHtmlBuilder {
    public static void main(String[] args) {
        TeaBuildConfiguration config = new TeaBuildConfiguration();
        config.assetFilter = (path, directory) -> !path.endsWith("/assets.txt");
        config.assetsPath.add(new AssetFileHandle(args.length > 0 ? args[0] : "assets"));
        config.webappPath = args.length > 1 ? args[1] : "html/build/dist-lazy";
        config.mainClass = LazyHtmlLauncher.class.getName();
        config.htmlTitle = "Demo - On demand";
        config.showLoadingLogo = false;
        TeaVMTool tool = TeaBuilder.config(config);
        tool.setObfuscated(true);
        if (!TeaBuilder.build(tool)) {
            throw new IllegalStateException("On-demand HTML compilation failed.");
        }
    }
}
