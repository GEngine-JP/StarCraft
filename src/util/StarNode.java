package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StarNode {

	private LinkedList<StarNode> neighbors = new LinkedList<StarNode>();

	Point location;

	StarNode searchParent;

	StarNode(Point location) {
		this.location = location;
	}

	StarNode add(StarNode neighbor) {
		neighbor.add(this);
		return this;
	}

	boolean contains(StarNode neighbor) {
		return neighbors.contains(neighbor);
	}

	List<StarNode> getNeighbors() {

		List neighbors = new ArrayList<StarNode>();
		int x = location.x;
		int y = location.y;
		// 上
		neighbors.add(new StarNode(new Point(x, y - 1)));
		// 右上
		neighbors.add(new StarNode(new Point(x + 1, y - 1)));
		// 右
		neighbors.add(new StarNode(new Point(x + 1, y)));
		// 右下
		neighbors.add(new StarNode(new Point(x + 1, y + 1)));
		// 下
		neighbors.add(new StarNode(new Point(x, y + 1)));
		// 左下
		neighbors.add(new StarNode(new Point(x - 1, y + 1)));
		// 左
		neighbors.add(new StarNode(new Point(x - 1, y)));
		// 左上
		neighbors.add(new StarNode(new Point(x - 1, y - 1)));
		return neighbors;
	}

	/**
	 * 在地图中是否是可通过区域
	 * 
	 * @param map
	 * @return
	 */
	boolean isHit(int[][] map) {
		int x = location.x;
		int y = location.y;
		if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
			return false;
		} else {
			return map[y][x] == 0;
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof StarNode) {
			StarNode n = (StarNode) obj;
			if (this.location.equals(n.location)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
