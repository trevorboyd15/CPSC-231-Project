
import java.util.*;

public class Game {//where the game runs
    public static void main (String[] args){
		GameState gs = new GameState();
		gs.addHumanPlayer(1);
		gs.addHumanPlayer(2);
		while(true){
			gs.doTurns();
			
		}
        
    }
  
  
}

class Character {//base object that all placeable things inheret from
	private int posX;
	private int posY;
	private int health;
	private String name;
	private List<Queue> myQueues = new ArrayList<Queue>(0);
	private List<String> myActions = new ArrayList<String>(0);
	
	public String toString(){
		return name + " at " + posX +","+posY;
	}
	
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
	
	void setHealth(int h){
		health = h;
	}

	void setX(int x){// changes posX
		posX = x;
	}
	
	void setY(int y){//change posY
		posY = y;
	}
	
	List<Queue> getMyQueues(){
		return myQueues;
	}
	
	void addMyActions(String ma){
		myActions.add(ma);
	}
	
	List<String> getMyActions(){
		return myActions;
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

  void attackUnit(Character enemy){//Used to attack a unit
 		enemy.setHealth(enemy.getHealth()-damage);
 	}
}

class Building extends Character{// generic building

	
	Building (String n, int x, int y,int h){//creates a building with character properties
		super(x,y,n,h);
	}
	
	
}

class Map {// the map of the world
	private int size = 10;
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
	
	int getSize(){
		return size;
	}
	
	String[][] getBoard(){
		return map;
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
	
	void doTurns(){
		for (int i = 0; i<players.size(); i++){
			updateBoard();
			displayBoard();
			players.get(i).turn(this);
			
		}
	
	}
	
	
	void addHumanPlayer(int num) {
		players.add(new HumanPlayer(num));
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

	Map getMap(){
		return map;
	}
	
}

class Player {// generic player
	private int pNum;
	
	private List <Unit> units = new ArrayList<Unit>(0);
	private List <Building> buildings = new ArrayList<Building>(0);
	private List <Character> selectables = new ArrayList<Character>(0);
	private List <String> actions = new ArrayList<String>(0);
	private List <String> construct = new ArrayList<String>(0);
	
	private Character selection;
	private String actionSelected;
	private String item;
	private int desX;
	private int desY;
	
	
	Player(int num){//saves the player number
		pNum = num;
		if (pNum == 1){
			buildings.add(new MainBase("mb",1,8,200));
		}else if (pNum == 2){
			buildings.add(new MainBase("mb",8,1,200));
		}
	}
	
	void turn(GameState gs){//gets overwritten by inherited class just need to be defined here for now
	}
	
	void findSelectables(){//combines the list of units and buildings
		selectables.clear();
		for (int i = 0; i<buildings.size(); i++){
			if (buildings.get(i).getName() == "mb"){
				selectables.add(buildings.get(i));
			}	
		}
		for (int i = 0; i<units.size(); i++){
			if (units.get(i).getName() == "wk"){
				selectables.add(units.get(i));
			}else if (units.get(i).getName() == "sd"){
        selectables.add(units.get(i));
      }
    }
	
	
	}

	void findActions(Character c){//finds what the given character can do
		actions = c.getMyActions();
	}
	
	List<String> getActions(){//returns actions
		return actions;
	}
	
	void buildUnit(String selection,Character b){//uses the building pos to create a unit next to it
		if (selection == "worker" ){
			units.add(new Worker(b.getX()+1,b.getY()+1));
		}else if (selection == "soldier"){
			units.add(new Soldier(b.getX()+1,b.getY()));
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

	List<Character> getSelectables(){//returns selectables
		return selectables;
	}
	
	Character getSelection(){
		return selection;
	}
	
	void setSelection(Character c){
		selection = c;
	}
	
	String getActionSelected(){
		return actionSelected;
	}
	
	void setActionSelected(String s){
		actionSelected = s;
	}

	void findConstruct(Character b){
		construct.clear();
		if (b.getName() == "mb"){
			construct.add("worker");
			construct.add("soldier");
		}
	}
	
	List<String> getConstruct(){
		return construct;
	}
	
	String getItem(){
		return item;
	}
	
	void setItem(String i){
		item = i;
	}
	
	void createQueue(String action, Character charac, String item){
		if (charac instanceof MainBase && item == "worker" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,2));
		} else if (charac instanceof MainBase && item == "soldier" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,3));
		}
	}
	
