import java.util.*;

public class Game {//where the game runs
    public static void main (String[] args){//main function; creates and runs the game
		GameState gs = new GameState();
		gs.addHumanPlayer(1);
		//gs.addHumanPlayer(2);
		gs.addAIPlayer(2);
		while(true){
			
			gs.doTurns();
			gs.checkBase();
			if (gs.wonGame()){
				System.out.println("Player " + gs.getPlayers().get(0).getNum() + " has won the game!");
				break;
			}
			
		}
		/*Graphics g = new Graphics();
		g.starter();
        
    */}
  
}

class Character {//base object that all placeable things inherit from
	private int posX;
	private int posY;
	private int health;
	private int damage;
	private int cost;
	private String name;
	private List<Queue> myQueues = new ArrayList<Queue>(0);
	private List<String> myActions = new ArrayList<String>(0);
	
	public String toString(){//shows character locations
		return name + " at " + posX +","+posY;
	}
	
	Character (int x, int y,String n,int h,int c){//saves the values
	posX = x;
	posY = y;
	name = n;
	health = h;
	cost = c;
	addMyActions("pass");
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
	
	int getHealth(){//returns health
		return health;
	}
	
	void setHealth(int h){//changes health
		health = h;
	}

	void setX(int x){// changes posX
		posX = x;
	}
	
	void setY(int y){//changes posY
		posY = y;
	}
	
	void setCost(int c){//changes cost
		cost = c;
	}
	
	int getCost(){//returns cost
		return cost;
	}
	
	List<Queue> getMyQueues(){//returns list of queues
		return myQueues;
	}
	
	void addMyActions(String ma){//adds a new action
		myActions.add(ma);
	}
	
	List<String> getMyActions(){//returns all actions
		return myActions;
	}
	
	void decrementHealth(int h){//removes health
		health -= h;
	}
	
	int getDamage(){//returns damage
		return damage;
	}
	
	void setDamage(int d){//changes damage
		damage = d;
	}
	
}

class Unit extends Character {// generic unit

	
	private int armor;
	
	Unit(String n, int h, int a, int x, int y,int d,int c){//creates a character with armor and damage
		super(x,y,n,h,c);
		armor = a;
		setDamage(d);
	}

	void moveUnit(int x, int y){//Used to move a unit
		setX(x);
		setY(y);
	}

	void attackUnit(Character enemy){//Used to attack a unit

 		enemy.setHealth(enemy.getHealth()-getDamage());
 	}

	int collectResources(int r){//Used to collect new resources (for workers)
		return(r+5);
	}

}

class Building extends Character{// generic building

	
	Building (String n, int x, int y,int h,int c){//creates a building with character properties
		super(x,y,n,h,c);
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
	
	int getSize(){//returns board size
		return size;
	}
	
	String[][] getBoard(){//returns game map
		return map;
	}
}

class GameState {// the game state that holds all information required to run the game
	private List <Player> players = new ArrayList<Player>();
	private Map map = new Map();
	
	void doTurns(){//updates and displays the board between turns
		for (int i = 0; i<players.size(); i++){
			updateBoard();
			displayBoard();
			players.get(i).turn(this);
		}
	
	}
	
	boolean wonGame(){//Checks if a player has won the game, by checking the number of players
		return (players.size() == 1);
	}
	
	void checkBase(){//Removes a player from the player list if their main base is destroyed
		for (int index = 0; index < players.size(); index++){
			if (players.get(index).getBuildingList().size()== 0 ||
			!(players.get(index).getBuildingList().get(0) instanceof MainBase)){
				players.remove(index);
			}
		}
	}
	
	boolean checkRange(Character attacker, Character target){//Checks range for attacks
		int x = attacker.getX();
		int y = attacker.getY();
		
		int tx = target.getX();
		int ty = target.getY();
		
		return (Math.abs(x-tx)<=1 && Math.abs(y-ty) <= 1);
	}
	
	void addHumanPlayer(int num) {//Forms a new human player
		players.add(new HumanPlayer(num));
	}
	
	void addAIPlayer(int num) {
		players.add(new AIPlayer(num));
	}
	
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
	
	void displayBoard(){//displays the board
		map.display();
	}

	Map getMap(){//returns map object
		return map;
	}
	
	List<Player> getPlayers(){//returns players list
		return players;
	}
	
}

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
	
	
	Player(int num){//saves the player numbers, and creates initial board conditions
		pNum = num;
		if (pNum == 1){
			buildings.add(new MainBase("mb",1,8,100,20000));
			resources += 20;
		}else if (pNum == 2){
			buildings.add(new MainBase("mb",8,1,100,20000));
			resources += 20;
		}
	}
	
