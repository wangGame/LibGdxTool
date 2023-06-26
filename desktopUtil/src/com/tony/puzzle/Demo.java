package com.tony.puzzle;

public class Demo {
   public static void main(String[] args) {
//      float time = 0.0f;
//      while (time<10) {
//         System.out.println(
//                 (float) (time - Math.floor(time)));
//         time+=0.01667;
//      }

      for (int i = 0; i < 45; i++) {
         System.out.println(i + "     " +Math.tan(Math.toRadians(i)));
      }
   }
}
