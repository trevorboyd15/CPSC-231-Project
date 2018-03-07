import java.util.*;


public class DSA {

	private int[] node;
	private double[] distance;
	private int[] path;
	
	private List<Integer> queue = new ArrayList<Integer>();
	
	private int size;
	
	private Data[][] edges;
	
	
	DSA(int s){
		size = s;
		edges = new Data[s*s][8];
		node = new int[size*size];
		path = new int[size*size];
		distance = new double[size*size];
	}
	
	String[][] map;

	public int findFirstMove(String[][] m, int sx,int sy, int gx, int gy){
		m[gy][gx] = "---";
		convertMap(m);
		queue.clear();
		for (int i = 0;i< size*size; i++){
			node[i] = 0;
			path[i] = -1;
			distance[i] = -1;
		}
		int sNode = sx + sy*size;
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
					}else if (node[edges[nNode][i].getNode()] == 1 &&
					distance[edges[nNode][i].getNode()] > 
					(cDis + edges[nNode][i].getDistance())){
						path[edges[queue.get(0)][i].getNode()] = queue.get(0);
						distance[edges[nNode][i].getNode()] = cDis + edges[nNode][i].getDistance();
					}
				}
			}
			node[nNode] = -1;
			
			for (int i = 0; i < queue.size(); i++){
				//System.out.print("cool");
				if (queue.get(i) == nNode){
					//System.out.println(queue.size());
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
	
	public void convertMap(String[][] m){
		int nodeNum = 0;
		for (int i = 0;i<size*size;i++){
			for(int j = 0;j<8;j++){
				edges[i][j] = new Data(-1,-1);
			}
		}
		for (int y = 0; y < size; y++){
			for (int x = 0; x < size; x++){
				if (y-1 >= 0 && m[y-1][x] == "---" ){
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
				nodeNum ++ ;
			}
		}	
	}
	
	public void disEdge(){
		for (int i = 0;i<size*size;i++){
			for (int j = 0; j<8; j++){
				System.out.print(edges[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	public void disNode(){
		for (int i = 0;i<size *size;i += size){
			for (int j = 0;j<size;j++){
				System.out.print(node[i+j] + " ");
			}
			System.out.println("");
		}
	}

	public void disQueue(){
		for (int i = 0;i<queue.size();i++){
			System.out.println(queue.get(i));
		}
	}
	
	public static void main(String[] args){
		
		GameState gs = new GameState();
		gs.updateBoard();
		DSA dsa = new DSA(gs.getMap().getSize());
		System.out.println(dsa.findFirstMove(gs.getMap().getBoard(),0,3,3,3));
		dsa.disNode();
	}
	
}

class Data{

	private int node;
	private double distance;
	
	Data(int n, double d){
		node = n;
		distance = d;
	}
	
	int getNode(){
		return node;
	}
	
	double getDistance(){
		return distance;
	}
	
	void setData(int n, double d){
		node = n;
		distance = d;
	}
}


