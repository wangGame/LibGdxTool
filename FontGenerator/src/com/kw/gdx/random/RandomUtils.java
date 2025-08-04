package com.kw.gdx.random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;

public class RandomUtils {
       public RandomXS128 random;
       public int counter = 0;
        
       public RandomUtils() {
           this(Long.valueOf(MathUtils.random(9999L)), MathUtils.random(99));
       }
        
       public RandomUtils(Long seed) {
           this.random = new RandomXS128(seed.longValue());
       }
        
       public RandomUtils(Long seed, int counter) {
           this.random = new RandomXS128(seed.longValue());
            for (int i = 0; i < counter; i++) {
                random(999);
            }
       }

       public RandomUtils copy() {
           RandomUtils copied = new RandomUtils();
            copied.random = new RandomXS128(this.random.getState(0), this.random.getState(1));
            copied.counter = this.counter;
            return copied;
       }

       public void setCounter(int targetCounter) {
           if (this.counter < targetCounter) {
               int count = targetCounter - this.counter;
               for (int i = 0; i < count; i++) {
                   randomBoolean();
               }
           }
       }


       public int random(int range) {
           this.counter++;
           return this.random.nextInt(range + 1);
       }


       public int random(int start, int end) {
        /*  70 */     this.counter++;
        /*  71 */     return start + this.random.nextInt(end - start + 1);
           }


       public long random(long range) {
        /*  76 */     this.counter++;
        /*  77 */     return (long)(this.random.nextDouble() * range);
           }


       public long random(long start, long end) {
        /*  82 */     this.counter++;
        /*  83 */     return start + (long)(this.random.nextDouble() * (end - start));
           }


       public long randomLong() {
        /*  88 */     this.counter++;
        /*  89 */     return this.random.nextLong();
           }


       public boolean randomBoolean() {
        /*  94 */     this.counter++;
        /*  95 */     return this.random.nextBoolean();
           }


       public boolean randomBoolean(float chance) {
           this.counter++;
           return (this.random.nextFloat() < chance);
       }


       public float random() {
           this.counter++;
           return this.random.nextFloat();
       }


       public float random(float range) {
           this.counter++;
           return this.random.nextFloat() * range;
       }

       public float random(float start, float end) {
          this.counter++;
          return start + this.random.nextFloat() * (end - start);
      }
}
