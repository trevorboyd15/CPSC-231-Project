import javafx.scene.*;
import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.paint.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.event.*;
import javafx.util.Duration;
import java.util.*;

public class GraphicsGame extends Application{
	private GameState gs = new GameState();
	private Character c;
	
	private int MouseState = 0;
	private int selector = 0;
	
	private List<ImageView> p1unit = new ArrayList<ImageView>();
	private List<ImageView> p2unit = new ArrayList<ImageView>();
	private List<ImageView> p1build = new ArrayList<ImageView>();
	private List<ImageView> p2build = new ArrayList<ImageView>();
	private List<List<ImageView>> imstorage = new ArrayList<List<ImageView>>(4);
	
	
	public void addTwoPlayers(){
		gs.addHumanPlayer(1);
		gs.addHumanPlayer(2);
	}
	
	public static void main(String[] args){
		launch();
		
	}
	public void start(Stage stage){
	
		
		
		addTwoPlayers();
		Group root = new Group();
		Scene scene = new Scene(root, 1000, 1000,Color.BLACK);
		stage.setTitle("StarCraft III");
		final Canvas canvas = new Canvas(1000,1000);
		root.getChildren().add(canvas);
		
		
		imstorage.add( p1build);	
		imstorage.add( p2build);
		imstorage.add( p1unit);	
		imstorage.add( p2unit);	
		
		display(root);
		
		
		EventHandler<MouseEvent> mhand = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				System.out.println("cool");
				int x = (int)(e.getX()/100);
				int y = (int)(e.getY()/ 100.0);
				
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
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1),ae -> up(root,timeline)));
		
		
		
		
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		root.addEventFilter(MouseEvent.MOUSE_CLICKED, mhand);
		scene.addEventFilter(KeyEvent.KEY_PRESSED, kb);
		
		
		stage.setScene(scene);
		stage.show();
	}

	
	public void up(Group root,Timeline timeline){
		int a = 0;
		int b = 0;
		
		//ImageView iV2 = new ImageView();
		
		for (int index = 0; index < gs.getPlayers().size();index++){
			gs.getPlayers().get(index).updateQueues(gs);
		}
		
		checkHealth(root);
		
		if (gs.getPlayers().get(0).getUnitList().size() > imstorage.get(2).size()){
			a = gs.getPlayers().get(0).getUnitList().size();
			b = imstorage.get(2).size();
			for(int i = a-1; i >= b; i--){
				unitimAdd(i,root);
				//System.out.println(b);
			}
		}
		
		if (gs.getPlayers().get(0).getBuildingList().size() > imstorage.get(0).size()){
			a = gs.getPlayers().get(0).getBuildingList().size();
			b = imstorage.get(0).size();
			for(int i = a-1; i >= b; i--){
				buildingImAdd(i,root);
				//System.out.println(b);
			}
		} 
		
		
		//System.out.println(c.getMyQueues());
		updateUnitLocations(gs);
		//System.out.println("you have " + gs.getPlayers().get(0).getResources());
		//gs.displayBoard();
		gs.checkBase();
		if (gs.wonGame()){
			if (gs.getPlayers().get(0).getNum() == 1){
				System.out.println("you won!");
			} else {
				System.out.println("you lost :(");
			}
			timeline.stop();
		}
	
	}
	
	public void unitimAdd(int i,Group root){
		ImageView iV2 = new ImageView();
		Image i2;
		boolean valid = false;
		
		if (gs.getPlayers().get(0).getUnitList().get(i) instanceof Worker){
			i2 = new Image("MoonWorker1.png",true);
			iV2.setImage(i2);
			valid = true;
			
		} else if (gs.getPlayers().get(0).getUnitList().get(i) instanceof Soldier){
			i2 = new Image("MoonSoldier1.png",true);
			iV2.setImage(i2);
			valid = true;
		}	
		if (valid){
			int j = gs.getPlayers().get(0).getUnitList().get(i).getX();
			int l = gs.getPlayers().get(0).getUnitList().get(i).getY();
			iV2.setX(j*100+1);
			iV2.setY(l*100+1);
			iV2.setFitHeight(98);
			iV2.setFitWidth(98);
			root.getChildren().add(iV2);
			imstorage.get(2).add(iV2);
		}
	}
	
	public void buildingImAdd(int i,Group root){
		Image i2;
		ImageView iV2 = new ImageView();
		boolean valid = false;
		
		if (gs.getPlayers().get(0).getBuildingList().get(i) instanceof Barracks){
			i2 = new Image("MoonBarracks1.jpg",true);
			iV2.setImage(i2);
			valid = true;
		} 
		if (valid){
			int j = gs.getPlayers().get(0).getBuildingList().get(i).getX();
			int l = gs.getPlayers().get(0).getBuildingList().get(i).getY();
			iV2.setX(j*100+1);
			iV2.setY(l*100+1);
			iV2.setFitHeight(98);
			iV2.setFitWidth(98);
			root.getChildren().add(iV2);
			imstorage.get(0).add(iV2);
		}
	}
	
	public void checkHealth(Group root){
		
		for (int i = 0; i < gs.getPlayers().size(); i++ ){
			for (int j = 0; j < gs.getPlayers().get(i).getBuildingList().size(); j ++){
				System.out.println(j);
				if (gs.getPlayers().get(i).getBuildingList().get(j).getHealth() <= 0){
					gs.getPlayers().get(i).getBuildingList().remove(j);
					root.getChildren().remove(imstorage.get(i).get(j));
					imstorage.get(i).remove(j);
					
				}
			}
		}
	}		
	
	
	public void updateUnitLocations(GameState gs){
		int pn = gs.getPlayers().size();
		for (int i = 0; i < pn; i++){
			for(int j = 0; j < gs.getPlayers().get(i).getUnitList().size();j ++){
				imstorage.get(i+2).get(j).setX(100*gs.getPlayers().get(i).getUnitList().get(j).getX());
				imstorage.get(i+2).get(j).setY(100*gs.getPlayers().get(i).getUnitList().get(j).getY());
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
		
		
		for (int i = 0;i<10;i++){
			for (int j = 0;j<10;j++){
				ImageView iV = new ImageView();
				Image i1 = new Image("MoonSpace.jpg",true);
				iV.setImage(i1);
				iV.setX(j*100+1);
				iV.setY(i*100+1);
				iV.setFitHeight(98);
				iV.setFitWidth(98);
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
					iV2.setX(j*100+1);
					iV2.setY(i*100+1);
					iV2.setFitHeight(98);
					iV2.setFitWidth(98);
					root.getChildren().add(iV2);
					imstorage.get(selector).add(iV2);
				}
			}
		}
		
	
	
	}
}