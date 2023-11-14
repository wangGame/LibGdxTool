package com.libGdx.test.zhujie;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/14 11:00
 */
public class App {
    public static void main(String[] args) {
        App app = new App();
        Inte.check(app.getClass());
        app.setAl(399);
    }

    public void setAl(@IntRange(from =1, to=2) int num){
        System.out.println(num);
    }
}