	void turn(GameState gs){//gets overwritten by inherited class just need to be defined here for now
	}
	
	void findSelectables(){//combines the list of units and buildings
		selectables.clear();
		for (int i = 0; i<buildings.size(); i++){
			if (buildings.get(i).getName() == "mb" || buildings.get(i).getName() == "bk" ){
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
	
	void buildUnit(String selection,Character b, int x, int y){//uses the building pos to create a unit next to it
		if (selection == "worker" ){
			units.add(new Worker(x,y));
		}else if (selection == "soldier"){
			units.add(new Soldier(x,y));
 		}
	}
	
	void buildBuilding(String selection,int x, int y){//adds a new barracks building to the board
	
		if (selection == "barracks" ){
			buildings.add(new Barracks("bk",x,y,100,50));
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
	
	Character getSelection(){//returns chosen character
		return selection;
	}
	
	void setSelection(Character c){//changes character chosen
		selection = c;
	}
	
	String getActionSelected(){//returns selected action
		return actionSelected;
	}
	
	void setActionSelected(String s){//changes selected action
		actionSelected = s;
	}
	

	void setAttackSelection(Character c){//changes attacked unit
		attackSelection = c;
	}
	
	Character getAttackSelection(){//returns attacked unit
		return attackSelection;
	}

	int getResources(){//returns resource count
		return resources;
	}
	
	void setResources(int r){//changes resource count
		resources = r;
	}
	
	void decRes(int r){
		resources -= r;
	}

	void findConstruct(Character b){//sets list of constructable units
		construct.clear();
		if (b.getName() == "mb"){
			if (getResources()>=10){
				construct.add("worker");
			}
		}else if (b.getName() == "bk"){
			if (getResources()>=20){
				construct.add("soldier");
			}
		}
	}
	
	void findBuild(){//checks resource number, and adds a barracks to the list of things to be built
		build.clear();
		build.add("pass");
		if (getResources() >= 50){
			build.add("barracks");
		}
	}
	
	void findAttackSelection(Character c, GameState gs){//finds list of attackable units
		attackSelectable.clear();
		for (int index = 0; index < gs.getPlayers().size();index++){
			if (gs.getPlayers().get(index).getNum() != pNum){
				for (int index1 = 0; index1 < gs.getPlayers().get(index).getBuildingList().size();index1++){
					attackSelectable.add(gs.getPlayers().get(index).getBuildingList().get(index1));
				}
				for (int index1 = 0; index1 < gs.getPlayers().get(index).getUnitList().size();index1++){
					attackSelectable.add(gs.getPlayers().get(index).getUnitList().get(index1));
				}
			}
		}
	}
	
	List<String> getConstruct(){//returns constructable unit list
		return construct;
	}
	
	List<String> getBuild(){//returns list of buildings to be built
		return build;
	}
	
	List<Character> getAttackSelectable(){//returns attackable units list
		return attackSelectable;
	}
	
	String getItem(){//returns constructed unit
		return item;
	}
	
	void setItem(String i){//changes constructed unit
		item = i;
	}
	
	void createQueue(String action, Character charac, String item){//Creates queue for unit construction
		if (charac instanceof MainBase && item == "worker" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,2));
			setResources(getResources()-10);
			
		} else if (charac instanceof Barracks && item == "soldier" && action == "construct"){
			charac.getMyQueues().add(new ConstructQueue(action,charac,item,3));
			setResources(getResources()-20);
		}
	}
	
	void createQueue(String action, Character charac, int x, int y){//Creates queue for movement
		if (charac instanceof Unit && action == "move"){
			charac.getMyQueues().add(new MoveQueue(action,charac,x,y));
		}
	}
	
	void createQueue(String action, Character charac, int x, int y,String item){//Creates queue for building buildings
		if (charac instanceof Worker && action == "build"){
			charac.getMyQueues().add(new BuildQueue(action,charac,x,y));
			setResources(getResources()-50);
		}
	}
		
	void createQueue(String action, Character charac){//Creates queue for resource collection
		if (charac instanceof Worker && action == "collect"){
			charac.getMyQueues().add(new CollectionQueue(action,charac));
		}
	}
	
	void createQueue (String action, Character select, Character target){//Creates queue for attacking
		if (select instanceof Unit && action == "attack"){
			select.getMyQueues().add(new AttackQueue(action,select,target));
		}
	}
	
	void checkHealth(){//Checks characters' healths, and deletes them if necessary
		for (int index = 0; index < buildings.size();index ++){
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
	
	void doTurn(GameState s){//Game turn
		checkHealth();
		updateQueues(s);
	}
	
	void updateQueues(GameState gameS){//Cycles through all characters' queues, and runs the first item in each
		String a;
		Character c;
		String i;
		boolean done;
		int p = 0;
		
		if (pNum == 1){
			p = 1;
		} else if (pNum == 2){
			p = -1;
		}
		
		for (int index = 0; index < buildings.size(); index++){//goes through the building list and runs the first queue action
			done = false;
			if (buildings.get(index).getMyQueues().size() > 0){
				if (buildings.get(index).getMyQueues().get(0).ready()){
					a = buildings.get(index).getMyQueues().get(0).getAction();
					c = buildings.get(index).getMyQueues().get(0).getSelection();
					i = buildings.get(index).getMyQueues().get(0).getItem();
					if ((c instanceof MainBase && (i == "worker") )||(c instanceof Barracks && (i == "soldier"))){
						if (gameS.getMap().getBoard()[c.getY()+(p*-1)][c.getX()+p] == "---"){
							buildUnit(i,c,c.getX()+p,c.getY()+(p*-1));
							done = true;
						} else if (gameS.getMap().getBoard()[c.getY()][c.getX()+p] == "---"){
							buildUnit(i,c,c.getX()+p,c.getY());
							done = true;
						} else if (gameS.getMap().getBoard()[c.getY()+(p*-1)][c.getX()] == "---"){
							buildUnit(i,c,c.getX(),c.getY()+(p*-1));
							done = true;
						}
					}
					if (done){// if the queue action was completed remove the queue from existence
						buildings.get(index).getMyQueues().remove(0);
					}
				}else{
				buildings.get(index).getMyQueues().get(0).decrementTime();// becuase the buildings only have construct queue 
				}
			}
			gameS.updateBoard();
		}
		
		for (int index = 0; index < units.size(); index++){//goes through the units to update there queues
			done = false;
			
			if (units.get(index).getMyQueues().size() > 0){
				if (units.get(index).getMyQueues().get(0) instanceof MoveQueue){//if the unit is moving
					int x = units.get(index).getMyQueues().get(0).getX();
					int y = units.get(index).getMyQueues().get(0).getY();
					if (gameS.getMap().getBoard()[y][x] == "---"){
						done = true;
						units.get(index).moveUnit(x,y);
					}

				}else if(units.get(index).getMyQueues().get(0) instanceof AttackQueue){//if the unit is attacking
					Character c1= units.get(index).getMyQueues().get(0).getSelection();
					Character c2 = units.get(index).getMyQueues().get(0).getSelectionTwo();
					if (gameS.checkRange(c1,c2)){
						c2.decrementHealth(c1.getDamage());
					}else{
						int x = units.get(index).getMyQueues().get(0).getSelectionTwo().getX();
						int y = units.get(index).getMyQueues().get(0).getSelectionTwo().getY();
						if (gameS.getMap().getBoard()[y+1][x] == "---"){
							units.get(index).moveUnit(x,y+1);
						}
				
					}
					
					if (c2.getHealth() <= 0){
						done = true;
					}
				

				} else if (units.get(index).getMyQueues().get(0) instanceof CollectionQueue){// if the unit is collecting
					resources = units.get(index).collectResources(resources);
					
				} else if (units.get(index).getMyQueues().get(0) instanceof BuildQueue){
					int unitX = units.get(index).getX();
					int unitY = units.get(index).getY();
					int goalX = units.get(index).getMyQueues().get(0).getX();
					int goalY = units.get(index).getMyQueues().get(0).getY();
					if (goalY >= unitY -1 && goalY <= unitY +1 && goalX >= unitX - 1 && goalX <= unitX +1
					&& gameS.getMap().getBoard()[goalY][goalX] == "---"){
						buildBuilding("barracks" , goalX, goalY);
						done = true;
					}else if (gameS.getMap().getBoard()[goalY+1][goalX] == "---"){
						units.get(index).moveUnit(goalX,goalY+1);
					}
				
				
				}
				if (done){
					units.get(index).getMyQueues().remove(0);
				}
			
			}
			gameS.updateBoard();

		}
	}
	
	void setX(int x){//Changes selected x-value
		desX = x;
	}
	
	int getX(){//Returns selected x-value
		return desX;
	}
	
	void setY(int y){//Changes selected y-value
		desY = y;
	}
	
	int getY(){//Returns selected y-value
		return desY;
	}
		
}

class MainBase extends Building{// The core structure of an army; If destroyed, game is lost
	
	MainBase (String n, int x, int y,int h,int c){//Creates MainBase, using values inherited from Building
		super(n,x,y,h,c);
		addMyActions("construct");
	
	}
	
}

class Barracks extends Building{//building used for creating soldiers
	
	Barracks (String n, int x, int y,int h,int c){//Creates barracks, using values from Building
		super(n,x,y,h,c);
		addMyActions( "construct");
	
	}
	
}

class Worker extends Unit {//the resource gatherer of the army

	private String state;
	
	Worker (int x, int y){//Creates worker, using values inherited from Unit, and adds possible actions
		super ("wk",10,0,x,y,2,10);
		addMyActions("move");
		addMyActions("build");
		addMyActions("collect");
		addMyActions("attack");
	}

}

class Soldier extends Unit {//the main fighting unit of the army

  Soldier (int x, int y){//Creates soldier and adds soldiers' actions
 		super ("sd",20,0,x,y,10,20);
    addMyActions("move");
    addMyActions("attack");
 	}
 }
 
class HumanPlayer extends Player{//used for human players, including taking input

	private Scanner sc = new Scanner(System.in);
	private String input;
	private boolean valid;
	private int nInput;
	
	HumanPlayer(int num){//Creates Human player, with player number
		super(num);
	}
	
	void turn(GameState s){//Runs a turn, including revealing number of resources
		System.out.println("Player: "+getNum()+" it is your turn. You have "+getResources()+" resources.");
		getInput(s);
		doTurn(s);
	}
	
	boolean inputSelection(){//used to get a selected character from user, and checks if input is valid
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
	
	void inputAction(){//used to get the desired action from user, and checks for validity
		boolean valid = false;
		boolean haveResources = true;
		findActions(getSelection());
		while (valid == false){
			System.out.println("what would you like to do with that? (String) " +getActions());
			input = sc.next();
			for (int i = 0; i <getActions().size(); i++){
				if (input.contains(getActions().get(i))){
					if (getResources()<10&&getActions().get(i)=="construct"){
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
	
	void getInput(GameState gs){//uses inputSelection and inputAction with extra code to get final input
		valid = false;
		while (valid == false){
			if(inputSelection() == false){
				continue;
			}
			inputAction();
			valid = false;
			while (valid == false){
				
				if(getActionSelected() == "pass"){
					valid = true;
					break;
				}else if(getActionSelected() == "construct"){
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
					findAttackSelection(getSelection(), gs);
					System.out.println("what would you like to attack? (int)");
					System.out.println(getAttackSelectable());
					try{
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
				}else if (getActionSelected() == "build"){
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
						try{
							input = sc.next();  //gets an int input from user
							setX(Integer.parseInt(input)); //returns an int from a string number
							input = sc.next();
							setY(Integer.parseInt(input));
						} catch (NumberFormatException e1) {
							System.out.println("That is not a number");
							continue;
						}
						if (gs.getMap().getSize()-1 >= getX() && getX() >= 0 //checks if the inputted values are
						&& gs.getMap().getSize()-1 >= getY() && getY() >= 0 &&
						gs.getMap().getBoard()[getY()][getX()] == "---"){//within the parameters of the board
							valid = true;
							createQueue(getActionSelected(), getSelection(), getX(), getY(), getItem());
							break;
						}else{
							System.out.println("That space is not on the board and/or it has something there.");
						
						}
					}
				}else if (getActionSelected() == "collect"){
					System.out.println("You have gained 5 resources.");
					createQueue(getActionSelected(),getSelection());
					valid = true;
					break;
				}
			
			}
			
		}
	}
	
}

class AIPlayer extends Player{
	
	AIPlayer(int num){
		super(num);
	}
	
	void turn(GameState gs){
		getChoice(gs);
		//doTurn(gs);
	}
	
	void getChoice(GameState gs){
		
		int wkCount = checkWorkers();
		for (int index = 0; index<getBuildingList().get(0).getMyQueues().size();index++){
			if (getBuildingList().get(0).getMyQueues().get(index) instanceof ConstructQueue){
				wkCount += 1;
			}
		}
		int bkCount = checkBarracks();
		int sdCount = checkSoldiers();
		
		if (getUnitList().size()<3){
			if (getResources()>=10&&wkCount<3){
				createQueue("construct",getBuildingList().get(0),"worker");
			} else {
				for (Unit w : getUnitList()){
					if (w.getName()=="wk"){
						createQueue("collect",w);
					}
				}
			}
		} else if (sdCount>0){
			for (Unit s : getUnitList()){
				if (s.getName()=="sd" && s.getMyQueues().isEmpty()){
					//System.out.println(s.getMyQueues());
					findAttackSelection(s,gs);
					Random r = new Random();
					int r2 = r.nextInt(getAttackSelectable().size());
					createQueue("attack",s,getAttackSelectable().get(r2));
					break;
				}
			}
		} else if (wkCount>=3){
			if (bkCount>=1){
				if (getResources()>=20){
					createQueue("construct",getBuildingList().get(1),"soldier");
				}
			} else {
				if (getResources()>=50){
					for (Unit w : getUnitList()){
						if (w.getName()=="wk"){
							if (w == getUnitList().get(2)){
								createQueue("build",w,6,3,"barracks");
							}
						}
					}
				}
			}
		}
	}
	
	int checkWorkers(){
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName()=="wk"){
				count += 1;
			}
		}
		return count;
	}
	
	int checkBarracks(){
		int count = 0;
		for (int index = 0;index<getBuildingList().size();index++){
			if (getBuildingList().get(index).getName()=="bk"){
				count += 1;
			}
		}
		return count;
	}
	
	int checkSoldiers(){
		int count = 0;
		for (int index = 0;index<getUnitList().size();index++){
			if (getUnitList().get(index).getName()=="sd"){
				count += 1;
			}
		}
		return count;
	}
	
	boolean checkResources(Character c){
		boolean b;
		if (getResources() >= c.getCost()){
			b = true;
		} else {
			b = false;
		}
		return b;
	}
}

class Queue{//used to store actions needed for characters

	private String action;
	private Character selection;
	private Character selectionTwo;
	private String item;
	private int desX;
	private int desY;

	Queue(String a, Character c, String i){//Sets values for construction queues
		action = a;
		selection = c;
		item = i;
	}
	
	Queue(String a, Character c,int x, int y){//Sets values for movement queues
		action = a;
		selection = c;
		desX = x;
		desY = y;
	}
	
	Queue(String a, Character c, Character cTwo){//Sets values for attacking queues
		action = a;
		selection = c;
		selectionTwo =cTwo;
	}
	
	Queue(String a, Character c){//Sets values for resource collection queues
		action = a;
		selection = c;
	}
	
	String getAction(){//returns chosen action
		return action;
	}
	
	Character getSelection(){//returns selected unit
		return selection;
	}
	
	Character getSelectionTwo(){//returns attacked unit
		return selectionTwo;
	}
	
	
	String getItem(){//returns constructed item
		return item;
	}

	boolean ready(){//Overwritten later
		return true;
	}
	
	int getX(){//Returns selected x-value
		return desX;
	}
	
	int getY(){//Returns selected y-value
		return desY;
	}
	
	void decrementTime(){//Overwritten later
	}
	
}

class ConstructQueue extends Queue{//used to store actions that need to be executed for buildings
	 private int timeLeft;
	
	
	ConstructQueue(String a, Character c, String i,int t){//Creates queue, using values from Queue
		super(a,c,i);
		timeLeft = t;
		
	}
	
	void decrementTime(){//decrease the time left in the queue
		timeLeft --;
	}

	boolean ready(){//returns if the action should be done
		return timeLeft <= 0;
	}
	
}

//the following queues are used for units' actions

class MoveQueue extends Queue {//moves a unit to a different location
	
	MoveQueue (String a, Character c,int x, int y){//Creates queue, using values from Queue
		super(a,c,x,y);
	}
	
}

class AttackQueue extends Queue {// combination of moving and attacking

	AttackQueue(String a, Character c, Character cTwo){//Creates queue, using values from Queue
		super(a,c,cTwo);
	}

}

class CollectionQueue extends Queue {//a worker uses this to collect resources
	
	CollectionQueue(String a, Character c){//Creates queue, using values from Queue
		super(a,c);
	}
}

class BuildQueue extends Queue {// a worker uses this to construct a new building

	BuildQueue(String a, Character c, int x, int y){//Creates queue, using values from Queue
		super(a,c,x,y);
	}


}







