import java.util.*;

public class Game {
    public static void main (String[] args){
        Map m = new Map();
		m.resetMap();
		m.display();
        
        
    }
  
  
}


class Unit {

	private int posX;
	private int posY;
	private int health;
	private int armor;
	private String name;
	
	Unit(String n, int h, int a, int x, int y){
		health = h;
		name = n;
		armor = a;
		posX = x;
		posY = y;
	}



}

class Building {

	private String name;
	private int posX;
	private int posY;
	
	Building (String n, int x, int y){
		name = n;
		posX = x;
		posY = y;
	}
}

class Map {
	private int size = 25;
	private String[][] map = new String[size][size];
	
	void resetMap (){
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				this.map[i][j] = "--";
			}
		}
	}
	
	void display(){
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				System.out.print(this.map[i][j] + " ");
			}
			System.out.println("");
		}
	}
}

class Resource {
	
	private String type;
	private int amount;
	private int posX;
	private int posY;
	private int mineAmount;


}

class GameState {
	


}

class Player {
	private String pNum;
	private List <Unit> units;
	private List <Building> buildings;




}




