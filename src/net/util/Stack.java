package net.util;

import java.util.LinkedList;
/**
 * Stack类用来管理类似gamelist的功能。
 * 通过当有新用户加入的时候就use了一个空间
 * @author jiangyp
 *
 */
@SuppressWarnings("serial")
public class Stack {
	
	private LinkedList<Integer> removeList = new LinkedList<Integer>();

	private LinkedList<Integer> itemlist = new LinkedList<Integer>(){
		
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

	public Stack(int size) {
		for (int i = 0; i < size; ++i) {
			itemlist.add(i);
		}
	}

	public void use(Integer n) {
		itemlist.remove(n);
		removeList.add(n);
	}
	
	public void restore(Integer n) {
		if(removeList.contains(n))
			itemlist.add(n);
	}
	 
	public int next() {
		return itemlist.isEmpty() ? -1 : itemlist.removeFirst();
	}

	public static void main(String[] args) {

		Stack stack = new Stack(4);
		stack.use(0);
		System.out.println(stack.itemlist);
		stack.use(9);
		System.out.println(stack.itemlist);
		stack.restore(0);
		System.out.println(stack.itemlist);
//		System.out.println(stack.next());
//		System.out.println(stack.next());
//		System.out.println(stack.next());

	}
}
