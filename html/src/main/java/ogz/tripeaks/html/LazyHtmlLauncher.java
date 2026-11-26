package ogz.tripeaks.html;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetLoader;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetLoaderListener;
import com.github.xpenatan.gdx.backends.teavm.assetloader.AssetType;
import com.github.xpenatan.gdx.backends.teavm.assetloader.Blob;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;

/** Browser experiment with resource preloading disabled. */
public final class LazyHtmlLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        config.storagePrefix = "ogz.tripeaks.lazy";
        config.preloadAssets = false;
        new TeaApplication(new LazyGame(), config);
    }

    private static final class LazyGame extends GameTest {
        private LazyGame() {
            super(new UserInputListener() {
                @Override
                public void showHandleInput(String hint, Input.TextInputListener callback) {
                    Gdx.input.getTextInput(callback, hint, "", hint);
                }
            });
        }

        @Override
        protected void loadingView() {
            // This scene uses only 000.png. Wait for that file asynchronously before
            // calling the existing synchronous texture accessor in AshleyScreen.
            AssetLoader.getInstance().loadAsset(true, "000.png", AssetType.Binary,
                    Files.FileType.Internal, new AssetLoaderListener<Blob>() {
                        @Override
                        public void onSuccess(String url, Blob result) {
                            LazyGame.super.loadingView();
                        }

                        @Override
                        public void onFailure(String url) {
                            Gdx.app.error("Lazy assets", "Cannot load scene image: " + url);
                        }
                    });
        }
    }
}
