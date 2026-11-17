package com.example;

import com.example.mvvm.MutableLiveData;
import com.example.mvvm.Observer;

public class App {
    public static void main(String[] args) {
        // 1. 创建 MutableLiveData
        MutableLiveData<Integer> count =
                new MutableLiveData<>(0, Runnable::run);

        // 2. 创建观察者 A
        Observer<Integer> observerA = value -> {
            System.out.println("观察者 A 收到数据：" + value);
        };

        // 3. 创建观察者 B
        Observer<Integer> observerB = value -> {
            System.out.println("观察者 B 收到数据：" + value);
        };

        // 4. 注册观察者
        System.out.println("----- 注册观察者 -----");

        count.observe(observerA);
        count.observe(observerB);

        // 5. 修改数据
        System.out.println("----- 修改数据 -----");

        count.setValue(10);

        count.setValue(20);

        // 6. 获取当前数据
        System.out.println("当前数据：" + count.getValue());

        // 7. 移除观察者 A
        System.out.println("----- 移除观察者 A -----");

        count.removeObserver(observerA);

        // 8. 再次修改数据
        System.out.println("----- 再次修改数据 -----");

        count.setValue(30);

        // 9. 测试 postValue
        System.out.println("----- postValue -----");

        count.postValue(40);
    }
}