	void createQueue(String action, Character charac, int x, int y){
		if (charac instanceof Unit && action == "move"){
			charac.getMyQueues().add(new MoveQueue(action,charac,x,y));
		}
	}
	
	void doTurn(GameState s){
		updateQueues(s);
	}
	
	void updateQueues(GameState gameS){
		String a;
		Character c;
		String i;
		boolean done;
		for (int index = 0; index < buildings.size(); index++){
			done = false;
			if (buildings.get(index).getMyQueues().size() > 0){
				if (buildings.get(index).getMyQueues().get(0).ready()){
					a = buildings.get(index).getMyQueues().get(0).getAction();
					c = buildings.get(index).getMyQueues().get(0).getSelection();
					i = buildings.get(index).getMyQueues().get(0).getItem();
					if (c instanceof MainBase && (i == "worker" || i == "soldier") ){
						if (gameS.getMap().getBoard()[c.getY()+1][c.getX()+1] == "---"){
							buildUnit(i,c);
							done = true;
						}
					}
					if (done){
						buildings.get(index).getMyQueues().remove(0);
					}
				}else{
				buildings.get(index).getMyQueues().get(0).decrementTime();
				}
				
			}
			
		}
		
		for (int index = 0; index < units.size(); index++){
			done = false;
			
			if (units.get(index).getMyQueues().size() > 0){
				if (units.get(index).getMyQueues().get(0) instanceof MoveQueue){
					int x = units.get(index).getMyQueues().get(0).getX();
					int y = units.get(index).getMyQueues().get(0).getY();
					if (gameS.getMap().getBoard()[y][x] == "---"){
						done = true;
						units.get(index).moveUnit(x,y);
					}
				}
				if ( done){
					units.get(index).getMyQueues().remove(0);
				}
			
			}
		}
					
		
		
		
	}
	
	void setX(int x){
		desX = x;
	}
	
	int getX(){
		return desX;
	}
	
	void setY(int y){
		desY = y;
	}
	
	int getY(){
		return desY;
	}
		
}

class MainBase extends Building{// The core structure of an army
	
	MainBase (String n, int x, int y,int h){
	super(n,x,y,h);
	addMyActions( "construct");
	
	}
	
}


class Barracks extends Building{
	
	Barracks (String n, int x, int y, int h){
		super(n, x, y, h);
		addMyActions("construct");
	}
	
}
	
		
class Worker extends Unit {//the resource gatherer of the army

	private String state;
	
	Worker (int x, int y){
		super ("wk",20,0,x,y,1);
		addMyActions("move");
		addMyActions("build");
		addMyActions("collect");
		addMyActions("attack");
	}

}

class Soldier extends Unit {//the main fighting unit of the army

  Soldier (int x, int y){
 		super ("sd",80,0,x,y,20);
    addMyActions("move");
    addMyActions("attack");
 	}
 }
 
class HumanPlayer extends Player{

	private Scanner sc = new Scanner(System.in);
	private String input;
	private boolean valid;
	private int nInput;
	
	HumanPlayer(int num){
		super(num);
	}
	
	void turn(GameState s){
		System.out.println("Player: "+getNum()+" it is your turn.");
		
		getInput(s);
		doTurn(s);
	}
	
	boolean inputSelection(){//used to get a selection from user
		findSelectables();
		System.out.println("what would you like to select? (number) ");
		List <Character> s = getSelectables();
		for (int index = 0; index < s.size(); index++){
			System.out.print(s.get(index).toString() + " ");
		}
		System.out.println("");
		input = sc.next();
		try{
		nInput = Integer.parseInt(input);
			if (nInput >= getSelectables().size() || nInput < 0){
				throw new NumberFormatException("number to high or low");
			}
		}catch (NumberFormatException e){
			System.out.println("that is not a valid input");
			return false;
		}
		setSelection(getSelectables().get(nInput));	
		return true;
		
	}
	
