package net.util;

import java.util.LinkedList;

public class MyTest {

	private static LinkedList<Integer> itemlist = new LinkedList<Integer>(){
		
		public boolean add(Integer e) {
			
			for(int i=0;i<size();++i){
				if(e.compareTo(get(i))<0){
					add(i, e);
					return true;
				}
			}
			addLast(e);
			return true;
		}
	};
	
	public static void main(String[] args) {
		
		int[] array = {5,2,534,1,52,5,1,43,6};
		for(int i=0;i<array.length;++i){
			itemlist.add(array[i]);
		}
		System.out.println(itemlist);
	}
}
