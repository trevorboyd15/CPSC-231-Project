import java.util.*;

public class Game {//where the game runs
    public static void main (String[] args){
        Map m = new Map();
		m.resetMap();
		m.display();
        
        
    }
  
  
}


class Unit {// generic unit

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

class Building {// generic building

	private String name;
	private int posX;
	private int posY;
	
	Building (String n, int x, int y){
		name = n;
		posX = x;
		posY = y;
	}
}

class Map {// the map of the world
	private int size = 25;
	private String[][] map = new String[size][size];
	
	void resetMap (){// sets the board to an empty state
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				this.map[i][j] = "--";
			}
		}
	}
	
	void display(){// displays what is on the board
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				System.out.print(this.map[i][j] + " ");
			}
			System.out.println("");
		}
	}
}

class Resource {// generic resource
	
	private String type;
	private int amount;
	private int posX;
	private int posY;
	private int mineAmount;


}

class GameState {// the game state that holds all information required to run the game
	


}

class Player {// generic player
	private String pNum;
	private List <Unit> units;
	private List <Building> buildings;




}




