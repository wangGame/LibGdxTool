package com.test.down;

public class JsonUtils {
    public static void main(String[] args) {
        People people = new People("test",13);
        com.kw.gdx.file.JsonUtils.save("people",people);
        People people1 = com.kw.gdx.file.JsonUtils.read("people", people);
        System.out.println(people1.getAge()+"   "+ people1.getName());
    }
}
