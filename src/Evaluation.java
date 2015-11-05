import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


public class Evaluation {

	int sum = 0;
	int color_value, mover;

	public Evaluation(HashMap<Point, Integer> inputBoard, int color_value1, int mover){
		this.color_value = color_value1;
		color_value = 1;
		this.mover = mover;
		
		for (Map.Entry<Point, Integer> entry : inputBoard.entrySet()) {
			if(entry.getValue()==color_value){
				sum++;
			}
			else if(entry.getValue() == color_value+10){
				sum+=15;
			}else if(entry.getValue() == -1*color_value){
				sum--;
			}
			else if(entry.getValue() == -1*color_value+10){
				sum-=15;
			}
		}
		if(!(inputBoard.containsValue(color_value) || inputBoard.containsValue(color_value+10))){
			sum = -100000;
		}
		else if(!(inputBoard.containsValue(-1*color_value) || inputBoard.containsValue(-1*color_value+10))){
			sum = 100000;
		}
	}
}
