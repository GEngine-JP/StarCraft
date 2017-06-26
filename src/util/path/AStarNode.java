package util.path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import core.GridMap;
import core.GridMapRender;



 

public class AStarNode implements Comparable {

	AStarNode pathParent;

	float costFromStart;

	float estimatedCostToGoal;

	Point location;

//	int x,y;
	
	public AStarNode(Point location) {
		super();
		this.location = location;
	}
	
//	public AStarNode(float x,float y) {
//		super();
//		this.location = GridMapRender.pxTolTile(x, y);
//	}
	
	 
	public float getCost() {
		return costFromStart + estimatedCostToGoal;
	}

	public int compareTo(Object other) {
		float thisValue = this.getCost();
		float otherValue = ((AStarNode) other).getCost();
		float v = thisValue - otherValue;
		return (v > 0) ? 1 : (v < 0) ? -1 : 0;
	}

	public float getCost(AStarNode node) {

		return getEstimatedCost(node);

	}

	public float getEstimatedCost(AStarNode node) {

		AStarNode other = (AStarNode) node;
		float dx = location.x - other.location.x;
		float dz = location.y - other.location.y;
		return (float) Math.sqrt(dx * dx + dz * dz);
	}

	public List<AStarNode> getNeighbors() {

		List neighbors = new ArrayList<AStarNode>();
		int x = location.x;
		int y = location.y;
		// 上
		neighbors.add(new AStarNode(new Point(x, y - 1)));
		// 右上
		neighbors.add(new AStarNode(new Point(x + 1, y - 1)));
		// 右
		neighbors.add(new AStarNode(new Point(x + 1, y)));
		// 右下
		neighbors.add(new AStarNode(new Point(x + 1, y + 1)));
		// 下
		neighbors.add(new AStarNode(new Point(x, y + 1)));
		// 左下
		neighbors.add(new AStarNode(new Point(x - 1, y + 1)));
		// 左
		neighbors.add(new AStarNode(new Point(x - 1, y)));
		// 左上
		neighbors.add(new AStarNode(new Point(x - 1, y - 1)));
		return neighbors;

	}
	
	boolean isHit(GridMap map) {
		int x = location.x;
		int y = location.y;
		if (x < 0 || y < 0 || x >= map.getWidth()|| y >= map.getHeight()) {
			return true;
		} else {
			return map.isHit(x, y);
		}
	}
	

	public boolean equals(Object obj) {
		if (obj instanceof AStarNode) {
			AStarNode n = (AStarNode) obj;
			if (this.location.equals(n.location)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public String toString(){
		return location.toString();
	}

	public int getX() {
		return GridMapRender.tileXToPx(location.x);
	}

	public int getY() {
		return GridMapRender.tileYToPx(location.y);
	}
	
	
}