	void inputAction(){//used to get the desired action from user
		boolean valid = false;
		findActions(getSelection());
		while (valid == false){
			System.out.println("what would you like to do with that? (String) " +getActions());
			input = sc.next();
			for (int i = 0; i <getActions().size(); i++){
				if (input.contains(getActions().get(i))){
					setActionSelected(getActions().get(i));
					valid = true;
					break;
				}
			}
			if (valid == false){
				System.out.println("that is not an action");
			}
		}
	}
	
	void getInput(GameState gs){//uses inputSelection and inputAction with extra code to get input from user
		valid = false;
		while (valid == false){
			if(inputSelection() == false){
				continue;
			}
			inputAction();
			valid = false;
			while (valid == false){
				
				if (getActionSelected() == "construct"){
					findConstruct(getSelection());
					System.out.println("what would you like to construct with that? (String) "+ getConstruct());
					input = sc.next();
					for (int i = 0; i<getConstruct().size(); i++){
						if (input.contains(getConstruct().get(i))){
							setItem(getConstruct().get(i));
							createQueue(getActionSelected(),getSelection(),getItem());
							valid = true;
							break;
						}
					}
					if (valid == false){
						System.out.println("that cannot be constructed");
					}
				}else if(getActionSelected() == "move"){
					System.out.println("where would you like to move (x y)");
					try{
					input = sc.next();
					setX(Integer.parseInt(input));
					input = sc.next();
					setY(Integer.parseInt(input));
					} catch (NumberFormatException e1) {
						System.out.println("that is not a number");
						continue;
					}
					if (gs.getMap().getSize() -1 >= getX() && getX() >= 0 
					&& gs.getMap().getSize()-1 >= getY() && getY() >= 0){
						valid = true;
						createQueue(getActionSelected(),getSelection(),getX(),getY());
						break;
					}else{
						System.out.println("That is not on the board");
					}
				
				}else if(getActionSelected() == "attack"){
					valid = true;
					break;
				}else if (getActionSelected() == "build"){
					valid = true;
					break;
				}else if (getActionSelected() == "collect"){
					valid = true;
					break;
				}
			
			}
			
		}
	}
	
	

}

class Queue{

	private String action;
	private Character selection;
	private Character selectionTwo;
	private String item;
	private int desX;
	private int desY;

	Queue(String a, Character c, String i){
		action = a;
		selection = c;
		item = i;
	}
	
	Queue(String a, Character c,int x, int y){
		action = a;
		selection = c;
		desX = x;
		desY = y;
	}
	
	Queue(String a, Character c, Character cTwo){
		action = a;
		selection = c;
		selectionTwo =cTwo;
	}
	
	String getAction(){//returns action
		return action;
	}
	
	Character getSelection(){//returns selection
		return selection;
	}
	
	String getItem(){//returns item
		return item;
	}

	boolean ready(){
		return true;
	}
	
	int getX(){
		return desX;
	}
	
	int getY(){
		return desY;
	}
	
	void decrementTime(){
	}
	
}

class ConstructQueue extends Queue{//used to store actions that need to be executed for buildings
	 private int timeLeft;
	
	
	ConstructQueue(String a, Character c, String i,int t){
		super(a,c,i);
		timeLeft = t;
		
	}
	
	void decrementTime(){//decrease the time left in the que
		timeLeft --;
	}

	boolean ready(){//returns if the action should be done
		return timeLeft <= 0;
	}
	
}

class MoveQueue extends Queue {//will move to the location over several turns
	
	MoveQueue (String a, Character c,int x, int y){
		super(a,c,x,y);
	}
	
}

class AttackQueue extends Queue {// combination of moving and attacking

	AttackQueue(String a, Character c, Character cTwo){
		super(a,c,cTwo);
	}

}

class BuildQueue{// a worker uses this to construct a new building


}







