import javafx.scene.*;
import javafx.application.Application;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import java.util.*;

public class GraphicsGame extends Application{
	private GameState gs = new GameState();
	
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
		Scene scene = new Scene(root, 1150, 1000);
		stage.setTitle("StarCraft III");
		final Canvas canvas = new Canvas(1150,1000);
		root.getChildren().add(canvas);
		gs.updateBoard();
		
		String[][] map = gs.getMap().getBoard();
		
		for (int i = 0;i<10;i++){
			for (int j = 0;j<10;j++){
				ImageView iV = new ImageView();
				Image i1 = new Image("MoonSpace.jpg",true);
				iV.setImage(i1);
				iV.setX(j*100);
				iV.setY(i*100);
				iV.setFitHeight(100);
				iV.setFitWidth(100);
				root.getChildren().add(iV);
				

				if (map[i][j]!="---"){
					ImageView iV2 = new ImageView();
					String im = "MoonBarricade.jpg";
					if (map[i][j].equals("1mb")){
						im = "MoonBase1.jpg";
					} else if (map[i][j].equals("2mb")){
						im = "MoonBase2.jpg";
					} else if (map[i][j].equals("1bk")){
						im = "MoonBarracks1.jpg";
					} else if (map[i][j].equals("2bk")){
						im = "MoonBarracks2.jpg";
					} else if (map[i][j].equals("1sd")){
						im = "MoonSoldier1.png";
					} else if (map[i][j].equals("2sd")){
						im = "MoonSoldier2.png";
					} else if (map[i][j].equals("1wk")){
						im = "MoonWorker1.png";
					} else if (map[i][j].equals("2wk")){
						im = "MoonWorker2.png";
					}
					Image i2 = new Image(im,true);
					iV2.setImage(i2);
					iV2.setX(j*100);
					iV2.setY(i*100);
					iV2.setFitHeight(100);
					iV2.setFitWidth(100);
					root.getChildren().add(iV2);
				}
			}
		}
		stage.setScene(scene);
		stage.show();
	}

}