public class DSATest{

	private DSA dsa;
	private int size = 20;
	
	DSATest(int s){
		size = s;
		dsa = new DSA(s);
	}
	
	public static void main( String[] args){
		DSATest dt = new DSATest(20);
		dt.runTest();
	
	}

	public String[][] getLargeMap(){
		String[][] ret = new String[size+5][size+5];
		for (int i = 0; i < size+5; i++){
			for( int j = 0; j < size+5; j++){
					ret[i][j] = "---";
			}
		}
		return ret;
	}
	
	public String[][] getSmallMap(){
		String[][] ret = new String[size-10][size-10];
		for (int i = 0; i < size-10; i++){
			for( int j = 0; j < size-10; j++){
					ret[i][j] = "---";
			}
		}
		return ret;
	}
	
	public String[][] getBlockedMap(){
		String[][] ret = new String[size][size];
		for (int i = 0; i < size; i++){
			for( int j = 0; j < size; j++){
				if (i == j || i == j+1 || i == j-1){
					ret[i][j] = "-x-";
				}else{
					ret[i][j] = "---";
				}
			}
		}
		return ret;
	}

	public String[][] getOpenMap(){
		String[][] ret = new String[size][size];
		for (int i = 0; i < size; i++){
			for( int j = 0; j < size; j++){
					ret[i][j] = "---";
			}
		}
		return ret;
	}
	
	public String[][] getRectMap(){
		String[][] ret = new String[size][size-1];
		for (int i = 0; i < size; i++){
			for( int j = 0; j < size-1; j++){
					ret[i][j] = "---";
			}
		}
		return ret;
	}
	
	public boolean testRange(){
		int node;
		try{
			node = dsa.findFirstMove(getOpenMap(),-1,-1,0,0);
			node = dsa.findFirstMove(getOpenMap(),0,0,-1,-1);
			node = dsa.findFirstMove(getOpenMap(),size,size,0,0);
			node = dsa.findFirstMove(getOpenMap(),0,0,size,size);
		}catch (IndexOutOfBoundsException e1){
			return false;
		}
		return true;
	}
	
	public boolean testBlock(){
		int node = dsa.findFirstMove(getBlockedMap(),size-1,0,0,size-1);
		if (node == (size-1)){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean testDirection(){
		int node;
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-1,size-5);
		if (node != (size-2)+ ((size-5)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-1,size-3);
		if (node != (size-2)+ ((size-4)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-3,size-3);
		if (node != (size-3)+ ((size-4)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-5,size-3);
		if (node != (size-4)+ ((size-4)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-5,size-5);
		if (node != (size-4)+ ((size-5)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-5,size-7);
		if (node != (size-4)+ ((size-6)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-3,size-7);
		if (node != (size-3)+ ((size-6)*size)){
			return false;
		}
		node = dsa.findFirstMove(getOpenMap(),size-3,size-5,size-1,size-7);
		if (node != (size-2)+ ((size-6)*size)){
			return false;
		}
		
		return true;
	}
	
	
	
	
	public boolean testMapSize(){
		int node;
		node = dsa.findFirstMove(getLargeMap(),0,0,size,size);
		if (node != 0){
			return false;
		}
		node = dsa.findFirstMove(getSmallMap(),0,0,1,1);
		if (node != 0){
			return false;
		}
		node = dsa.findFirstMove(getRectMap(),0,0,1,1);
		if (node != 0){
			return false;
		}
		return true;
	}
	
	
	public void runTest(){
		System.out.println("Block: " + testBlock());
		System.out.println("Direction: " + testDirection());
		System.out.println("Range: " + testRange());
		System.out.println("MapSize: " + testMapSize());
	
	}
	
}