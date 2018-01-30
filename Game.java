import java.util.*;

public class Game {//where the game runs
    public static void main (String[] args){
		GameState gs = new GameState();
		gs.addPlayer(1);
		gs.updateBoard();
		gs.displayBoard();
        
    }
  
  
}

class Character {//base object that all placeable things inheret from
	private int posX;
	private int posY;
	private int health;
	private String name;
	
	Character (int x, int y,String n,int h){//saves the values
	posX = x;
	posY = y;
	name = n;
	health = h;
	}

	int getX(){//returns posX
		return posX;
	}
	
	int getY(){//returns posY
		return posY;
	}
	
	String getName(){//returns name
		return name;
	}
	
	int getHealth(){// returns health
		return health;
	}

	void setX(int x){// changes posX
		posX = x;
	}
	
	void setY(int y){//change posY
		posY = y;
	}
	
}



class Unit extends Character {// generic unit

	private int damage;
	private int armor;
	
	Unit(String n, int h, int a, int x, int y,int d){//creates a character with armor and damage
		super(x,y,n,h);
		armor = a;
		damage = d;
	}

	void moveUnit(int x, int y){//Used to move a unit
		setX(x);
		setY(y);
	}

}

class Building extends Character{// generic building

	
	Building (String n, int x, int y,int h){//creates a building with character properties
		super(x,y,n,h);
	}
	
	
}

class Map {// the map of the world
	private int size = 25;
	private String[][] map = new String[size][size];
	
	void resetMap (){// sets the board to an empty state
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				this.map[i][j] = "---";
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

	void placeObject(int p,Character c){// uses the given player and character to change the map
		map[c.getY()][c.getX()] = p + c.getName();
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
	private List <Player> players = new ArrayList<Player>();
	private Map map = new Map();
	
	
	void addPlayer(int num){//creates a new player
		players.add(new Player(num));
	}
	
	void updateBoard(){//itereates through each player placing the units and buildings on the board
		map.resetMap();
		//System.out.println(players.get(0).getUnitList().get(0).printthing());
		for (int i = 0; i < players.size(); i++){
			for (int j = 0;j < players.get(i).getUnitList().size();j++){
				map.placeObject(players.get(i).getNum(), players.get(i).getUnitList().get(j));
			}
			
			for (int j = 0;j < players.get(i).getBuildingList().size();j++){
				map.placeObject(players.get(i).getNum(), players.get(i).getBuildingList().get(j));
			}
		}
	}
	
	void displayBoard(){//dsiplays the board
		map.display();
	}

}

class Player {// generic player
	private int pNum;
	private List <Unit> units = new ArrayList<Unit>(0);
	private List <Building> buildings = new ArrayList<Building>(0);
	
	Player(int num){//saves the player number
		pNum = num;
	}

	void buildUnit(String selection,Building b){//uses the building pos to create a unit next to it
		if (selection == "worker"){
			units.add(new Worker(b.getX()+1,b.getY()+1));
		
		}
	}
	
	List<Unit> getUnitList(){//get function for unit list
		return units;
	}
	
	List<Building> getBuildingList(){//get function for building list
		return buildings;
	}
	
	int getNum(){//get function for player num
		return pNum;
	}

	
}

class MainBase extends Building{// The core structure of an army
	
	MainBase (String n, int x, int y,int h){
	super(n,x,y,h);
	}
	

}


class Worker extends Unit {//the resource gatherer of the army

	Worker (int x, int y){
		super ("wk",20,0,x,y,1);
	}

}














