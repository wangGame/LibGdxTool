package com.libGdx.test.dfs;

import java.util.LinkedList;

public class Utils {
    public static Object lock = new Object();
    private void tt(){
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void br(int arr[], LinkedList<Integer> track) {
        if (track.size() == arr.length) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            if (track.contains(arr[i])) {
                continue;
            }
            showInfo(track);
            tt();
            track.add(arr[i]);
            br(arr, track);
            track.removeLast();
        }
    }

    public void notifyM() {
        synchronized (lock) {
            lock.notify();
        }
    }

    StringBuilder builder = new StringBuilder();
    private void showInfo(LinkedList<Integer> track){
        builder.setLength(0);
        for (Integer i : track) {
            builder.append(i);
        }
        System.out.println(builder.toString());
    }
}
