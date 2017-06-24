package com.imageverify.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomIntGenerate {
	public static Random random=new Random();

	public static int getRandomInt(){
		return random.nextInt();
	}
	public static int getRandomInt(int max){
		return random.nextInt(max);
	}
	public static int getRandomInt(int max,Integer...ints){
		Integer randomInt=random.nextInt(max);
		List<Integer> list=Arrays.asList(ints);
		while(list.contains(randomInt)){
			randomInt=random.nextInt(max);
		}
		return randomInt;
	}
}
