package com.libGdx.test.zhujie;

public interface AppApi {

    void setAl(
            @IntRange(from = 1, to = 2)
            int num
    );
}