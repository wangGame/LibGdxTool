package com.libGdx.test.zhujie;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/14 11:00
 */
public class App implements AppApi{
    public static void main(String[] args) {
//        App app = new App();
//        Inte.check(app.getClass());
//        app.setAl(399);



        App app =
                new App();


        AppApi proxy =
                CheckProxy.create(app);



        proxy.setAl(1);


        proxy.setAl(399);

    }

    @Override
    public void setAl(@IntRange(from =1, to=2) int num){
        System.out.println(num);
    }
}
