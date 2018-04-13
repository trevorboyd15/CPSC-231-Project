/** The main program for our RTS game. Runs the text-based game, and contains key classes and variables for the graphics-based version.
*/
import java.util.*;

/** main class used to run the game
*/
public class Game {//where the game runs
	/** main class adds players and gamestate, and then runs the game turns and checks for win condition.
	* @param args initial arguments
	*/
    public static void main (String[] args){//main function; creates and runs the game
		GameState gs = new GameState();
		gs.addHumanPlayer(1,gs);
		//gs.addHumanPlayer(2); /* Optional to use a second human player for the text version. */
		gs.addAIPlayer(2,gs);
		while(true){
			gs.doTurns();
			gs.checkBase();
			if (gs.wonGame()){
				System.out.println("Player " + gs.getPlayers().get(0).getNum() + " has won the game!");
				break;
			}
		}
    }
}

/** base object class for all placeable things
*/
class Character {//base object that all placeable things inherit from
	private int posX;
	private int posY;
	private int health;
	private int damage;
	private int cost;
	private String name;
	private List<Queue> myQueues = new ArrayList<Queue>(0);
	private List<String> myActions = new ArrayList<String>(0);
	
	/** Tells the player where a character is located
	* @return character's name and position
	*/
	public String toString(){//shows character locations
		return name + " at " + posX +","+posY;
	}
	
	/** Constructor for the character class
	* @param x the x value of the character's location
	* @param y the y value of the character's location
	* @param n the name of the character
	* @param h the health of the character
	* @param c the cost of the character
	*/
	Character (int x, int y,String n,int h,int c){//saves the values
		posX = x;
		posY = y;
		name = n;
		health = h;
		cost = c;
		addMyActions("pass");
	}
	
	/** Returns the x position of the character
	* @return x position
	*/
	int getX(){//returns posX
		return posX;
	}
	
	/** Returns the y position of the character
	* @return y position
	*/
	int getY(){//returns posY
		return posY;
	}
	
	/** Returns the name of the character
	* @return name
	*/
	String getName(){//returns name
		return name;
	}
	
	/** Returns the health of the character
	* @return health
	*/
	int getHealth(){//returns health
		return health;
	}
	
	/** Changes the health of the character
	* @param h The new health value
	*/
	void setHealth(int h){//changes health
		health = h;
	}
	
	/** Changes the x position of the character
	* @param x The new x position
	*/
	void setX(int x){// changes posX
		posX = x;
	}
	
	/** Changes the y position of the character
	* @param y The new y position
	*/
	void setY(int y){//changes posY
		posY = y;
	}
	
	/** Returns the cost of the character
	* @return cost
	*/
	int getCost(){//returns cost
		return cost;
	}
	
	/** Returns a list of the character's queues
	* @return queues list
	*/
	List<Queue> getMyQueues(){//returns list of queues
		return myQueues;
	}
	
	/** Adds a new action to the actions list
	* @param ma new action to add
	*/
	void addMyActions(String ma){//adds a new action
		myActions.add(ma);
	}
	
	/** Returns a list of the character's actions
	* @return actions list
	*/
	List<String> getMyActions(){//returns all actions
		return myActions;
	}
	
	/** Removes health from the character
	* @param h amount of health to remove
	*/
	void decrementHealth(int h){//removes health
		health -= h;
	}
	
	/** Returns the character's damage value
	* @return damage
	*/
	int getDamage(){//returns damage
		return damage;
	}
	
	/** Changes the character's damage
	* @param d new damage value
	*/
	void setDamage(int d){//changes damage
		damage = d;
	}
	
}

/** base unit class for moveable characters
*/
class Unit extends Character {// generic unit

	private int armor;
	
	/** Constructor for the unit class
	* @param n the name of the unit	
	* @param h the health of the unit	
	* @param a the armor of the unit
	* @param x the x value of the unit's location
	* @param y the y value of the unit's location
	* @param d the damage of the unit
	* @param c the cost of the unit
	*/
	Unit(String n, int h, int a, int x, int y,int d,int c){//creates a character with armor and damage
		super(x,y,n,h,c);
		armor = a;
		setDamage(d);
	}
	
	/** moves a unit by changing its x and y values
	* @param x the new x position
	* @param y the new y position
	*/
	void moveUnit(int x, int y){//Used to move a unit
		setX(x);
		setY(y);
	}
	
	/** attacks a unit, decreasing their health value
	* @param enemy the unit being attacked
	*/
	void attackUnit(Character enemy){//Used to attack a unit
 		enemy.decrementHealth(getDamage());
 	}
	
	/** collects five more resources and adds to your total
	* @param r your accumulated amount of resources
	* @return increased amount of resources
	*/
	int collectResources(int r){//Used to collect new resources (for workers)
		return(r+5);
	}

}

/** base building class for immobile characters
*/
class Building extends Character{// generic building
	
	/** Constructor for the building class
	* @param n the name of the building
	* @param x the x value of the building's location
	* @param y the y value of the building's location
	* @param h the health of the building
	* @param c the cost of the building
	*/
	Building (String n, int x, int y,int h,int c){//creates a building with character properties
		super(x,y,n,h,c);
	}
}

/** class that holds the board setup, and the positions of characters
*/
class Map {// the map of the world
	private int size = 20;
	private String[][] map = new String[size][size];
	
