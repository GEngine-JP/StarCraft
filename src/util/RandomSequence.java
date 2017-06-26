package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSequence {

	static Random random = new Random();
	
	public static List<Integer> generate(int n) {
		
		List<Integer> item = new ArrayList();
		int i;
		while(item.size()<n ){
			i=random.nextInt(n);
			if(item.contains(i))
				continue;
			item.add(i);
		}
		return item;
	}
	
	public static void main(String[] args) {
	
		System.out.println(RandomSequence.generate(5));
	}
}
