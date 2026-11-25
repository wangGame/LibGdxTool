package ogz.tripeaks.html;

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder;
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier;
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass;
import org.teavm.tooling.TeaVMTool;

/** Runs on the JVM during the build, never in the browser. */
@SkipClass
public final class HtmlBuilder {
    public static void main(String[] args) {
        // libGDX JSON needs access to model fields and no-argument constructors.
//        会被反射使用的类
//        TeaReflectionSupplier.addReflectionClass("ogz.tripeaks.models");
//        TeaReflectionSupplier.addReflectionClass("com.badlogic.gdx.utils.IntArray");
//        TeaReflectionSupplier.addReflectionClass("ogz.tripeaks.services.SettingsData");
//        TeaReflectionSupplier.addReflectionClass("ogz.tripeaks.services.Layouts");
//        TeaReflectionSupplier.addReflectionClass("ogz.tripeaks.services.AnimationStrategies");
//        TeaReflectionSupplier.addReflectionClass("ogz.tripeaks.services.DrawingStrategies");
//        TeaReflectionSupplier.addReflectionClass("com.ray3k.stripe.PopTable$PopTableStyle");

        TeaBuildConfiguration config = new TeaBuildConfiguration();
        // The desktop asset list has a different format; TeaVM generates its own manifest.
        config.assetFilter = (path, directory) -> !path.endsWith("/assets.txt");
        config.assetsPath.add(new AssetFileHandle(args[0]));
        config.webappPath = args[1];
        config.mainClass = HtmlLauncher.class.getName();
        config.htmlTitle = "TriPeaks";
        config.showLoadingLogo = false;
        TeaVMTool tool = TeaBuilder.config(config);
        tool.setObfuscated(true);
        if (!TeaBuilder.build(tool)) {
            throw new IllegalStateException("HTML compilation failed; see TeaVM diagnostics above.");
        }
    }
}
