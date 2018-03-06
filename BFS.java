import java.util.*;


public class BFS {

	private int[] node;
	
	private int[] path;
	
	private List<Integer> queue = new ArrayList<Integer>();
	
	private int size;
	
	private int[][] edges;
	
	
	BFS(int s){
		size = s;
		edges = new int[s*s][8];
		node = new int[size*size];
		path = new int[size*size];
	}
	
	String[][] map;

	public int findFirstMove(String[][] m, int sx,int sy, int gx, int gy){
		m[gy][gx] = "---";
		convertMap(m);
		queue.clear();
		for (int i = 0;i< size*size; i++){
			node[i] = 0;
			path[i] = -1;
		}
		int sNode = sx + sy*size;
		int fNode = gx + gy*size;
		node[sNode] = -1;
		queue.add(sNode);
		while (!queue.isEmpty()){
			
			for (int i = 0; i < 8; i++){
				if (edges[queue.get(0)][i] != -1 && node[edges[queue.get(0)][i]] == 0){
					path[edges[queue.get(0)][i]] = queue.get(0);
					node[edges[queue.get(0)][i]] = 1;
					queue.add(edges[queue.get(0)][i]);
				}
			}
			node[queue.get(0)] = -1;
			queue.remove(0);
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
				edges[i][j] = -1;
			}
		}
		for (int y = 0; y < size; y++){
			for (int x = 0; x < size; x++){
				if (y-1 >= 0 && m[y-1][x] == "---" ){
					edges[nodeNum][0] = nodeNum-size;
				}
				if (y-1 >= 0 && x+1<size && m[y-1][x+1] == "---"){
					edges[nodeNum][1] = nodeNum-size +1;
				}
				if (x+1 < size&& m[y][x+1] == "---"){
					edges[nodeNum][2] = nodeNum + 1;
				}
				if (x+1 < size && y+1 < size && m[y+1][x+1] == "---"){
					edges[nodeNum][3] = nodeNum +1 +size;
				}
				if (y+1 <size && m[y+1][x] == "---"){
					edges[nodeNum][4] = nodeNum +size;
				}
				if (y+1 < size && x-1 >= 0 && m[y+1][x-1] == "---"){
					edges[nodeNum][5] = nodeNum +size -1;
				}
				if (x-1 >= 0 && m[y][x-1] == "---"){
					edges[nodeNum][6] = nodeNum -1;
				}
				if (x-1 >=0 && y-1 >= 0 && m[y-1][x-1] == "---"){
					edges[nodeNum][7] = nodeNum -1 -size;
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
		BFS bfs = new BFS(gs.getMap().getSize());
		System.out.println(bfs.findFirstMove(gs.getMap().getBoard(),0,0,5,5));
		bfs.disNode();
	}
	
}