	/** erases any pre-existing maps and creates a new empty map
	*/
	void resetMap (){// sets the board to an empty state
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				this.map[i][j] = "---";
			}
		}
	}
	
	/** prints the board for the text version
	*/
	void display(){// displays what is on the board
		for (int i = 0;i<this.size;i++){
			for (int j = 0; j<this.size; j++){
				System.out.print(this.map[i][j] + " ");
			}
			System.out.println("");
		}
	}

	/** adds character to the map, showing it as the player's number plus the unit's abbreviated name
	* @param p the player number
	* @param c the chosen character
	*/
	void placeObject(int p,Character c){// uses the given player and character to change the map
		map[c.getY()][c.getX()] = p + c.getName();
	}
	
	/** returns the size of the square board (side length)
	* @return size
	*/
	int getSize(){//returns board size
		return size;
	}
	
	/** returns the map, represented as an array of strings
	* @return map
	*/
	String[][] getBoard(){//returns game map
		return map;
	}
}

/** class used for storing game information and contains key methods for running the game
*/
class GameState {// the game state that holds all information required to run the game
	private List <Player> players = new ArrayList<Player>();
	private Map map = new Map();
	private DSA bfs = new DSA(map.getSize());
	
	/** returns the search algorithm used for movement
	* @return bfs
	*/
	DSA getBFS(){//returns the Djikstra's search algorithm
		return bfs;
	}
	
	/** updates and displays the map for the text version
	*/
	void doTurns(){//updates and displays the board between turns
		for (int i = 0; i<players.size(); i++){
			updateBoard();
			displayBoard();
			players.get(i).turn(this);
		}
	}
	
	/** Checks win condition by checking the number of remaining players
	* @return size of players list
	*/
	boolean wonGame(){//Checks if a player has won the game, by checking the number of players
		return (players.size() == 1);
	}
	
	/** Erases a player and their charaacters if their base is destroyed
	*/
	void checkBase(){//Removes a player from the player list if their main base is destroyed
		for (int index = 0; index < players.size(); index++){
			if (players.get(index).getBuildingList().size()== 0 ||
			!(players.get(index).getBuildingList().get(0) instanceof MainBase)){
				players.get(index).findSelectables();
				for (int i = 0; i < players.get(index).getSelectables().size(); i++){
					players.get(index).getSelectables().get(i).setHealth(0);
				}
			}
		}
	}
	
	/** Checks if an enemy unit is within range of attack
	* @param attacker the unit that is attacking
	* @param target the unit that is being attacked
	* @return within range boolean
	*/
	boolean checkRange(Character attacker, Character target){//Checks range for attacks
		int x = attacker.getX();
		int y = attacker.getY();
		
		int tx = target.getX();
		int ty = target.getY();
		
		if ((attacker instanceof Worker) || (attacker instanceof Soldier)){//Checks if the unit is a unit, then uses their range to check if they can attack
			return (Math.abs(x-tx)<=1 && Math.abs(y-ty) <= 1);
		}else if (attacker instanceof RangedFighter){
			return (Math.abs(x-tx)<=2 && Math.abs(y-ty)<=2);
		}else if (attacker instanceof Tank){
			return (Math.abs(x-tx)<=3 && Math.abs(y-ty)<=3);
		}else{
			return false;
		}
	}
	
	/** Creates a new human player
	* @param num the player's number
	* @param gs the gamestate
	*/
	void addHumanPlayer(int num,GameState gs) {//Forms a new human player
		players.add(new HumanPlayer(num,gs));
	}
	
	/** Creates a new AI player
	* @param num the player's number
	* @param gs the gamestate
	*/
	void addAIPlayer(int num,GameState gs) {//Forms a new AI player
		players.add(new AIPlayer(num,gs));
	}
	
	/** resets the map, then adds every character to the new map
	*/
	void updateBoard(){//iterates through each player placing the units and buildings on the board
		map.resetMap();
		for (int i = 0; i < players.size(); i++){
			for (int j = 0;j < players.get(i).getUnitList().size();j++){
				map.placeObject(players.get(i).getNum(), players.get(i).getUnitList().get(j));
			}
			
			for (int j = 0;j < players.get(i).getBuildingList().size();j++){
				map.placeObject(players.get(i).getNum(), players.get(i).getBuildingList().get(j));
			}
		}
	}
	
	/** displays the board
	*/
	void displayBoard(){//displays the board
		map.display();
	}

	/** returns the map object
	* @return map
	*/
	Map getMap(){//returns map object
		return map;
	}
	
	/** returns the list of players
	* @return players list
	*/
	List<Player> getPlayers(){//returns players list
		return players;
	}
	
}

/** player class used for any player controlling characters on the board
*/
class Player {//generic player, used for human and AI
	private int pNum;
	private int resources;
	
	private List <Unit> units = new ArrayList<Unit>(0);
	private List <Building> buildings = new ArrayList<Building>(0);
	private List <Character> selectables = new ArrayList<Character>(0);
	private List <String> actions = new ArrayList<String>(0);
	private List <String> construct = new ArrayList<String>(0);
	private List <Character> attackSelectable = new ArrayList<Character>(0);
	private List <String> build = new ArrayList<String>(0);
	
	private Character selection;
	private Character attackSelection;
	private String actionSelected;
	private String item;
	private int desX;
	private int desY;
	
	/** Constructor for the player class
	* @param num the player's number
	* @param gs the gamestate
	*/
	Player(int num,GameState gs){//saves the player numbers, and creates initial board conditions
		pNum = num;
		if (pNum == 1){
			buildings.add(new MainBase("mb",1,gs.getMap().getSize()-2,100,20000));
			resources += 20;
		}else if (pNum == 2){
			buildings.add(new MainBase("mb",gs.getMap().getSize()-2,1,100,20000));
			resources += 20;
		}else if (pNum == 4){
			buildings.add(new MainBase("mb",gs.getMap().getSize()-2,gs.getMap().getSize()-2,100,20000));
			resources += 20;
		}else if (pNum == 3){
			buildings.add(new MainBase("mb",1,1,100,20000));
			resources += 20;
		}
	}
	
