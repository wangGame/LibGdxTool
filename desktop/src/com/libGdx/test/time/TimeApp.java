package com.libGdx.test.time;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.utils.Timer;
import com.kw.gdx.utils.TimerTask;
import com.libGdx.test.base.LibGdxTestMain;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TimeApp app = new TimeApp();
        app.start();
    }

    private long startTime;
    private long l;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        // ������ʱ��
        l = converServerTime();
        startTime = startTime();
        showEnd(startTime - l);

//        ִ�ж�ʱ���񣨱����ӳ�1���ִ������
//        timer.scheduleTask(new Timer.Task() {
//            @Override
//            public void run() {
//                // �������д����������
//                System.out.println("����ִ���ˣ�");
//            }
//        }, 1); // �ӳ�1���ִ������


//        String str = "Fri, 23 Feb 2024 08:14:55 GMT";
        Timer timer = new Timer();
// ��ѡ�����ü�ʱ��Ϊ����ѭ��ִ��
//        timer.schedule();

// ��ѡ�����ü�ʱ�����ظ������ͼ��ʱ�䣨����ÿ��0.5��ִ��һ�Σ��ܹ�ִ��5�Σ�
        timer.schedule(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                // �������д����������
                l += 1000;
                showEnd(startTime - l);
            }
        }, 0, 1000); // �ӳ�1���ִ�е�һ������֮��ÿ��0.5��ִ��һ��

        // ����Ҫֹͣ��ʱ��ʱ�����Ե���stop()����
        // timer.stop();
    }

    private void showEnd(long millis) {
        if (millis<=0){
            //����  �Ƿ��û�ֱ�ӽ���
        }
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        System.out.println(
                String.format("%d %02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60));
    }

    public static long converServerTime(){
        // �������ڽ��������ַ����ĸ�ʽ������
        try {
            String str = "Fri, 23 Feb 2024 08:14:55 GMT";
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            Date date = formatter.parse(str);
            System.out.println(
                    (date.getYear()+1900) +
                            "-"+
                            (date.getMonth()+1) +
                            "-"+date.getDate() +"  "+
                            date.getHours() + " : " +
                            date.getMinutes() + " : " +
                            date.getSeconds());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }



    public long startTime(){
        Date date = new Date();
        date.setYear(2024-1900);
        date.setMonth(1);
        date.setDate(23 + 20);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date.getTime();
    }
}
