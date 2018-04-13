import java.util.*;

/** Dijkstra's Algorithm for pathfinding
*/
public class DSA {

	private int[] node;
	private double[] distance;
	private int[] path;
	
	private List<Integer> queue = new ArrayList<Integer>();
	
	private int size;
	
	private Data[][] edges;
	
	/** Constructor for the DSA class
	* @param s the board size
	*/
	DSA(int s){
		size = s;
		edges = new Data[s*s][8];
		node = new int[size*size];
		path = new int[size*size];
		distance = new double[size*size];
	}
	
	/** used to find the ideal next move to get to a desired location
	* @param m array representation of the board
	* @param sx the starting x value (unit's location)
	* @param sy the starting y value (unit's location)
	* @param gx the goal x value (end location)
	* @param gy the goal y value (end location)
	* @return next node to move to
	*/
	public int findFirstMove(String[][] m, int sx,int sy, int gx, int gy){
		
		if (m.length != size){//checks to ensure lengths are correct
			return sx + sy*size;
		}else{
			for (int i = 0;i<size;i++){
				if (m[i].length != size){
					return sx + sy*size;
				}
			}
		}
		
		queue.clear();
		for (int i = 0;i< size*size; i++){//sets all nodes to 0, sets path and distance to -1 (for now)
			node[i] = 0;
			path[i] = -1;
			distance[i] = -1;
		}
		
		int sNode = sx + sy*size;
		if (sx < 0 || sx >= size || sy < 0 || sy >= size || gx < 0 || gx >= size || gy < 0 || gy >= size){//if either x/y pair is off the board, just return starting node
			return sNode;
		}
		
		m[gy][gx] = "---";
		convertMap(m);//creates an array of values for every node
		int fNode = gx + gy*size;
		int nNode = sNode;
		double cDis = 0;
		node[sNode] = 1;
		path[sNode] = sNode;
		queue.add(sNode);
		while (!queue.isEmpty()){
			cDis = size*size;
			for (int i = 0; i<size*size;i++){
				if (node[i] == 1 && distance[i] < cDis){
					cDis = distance[i];
					nNode = i;
				}
			}
			
			for (int i = 0; i < 8; i++){
				if (edges[nNode][i].getNode() != -1 && node[edges[nNode][i].getNode()] != -1){
					if (node[edges[nNode][i].getNode()] == 0){
						distance[edges[nNode][i].getNode()] = cDis + edges[nNode][i].getDistance();
						node[edges[nNode][i].getNode()] = 1;
						path[edges[nNode][i].getNode()] = nNode;
						queue.add(edges[nNode][i].getNode());
					}else if (node[edges[nNode][i].getNode()] == 1 && distance[edges[nNode][i].getNode()] > 
					(cDis + edges[nNode][i].getDistance())){
						path[edges[nNode][i].getNode()] = nNode;
						distance[edges[nNode][i].getNode()] = cDis + edges[nNode][i].getDistance();
					}
				}
			}
			node[nNode] = -1;
			
			for (int i = 0; i < queue.size(); i++){
				if (queue.get(i) == nNode){
					queue.remove(i);
					break;
				}
			}
		}
		
		int cNode = fNode;
		int oNode = fNode;
		if (path[fNode] == -1){
			return sNode;
		}
		while (cNode != sNode){
			oNode = cNode;
			cNode = path[cNode];
		}
		return oNode;
	}
	
	/** method for assigning data values to nodes
	* @param m the array representation of the board
	*/
	public void convertMap(String[][] m){
		int nodeNum = 0;
		for (int i = 0;i<size*size;i++){//set all data to defaults of -1, -1. May be changed later
			for(int j = 0;j<8;j++){
				edges[i][j] = new Data(-1,-1);
			}
		}
		for (int y = 0; y < size; y++){//for every node adjacent to the chosen node, check if it is on the board and empty
			for (int x = 0; x < size; x++){//if it is, pass the edges array a set of data with the node number and the distance between nodes (1 if directly adjacent and 1.5 if diagonal)
				if (y-1 >= 0 && m[y-1][x] == "---" ){//the edges array has a section for every node, and stores values for all nodes adjacent to each node
					edges[nodeNum][0].setData(nodeNum-size,1);
				}
				if (y-1 >= 0 && x+1<size && m[y-1][x+1] == "---"){
					edges[nodeNum][1].setData( nodeNum-size +1,1.5);
				}
				if (x+1 < size&& m[y][x+1] == "---"){
					edges[nodeNum][2].setData(nodeNum + 1,1);
				}
				if (x+1 < size && y+1 < size && m[y+1][x+1] == "---"){
					edges[nodeNum][3].setData(nodeNum +1 +size,1.5);
				}
				if (y+1 <size && m[y+1][x] == "---"){
					edges[nodeNum][4].setData(nodeNum +size,1);
				}
				if (y+1 < size && x-1 >= 0 && m[y+1][x-1] == "---"){
					edges[nodeNum][5].setData(nodeNum +size -1,1.5);
				}
				if (x-1 >= 0 && m[y][x-1] == "---"){
					edges[nodeNum][6].setData(nodeNum -1,1);
				}
				if (x-1 >=0 && y-1 >= 0 && m[y-1][x-1] == "---"){
					edges[nodeNum][7].setData(nodeNum -1 -size,1.5);
				}
				nodeNum ++ ;//repeat for every node
			}
		}	
	}
	
	/** main method used for finding the path
	* @param args initial arguments
	*/
	public static void main(String[] args){
		
		GameState gs = new GameState();
		gs.updateBoard();
		DSA dsa = new DSA(gs.getMap().getSize());
		System.out.println(dsa.findFirstMove(gs.getMap().getBoard(),0,3,3,3));
	}
}

class Data{// used for storing nodes and their distances

	private int node;
	private double distance;
	
	/** Constructor for the data class
	* @param n the node number
	* @param d the distance
	*/
	Data(int n, double d){
		node = n;
		distance = d;
	}
	
	/** returns node number
	* @return node number
	*/
	int getNode(){
		return node;
	}
	
	/** returns distance
	* @return distance
	*/
	double getDistance(){
		return distance;
	}
	
	/** Changes node number and distance
	* @param n the node number
	* @param d the distance
	*/
	void setData(int n, double d){
		node = n;
		distance = d;
	}
}