	/** function to be overwritten later
	*/
	void turn(GameState gs){//gets overwritten by inherited class just need to be defined here for now
	}
	
	/** creates a list of all currently active units and buildings
	*/
	void findSelectables(){//combines the list of units and buildings
		selectables.clear();
		for (int i = 0; i<buildings.size(); i++){//adds all buildings to the list
			if (buildings.get(i).getName() == "mb" || buildings.get(i).getName() == "bk" ){
				selectables.add(buildings.get(i));
			}	
		}
		for (int i = 0; i<units.size(); i++){//adds all units to the list
			if (units.get(i).getName() == "wk"){
				selectables.add(units.get(i));
			}else if (units.get(i).getName() == "sd"){
				selectables.add(units.get(i));
			}else if (units.get(i).getName() == "tk"){
				selectables.add(units.get(i));
			}else if (units.get(i).getName() == "rf"){
				selectables.add(units.get(i));
			}
		}
	}

	/** creates a list of all possible actions for a character
	* @param c the character whose actions you want to find
	*/
	void findActions(Character c){//finds what the given character can do
		actions = c.getMyActions();
	}
	
	/** returns a list of possible actions
	* @return list of actions
	*/
	List<String> getActions(){//returns actions
		return actions;
	}
	
	/** creates a new unit constructed from a building
	* @param selection the unit being constructed
	* @param x the x position of the new unit
	* @param y the y position of the new unit
	*/
	void buildUnit(String selection, int x, int y){//constructs a new unit that has been built by a building
		if (selection == "worker" ){
			units.add(new Worker(x,y));
		}else if (selection == "soldier"){
			units.add(new Soldier(x,y));
 		}else if (selection == "tank"){
 			units.add(new Tank(x,y));
 		}else if (selection == "ranged"){
 			units.add(new RangedFighter(x,y));
 		}
	}
	
	/** creates a new building constructed from a worker
	* @param selection the building being built
	* @param x the x position of the new building
	* @param y the y position of the new building
	*/
	void buildBuilding(String selection,int x, int y){//creates a new building that has been built by a worker
		if (selection == "barracks" ){
			buildings.add(new Barracks("bk",x,y,100,50));
		}
	}
	
	/** returns a list of units
	* @return units list
	*/
	List<Unit> getUnitList(){//get function for unit list
		return units;
	}
	
	/** returns a list of buildings
	* @return buildings list
	*/
	List<Building> getBuildingList(){//get function for building list
		return buildings;
	}
	
	/** returns the player's number
	* @return player number
	*/
	int getNum(){//get function for player num
		return pNum;
	}

	/** returns a list of selectable characters
	* @return selectable character list
	*/
	List<Character> getSelectables(){//returns selectables
		return selectables;
	}
	
	/** returns the selected character
	* @return selected character
	*/
	Character getSelection(){//returns chosen character
		return selection;
	}
	
	/** changes the selected character
	* @param c new selected character
	*/
	void setSelection(Character c){//changes character chosen
		selection = c;
	}
	
	/** returns the selected action
	* @return selected action
	*/
	String getActionSelected(){//returns selected action
		return actionSelected;
	}
	
	/** changes the selected action
	* @param s new selected action
	*/
	void setActionSelected(String s){//changes selected action
		actionSelected = s;
	}
	
	/** changes the character being attacked
	* @param c new attacked character
	*/
	void setAttackSelection(Character c){//changes attacked character
		attackSelection = c;
	}
	
	/** returns the character being attacked
	* @return attacked character
	*/
	Character getAttackSelection(){//returns attacked character
		return attackSelection;
	}

	/** returns amount of resources
	* @return resources
	*/
	int getResources(){//returns resource count
		return resources;
	}
	
	/** changes the amount of resources
	* @param r new resource count
	*/
	void setResources(int r){//changes resource count
		resources = r;
	}
	
	/** removes resources from the resource count
	* @param r amount of resources being removed
	*/
	void decRes(int r){//decreases resource count
		resources -= r;
	}

	/** creates a list of constructable units based on your resource count
	* @param b the building you are constructing from
	*/
	void findConstruct(Character b){//sets list of constructable units
		construct.clear();
		if (b.getName() == "mb"){//base can only build workers
			if (getResources()>=10){
				construct.add("worker");
			}
		}else if (b.getName() == "bk"){//barracks can build everything except the worker
			if (getResources()>=20){
				construct.add("soldier");
				if (getResources()>=30){
					construct.add("ranged");
					if (getResources()>=50){
						construct.add("tank");
					}
				}
			}
		}
	}
	
	/** creates a list of constructable buildings based on your resource count
	*/
	void findBuild(){//checks resource number, and adds a barracks to the list of things that can be built
		build.clear();
		build.add("pass");
		if (getResources() >= 50){
			build.add("barracks");
		}
	}
	
	/** creates a list of attackable characters
	* @param gs the gamestate
	*/
	void findAttackSelection(Character c, GameState gs){//finds list of attackable characters
		attackSelectable.clear();
		for (int index = 0; index < gs.getPlayers().size();index++){
			if (gs.getPlayers().get(index).getNum() != pNum){//for every player that is not you, their buildings and units are added to a list
				for (int index1 = 0; index1 < gs.getPlayers().get(index).getBuildingList().size();index1++){
					attackSelectable.add(gs.getPlayers().get(index).getBuildingList().get(index1));
				}
				for (int index1 = 0; index1 < gs.getPlayers().get(index).getUnitList().size();index1++){
					attackSelectable.add(gs.getPlayers().get(index).getUnitList().get(index1));
				}
			}
		}
	}
	
