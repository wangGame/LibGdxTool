package ogz.tripeaks.html;

import com.badlogic.gdx.Input;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;


/** Browser entry point; uses the same Kotlin game as Android and desktop. */
public final class HtmlLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        config.storagePrefix = "ogz.tripeaks";
        new TeaApplication(new GameTest(new UserInputListener() {
            @Override
            public void showHandleInput(String hint, Input.TextInputListener callback) {

            }
        }), config);
    }
}
