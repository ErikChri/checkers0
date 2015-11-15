import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


public class Evaluation {

	int sum = 0;
	int color_value;

	public Evaluation(HashMap<Point, Integer> inputBoard){
		color_value = 1;
		for (Map.Entry<Point, Integer> entry : inputBoard.entrySet()) {
			if(entry.getValue()==color_value){
				sum++; //add 1 for computers pieces
			}
			else if(entry.getValue() == color_value+10){
				sum+=15; //add 15 for computers kings
			}else if(entry.getValue() == -1*color_value){
				sum--; //subtract for opponents piece
			}
			else if(entry.getValue() == -1*color_value+10){
				sum-=15; //subtract for opponents kings
			}
		}
		if(!(inputBoard.containsValue(color_value) || inputBoard.containsValue(color_value+10))){
			sum = -100000; //lost board
		}
		else if(!(inputBoard.containsValue(-1*color_value) || inputBoard.containsValue(-1*color_value+10))){
			sum = 100000; //won board
		}
	}
}
