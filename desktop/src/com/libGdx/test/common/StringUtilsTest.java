package com.libGdx.test.common;

import com.kw.common.StringUtils;

public class StringUtilsTest {
    public static void main(String[] args) {
        StringUtils.isEmpty("xx");
        StringUtils.isTrimEmpty("xx");
        System.out.println(StringUtils.format("xx %s", 12, 23));
    }
}
