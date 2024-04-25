package kw.learn.libgdxrare.math;

import java.util.Random;

public class RandomUtil {
	private static final Random _RND = new Random(System.currentTimeMillis());

	/**
	 * 随机 [min,max]
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			int tm = min;
			min = max;
			max = tm;
		}
		return Math.abs(_RND.nextInt() % (max + 1 - min)) + min;
	}

	/**
	 * 随机 [0,max)
	 * 
	 * @param max
	 *            如果max<=0 则返回 0;
	 * @return
	 */
	public static int randInt(int max) {
		if (max <= 0) {
			return 0;
		}
		return randInt(0, max - 1);
	}

	/**
	 * 直接返回Random对象
	 * 
	 * @return
	 */
	public static Random getRand() {
		return _RND;
	}

	public static float rand(float min, float max) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			float tm = min;
			min = max;
			max = tm;
		}
		return _RND.nextFloat() * (max - min) + min;
	}

	/**
	 * 数组为随机概率，然后返回随机中的值 </br>
	 * 例如[50,20,30],返回0\1\2,概率分别为50%,20%,30%
	 * 
	 * @param array
	 * @return
	 */
	public static int randIndex(int[] array) {
		int total = 0;
		for (int i = 0; i < array.length; i++) {
			total += array[i];
		}
		int rand = randInt(total);
		total = 0;
		for (int i = 0; i < array.length; i++) {
			total += array[i];
			if (rand < total) {
				return i;
			}
		}
		return 0;
	}

	public static int[] randArray(int num, int max) {
		int[] array = new int[max + 1];
		int count = 0;
		while (count < num) {
			int v = randInt(1, max);
			if (array[v] == 0) {
				array[v] = 1;
				count++;
			}
		}
		int j = 0;
		int[] values = new int[num];
		for (int i = 1; i <= max; i++) {
			if (array[i] == 1) {
				values[j++] = i;
			}
		}
		return values;
	}
}