	/** returns constructable unit list
	* @return constructable unit list
	*/
	List<String> getConstruct(){//returns constructable unit list
		return construct;
	}
	
	/** returns constructable building list
	* @return constructable building list
	*/
	List<String> getBuild(){//returns list of buildings to be built
		return build;
	}
	
	/** returns attackable character list
	* @return attackable character list
	*/
	List<Character> getAttackSelectable(){//returns attackable character list
		return attackSelectable;
	}
	
	/** returns constructed character
	* @return constructed character
	*/
	String getItem(){//returns constructed character
		return item;
	}
	
	/** changes constructed character
	* @param new constructed character
	*/
	void setItem(String i){//changes constructed character
		item = i;
	}
	
	/** creates a new entry in the unit construction queue
	* @param action the chosen action to perform
	* @param charac the chosen building to build from
	* @param item the chosen unit being built
	*/
	void createQueue(String action, Character charac, String item){//Creates queue for unit construction
		if (charac instanceof MainBase && item == "worker" && action == "construct"){//for each possible unit, if the selected unit meets all requirements, it is added to a construction queue and your resource count is updated
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,2));
			decRes(10);
		}else if (charac instanceof Barracks && item == "soldier" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,3));
			decRes(20);
		}else if (charac instanceof Barracks && item == "ranged" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action, charac, item, 4));
			decRes(30);
		}else if (charac instanceof Barracks && item == "tank" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action, charac, item, 5));
			decRes(50);
		}
	}
	
	/** creates a new entry in the movement queue
	* @param action the chosen action to perform
	* @param charac the unit to move
	* @param x the new x position
	* @param y the new y position
	*/
	void createQueue(String action, Character charac, int x, int y){//Creates queue for movement
		if (charac instanceof Unit && action == "move"){
			charac.getMyQueues().add(new MoveQueue(action,charac,x,y));
		}
	}
	
	/** creates a new entry in the building construction queue
	* @param action the chosen action to perform
	* @param charac the chosen unit to build with
	* @param x the chosen x position
	* @param y the chosen y position
	* @param item the building being built
	*/
	void createQueue(String action, Character charac, int x, int y,String item){//Creates queue for building buildings
		if (charac instanceof Worker && action == "build"){
			charac.getMyQueues().add(new BuildQueue(action,charac,x,y));
			setResources(getResources()-50);
		}
	}
		
	/** creates a new entry in the resource collection queue
	* @param action the chosen action to perform
	* @param charac the unit doing the collecting
	*/
	void createQueue(String action, Character charac){//Creates queue for resource collection
		if (charac instanceof Worker && action == "collect"){
			charac.getMyQueues().add(new CollectionQueue(action,charac));
		}
	}
	
	/** creates a new entry in the attack queue
	* @param action the chosen action to perform
	* @param select the unit doing the attacking
	* @param target the unit being attacked
	*/
	void createQueue (String action, Character select, Character target){//Creates queue for attacking
		if (select instanceof Unit && action == "attack"){
			select.getMyQueues().add(new AttackQueue(action,select,target));
		}
	}
	
	/** checks a character's health and deletes it if it has no health left
	*/
	void checkHealth(){//Checks characters' healths, and deletes them if necessary
		for (int index = 0; index < buildings.size();index ++){//runs through the unit and building lists and deletes anything with zero or less health
			if(buildings.get(index).getHealth() <= 0){
				buildings.remove(index);
			}
		}
		for (int index = 0; index < units.size();index ++){
			if(units.get(index).getHealth() <= 0){
				units.remove(index);
			}
		}
	}
	
	/** runs the end of turn activities, including deleting destroyed units and updating the queues
	* @param s the gamestate
	*/
	void doTurn(GameState s){//Game turn
		checkHealth();
		updateQueues(s);
	}
	
	/** cycles through every queue for every character, and completes the first entry in each
	* @param gameS the gamestate
	*/
	void updateQueues(GameState gameS){//Cycles through all characters' queues, and runs the first item in each
		String a;
		Character c;
		String i;
		boolean done;
		int p1 = 0;
		int p2 = 0;
		
		if (pNum == 1){//creates unique values for each player for which direction to build their units from their buildings (all towards the centre of the board from the building)
			p1 = -1;
			p2 = 1;
		} else if (pNum == 2){
			p1 = 1;
			p2 = -1;
		} else if (pNum == 3){
			p1 = 1;
			p2 = 1;
		} else if (pNum == 4){
			p1 = -1;
			p2 = -1;
		}
		
		for (int index = 0; index < buildings.size(); index++){//goes through the building list and runs the first queue action
			done = false;
			if (buildings.get(index).getMyQueues().size() > 0){
				if (buildings.get(index).getMyQueues().get(0).ready()){//if the action is ready to be performed, it is done
					a = buildings.get(index).getMyQueues().get(0).getAction();
					c = buildings.get(index).getMyQueues().get(0).getSelection();
					i = buildings.get(index).getMyQueues().get(0).getItem();

					if (c instanceof MainBase && (i == "worker") || c instanceof Barracks && (i == "soldier" || i == "tank" || i == "ranged")){
						if (gameS.getMap().getBoard()[c.getY()+p1][c.getX()+p2] == "---"){//checks three different spaces in relation to the building to see if they are free
							buildUnit(i,c.getX()+p2,c.getY()+p1);//in the first free space it finds, it builds the unit
							done = true;
						} else if (gameS.getMap().getBoard()[c.getY()][c.getX()+p2] == "---"){
							buildUnit(i,c.getX()+p2,c.getY());
							done = true;
						} else if (gameS.getMap().getBoard()[c.getY()+p1][c.getX()] == "---"){
							buildUnit(i,c.getX(),c.getY()+p1);
							done = true;
						}
						if (done){// if the queue action was completed remove the queue from existence
							buildings.get(index).getMyQueues().remove(0);
						}
					}
				}else{
					buildings.get(index).getMyQueues().get(0).decrementTime();// becuase the buildings only have construct queue 
				}
				gameS.updateBoard();//displays the newly built units
			}
		}
		for (int index = 0; index < units.size(); index++){//goes through the units to update their queues
			done = false;
			if (units.get(index).getMyQueues().size() > 0){
				if (units.get(index).getMyQueues().get(0) instanceof MoveQueue){//if the unit is moving
					int sx = units.get(index).getX();
					int sy = units.get(index).getY();
					int gx = units.get(index).getMyQueues().get(0).getX();
					int gy = units.get(index).getMyQueues().get(0).getY();
					int node = gameS.getBFS().findFirstMove(gameS.getMap().getBoard(),sx,sy,gx,gy);//uses search algorithm to find the optimal path, and returns node number
					int x = node%gameS.getMap().getSize();
					int y = node/gameS.getMap().getSize();//converts node number to x and y values for the node
					if (gameS.getMap().getBoard()[y][x] == "---"){//checks if space is empty
						units.get(index).moveUnit(x,y);
						if (x== gx && y == gy){
							done = true;
						}
					}

				}else if(units.get(index).getMyQueues().get(0) instanceof AttackQueue){//if the unit is attacking
					Character c1= units.get(index).getMyQueues().get(0).getSelection();
					Character c2 = units.get(index).getMyQueues().get(0).getSelectionTwo();
					if (gameS.checkRange(c1,c2)){//checks range, and damages enemy if within
						c2.decrementHealth(c1.getDamage());
					}else{//otherwise, repeats movement algorithm to move closer to the enemy
						int sx = units.get(index).getX();
						int sy = units.get(index).getY();
						int gx = units.get(index).getMyQueues().get(0).getSelectionTwo().getX();
						int gy = units.get(index).getMyQueues().get(0).getSelectionTwo().getY();
						int node = gameS.getBFS().findFirstMove(gameS.getMap().getBoard(),sx,sy,gx,gy);
						int x = node%gameS.getMap().getSize();
						int y = node/gameS.getMap().getSize();
						units.get(index).moveUnit(x,y);
					}
					if (c2.getHealth() <= 0){//stops attacking when enemy character is destroyed
						done = true;
					}
				} else if (units.get(index).getMyQueues().get(0) instanceof CollectionQueue){//if the unit is collecting
					resources = units.get(index).collectResources(resources);
					
				} else if (units.get(index).getMyQueues().get(0) instanceof BuildQueue){//if a worker is building
					int unitX = units.get(index).getX();
					int unitY = units.get(index).getY();
					int goalX = units.get(index).getMyQueues().get(0).getX();
					int goalY = units.get(index).getMyQueues().get(0).getY();
					if (goalY >= unitY -1 && goalY <= unitY +1 && goalX >= unitX - 1 && goalX <= unitX +1){//if build location is adjacent to the worker, build a barracks
						if(gameS.getMap().getBoard()[goalY][goalX] == "---"){
							buildBuilding("barracks" , goalX, goalY);
							done = true;
						}
					}else{//otherwise move closer to location
						int node = gameS.getBFS().findFirstMove(gameS.getMap().getBoard(),unitX,unitY,goalX,goalY);
						int x = node%gameS.getMap().getSize();
						int y = node/gameS.getMap().getSize();
						if (gameS.getMap().getBoard()[y][x] == "---"){
							units.get(index).moveUnit(x,y);
						}
					}
				}
				if (done){
					units.get(index).getMyQueues().remove(0);
				}
			}
			gameS.updateBoard();
		}
	}
	
	/** change the chosen x value
	* @param x new x value
	*/
	void setX(int x){//Changes selected x-value
		desX = x;
	}
	
	/** return the chosen x value
	* @return the chosen x value
	*/
	int getX(){//Returns selected x-value
		return desX;
	}
	
	/** change the chosen y value
	* @param y new y value
	*/
	void setY(int y){//Changes selected y-value
		desY = y;
	}
	
	/** return the chosen y value
	* @return the chosen y value
	*/
	int getY(){//Returns selected y-value
		return desY;
	}
	
	void getChoice(GameState gs){//gets overwritten later
	}	
}

