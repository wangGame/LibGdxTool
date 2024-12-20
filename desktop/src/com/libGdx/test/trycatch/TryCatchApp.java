package com.libGdx.test.trycatch;

public class TryCatchApp {
    public static Result run(Result result){
        try {
            result.success();
        }catch (Exception e){
            result.failed();
        }
        return result;
    }

    public static void main(String[] args) {
        TryCatchApp.run(new Result(){
            @Override
            public void success() {
                super.success();
            }

            @Override
            public void failed() {
                super.failed();
            }
        });
    }
}

