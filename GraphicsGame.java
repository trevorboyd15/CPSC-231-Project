import javafx.scene.*;
import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.swing.JOptionPane;

import javafx.animation.KeyValue;


public class GraphicsGame extends Application{
	private GameState gs = new GameState();


	private Character charac;
	highScore hs = new highScore("", 0);
	private 	Optional<String> name;

	
	private Text res = new Text();
	private State state;
	
	private Text keyOne = new Text();
	private Text keyTwo = new Text();
	private Text keyThree = new Text();
	
	private Timeline timeline = new Timeline();
	private Timeline aiTurn = new Timeline();
	

	private Stage s1 = new Stage();
	private Stage s2 = new Stage();

	
	private int numAI = 1;
	private int theme = 0;
  
	private String theme2 = "moon";
	
	private int score;

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
	private String[] cursTheme = {"Images/MoonOutline.png","Images/PlainsOutline.png"};
	private Label pauseT = new Label("The Game is Paused");
	private Label introT = new Label("Your Main Base is in the Bottom Left Corner. From This base you can build workers" +
	" using the b key for 10 Resources. A worker can gather resources by selecting it and pressing the g key. A Worker can also build"+
	" a barracks for 50 resources. The Barracks can construct Units for your army! the most basic is the soldier which costs 10"+
	" resources and can attack anything around it. The next is the Basic Ranged unit which costs 30 Resources and has a" +
	" larger range can be built by pressing r. Finally there is the tank that costs 50 Resources and has a large range and"+
	" armor to protect it which can be built with t." +
	" Your goal is to build an army and destroy the enemy, Good Luck!");
	
	
	public void addPlayers(int num){
		gs.addHumanPlayer(1,gs);
		for (int index = 2; index < num +2; index ++){
			gs.addAIPlayer(index,gs);
		}
	}

