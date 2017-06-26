package util.path;


import java.awt.Image;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import tile.Headquarter;
import tile.Tile;
import core.GridMap;
import core.GridMapRender;

 


public class AStarSearch {

	/**
	 * A simple priority list, also called a priority queue. Objects in the list
	 * are ordered by their priority, determined by the object's Comparable
	 * interface. The highest priority item is first in the list.
	 */
	public static class PriorityList extends LinkedList {

		public void add(Comparable object) {
			for (int i = 0; i < size(); i++) {
				if (object.compareTo(get(i)) <= 0) {
					add(i, object);
					return;
				}
			}
			addLast(object);
		}
	}

	/**
	 * Construct the path, not including the start node.
	 */
	protected static LinkedList<Point> constructPath(AStarNode node) {
		LinkedList<Point> path = new LinkedList<Point>();
		while (node.pathParent != null) {
			path.addFirst(node.location);
			node = node.pathParent;
		}
		return path;
	}

	/**
	 * 寻找攻击路线
	 * @param map
	 * @param startNode
	 * @param goalNode 目标target的位置
	 * @param size
	 * @return
	 */
	public static LinkedList<AStarNode> findFightPath(GridMap map,Tile goal){
		AStarNode goalNode = new AStarNode(new Point(goal.getTileX(),goal.getTileY()));
		Point size = goal.getSize();
		
		LinkedList<AStarNode> boundNodes = new LinkedList<AStarNode>();
		//boundNodes.add(goalNode);
		
		for(int y=0;y<size.getY();++y){
			for(int x=0;x<size.getX();++x){
				
				AStarNode node = new AStarNode(new Point(goalNode.location.x+x,goalNode.location.y+y));
				for(AStarNode neighbor : node.getNeighbors()){
					
					if(!boundNodes.contains(neighbor)&&!map.contains(neighbor.location.x, neighbor.location.y))
						boundNodes.add(neighbor);
				}
			}
		}
		//System.out.println(boundNodes.size()+","+boundNodes);
		return boundNodes;
		//return findPath(map,startNode,boundNodes.getFirst());
		
	}
	public static LinkedList<Point> findPath(GridMap map,AStarNode startNode,AStarNode goalNode){
		
		PriorityList openList = new PriorityList();
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>();
		 
		startNode.costFromStart = 0;
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
		startNode.pathParent = null;
		openList.add(startNode);

		while (!openList.isEmpty()) {
		
			AStarNode node = (AStarNode) openList.removeFirst();
			 
			if (node.equals(goalNode)) {
				return constructPath(node);
			}

			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				boolean isClosed = closedList.contains(neighborNode);
				float costFromStart = node.costFromStart+ node.getCost(neighborNode);

				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				if ( ( (!isOpen && !isClosed)
						|| costFromStart < neighborNode.costFromStart)&&!neighborNode.isHit(map)) {
					
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					neighborNode.estimatedCostToGoal = neighborNode
							.getEstimatedCost(goalNode);
					if (isClosed) {
						closedList.remove(neighborNode);
					}
					if (!isOpen) {
						openList.add(neighborNode);
					}
				}
			}
			closedList.add(node);
		}

