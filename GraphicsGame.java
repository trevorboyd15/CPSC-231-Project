import javafx.scene.*;
import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import java.util.*;
import javafx.animation.KeyValue;


public class GraphicsGame extends Application{
	private GameState gs = new GameState();
	private Character c;
	
	private Text res = new Text();
	
	private Text keyOne = new Text();
	private Text keyTwo = new Text();
	private Text keyThree = new Text();
	
	private int numAI = 1;
	private int theme = 0;
	
	private int MouseState = 0;
	private int selector = 0;
	private double speed = 0.5;
	private int imSize = 100;
	
	private List<ImageView> p1unit = new ArrayList<ImageView>();
	private List<ImageView> p2unit = new ArrayList<ImageView>();
	private List<ImageView> p3unit = new ArrayList<ImageView>();
	private List<ImageView> p4unit = new ArrayList<ImageView>();
	private List<ImageView> p1build = new ArrayList<ImageView>();
	private List<ImageView> p2build = new ArrayList<ImageView>();
	private List<ImageView> p3build = new ArrayList<ImageView>();
	private List<ImageView> p4build = new ArrayList<ImageView>();
	private List<List<ImageView>> imstorage = new ArrayList<List<ImageView>>();
	
	private ImageView cursor = new ImageView();
	private String[] cursTheme = {"MoonOutline.png","PlainsOutline.png"};
	
	
	public void addPlayers(int a){
		gs.addHumanPlayer(1,gs);
		for (int i = 2; i < a +2; i ++){
			gs.addAIPlayer(i,gs);
		}
	}
	public void addAiPlayers(int a){
		for (int i = 0; i < a; i ++){
			gs.addAIPlayer(i+3,gs);
		}
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public void start(Stage stage){
		imSize = 1000/gs.getMap().getSize();
		
		Timeline timeline = new Timeline();
		Timeline aiTurn = new Timeline();
		
		Timeline timeline2 = new Timeline();
		timeline2.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				int pn = gs.getPlayers().size();
				//timeline2.stop();
				timeline2.getKeyFrames().clear();
				for (int i = 0; i < pn; i++){
					int n = gs.getPlayers().get(i).getNum();
					for(int j = 0; j < gs.getPlayers().get(i).getUnitList().size();j ++){
						if (imstorage.get(n+3).size() > j){
							imstorage.get(n+3).get(j).setX(imSize*gs.getPlayers().get(i).getUnitList().get(j).getX());
							imstorage.get(n+3).get(j).setY(imSize*gs.getPlayers().get(i).getUnitList().get(j).getY());
						}
					}
				}
				//timeline2.play();
			}
		});
		
		addPlayers(1);
		Group root = new Group();
		VBox men = new VBox(5);
		VBox rest = new VBox(10);
		HBox buttons = new HBox(10);
		HBox themeSel = new HBox(10);
		Scene scene = new Scene(root, 1100, 1000,Color.BLACK);
		Scene restart = new Scene(rest,200,200);
		Scene menu = new Scene(men,400,400);
		stage.setTitle("StarCraft III");
		
		Text didWin = new Text();
		
		rest.setAlignment(Pos.TOP_CENTER);
		
		Button backToMen = new Button();
		Button quit = new Button();
		
		backToMen.setText("Return to Menu");
		quit.setText("Quit");
		
		rest.getChildren().add(didWin);
		rest.getChildren().add(backToMen);
		rest.getChildren().add(quit);
		
		backToMen.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				stage.setScene(menu);
			}
		});
		quit.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				stage.close();
			}
		});
		
		
		
		
		Label buttonExp = new Label("How Many Opponents Would You Like To Face?");
		Label themeExp = new Label("Please Select A Theme");
		Text numOfAi = new Text();
		Text curTheme = new Text();
		curTheme.setText("Theme: Moon");
		numOfAi.setText("You Have 1 Opponent");
		
		Button moon = new Button();
		Button plain = new Button();
		
		moon.setText("Moon");
		plain.setText("Plains");
		
		moon.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				theme = 0;
				curTheme.setText("Theme: Moon");
			}
		});
		plain.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				theme = 1;
				curTheme.setText("Theme: Plains");
			}
		});
		
		
		themeSel.getChildren().add(moon);
		themeSel.getChildren().add(plain);
		themeSel.setAlignment(Pos.CENTER);
		
		Button one = new Button();
		Button two = new Button();
		Button three = new Button();
		
		one.setText("One");
		two.setText("Two");
		three.setText("Three");
		
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().add(one);
		buttons.getChildren().add(two);
		buttons.getChildren().add(three);
		
		Button start = new Button();
		start.setText("START");
		
		start.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				gs = new GameState();
				for (int i = 0; i < imstorage.size(); i++){
					imstorage.get(i).clear();
				}
				root.getChildren().clear();
				root.getChildren().add(res);
				root.getChildren().add(keyOne);
				root.getChildren().add(keyTwo);
				root.getChildren().add(keyThree);
				addPlayers(numAI);
				display(root);
				stage.setScene(scene);
				timeline.play();
				aiTurn.play();
			}
		});
		
		
		one.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				numOfAi.setText("You Have 1 Opponent");
				numAI = 1;
			}
		});
		two.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				numOfAi.setText("You Have 2 Opponents");
				numAI = 2;
			}
		});
		three.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				numOfAi.setText("You Have 3 Opponents");
				numAI = 3;
			}
		});
		
		men.setAlignment(Pos.TOP_CENTER);
		
		Label title = new Label("RTS is Best");
		
		men.getChildren().add(title);
		men.getChildren().add(buttonExp);
		men.getChildren().add(buttons);
		men.getChildren().add(numOfAi);
		men.getChildren().add(themeExp);
		men.getChildren().add(themeSel);
		men.getChildren().add(curTheme);
		men.getChildren().add(start);
		
		
		res.setText("Minerals: " +gs.getPlayers().get(0).getResources());
		res.setX(1005);
		res.setY(50);
		res.setFill(Color.WHITE);
		res.setFont(Font.font ("Verdana", 20));
		res.setWrappingWidth(100.0);
		res.setTextAlignment(TextAlignment.CENTER);
		
		keyOne.setX(1005);
		keyOne.setY(200);
		keyOne.setFill(Color.WHITE);
		keyOne.setFont(Font.font ("Verdana", 15));
		keyOne.setWrappingWidth(90.0);
		keyOne.setTextAlignment(TextAlignment.CENTER);
		
		keyTwo.setX(1005);
		keyTwo.setY(400);
		keyTwo.setFill(Color.WHITE);
		keyTwo.setFont(Font.font ("Verdana", 15));
		keyTwo.setWrappingWidth(90.0);
		keyTwo.setTextAlignment(TextAlignment.CENTER);
		
		keyThree.setX(1005);
		keyThree.setY(400);
		keyThree.setFill(Color.WHITE);
		keyThree.setFont(Font.font ("Verdana", 15));
		keyThree.setWrappingWidth(90.0);
		keyThree.setTextAlignment(TextAlignment.CENTER);
		
		imstorage.add( p1build);	
		imstorage.add( p2build);
		imstorage.add( p3build);
		imstorage.add( p4build);
		imstorage.add( p1unit);	
		imstorage.add( p2unit);	
		imstorage.add( p3unit);	
		imstorage.add( p4unit);	
			
		
		
		
		
		EventHandler<MouseEvent> mhand = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				//System.out.println("cool");
				int x = (int)(e.getX()/imSize);
				int y = (int)(e.getY()/ imSize);
				if (x >= gs.getMap().getSize()){
					x = gs.getMap().getSize() -1;
				}
				if ( y >= gs.getMap().getSize()){
					y = gs.getMap().getSize();
				}
				
				if (gs.getMap().getBoard()[y][x].charAt(0) == '1'){
					if (MouseState == 0){
						cursor.setImage(new Image(cursTheme[theme]));
						cursor.setFitHeight(imSize-2);
						cursor.setFitWidth(imSize-2);
						root.getChildren().add(cursor);
					}
					Player p = gs.getPlayers().get(0);
					p.findSelectables();
					for (int index = 0;index<p.getSelectables().size();index++){
						if (p.getSelectables().get(index).getX() == x &&p.getSelectables().get(index).getY() == y){
							c = p.getSelectables().get(index);
							cursor.setX(c.getX()*imSize+1);
							cursor.setY(c.getY()*imSize+1);
							if (c instanceof Worker){
								keyOne.setText("C: Construct Barracks");
								keyTwo.setText("G: Gather Resources");
								keyThree.setText("");
							} else if (c instanceof MainBase){
								keyOne.setText("B: Build Worker");
								keyTwo.setText("");
								keyThree.setText("");
							}else if (c instanceof Barracks){
								keyOne.setText("B: Build Soldier");
								keyTwo.setText("");
								keyThree.setText("");
							}else if (c instanceof Soldier){
								keyOne.setText("Click To Attack");
								keyTwo.setText("");
								keyThree.setText("");
							}
							MouseState = 1;
							break;
						}
					}
				}else if(MouseState == 1 && c instanceof Unit){
					if (gs.getMap().getBoard()[y][x].equals("---")){
						c.getMyQueues().clear();
						c.getMyQueues().add(new MoveQueue("move",c,x,y));
					}else if(gs.getMap().getBoard()[y][x].charAt(0) != '1'){
						c.getMyQueues().clear();
						gs.getPlayers().get(0).findAttackSelection(c,gs);
						for (int i = 0;i < gs.getPlayers().get(0).getAttackSelectable().size();i++){
							Character a = gs.getPlayers().get(0).getAttackSelectable().get(i);
							if (a.getX() == x && a.getY() == y){
								c.getMyQueues().add(new AttackQueue("attack",c,a));
								break;
							}
						}
					}
				}else if(MouseState == 2){
					if (gs.getMap().getBoard()[y][x].equals("---")){
						c.getMyQueues().clear();
						c.getMyQueues().add(new BuildQueue("build",c,x,y));
						gs.getPlayers().get(0).decRes(50);
						MouseState = 1;
					}
				}
			}
		};
		
		
		EventHandler<KeyEvent> kb = new EventHandler<KeyEvent>() { 
			@Override 
			public void handle(KeyEvent k) { 
				if (MouseState == 1){
					if (c instanceof MainBase && k.getText().equals("b") && enoughRes("worker",0)){
						c.getMyQueues().add(new ConstructQueue("construct",c,"worker",2));
						gs.getPlayers().get(0).decRes(10);
					}else if(c instanceof Worker && k.getText().equals("g")){
						c.getMyQueues().clear();
						c.getMyQueues().add(new CollectionQueue("collect",c));
					}else if(c instanceof Worker && k.getText().equals("c") &&enoughRes("barracks",0) ){
						MouseState = 2;
					}else if (c instanceof Barracks && k.getText().equals("b") && enoughRes("soldier",0)){
						c.getMyQueues().add(new ConstructQueue("construct",c,"soldier",3));
						gs.getPlayers().get(0).decRes(20);
					}
				}
			}
				
		};     
		
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5),ae -> up(root,timeline,
		stage,restart,aiTurn,didWin,timeline2)));
		aiTurn.getKeyFrames().add(new KeyFrame(Duration.seconds(speed),ae -> aiT()));
		timeline2.play();
		
		root.getChildren().add(res);
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		aiTurn.setCycleCount(Timeline.INDEFINITE);
		
		root.addEventFilter(MouseEvent.MOUSE_CLICKED, mhand);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, kb);
		
		
		stage.setScene(menu);
		//stage.setScene(scene);
		stage.show();
	}
	
	public void aiT (){
		for (int i = 1;i < gs.getPlayers().size();i++){
			if (gs.getPlayers().get(i).getBuildingList().size() >= 1){
				gs.getPlayers().get(i).getChoice(gs);
			}
		}
	}
		
	
	public void up(Group root,Timeline timeline,Stage stage,Scene restart,Timeline aiTurn,Text didWin,Timeline timeline2){
		int a = 0;
		int b = 0;
		int dead = 0;
		
		
		//ImageView iV2 = new ImageView();
		
		for (int index = 0; index < gs.getPlayers().size();index++){
			
			gs.getPlayers().get(index).updateQueues(gs);
			checkHealth(root,index);
			
			if (gs.getPlayers().get(index).getUnitList().size() > imstorage.get(index+4).size()){
			a = gs.getPlayers().get(index).getUnitList().size();
			b = imstorage.get(index+4).size();
			for(int i = a-1; i >= b; i--){
				unitimAdd(i,root,index);
				//System.out.println(b);
			}
			}
			if (gs.getPlayers().get(index).getBuildingList().size() > imstorage.get(index).size()){
			a = gs.getPlayers().get(index).getBuildingList().size();
			b = imstorage.get(index).size();
			for(int i = a-1; i >= b; i--){
				buildingImAdd(i,root,index);
				//System.out.println(b);
			}
			
		}
		}
		
		
		//System.out.println(c.getMyQueues());
		
		//System.out.println("you have " + gs.getPlayers().get(0).getResources());
		//gs.displayBoard();
		updateUnitLocations(gs,timeline2);
		gs.checkBase();
		for (int i = 0; i < gs.getPlayers().size(); i ++){
			if (gs.getPlayers().get(i).getBuildingList().size() == 0){
				dead ++;
			}
		}
		if (dead >= numAI){
			if (gs.getPlayers().get(0).getBuildingList().size() >= 1){
				System.out.println("you won!");
				didWin.setText("you won!");
			} else {
				System.out.println("you lost :(");
				didWin.setText("you lost :(");
			}
			timeline.stop();
			timeline2.stop();
			aiTurn.stop();
			stage.setScene(restart);
		}
		res.setText("Minerals: " +gs.getPlayers().get(0).getResources());
		
	}
	
	public void unitimAdd(int i,Group root,int j){
		String[][] uRef = { {"MoonWorker1.png","MoonWorker2.png","MoonWorker3.png",
		"MoonWorker4.png","MoonSoldier1.png","MoonSoldier2.png","MoonSoldier3.png",
		"MoonSoldier4.png"}, {"PlainsWorker1.png","PlainsWorker2.png","PlainsWorker3.png",
		"PlainsWorker4.png","PlainsSoldier1.png","PlainsSoldier2.png","PlainsSoldier3.png",
		"PlainsSoldier4.png"}};
		ImageView iV2 = new ImageView();
		Image i2;
		//System.out.println(i);
		//System.out.println(j);
		boolean valid = false;
		if (gs.getPlayers().get(j).getUnitList().get(i) instanceof Worker){
			i2 = new Image(uRef[theme][j],true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(j).getUnitList().get(i) instanceof Soldier){
			i2 = new Image(uRef[theme][j+4],true);
			iV2.setImage(i2);
			valid = true;
		}	
		if (valid){
			int k = gs.getPlayers().get(j).getUnitList().get(i).getX();
			int l = gs.getPlayers().get(j).getUnitList().get(i).getY();
			iV2.setX(k*imSize+1);
			iV2.setY(l*imSize+1);
			iV2.setFitHeight(imSize -2);
			iV2.setFitWidth(imSize -2);
			root.getChildren().add(iV2);
			//System.out.println(j+2);
			imstorage.get(j+4).add(iV2);
		}
	}
	
	public void buildingImAdd(int i,Group root,int j){
		String[][] uRef = {{"MoonBarracks1.jpg","MoonBarracks2.jpg","MoonBarracks3.jpg",
		"MoonBarracks4.jpg","MoonBase1.jpg","MoonBase2.jpg","MoonBase3.jpg",
		"MoonBase4.jpg"},{"PlainsBarracks1.jpg","PlainsBarracks2.jpg","PlainsBarracks3.jpg",
		"PlainsBarracks4.jpg","PlainsBase1.jpg","PlainsBase2.jpg","PlainsBase3.jpg",
		"PlainsBase4.jpg"}};
		Image i2;
		ImageView iV2 = new ImageView();
		boolean valid = false;
		
		if (gs.getPlayers().get(j).getBuildingList().get(i) instanceof Barracks){
			i2 = new Image(uRef[theme][j],true);
			iV2.setImage(i2);
			valid = true;
		} else if (gs.getPlayers().get(j).getBuildingList().get(i) instanceof MainBase){
			i2 = new Image(uRef[theme][j+4],true);
			iV2.setImage(i2);
			valid = true;
		}
		if (valid){
			int k = gs.getPlayers().get(j).getBuildingList().get(i).getX();
			int l = gs.getPlayers().get(j).getBuildingList().get(i).getY();
			iV2.setX(k*imSize+1);
			iV2.setY(l*imSize+1);
			iV2.setFitHeight(imSize-2);
			iV2.setFitWidth(imSize-2);
			root.getChildren().add(iV2);
			imstorage.get(j).add(iV2);
		}
	}
	
	public void checkHealth(Group root,int i){
		
		
			for (int j = 0; j < gs.getPlayers().get(i).getBuildingList().size(); j ++){
				//System.out.println(j);
				if (gs.getPlayers().get(i).getBuildingList().get(j).getHealth() <= 0){
					gs.getPlayers().get(i).getBuildingList().remove(j);
					root.getChildren().remove(imstorage.get(i).get(j));
					imstorage.get(i).remove(j);
					
				}
			}
			for (int j = 0; j < gs.getPlayers().get(i).getUnitList().size(); j ++){
				//System.out.println(j + "ch" + i);
				if (gs.getPlayers().get(i).getUnitList().get(j).getHealth() <= 0){
					//System.out.println(j + "ch" + i);
					gs.getPlayers().get(i).getUnitList().remove(j);
					root.getChildren().remove(imstorage.get(i+4).get(j));
					imstorage.get(i+4).remove(j);
					
				}
			}
		
	}		
	
	public void updateUnitLocations(GameState gs,Timeline timeline){
		if (MouseState != 0){
			cursor.setX(c.getX()*imSize+1);
			cursor.setY(c.getY()*imSize+1);
		}
		int pn = gs.getPlayers().size();
		
		if (pn > 1){
			timeline.stop();
			timeline.getKeyFrames().clear();
			for (int i = 0; i < pn; i++){
				int n = gs.getPlayers().get(i).getNum();				
				for(int j = 0; j < gs.getPlayers().get(i).getUnitList().size();j ++){
					if (imstorage.get(n+3).size() > j){
						final KeyValue keyValue1 = new KeyValue(imstorage.get(n+3).get(j).xProperty(),imSize*gs.getPlayers().get(i).getUnitList().get(j).getX());
						final KeyValue keyValue2 = new KeyValue(imstorage.get(n+3).get(j).yProperty(),imSize*gs.getPlayers().get(i).getUnitList().get(j).getY());
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), keyValue1, keyValue2);
						timeline.getKeyFrames().add(keyFrame);
					}
				}
			}
			timeline.play();
		}
	}
	
	public boolean enoughRes(String s,int n){
		boolean res = false;
		if (s.equals("worker") && gs.getPlayers().get(n).getResources() >= 10){
			res = true;
		}else if (s.equals("barracks") && gs.getPlayers().get(n).getResources() >= 50){
			res = true;
		}else if (s.equals("soldier") && gs.getPlayers().get(n).getResources() >= 20){
			res = true;
		}
		return res;
	}
	
	public void display(Group root){
	
		String[][] base = {{"MoonBase1.jpg","MoonBase2.jpg","MoonBase3.jpg","MoonBase4.jpg"}
		,{"PlainsBase1.jpg","PlainsBase2.jpg","PlainsBase3.jpg","PlainsBase4.jpg"}};
		String[] space = {"MoonSpace.jpg","PlainsSpace.jpg"};
		gs.updateBoard();
		
		String[][] map = gs.getMap().getBoard();
		int s = gs.getMap().getSize();
		
		
		for (int i = 0;i<s;i++){
			for (int j = 0;j<s;j++){
				ImageView iV = new ImageView();
				Image i1 = new Image(space[theme],true);
				iV.setImage(i1);
				iV.setX(j*imSize+1);
				iV.setY(i*imSize+1);
				iV.setFitHeight(imSize-2);
				iV.setFitWidth(imSize-2);
				root.getChildren().add(iV);
				

				if (map[i][j]!="---"){
					ImageView iV2 = new ImageView();
					String im = "MoonBarricade.jpg";
					if (map[i][j].equals("1mb")){
						im = base[theme][0];
						selector = 0;
					} else if (map[i][j].equals("2mb")){
						im = base[theme][1];
						selector = 1;
					} else if (map[i][j].equals("3mb")){
						im = base[theme][2];
						selector = 2;
					}else if (map[i][j].equals("4mb")){
						im = base[theme][3];
						selector = 3;
					}
					Image i2 = new Image(im,true);
					iV2.setImage(i2);
					iV2.setX(j*imSize+1);
					iV2.setY(i*imSize+1);
					iV2.setFitHeight(imSize-2);
					iV2.setFitWidth(imSize-2);
					root.getChildren().add(iV2);
					imstorage.get(selector).add(iV2);
				}
			}
		}
		
	
	
	}
}








