public class KeyboardHeightCallbackCase {

    private static class FakeLayout {
        float x;
        float y;
        float scaleX = 1f;

        void setX(float x) { this.x = x; }
        void setY(float y) { this.y = y; }
        void setScaleX(float scaleX) { this.scaleX = scaleX; }

        void animateTo(float y, float scaleX, float x) {
            this.y = y;
            this.scaleX = scaleX;
            this.x = x;
        }
    }

    private static class KeyboardCaseEngine {
        private final FakeLayout relativeLayoutField = new FakeLayout();
        private final int screenWidth;

        int observerHeight = -1;
        boolean closedTextInputField;
        boolean popupRefreshed;

        KeyboardCaseEngine(int screenWidth) {
            this.screenWidth = screenWidth;
        }

        void onKeyboardHeightChanged(
            int height,
            int leftInset,
            int rightInset,
            boolean isNativeInputOpen,
            boolean isStandardHeightProvider,
            boolean popupShowing,
            boolean useImmersiveMode,
            int softButtonsBarHeight,
            boolean adjustNothing,
            int keyboardLandscapeHeight,
            int keyboardPortraitHeight,
            int editTextHeight
        ) {
            if (useImmersiveMode && isStandardHeightProvider) {
                height += softButtonsBarHeight;
            }

            if (!isNativeInputOpen) {
                observerHeight = height;
                return;
            }

            if (height == 0) {
                if (!isStandardHeightProvider && (keyboardLandscapeHeight != 0 || keyboardPortraitHeight != 0)) {
                    closedTextInputField = true;
                }
                if (isStandardHeightProvider && popupShowing) {
                    return;
                }
                observerHeight = 0;
                relativeLayoutField.setY(0);
                return;
            }

            observerHeight = height + editTextHeight;
            relativeLayoutField.setX(0);
            relativeLayoutField.setScaleX(1);
            relativeLayoutField.setY(0);

            if (!adjustNothing) {
                height = 0;
            }

            float targetY = -height;
            float targetScaleX = ((float)screenWidth - rightInset - leftInset) / screenWidth;
            float targetX = (float)(leftInset - rightInset) / 2;

            relativeLayoutField.animateTo(targetY, targetScaleX, targetX);

            if (popupShowing) {
                popupRefreshed = true;
            }
        }

        String state() {
            return "layout(x=" + relativeLayoutField.x
                + ", y=" + relativeLayoutField.y
                + ", scaleX=" + relativeLayoutField.scaleX
                + "), observerHeight=" + observerHeight
                + ", closedTextInputField=" + closedTextInputField
                + ", popupRefreshed=" + popupRefreshed;
        }

        void resetFlags() {
            closedTextInputField = false;
            popupRefreshed = false;
        }
    }

    public static void main(String[] args) {
        KeyboardCaseEngine engine = new KeyboardCaseEngine(1280);

        System.out.println("Case 1: keyboard opens, standard provider, no insets");
        engine.onKeyboardHeightChanged(
            400, 0, 0,
            true, true, false,
            false, 0,
            true,
            0, 0,
            56
        );
        System.out.println(engine.state());
        engine.resetFlags();

        System.out.println("\nCase 2: keyboard opens with insets (notch/pill)");
        engine.onKeyboardHeightChanged(
            420, 40, 60,
            true, true, true,
            false, 0,
            true,
            0, 0,
            56
        );
        System.out.println(engine.state());
        engine.resetFlags();

        System.out.println("\nCase 3: adjustResize active (not adjustNothing), manual Y move disabled");
        engine.onKeyboardHeightChanged(
            380, 0, 0,
            true, true, false,
            false, 0,
            false,
            0, 0,
            56
        );
        System.out.println(engine.state());
        engine.resetFlags();

        System.out.println("\nCase 4: keyboard closes while popup is showing (standard provider)");
        engine.onKeyboardHeightChanged(
            0, 0, 0,
            true, true, true,
            false, 0,
            true,
            0, 0,
            56
        );
        System.out.println(engine.state());
        engine.resetFlags();

        System.out.println("\nCase 5: keyboard closes on floating provider with cached heights");
        engine.onKeyboardHeightChanged(
            0, 0, 0,
            true, false, false,
            false, 0,
            true,
            120, 140,
            56
        );
        System.out.println(engine.state());
    }
}