		// no path found
		return null;
	}
	
	/**
	 * 在一个不可访问的终点搜索出其周围可以访问的节点，因为需要和起点进行最短路径比较
	 * 所以需要多传入一个起点变量
	 * @param map
	 * @param goalNode 不可访问的节点
	 * @param startNode 起点
	 * @return
	 */
	private static AStarNode findNode(GridMap map,AStarNode goalNode,AStarNode startNode){
		
		//open
		PriorityList openList = new PriorityList();
		openList.add(goalNode);
		//closed
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>();
		goalNode.costFromStart = 0;
		goalNode.estimatedCostToGoal = goalNode.getEstimatedCost(startNode);
		
		while(!openList.isEmpty()){
			
			AStarNode node = (AStarNode) openList.removeFirst();
			if(!map.contains(node.location.x, node.location.y)){
				return node;
			}
//			if(!node.isHit(map)){
//				return node;
//			}
			
			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				
				AStarNode neighbor = (AStarNode)node.getNeighbors().get(i);
				boolean isOpen = openList.contains(neighbor);
				boolean isClosed = closedList.contains(neighbor);
				
				float costFromStart = node.costFromStart+ node.getCost(neighbor);
				
				if ( ( (!isOpen && !isClosed)
						|| costFromStart < neighbor.costFromStart)) {
					
					neighbor.costFromStart = costFromStart;
					neighbor.estimatedCostToGoal = neighbor.getEstimatedCost(startNode);
					//加入到list中
					openList.add(neighbor);
					closedList.add(neighbor);
				}
			}
		}
		
		//System.out.println("cant'find:"+goalNode);
		return null;

	}
	
	public static LinkedList<Point> findPath(GridMap map,int tileX,int tileY,float goalX,float goalY) {
		
		AStarNode startNode = new AStarNode(new Point(tileX,tileY));
		AStarNode goalNode = new AStarNode(GridMapRender.pxTolTile(goalX,goalY));
		
//		如果终点是一个不可以访问的节点
//		if(ignoreSprite){
//			if(goalNode.isHit(map)){
//				goalNode = findNode(map,goalNode,startNode);
//				 
//			}
//		}else{
//			if(map.contains(goalNode.location.x, goalNode.location.y)){
//				goalNode = findNode(map,goalNode,startNode);
//				 
//			}
//		} 
		if(goalNode.isHit(map)){
			goalNode = findNode(map,goalNode,startNode);
			 
		}
		
		PriorityList openList = new PriorityList();
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>();

		startNode.costFromStart = 0;
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
		startNode.pathParent = null;
		openList.add(startNode);

		while (!openList.isEmpty()) {
		
			AStarNode node = (AStarNode) openList.removeFirst();
			if (node.equals(goalNode)) {
				return constructPath(node);
			}

			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				boolean isClosed = closedList.contains(neighborNode);
				float costFromStart = node.costFromStart+ node.getCost(neighborNode);

				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				if ( ( (!isOpen && !isClosed)
						|| costFromStart < neighborNode.costFromStart)&&!neighborNode.isHit(map)) {
					
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					neighborNode.estimatedCostToGoal = neighborNode
							.getEstimatedCost(goalNode);
					if (isClosed) {
						closedList.remove(neighborNode);
					}
					if (!isOpen) {
						openList.add(neighborNode);
					}
				}
			}
			closedList.add(node);
		}

		// no path found
		return new LinkedList();
	}
	public static LinkedList<Point> findPath(GridMap map,float startX,float startY,float goalX,float goalY) {

		AStarNode startNode = new AStarNode(GridMapRender.pxTolTile(startX, startY));
		AStarNode goalNode = new AStarNode(GridMapRender.pxTolTile(goalX,goalY));
		//如果终点是一个不可以访问的节点
		if(map.contains(goalNode.location.x, goalNode.location.y)){
			goalNode = findNode(map,goalNode,startNode);
			 
		}
		
		PriorityList openList = new PriorityList();
		LinkedList<AStarNode> closedList = new LinkedList<AStarNode>();

		startNode.costFromStart = 0;
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
		startNode.pathParent = null;
		openList.add(startNode);

		while (!openList.isEmpty()) {
		
			AStarNode node = (AStarNode) openList.removeFirst();
			if (node.equals(goalNode)) {
				return constructPath(node);
			}

			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				boolean isClosed = closedList.contains(neighborNode);
				float costFromStart = node.costFromStart+ node.getCost(neighborNode);

				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				if ( ( (!isOpen && !isClosed)
						|| costFromStart < neighborNode.costFromStart)&&!neighborNode.isHit(map)) {
					
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					neighborNode.estimatedCostToGoal = neighborNode
							.getEstimatedCost(goalNode);
					if (isClosed) {
						closedList.remove(neighborNode);
					}
					if (!isOpen) {
						openList.add(neighborNode);
					}
				}
			}
			closedList.add(node);
		}

		// no path found
		return new LinkedList();
	}
	
	public static Point findNeighborNode(GridMap map,float x,float y) {
		
		AStarNode startNode = new AStarNode(GridMapRender.pxTolTile(x,y));
		LinkedList<AStarNode> open = new LinkedList<AStarNode>();
		LinkedList close = new LinkedList();
		open.add(startNode);
		while(!open.isEmpty()){
			
			List neighbors = open.removeFirst().getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				if(!map.contains(neighborNode.location.x, neighborNode.location.y)){
					return neighborNode.location;
				}else if(!open.contains(neighborNode)&&!close.contains(neighborNode)){
					open.add(neighborNode);
					close.add(neighborNode);
				}
			}
		}
		return null;
		
	}
	
	/**
	 * 获取修建建筑物的路径
	 * @param map
	 * @param sx
	 * @param sy
	 * @param tx
	 * @param ty
	 * @param size
	 * @return
	 */
	public static LinkedList<Point> findBuildPath(GridMap map,float x,float y,float sx,float sy,Point size) {
		 
		//建筑物的起点坐标
		Point building = GridMapRender.pxTolTile(sx,sy);
		AStarNode top = new AStarNode(new Point(building.x,building.y-1));
		AStarNode left = new AStarNode(new Point(building.x-1,building.y));
		AStarNode bottom = new AStarNode(new Point(building.x,building.y+size.y));
		AStarNode right = new AStarNode(new Point(building.x+size.x,building.y));
		//sprite的坐标
		AStarNode startNode = new AStarNode(GridMapRender.pxTolTile(x,y));
		LinkedList<Point> path=null;
		boolean leftHit = left.isHit(map);
		boolean rightHit = right.isHit(map);
		boolean topHit = top.isHit(map);
		boolean bottomHit = bottom.isHit(map);
		
		//先比较水平位置
		if(x>sx){
			if(!rightHit){
				path = findPath(map,startNode,right);
				if(path!=null){
					return path;
				}
			}
			if(!leftHit){
				path = findPath(map,startNode,left);
				if(path!=null){
					return path;
				}
			}
		}else{
			if(!leftHit){
				path = findPath(map,startNode,left);
				if(path!=null){
					return path;
				}
			}
			if(!rightHit){
				path = findPath(map,startNode,right);
				if(path!=null){
					return path;
				}
			}
			
		}
		//再比较垂直位置
		if(y<sy){
			if(!topHit){
				path = findPath(map,startNode,top);
				if(path!=null){
					return path;
				}
			}
			if(!bottomHit){
				path = findPath(map,startNode,bottom);
				if(path!=null){
					return path;
				}
			}
		}else{
			if(!bottomHit){
				path = findPath(map,startNode,bottom);
				if(path!=null){
					return path;
				}
			}
			if(!topHit){
				path = findPath(map,startNode,top);
				if(path!=null){
					return path;
				}
			}
		}
		//we can't find this path
		return null;
	}
	
	 
	
	
	
	public static  LinkedList<AStarNode> findSpace(GridMap map,float x,float y,int space){
		
		LinkedList open = new LinkedList();
		LinkedList close = new LinkedList();
		LinkedList result = new LinkedList();
		
		AStarNode node = new AStarNode(GridMapRender.pxTolTile(x,y));
		open.add(node);
		//如果终点是一个可以访问的节点才把终点放入reslut List中
		if(!node.isHit(map)){
			 
			result.add(node);
		}
		
		
		while(result.size()<space && !open.isEmpty()){
			
			AStarNode temp = (AStarNode) open.removeFirst();
			for(int i=0;i<temp.getNeighbors().size();++i){
				
				AStarNode neighbor = (AStarNode)temp.getNeighbors().get(i);
				if(!open.contains(neighbor)&&!close.contains(neighbor)&&!neighbor.isHit(map)){
					result.add(neighbor);
					if(result.size()>=space){
						//System.out.println("---------------------");
						return result;
					}
					open.add(neighbor);
					close.add(neighbor);
				}
			}
		}
		return result;
		
	}
	
	
	public static boolean isNeighbors(int tileX,int tileY,Tile goal){
	
		AStarNode startNode = new AStarNode(new Point(tileX,tileY));
		AStarNode goalNode = new AStarNode(new Point(goal.getTileX(),goal.getTileY()));
		
		Point size = goal.getSize();
		for(int y=0;y<size.getY();++y){
			for(int x=0;x<size.getX();++x){
				
				AStarNode node = new AStarNode(new Point(goalNode.location.x+x,goalNode.location.y+y));
				if(startNode.getNeighbors().contains(node)){
					return true;
				}
			}
		}
		return false;
	}
	 
	public static void main(String[] args) throws Exception {
		
		Headquarter goal = (Headquarter) new Headquarter(new Image[1],100)
		.clone(2, 2, new GridMap(5,5));
		System.out.println(isNeighbors(6, 1, goal));
	}	

}