/** main building used for producing workers; if destroyed, you lose the game
*/
class MainBase extends Building{// The core structure of an army; If destroyed, game is lost
	
	/** Constructor for the main base
	* @param n the name of the base
	* @param x the x value of the base's location
	* @param y the y value of the base's location
	* @param h the health of the base
	* @param c the cost of the base
	*/
	MainBase (String n, int x, int y,int h,int c){//Creates MainBase, using values inherited from Building
		super(n,x,y,h,c);
		addMyActions("construct");
	
	}
}

/** building used for creating army and offensive units
*/
class Barracks extends Building{//building used for creating soldiers
	
	/** Constructor for the main base
	* @param n the name of the barracks
	* @param x the x value of the barracks' location
	* @param y the y value of the barracks' location
	* @param h the health of the base
	* @param c the cost of the base
	*/
	Barracks (String n, int x, int y,int h,int c){//Creates barracks, using values inherited from Building
		super(n,x,y,h,c);
		addMyActions( "construct");
	
	}
}

/** unit class used for gathering resources and building barracks
*/
class Worker extends Unit {//the resource gatherer of the army

	/** Constructor for the worker class
	* @param x the x value of the worker's location
	* @param y the y value of the worker's location
	*/
	Worker (int x, int y){//Creates worker, using values inherited from Unit, and adds possible actions
		super ("wk",10,0,x,y,2,10);
		addMyActions("move");
		addMyActions("build");
		addMyActions("collect");
		addMyActions("attack");
	}

}

