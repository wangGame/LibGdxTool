package kw.artpuzzle;

import java.util.Date;
import java.util.HashSet;

import kw.artpuzzle.constant.LevelConfig;

class App {
   public static void main(String[] args) {

      Date date  = new Date();
      long time = date.getTime();
      System.out.println(time);



      long currentTimeMillis = date.getTime() - time;
      System.out.println(currentTimeMillis);
      long l1 = currentTimeMillis / 1000L / 60L / 60L / 24L;
      System.out.println(l1);

//      HashSet<Integer> alreadyIntegerHashSet = new HashSet<>();
//      HashSet<Integer> integerHashSet = new HashSet<>();
//      for (int i = 0; i < 30; i++) {
//         integerHashSet.add((i+1));
//      }
//      for (int i = 1; i <= 6; i++) {
//         integerHashSet.add(30 + i * 5);
//      }
//      for (int i = 1; i <= 8; i++) {
//         integerHashSet.add(60 + i * 15);
//      }
//      integerHashSet.add(240);
//      integerHashSet.add(360);
//      integerHashSet.add(420);
//      integerHashSet.add(480);
//      integerHashSet.add(540);
//      integerHashSet.add(600);
//      float start = 0;
//      for (int i = 0; i < 100000; i++) {
//         start += 0.016667f;
//         int floor = (int) Math.floor(start);
//         if (integerHashSet.contains(floor)) {
//            if (!alreadyIntegerHashSet.contains(floor)){
//               alreadyIntegerHashSet.add(floor);
//               System.out.println(floor);
//            }
//         }
//      }




//      for (int i = 0; i < 30; i++) {
//         System.out.println((i+1));
//      }
//      for (int i = 1; i <= 6; i++) {
//         System.out.println(30 + i * 5);
//      }
//      for (int i = 1; i <= 8; i++) {
//         System.out.println(60 + i * 15);
//      }
//      System.out.println(240);
//      System.out.println(360);
//      System.out.println(420);
//      System.out.println(480);
//      System.out.println(540);
//      System.out.println(600);


//      float gameCurrentLevelPlayTime = 100;
//      String value = "120-";
//      if(gameCurrentLevelPlayTime <= 120.0f){
//         value = 120+"-";
//      }else if (gameCurrentLevelPlayTime >= 600.0f){
//         value = 600+"+";
//      }else {
//         int[] dict = LevelConfig.dict;
//         for (int i = 1; i < dict.length; i++) {
//            if (dict[i] >= gameCurrentLevelPlayTime) {
//               value = dict[i-1]+"";
//               break;
//            }
//         }
//      }
//
//      System.out.println(value);
   }
}
