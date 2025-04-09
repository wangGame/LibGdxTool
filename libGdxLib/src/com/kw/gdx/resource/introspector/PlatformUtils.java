//package com.kw.gdx.resource.introspector;
//
//import com.badlogic.gdx.Gdx;
//
//
//public class PlatformUtils {
//    private enum UiType{
//        BUTTOS,
//        TOUCH
//    }
//
//    private static UiType sUiType;
//
//    public static boolean isTouchUI(){
//        init();
//        return sUiType == UiType.TOUCH;
//    }
//
//    public static boolean isDesktop(){
//        switch (Gdx.app.getType()){
//            case Desktop:
//            case HeadlessDesktop:
//            case Applet:
//            case WebGL:
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    public static void openURI(String uri){
//        if (Gdx.net.openURI(uri)){
//            return;
//        }
//        String command = null;
//        if (SharedLibraryLoader.isLinux){
//            command = "xdg-open";
//        }else if (SharedLibraryLoader.isWindows){
//            command = "start";
//        }else if (SharedLibraryLoader.isMac){
//            command = "open";
//        }
//        if (command == null)return;
//        try {
//            new ProcessBuilder().command(command,uri).start();
//        }catch (Exception e){
//            NLog.e("Command failed : %s ",e);
//        }
//    }
//
//    private static void init() {
//        if (sUiType != null){
//            return;
//        }
//        String envValue = System.getenv("AGC_UI_TYPE");
//        if (envValue == null) {
//            sUiType = isDesktop() ? UiType.BUTTOS : UiType.TOUCH;
//        } else {
//            sUiType = UiType.valueOf(envValue);
//            NLog.d("Forcing UI type to %s", sUiType);
//        }
//        NLog.i("UI type: %s", sUiType);
//    }
//}
