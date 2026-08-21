package com.kw.common.devenv;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Author by tony
 * Date on 2025/7/27.
 */
public class DevEnv {
    private final Locale defaultLocale;
    private final Charset default_charset;
    private final String default_encoding;
    private final String java_version;
    private final String os_arch;
    private final String os_name;
    private final String os_version;


    private DevEnv(){
        defaultLocale =Locale.getDefault()                ;
        default_charset =Charset.defaultCharset()           ;
        default_encoding =System.getProperty("file.encoding");
        java_version =System.getProperty("java.version") ;
        os_arch =System.getProperty("os.arch")      ;
        os_name =System.getProperty("os.name")      ;
        os_version =System.getProperty("os.version")   ;
    }

    @Override
    public String toString() {
        return "DevEnv{" +
                "defaultLocale=" + defaultLocale +
                ", default_charset=" + default_charset +
                ", default_encoding='" + default_encoding + '\'' +
                ", java_version='" + java_version + '\'' +
                ", os_arch='" + os_arch + '\'' +
                ", os_name='" + os_name + '\'' +
                ", os_version='" + os_version + '\'' +
                '}';
    }

    public static void main(String[] args) {
        DevEnv env = new DevEnv();
        System.out.println(env);
    }
}
