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

public class GraphicsGame extends Application{
	private GameState gs = new GameState();
	private Character c;
	
	private Text res = new Text();
	private int numAI = 1;
	
	private int MouseState = 0;
	private int selector = 0;
	private double speed = 1.0;
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
		
		addPlayers(1);
		Group root = new Group();
		VBox men = new VBox(5);
		HBox buttons = new HBox();
		Scene scene = new Scene(root, 1100, 1000,Color.BLACK);
		Scene menue = new Scene(men,800,200);
		stage.setTitle("StarCraft III");
		
		Label buttonExp = new Label("How Many Opponents Would You Like To Face?");
		Text numOfAi = new Text();
		numOfAi.setText("You Have 1 Opponent");
		
		Button one = new Button();
		Button two = new Button();
		Button three = new Button();
		
		one.setText("One");
		two.setText("two");
		three.setText("three");
		
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().add(one);
		buttons.getChildren().add(two);
		buttons.getChildren().add(three);
		
		Button start = new Button();
		start.setText("START");
		
		start.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
				stage.setScene(scene);
				addAiPlayers(numAI-1);
				timeline.play();
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
		men.getChildren().add(start);
		
		
		res.setText("Minerals: " +gs.getPlayers().get(0).getResources());
		res.setX(1005);
		res.setY(50);
		res.setFill(Color.WHITE);
		res.setFont(Font.font ("Verdana", 20));
		res.setWrappingWidth(100.0);
		res.setTextAlignment(TextAlignment.CENTER);
		
		imstorage.add( p1build);	
		imstorage.add( p2build);
		imstorage.add( p3build);
		imstorage.add( p4build);
		imstorage.add( p1unit);	
		imstorage.add( p2unit);	
		imstorage.add( p3unit);	
		imstorage.add( p4unit);	
			
		
		display(root);
		
		
		EventHandler<MouseEvent> mhand = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				//System.out.println("cool");
				int x = (int)(e.getX()/imSize);
				int y = (int)(e.getY()/ imSize);
				
				if (gs.getMap().getBoard()[y][x].charAt(0) == '1'){
					Player p = gs.getPlayers().get(0);
					p.findSelectables();
					for (int index = 0;index<p.getSelectables().size();index++){
						if (p.getSelectables().get(index).getX() == x &&p.getSelectables().get(index).getY() == y){
							c = p.getSelectables().get(index);
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
		
		
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(speed),ae -> up(root,timeline)));
		
		
		
		root.getChildren().add(res);
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		root.addEventFilter(MouseEvent.MOUSE_CLICKED, mhand);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, kb);
		
		
		stage.setScene(menue);
		//stage.setScene(scene);
		stage.show();
	}

	public void up(Group root,Timeline timeline){
		int a = 0;
		int b = 0;
		int dead = 0;
		
		for (int i = 1;i < gs.getPlayers().size();i++){
			if (gs.getPlayers().get(i).getBuildingList().size() >= 1){
				gs.getPlayers().get(i).getChoice(gs);
			}
		}
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
		updateUnitLocations(gs);
		gs.checkBase();
		for (int i = 0; i < gs.getPlayers().size(); i ++){
			if (gs.getPlayers().get(i).getBuildingList().size() == 0){
				dead ++;
			}
		}
		if (dead == numAI){
			if (gs.getPlayers().get(0).getBuildingList().size() >= 1){
				System.out.println("you won!");
			} else {
				System.out.println("you lost :(");
			}
			timeline.stop();
		}
		res.setText("Minerals: " +gs.getPlayers().get(0).getResources());
		
	}
	
	public void unitimAdd(int i,Group root,int j){
		String[] uRef = {"MoonWorker1.png","MoonWorker2.png","MoonWorker3.png","MoonWorker4.png","MoonSoldier1.png","MoonSoldier2.png","MoonSoldier3.png","MoonSoldier4.png"};
		ImageView iV2 = new ImageView();
		Image i2;
		//System.out.println(i);
		//System.out.println(j);
		boolean valid = false;
		if (gs.getPlayers().get(j).getUnitList().get(i) instanceof Worker){
			i2 = new Image(uRef[j],true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(j).getUnitList().get(i) instanceof Soldier){
			i2 = new Image(uRef[j+4],true);
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
		String[] uRef = {"MoonBarracks1.jpg","MoonBarracks2.jpg","MoonBarracks3.jpg","MoonBarracks4.jpg","MoonBase1.jpg","MoonBase2.jpg","MoonBase3.jpg","MoonBase4.jpg"};
		Image i2;
		ImageView iV2 = new ImageView();
		boolean valid = false;
		
		if (gs.getPlayers().get(j).getBuildingList().get(i) instanceof Barracks){
			i2 = new Image(uRef[j],true);
			iV2.setImage(i2);
			valid = true;
		} else if (gs.getPlayers().get(j).getBuildingList().get(i) instanceof MainBase){
			i2 = new Image(uRef[j+4],true);
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
	
	public void updateUnitLocations(GameState gs){
		int pn = gs.getPlayers().size();
		
		if (pn > 1){
		for (int i = 0; i < pn; i++){
			int n = gs.getPlayers().get(i).getNum();
			for(int j = 0; j < gs.getPlayers().get(i).getUnitList().size();j ++){
				//System.out.println(i);
				//System.out.println(j);
				//System.out.println(imstorage.get(i+2).get(j));
				if (imstorage.get(n+3).size() > j){
					imstorage.get(n+3).get(j).setX(imSize*gs.getPlayers().get(i).getUnitList().get(j).getX());
					imstorage.get(n+3).get(j).setY(imSize*gs.getPlayers().get(i).getUnitList().get(j).getY());
				}
			}
		}
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
	
	gs.updateBoard();
		
		String[][] map = gs.getMap().getBoard();
		int s = gs.getMap().getSize();
		
		
		for (int i = 0;i<s;i++){
			for (int j = 0;j<s;j++){
				ImageView iV = new ImageView();
				Image i1 = new Image("MoonSpace.jpg",true);
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
						im = "MoonBase1.jpg";
						selector = 0;
					} else if (map[i][j].equals("2mb")){
						im = "MoonBase2.jpg";
						selector = 1;
					} else if (map[i][j].equals("1bk")){
						im = "MoonBarracks1.jpg";
						selector = 0;
					} else if (map[i][j].equals("2bk")){
						im = "MoonBarracks2.jpg";
						selector = 1;
					} else if (map[i][j].equals("1sd")){
						im = "MoonSoldier1.png";
						selector = 2;
					} else if (map[i][j].equals("2sd")){
						im = "MoonSoldier2.png";
						selector = 3;
					} else if (map[i][j].equals("1wk")){
						im = "MoonWorker1.png";
						selector = 2;
					} else if (map[i][j].equals("2wk")){
						im = "MoonWorker2.png";
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