	public void addAiPlayers(int num){
		for (int index = 0; index < num; index ++){
			gs.addAIPlayer(index+3,gs);

		}
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public void start(Stage stage){
		imSize = 1000/gs.getMap().getSize();
		stage.setX(100);
		stage.setY(0);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				timeline.stop();
				aiTurn.stop();
				s2.close();
				s1.close();
			}
		});    
		
		
		Timeline timeline2 = new Timeline();
		timeline2.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				int pNum = gs.getPlayers().size();
				//timeline2.stop();
				timeline2.getKeyFrames().clear();
				for (int index = 0; index < pNum; index++){
					int num = gs.getPlayers().get(index).getNum();
					for(int index1 = 0; index1 < gs.getPlayers().get(index).getUnitList().size();index1 ++){
						if (imstorage.get(num+3).size() > index1){
							imstorage.get(num+3).get(index1).setX(imSize*gs.getPlayers().get(index).getUnitList().get(index1).getX());
							imstorage.get(num+3).get(index1).setY(imSize*gs.getPlayers().get(index).getUnitList().get(index1).getY());
						}
					}
				}
				//timeline2.play();
			}
		});
		
		addPlayers(1);
		Group root = new Group();
		VBox men = new VBox(5);
		BorderPane highScoreMenu = new BorderPane();
		VBox rest = new VBox(10);
		VBox paus = new VBox(10);
		VBox introL = new VBox(10);
		HBox buttons = new HBox(10);
		HBox themeSel = new HBox(10);
		Scene scene = new Scene(root, 1100, 1000,Color.BLACK);
		Scene restart = new Scene(rest,200,200);
		Scene pause = new Scene(paus,200,200);
		Scene intro = new Scene(introL,400,300);
		Scene menu = new Scene(men,400,400);
		Scene hsmenu = new Scene(highScoreMenu, 400, 300);
		stage.setTitle("StarCraft III");
		
		Text didWin = new Text();
		
		paus.getChildren().add(pauseT);
		paus.setAlignment(Pos.TOP_CENTER);

		
		Button resume = new Button();
		resume.setText("Resume");
		resume.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				timeline.play();
				aiTurn.play();
				s2.close();
			}
		});
		
		paus.getChildren().add(resume);
		
		rest.setAlignment(Pos.TOP_CENTER);
		
		Button play = new Button();
		play.setText("Lets Play!");
		play.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				timeline.play();
				aiTurn.play();
				s2.close();
			}
		});
		
		i.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				timeline.play();
				aiTurn.play();
			}
		});        
				
		
		introL.setAlignment(Pos.TOP_CENTER);
		
		introT.setWrapText(true);
		introL.getChildren().add(introT);
		introL.getChildren().add(play);
		
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
				stage.show();
				s1.close();
			}
		});
		
		
		quit.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				stage.close();
				s1.close();
			}
		});
		
		
		
		
		Label buttonExp = new Label("How Many Opponents Would You Like To Face?");
		Label themeExp = new Label("Please Select A Theme");
		Text numOfAi = new Text();
		Text curTheme = new Text();
		Text highScores = new Text();
		Text highScoreList = new Text();
		curTheme.setText("Theme: Moon");
		numOfAi.setText("You Have 1 Opponent");
		highScores.setText("High Scores");
		
		Button moon = new Button();
		Button plain = new Button();
		
		moon.setText("Moon");
		plain.setText("Plains");
		
		moon.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				theme = 0;
				theme2 = "Moon";
				curTheme.setText("Theme: Moon");
			}
		});
		plain.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				theme = 1;
				theme2 = "Plains";
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
				TextInputDialog dialog = new TextInputDialog();
				dialog.setHeaderText("Please enter your name:");
				name = dialog.showAndWait();
				if (name.isPresent()) {
					hs.setName(name.get());
				} else {
				}
				System.out.println(hs.getName());
				gs = new GameState();

				for (int index = 0; index < imstorage.size(); index++){
					imstorage.get(index).clear();
				}
				state = new State(imSize,theme2);
				root.getChildren().clear();
				root.getChildren().add(res);
				root.getChildren().add(keyOne);
				root.getChildren().add(keyTwo);
				root.getChildren().add(keyThree);
				addPlayers(numAI);
				display(root);
				buildingimAdd2(root);
				stage.setScene(scene);

				index.setScene(intro);
				index.show();

			}
		});

		Label title = new Label("RTS is Best");

		Button highScoreButton = new Button();
		highScoreButton.setText("High Scores");
		Button back = new Button();
		back.setText("Back");
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.setScene(menu);
				stage.show();
				r.close();

			}
		});
		highScoreButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				highScoreList.setText(hs.highScoreList());
				highScoreMenu.setCenter(highScoreList);
				Text hsTitle = new Text("High Scores");
				highScoreMenu.setTop(hsTitle);
				hsTitle.setStyle("-fx-font: 24 arial;");
				highScoreMenu.setAlignment(hsTitle, Pos.CENTER);
				stage.setScene(hsmenu);
				back.setVisible(true);
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
		
		
		men.getChildren().add(title);
		men.getChildren().add(buttonExp);
		men.getChildren().add(buttons);
		men.getChildren().add(numOfAi);
		men.getChildren().add(themeExp);
		men.getChildren().add(themeSel);
		men.getChildren().add(curTheme);
		men.getChildren().add(start);
		men.getChildren().add(highScores);
		men.getChildren().add(highScoreButton);
		highScoreMenu.setBottom(back);
		back.setVisible(false);
		highScoreMenu.setAlignment(back, Pos.CENTER);
		
		
		
		
		
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
			public void handle(MouseEvent mouseE){
				//System.out.println("cool");
				int x = (int)(mouseE.getX()/imSize);
				int y = (int)(mouseE.getY()/ imSize);
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
					Player player = gs.getPlayers().get(0);
					player.findSelectables();
					for (int index = 0;index<player.getSelectables().size();index++){
						if (player.getSelectables().get(index).getX() == x &&player.getSelectables().get(index).getY() == y){
							charac = player.getSelectables().get(index);
							cursor.setX(c.getX()*imSize+1);
							cursor.setY(c.getY()*imSize+1);
							if (charac instanceof Worker){
								keyOne.setText("C: Construct Barracks");
								keyTwo.setText("G: Gather Resources");
								keyThree.setText("");
							} else if (charac instanceof MainBase){
								keyOne.setText("B: Build Worker");
								keyTwo.setText("");
								keyThree.setText("");
							}else if (charac instanceof Barracks){
								keyOne.setText("B: Build Soldier");
								keyTwo.setText("B: Build Tank");
								keyThree.setText("B: Build Ranged Fighter");
							}else if (charac instanceof Soldier){
								keyOne.setText("Click To Attack");
								keyTwo.setText("");
								keyThree.setText("");
							
							}else if (charac instanceof Tank){
								keyOne.setText("Click To Attack");
								keyTwo.setText("");
								keyThree.setText("");
							
							}else if (charac instanceof RangedFighter){
								keyOne.setText("Click To Attack");
								keyTwo.setText("");
								keyThree.setText("");
							}
								
							MouseState = 1;
							break;
						}
					}
				}else if(MouseState == 1 && charac instanceof Unit){
					if (gs.getMap().getBoard()[y][x].equals("---")){
						charac.getMyQueues().clear();
						charac.getMyQueues().add(new MoveQueue("move",charac,x,y));
					}else if(gs.getMap().getBoard()[y][x].charAt(0) != '1'){
						c.getMyQueues().clear();
						gs.getPlayers().get(0).findAttackSelection(charac,gs);
						for (int index = 0;index < gs.getPlayers().get(0).getAttackSelectable().size();index++){
							Character charac1 = gs.getPlayers().get(0).getAttackSelectable().get(i);
							if (charac1.getX() == x && charac1.getY() == y){
								charac.getMyQueues().add(new AttackQueue("attack",charac,charac1));
								break;
							}
						}
					}
				}else if(MouseState == 2){
					if (gs.getMap().getBoard()[y][x].equals("---")){
						charac.getMyQueues().clear();
						charac.getMyQueues().add(new BuildQueue("build",charac,x,y));
						gs.getPlayers().get(0).decRes(50);
						MouseState = 1;
					}
				}
			}
		};
		
		
		EventHandler<KeyEvent> kb = new EventHandler<KeyEvent>() { 
			@Override 
			public void handle(KeyEvent keyE) { 
				if (MouseState == 1){
					if (charac instanceof MainBase && keyE.getText().equals("b") && enoughRes("worker",0)){
						charac.getMyQueues().add(new ConstructQueue("construct",charac,"worker",2));
						gs.getPlayers().get(0).decRes(10);
					}else if(charac instanceof Worker && keyE.getText().equals("g")){
						charac.getMyQueues().clear();
						charac.getMyQueues().add(new CollectionQueue("collect",charac));
					}else if(charac instanceof Worker && keyE.getText().equals("c") &&enoughRes("barracks",0) ){
						MouseState = 2;
					}else if (charac instanceof Barracks && keyE.getText().equals("b") && enoughRes("soldier",0)){
						charac.getMyQueues().add(new ConstructQueue("construct",charac,"soldier",3));
						gs.getPlayers().get(0).decRes(20);
					}else if (charac instanceof Barracks && keyE.getText().equals("b") && enoughRes("tank",0)){
						charac.getMyQueues().add(new ConstructQueue("construct",charac,"tank",3));
						gs.getPlayers().get(0).decRes(50);
					}else if (charac instanceof Barracks && keyE.getText().equals("b") && enoughRes("rangedFighter",0)){
						charac.getMyQueues().add(new ConstructQueue("construct",charac,"rangedFighter",3));
						gs.getPlayers().get(0).decRes(30);
						
					}
				}
				if (keyE.getText().equals("p")){
					timeline.stop();
					aiTurn.stop();
					s2.setScene(pause);
					s2.show(); 
				}
			}
				
		};     
		
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(speed),ae -> up(root,timeline,
		stage,restart,aiTurn,didWin,timeline2)));
		aiTurn.getKeyFrames().add(new KeyFrame(Duration.seconds(1),ae -> aiT()));
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
		for (int index = 1;index < gs.getPlayers().size();index++){
			if (gs.getPlayers().get(index).getBuildingList().size() >= 1){
				gs.getPlayers().get(index).getChoice(gs);
			}
		}
	}
		
	
	public void up(Group root,Timeline timeline,Stage stage,Scene restart,Timeline aiTurn,Text didWin,Timeline timeline2){
		int a = 0;
		int b = 0;
		int dead = 0;
		hs.updateScore(10);
		
		//ImageView iV2 = new ImageView();
		
		for (int index = 0; index < gs.getPlayers().size();index++){
			
			gs.getPlayers().get(index).updateQueues(gs);
			checkHealth(root,index);
		
		}
		unitimAdd2(root);
		buildingimAdd2(root);
		
		//System.out.println(c.getMyQueues());
		
		//System.out.println("you have " + gs.getPlayers().get(0).getResources());
		//gs.displayBoard();
		updateUnitLocations(gs,timeline2);
		gs.checkBase();

		for (int index = 0; index < gs.getPlayers().size(); index ++){
			if (gs.getPlayers().get(index).getBuildingList().size() == 0){
				dead ++;

			}
		}
		if (dead >= numAI) {
			if (gs.getPlayers().get(0).getBuildingList().size() >= 1) {
				System.out.println("you won!");
				didWin.setText("you won!");
				hs.writeHighScore(hs.getName(), hs.getScore() + 1000);
			} else {
				System.out.println("you lost :(");
				didWin.setText("you lost :(");
				hs.writeHighScore(hs.getName(), hs.getScore());
			}
			timeline.stop();
			timeline2.stop();
			aiTurn.stop();

			s1.setScene(restart); 
			s1.show();
			


		}
		res.setText("Minerals: " + gs.getPlayers().get(0).getResources());

	}

	

	private void unitimAdd2(Group root){
		for(int i = 0; i < gs.getPlayers().size(); i++){
			for (int j = 0; j < gs.getPlayers().get(i).getUnitList().size();j++){
				if (! root.getChildren().contains(gs.getPlayers().get(i).getUnitList().get(j).getImageView())){
					root.getChildren().add(gs.getPlayers().get(i).getUnitList().get(j).getImageView());
				}
			}
		}
	}
	
	private void buildingimAdd2(Group root){
		for(int i = 0; i < gs.getPlayers().size(); i++){
			for (int j = 0; j < gs.getPlayers().get(i).getBuildingList().size();j++){
				if (! root.getChildren().contains(gs.getPlayers().get(i).getBuildingList().get(j).getImageView())){
					root.getChildren().add(gs.getPlayers().get(i).getBuildingList().get(j).getImageView());
				}
			}
      
	public void unitimAdd(int num1,Group root,int num2){ 

		String[][] uRef = { {"Images/MoonWorker1.png","Images/MoonWorker2.png","Images/MoonWorker3.png",
		"Images/MoonWorker4.png","Images/MoonSoldier1.png","Images/MoonSoldier2.png","Images/MoonSoldier3.png",
		"Images/MoonSoldier4.png", "Images/MoonTank1.png", "Images/MoonTank2.png", "Images/MoonTank3.png", "Images/MoonTank4.png", 
		"Images/MoonRanged1.png", "Images/MoonRanged2.png", "MoonRanged3.png", "MoonRanged4.png" },
		{"Images/PlainsWorker1.png","Images/PlainsWorker2.png","Images/PlainsWorker3.png",
		"Images/PlainsWorker4.png","Images/PlainsSoldier1.png","Images/PlainsSoldier2.png","Images/PlainsSoldier3.png",
		"Images/PlainsSoldier4.png", "Images/PlainsTank1.png", "Images/PlainsTank2.png", "Images/PlainsTank3.png", "PlainsTank4.png",
		"Images/PlainsRanged1.png", "Images/PlainsRanged2.png", "Images/PlainsRanged3.png", "Images/PlainsRanged4.png" }};
		ImageView iV2 = new ImageView();
		Image i2;
		//System.out.println(i);
		//System.out.println(j);
		boolean valid = false;
		if (gs.getPlayers().get(num2).getUnitList().get(num) instanceof Worker){
			i2 = new Image(uRef[theme][num2],true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(num2).getUnitList().get(num) instanceof Soldier){
			i2 = new Image(uRef[theme][num2+4],true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(num2).getUnitList().get(num) instanceof Tank){
			i2 = new Image(uRef[theme][num2+8],true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(num2).getUnitList().get(num) instanceof RangedFighter){
			i2 = new Image(uRef[theme][num2+12],true);
			iV2.setImage(i2);
			valid = true;
		}
		
		if (valid){
			int k = gs.getPlayers().get(num2).getUnitList().get(num).getX();
			int l = gs.getPlayers().get(num2).getUnitList().get(num).getY();
			iV2.setX(k*imSize+1);
			iV2.setY(l*imSize+1);
			iV2.setFitHeight(imSize -2);
			iV2.setFitWidth(imSize -2);
			root.getChildren().add(iV2);
			//System.out.println(j+2);
			imstorage.get(num2+4).add(iV2);
		}
	}
	
	public void buildingImAdd(int num,Group root,int num2){
		String[][] uRef = {{"Images/MoonBarracks1.jpg","Images/MoonBarracks2.jpg","Images/MoonBarracks3.jpg",
		"Images/MoonBarracks4.jpg","Images/MoonBase1.jpg","Images/MoonBase2.jpg","Images/MoonBase3.jpg",
		"Images/MoonBase4.jpg"},{"Images/PlainsBarracks1.jpg","Images/PlainsBarracks2.jpg","Images/PlainsBarracks3.jpg",
		"Images/PlainsBarracks4.jpg","Images/PlainsBase1.jpg","Images/PlainsBase2.jpg","Images/PlainsBase3.jpg",
		"Images/PlainsBase4.jpg"}};
		Image i2;
		ImageView iV2 = new ImageView();
		boolean valid = false;
		
		if (gs.getPlayers().get(num2).getBuildingList().get(num) instanceof Barracks){
			i2 = new Image(uRef[theme][num2],true);
			iV2.setImage(i2);
			valid = true;
		} else if (gs.getPlayers().get(num2).getBuildingList().get(num) instanceof MainBase){
			i2 = new Image(uRef[theme][num2+4],true);
			iV2.setImage(i2);
			valid = true;
		}
		if (valid){
			int k = gs.getPlayers().get(num2).getBuildingList().get(num).getX();
			int l = gs.getPlayers().get(num2).getBuildingList().get(num).getY();
			iV2.setX(k*imSize+1);
			iV2.setY(l*imSize+1);
			iV2.setFitHeight(imSize-2);
			iV2.setFitWidth(imSize-2);
			root.getChildren().add(iV2);
			imstorage.get(num2).add(iV2);

		}
	}
	
	public void checkHealth(Group root,int num){
		
		
			for (int index = 0; index < gs.getPlayers().get(num).getBuildingList().size(); index ++){
				//System.out.println(j);
				if (gs.getPlayers().get(num).getBuildingList().get(index).getHealth() <= 0){
					
					root.getChildren().remove(gs.getPlayers().get(num).getBuildingList().get(index).getImageView());
					gs.getPlayers().get(num).getBuildingList().remove(index);
					
					
				}
			} 
			for (int index = 0; index < gs.getPlayers().get(num).getUnitList().size(); index ++){
				//System.out.println(j + "ch" + i);
				if (gs.getPlayers().get(num).getUnitList().get(index).getHealth() <= 0){
					//System.out.println(j + "ch" + i);
					
					root.getChildren().remove(gs.getPlayers().get(num).getUnitList().get(index).getImageView());
					gs.getPlayers().get(num).getUnitList().remove(index);
					
					
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

			for (int index = 0; index < pn; index++){
				int n = gs.getPlayers().get(index).getNum();				
				for(int index1 = 0; index1 < gs.getPlayers().get(index).getUnitList().size();index1 ++){
					
						final KeyValue keyValue1 = new KeyValue(gs.getPlayers().get(index).getUnitList().get(index1).getImageView().xProperty(),imSize*gs.getPlayers().get(index).getUnitList().get(index1).getX());
						final KeyValue keyValue2 = new KeyValue(gs.getPlayers().get(index).getUnitList().get(index1).getImageView().yProperty(),imSize*gs.getPlayers().get(index).getUnitList().get(index1).getY());
						KeyFrame keyFrame = new KeyFrame(Duration.seconds(speed), keyValue1, keyValue2);
						timeline.getKeyFrames().add(keyFrame);
					
				}
			}
			timeline.play();
		}
	}
	
	public boolean enoughRes(String string,int num) { 
		boolean res = false;
		if (string.equals("worker") && gs.getPlayers().get(num).getResources() >= 10){
			res = true;
		}else if (string.equals("barracks") && gs.getPlayers().get(num).getResources() >= 50){
			res = true;
		}else if (string.equals("soldier") && gs.getPlayers().get(num).getResources() >= 20){
			res = true;
		}else if (string.equals("tank") && gs.getPlayers().get(num).getResources() >= 50) {
			res = true;
		}else if (string.equals("rangedFighter") && gs.getPlayers().get(num).getResources() >= 30) {
			res = true;
		}
		return res;
	}
	
	public void display(Group root){
	
		String[][] base = {{"Images/MoonBase1.jpg","Images/MoonBase2.jpg","Images/MoonBase3.jpg","Images/MoonBase4.jpg"}
		,{"Images/PlainsBase1.jpg","Images/PlainsBase2.jpg","Images/PlainsBase3.jpg","Images/PlainsBase4.jpg"}};
		String[] space = {"Images/MoonSpace.jpg","Images/PlainsSpace.jpg"};
		gs.updateBoard();
		
		String[][] map = gs.getMap().getBoard();
		int s = gs.getMap().getSize();
		
		
		for (int index = 0;index<s;index++){
			for (int index1 = 0;index1<s;index1++){
				ImageView iV = new ImageView();
				Image i1 = new Image(space[theme],true);
				iV.setImage(i1);
				iV.setX(index1*imSize+1);
				iV.setY(index*imSize+1);
				iV.setFitHeight(imSize-2);
				iV.setFitWidth(imSize-2);
				root.getChildren().add(iV);
				
			}
		}

	}
}