/** melee fighting unit class
*/
class Soldier extends Unit {//the melee fighting unit of the army
	
	/** Constructor for the soldier class
	* @param x the x value of the soldier's location
	* @param y the y value of the soldier's location
	*/
	Soldier (int x, int y){//Creates soldier and adds soldiers' actions
 		super ("sd",20,0,x,y,10,20);
		addMyActions("move");
		addMyActions("attack");
 	}
 }
 
/** ranged fighting unit class
*/
class RangedFighter extends Unit { //the ranged fighting unit of the army
	
	/** Constructor for the ranged unit class
	* @param x the x value of the ranged unit's location
	* @param y the y value of the ranged unit's location
	*/
	RangedFighter (int x, int y) {// Creates ranged unit and adds actions
		super ("rf", 30, 0, x, y, 10, 30);
		addMyActions("move");
		addMyActions("attack");
	}
}
 
/** more powerful ranged fighting unit class
*/
class Tank extends Unit { //a more powerful version of the ranged unit, with more health, range, and damage
	
	/** Constructor for the worker class
	* @param x the x value of the soldier's location
	* @param y the y value of the soldier's location
	*/
	Tank (int x, int y) {//Creates tank and adds tank's actions
		super ("tk", 40, 10, x, y, 40, 50);
		addMyActions("move");
		addMyActions("attack");
	}
}

/** player class used for human players, takes input from users
*/
class HumanPlayer extends Player{//used for human players, including taking input

	private Scanner sc = new Scanner(System.in);
	private String input;
	private boolean valid;
	private int nInput;
	
	/** Constructor for the human player class
	* @param num the player number
	* @param gs the gamestate
	*/
	HumanPlayer(int num,GameState gs){//Creates Human player, with player number
		super(num,gs);
	}
	
	/** runs a human player's turn, and tells them their resource number (text version)
	* @param gs the gamestate
	*/
	void turn(GameState gs){//Runs a turn, including revealing number of resources
		System.out.println("Player: "+getNum()+" it is your turn. You have "+getResources()+" resources.");
		getInput(gs);
		doTurn(gs);
	}
	
	/** gets selection input from user and checks for validity
	* @return valid input boolean
	*/
	boolean inputSelection(){//used to get a selected character from user, and checks if input is valid
		findSelectables();
		System.out.println("what would you like to select? (number) ");
		List <Character> s = getSelectables();
		for (int index = 0; index < s.size(); index++){
			System.out.print(s.get(index).toString() + " ");
		}
		System.out.println("");
		input = sc.next();
		try{//throws an exception if you enter an invalid input
			nInput = Integer.parseInt(input);
			if (nInput >= getSelectables().size() || nInput < 0){
				throw new NumberFormatException("input number out of range");
			}
		}catch (NumberFormatException e){
			System.out.println("that is not a valid input");
			return false;
		}
		setSelection(getSelectables().get(nInput));
		return true;	
	}
	
	/** gets action input from user and checks for validity
	*/
	void inputAction(){//used to get the desired action from user, and checks for validity
		boolean valid = false;
		boolean haveResources = true;
		findActions(getSelection());
		while (valid == false){
			System.out.println("what would you like to do with that? (String) " +getActions());
			input = sc.next();
			for (int i = 0; i <getActions().size(); i++){
				if (input.contains(getActions().get(i))){
					if (getResources()<10&&getActions().get(i)=="construct"){//checks if you have enough resources to build anything
						System.out.println("You do not have enough resources to construct anything.");
						haveResources = false;
					} else {
						setActionSelected(getActions().get(i));
						valid = true;
						break;
					}
				}
			}
			if (valid == false&&haveResources==true){
				System.out.println("that is not an action");
			}
		}
	}
	
	/** computes final input to determine your chosen action for the turn
	* @param gs the gamestate
	*/
	void getInput(GameState gs){//uses inputSelection and inputAction with extra code to get final input
		valid = false;
		while (valid == false){
			if(inputSelection() == false){//will not allow an invalid selection
				continue;
			}
			inputAction();
			valid = false;
			while (valid == false){
				if(getActionSelected() == "pass"){//does nothing if you pass
					valid = true;
					break;
					
				}else if(getActionSelected() == "construct"){//makes a construction queue if you choose to construct
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
					
				}else if(getActionSelected() == "move"){//makes a movement queue if you choose to move
					System.out.println("where would you like to move (x y)");
					try{//Throws an exception if your input values are not numbers
						input = sc.next();
						setX(Integer.parseInt(input));
						input = sc.next();
						setY(Integer.parseInt(input));
					} catch (NumberFormatException e1) {
						System.out.println("that is not a number");
						continue;
					}
					if (gs.getMap().getSize() -1 >= getX() && getX() >= 0 && gs.getMap().getSize()-1 >= getY() && getY() >= 0){
						valid = true;
						createQueue(getActionSelected(),getSelection(),getX(),getY());
						break;
					}else{
						System.out.println("That is not on the board");
					}
					
				}else if(getActionSelected() == "attack"){//makes an attack queue if you choose to attack
					findAttackSelection(getSelection(), gs);
					System.out.println("what would you like to attack? (int)");
					System.out.println(getAttackSelectable());
					try{//Throws an exception if you enter an invalid input
						input = sc.next();
						setAttackSelection(getAttackSelectable().get(Integer.parseInt(input)));
					}catch (NumberFormatException e1){
						System.out.println("that is not a number");
						continue;
					}catch (IndexOutOfBoundsException e2){
						System.out.println("that is not a valid choice");
						continue;
					}
					createQueue(getActionSelected(),getSelection(),getAttackSelection());
					valid = true;
					break;
					
				}else if (getActionSelected() == "build"){//Makes a building queue if you choose to build
					findBuild();
					System.out.println("what do you want to build?" + getBuild());
					input = sc.next();
					for (int i = 0; i<getBuild().size(); i++){
						if (input.contains(getBuild().get(i))){
							setItem(getBuild().get(i));
							valid = true;
							continue;
						}
					}
					if ( valid == false){
						continue;
					}
					if (getItem() == "pass"){
						valid = true;
						break;
					}
					valid = false;
					while (valid == false){
						System.out.println("Where do you want to build it? (x y)");
						try{//Throws an exception if you enter an invalid input
							input = sc.next();
							setX(Integer.parseInt(input));
							input = sc.next();
							setY(Integer.parseInt(input));
						} catch (NumberFormatException e1) {
							System.out.println("That is not a number");
							continue;
						}
						if (gs.getMap().getSize()-1 >= getX() && getX() >= 0 && gs.getMap().getSize()-1 >= getY() && getY() >= 0
						&& gs.getMap().getBoard()[getY()][getX()] == "---"){//checks if the input values are inside the board and the space is empty
							valid = true;
							createQueue(getActionSelected(), getSelection(), getX(), getY(), getItem());
							break;
						}else{
							System.out.println("That space is not on the board and/or it has something there.");
						}
					}
					
				}else if (getActionSelected() == "collect"){//Makes a collection queue if you choose to collect
					System.out.println("You have gained 5 resources.");
					createQueue(getActionSelected(),getSelection());
					valid = true;
					break;
				}
			}
		}
	}
}

