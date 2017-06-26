package util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class PathSearch {

	  
	public static List<Point> findPath(int[][] map, Point start, Point goal) {
		// 转换成node
		StarNode startNode = new StarNode(start);
		StarNode goalNode = new StarNode(goal);
		// open list
		LinkedList<StarNode> open = new LinkedList<StarNode>();
		LinkedList<StarNode> close = new LinkedList<StarNode>();
		// init
		startNode.searchParent = null;
		open.add(startNode);
		close.add(startNode);

		while (!open.isEmpty()) {

			StarNode node = open.removeFirst();
			if (node.equals(goalNode)) {
				return construct(node);
			} else {

				for (StarNode n : node.getNeighbors()) {
					if (!open.contains(n) && !close.contains(n) && n.isHit(map)) {
						n.searchParent = node;
						open.add(n);
						close.add(n);
					}
				}
			}
		}
		return null;
	}

	public static List<Point> construct(StarNode node) {

		LinkedList<Point> path = new LinkedList<Point>();
		while (node != null) {
			path.addFirst(node.location);
			node = node.searchParent;
		}
		return path;
	}

	public static void main(String[] args) {
		int[][] map = { 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
				};
  
		List<Point> list = findPath(map, new Point(1, 1), new Point(10, 13));

		for (Point p : list) {
			System.out.println(p.x + "," + p.y);
		}
	}
}
