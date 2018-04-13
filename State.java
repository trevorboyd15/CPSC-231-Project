public class State{
	private static String theme = "";
	private static int imSize = -1;
	
	State(int im,String t){
		theme = t;
		imSize = im;
	}
	
	State(){}
	
	public String getTheme(){
		return theme;
	}
	
	public int getImSize(){
		return imSize;
	}
	
	

}