/** class for computer-controlled players
*/
class AIPlayer extends Player{
	
	/** Constructor for the AI player class
	* @param num the player's number
	* @param gs the gamestate
	*/
	AIPlayer(int num,GameState gs){
		super(num,gs);
	}
	
	/** runs a turn for the AI player
	*/
	void turn(GameState gs){//runs the AI player's turn, including getting their input
		getChoice(gs);
		doTurn(gs);
	}
	
	/** uses an algorithm to determine the AI player's next optimal move
	* @param gs the gamestate
	*/
	void getChoice(GameState gs){//gets the AI player's chosen move
		int wkCount = checkWorkers();
		for (int index = 0; index<getBuildingList().get(0).getMyQueues().size();index++){//adds the workers currently being built to the worker count
			if (getBuildingList().get(0).getMyQueues().get(index) instanceof ConstructQueue){
				wkCount += 1;
			}
		}
		int bkCount = checkBarracks();
		int sdCount = checkSoldiers();
		
		if (wkCount<3){//build a worker if you have less than three
			if (getResources()>=10&&wkCount<3){
				createQueue("construct",getBuildingList().get(0),"worker");
			} else {//if you don't have enough resources, set a worker to collect
				for (Unit w : getUnitList()){
					if (w.getName()=="wk"){
						createQueue("collect",w);
					}
				}
			}
		} else if (wkCount>=3){
			if (bkCount>=1){//if you have a barracks, build army units
				if (getResources()>=20 && getBuildingList().size() >= 2 && getBuildingList().get(1).getMyQueues().isEmpty()){//if you have resources, build soldiers
					createQueue("construct",getBuildingList().get(1),"soldier");
				} else {//if your barracks' queues are full, set army units to attack
					for (Unit s : getUnitList()){
						findAttackSelection(getSelection(), gs);
						if ((s.getName()=="sd"||s.getName()=="tk"||s.getName()=="rf")&& s.getMyQueues().isEmpty()&& getAttackSelectable().size() > 0){
							Random r = new Random();
							int r2 = r.nextInt(getAttackSelectable().size());
							createQueue("attack",s,getAttackSelectable().get(r2));
							break;
						}
					}
				}
			} else {//if you don't have a barracks, build one
				if (getResources()>=50){//if you have enough resources to build a barracks, build it
					int wC = 0;
					for (Unit w : getUnitList()){//find the third worker (the first two should be set to collect), and make it build a barracks
						if (w.getName()=="wk"){
							wC ++;
							if (wC == 3){
								w.getMyQueues().clear();
								int x = getBuildingList().get(0).getX();
								int y = getBuildingList().get(0).getY();
								if (getNum() == 2){//different values for each player's build location (no player one because player one is never an AI)
									x -= 2;
									y += 2;
								} else if (getNum() == 4){
									x -= 2;
									y -= 2;
								} else if (getNum() == 3){
									x += 2;
									y += 2;
								}
								createQueue("build",w,x,y,"barracks");
								break;
							}
						}
					}
				} else {//if you don't have resources, set more workers to collect
					for (Unit w : getUnitList()){
						if (w.getName()=="wk"){
							createQueue("collect",w);
						}
					}
				}
			}
		}
	}
	
	/** method for counting the number of workers a player has
	* @return worker count
	*/
	int checkWorkers(){//counts the number of workers
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName()=="wk"){
				count += 1;
			}
		}
		return count;
	}
	
	/** method for counting the number of barracks a player has
	* @return barracks count
	*/
	int checkBarracks(){//counts the number of barracks
		int count = 0;
		for (int index = 0;index<getBuildingList().size();index++){
			if (getBuildingList().get(index).getName()=="bk"){
				count += 1;
			}
		}
		return count;
	}
	
	/** method for counting the number of soldiers a player has
	* @return soldier count
	*/
	int checkSoldiers(){//counts the number of soldiers
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName()=="sd"){
				count += 1;
			}
		}
		return count;
	}
	
	/** method for counting the number of tanks a player has
	* @return tank count
	*/
	int checkTanks(){//counts the number of tanks
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName() == "tk"){
				count += 1;
			}
		}
		return count;
	}
	
	/** method for counting the number of ranged units a player has
	* @return ranged count
	*/
	int checkRangedFighters(){//counts the number of ranged units
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName() == "rf"){
				count += 1;
			}
		}
		return count;
	}
	
	/** method for checking if you have enough resources to purchase a character
	* @param c the character whose cost you're checking
	* @return enough cost boolean
	*/
	boolean checkResources(Character c){//checks if you have enough resources to buy something
		boolean b;
		if (getResources() >= c.getCost()){
			b = true;
		} else {
			b = false;
		}
		return b;
	}
}

