package kw.dgx.test;

public class ABTest {
    public static String currentV = "B";

    public static boolean isVersion(String name){
        if (name==null)return false;
        if (name.equals("A") && currentV.equals("")){
            return true;
        }
        if (currentV.equalsIgnoreCase(name)) {
            return true;
        }else {
            return false;
        }
    }
}