/** class for all information-storing queues
*/
class Queue{//used to store actions needed for characters

	private String action;
	private Character selection;
	private Character selectionTwo;
	private String item;
	private int desX;
	private int desY;
	
	/** Constructor for construction queues
	* @param a the chosen action
	* @param c the constructing character
	* @param i the item being built
	*/
	Queue(String a, Character c, String i){//Sets values for construction queues
		action = a;
		selection = c;
		item = i;
	}
	
	/** Constructor for movement queues and build queues
	* @param a the chosen action
	* @param c the character performing the action
	* @param x the chosen x value
	* @param y the chosen y value
	*/
	Queue(String a, Character c,int x, int y){//Sets values for movement queues or build queues
		action = a;
		selection = c;
		desX = x;
		desY = y;
	}
	
	/** Constructor for attack queues
	* @param a the chosen action
	* @param c the attacking character
	* @param cTwo the attacked character
	*/
	Queue(String a, Character c, Character cTwo){//Sets values for attacking queues
		action = a;
		selection = c;
		selectionTwo =cTwo;
	}
	
	/** Constructor for collection queues
	* @param a the chosen action
	* @param the collecting character
	*/
	Queue(String a, Character c){//Sets values for resource collection queues
		action = a;
		selection = c;
	}
	
	/** returns the chosen action
	* @return chosen action
	*/
	String getAction(){//returns chosen action
		return action;
	}
	
	/** returns the selected character
	* @return selected character
	*/
	Character getSelection(){//returns selected unit
		return selection;
	}
	
	/** returns the attacked unit (for attack queues)
	* @return attacked unit
	*/
	Character getSelectionTwo(){//returns attacked unit
		return selectionTwo;
	}
	
	/** returns the constructed item (for construction queues
	* @return item
	*/
	String getItem(){//returns constructed item
		return item;
	}

	/** method to check if a queue is ready (overwritten later)
	* @return boolean
	*/
	boolean ready(){//Overwritten later
		return true;
	}
	
	/** returns the chosen x value (for movement queues)
	* @return x value
	*/
	int getX(){//Returns selected x-value
		return desX;
	}
	
	/** returns the chosen y value (for movement queues)
	* @return y value
	*/
	int getY(){//Returns selected y-value
		return desY;
	}
	
	/** method for checking the amount of time left (overwritten later)
	*/
	void decrementTime(){//Overwritten later
	}
	
}

/** class for unit contruction queues, storing the order to build units in
*/
class ConstructQueue extends Queue{//used to store actions that need to be executed for buildings
	 private int timeLeft;
	
	/** Constructor for construction queues
	* @param a the chosen action
	* @param c the selected character performing the action
	* @param i the item being constructed
	* @param t the amount of time to build the item
	*/
	ConstructQueue(String a, Character c, String i,int t){//Creates queue, using values from Queue
		super(a,c,i);
		timeLeft = t;
	}
	
	/** returns the amount of time left before the item is built
	* @return time left
	*/
	int getTimeLeft(){
		return timeLeft;
	}
	
	/** changes the amount of time left before the item is built
	* @param t the new amount of time
	*/
	void setTimeLeft(int t){
		timeLeft = t;
	}
	
	/** decreases the amount of time left before the item is built
	*/
	void decrementTime(){//decrease the time left in the queue
		timeLeft --;
	}
	
	/** checks if the item is ready to be built
	*/
	boolean ready(){//returns if the action should be done
		return timeLeft <= 0;
	}
	
}

//the following queues are used for units' actions

/** class for movement queues, storing the location to move to
*/
class MoveQueue extends Queue {//moves a unit to a different location
	
	/** Constructor for movement queues
	* @param a the chosen action
	* @param c the character moving
	* @param x the chosen x value to move to
	* @param y the chosen y value to move to
	*/
	MoveQueue (String a, Character c,int x, int y){//Creates queue, using values from Queue
		super(a,c,x,y);
	}	
}

/** class for attacking queues, storing the unit to attack
*/
class AttackQueue extends Queue {// combination of moving and attacking

	/** Constructor for attack queues
	* @param a the chosen action
	* @param c the attacking character
	* @param cTwo the attacked character
	*/
	AttackQueue(String a, Character c, Character cTwo){//Creates queue, using values from Queue
		super(a,c,cTwo);
	}
}

/** class for resource collecting queues, storing the ongoing collection action
*/
class CollectionQueue extends Queue {//a worker uses this to collect resources
	
	/** Constructor for collection queues
	* @param a the chosen action
	* @param the collecting character
	*/
	CollectionQueue(String a, Character c){//Creates queue, using values from Queue
		super(a,c);
	}
}

/** class for barrack building queues, storing the barracks to build
*/
class BuildQueue extends Queue {// a worker uses this to construct a new building

	/** Constructor for build queues
	* @param a the chosen action
	* @param c the character doing the building
	* @param x the chosen x value to build it at
	* @param y the chosen y value to build it at
	*/
	BuildQueue(String a, Character c, int x, int y){//Creates queue, using values from Queue
		super(a,c,x,y);
	}
}
