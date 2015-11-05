import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MoveGenerator {
	
	ArrayList<HashMap<Point, Integer>> moves = new ArrayList<HashMap<Point, Integer>>();
	int move_value = 0;
	HashMap<Point, Integer> inputBoard = new HashMap<Point, Integer>();
	Move next_move;
	int color_value;
	HashMap<HashMap<Point, Integer>, Integer> list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
	
	public MoveGenerator(HashMap<Point, Integer> testBoard, int color_value){
		this.color_value = color_value;
		
		for (Map.Entry<Point, Integer> entry : testBoard.entrySet()) {
			if(entry.getValue()==color_value || entry.getValue()==color_value+10){
				Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
				generate_move(testBoard, point_to_move, false); 
			}
		}
		
		remove_illegals();
			
	}

	void generate_move(HashMap<Point, Integer> testBoard, Point point_to_move, boolean second_move){

		
		next_move = new Move(testBoard, color_value, point_to_move, second_move);

		boolean[] move_direction = {next_move.left, next_move.right, next_move.back_left, next_move.back_right};
		boolean[] move_direction_capture = {next_move.left_capture, next_move.right_capture, next_move.back_left_capture, next_move.back_right_capture};
		Point[] point_moved_to = {next_move.point_moved_to_left, next_move.point_moved_to_right, next_move.point_moved_to_back_left, next_move.point_moved_to_back_right};
		HashMap[] configuration = {next_move.configuration_left, next_move.configuration_right, next_move.configuration_back_left, next_move.configuration_back_right};
		
		for(int i=0; i<4; i++){
			if(move_direction_capture[i]  && (configuration[i].containsValue(1) || configuration[i].containsValue(11)) && (configuration[i].containsValue(-1) || configuration[i].containsValue(9))){ // && next_move.move_value>0){
				move_value++;
				testBoard =new HashMap<Point, Integer>();
				testBoard.putAll(configuration[i]);
				generate_move(testBoard, point_moved_to[i], true);
			}
			if(move_direction_capture[i]){ 
				move_value++;
				list_of_moves.put(configuration[i], move_value);
				move_value = 0;
			}
			else if(move_direction[i] && !second_move && (configuration[i].containsValue(1) || configuration[i].containsValue(11)) && (configuration[i].containsValue(-1) || configuration[i].containsValue(9))){ //next_move.move_value == 0 && ){
				list_of_moves.put(configuration[i], 0);
			}
		}
		second_move = false;
	}
	
	void remove_illegals(){
		int max = 0;
		if(list_of_moves.size()>0){
			max = Collections.max(list_of_moves.values());
		}
		
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
			if(entry.getValue() == Collections.max(list_of_moves.values())){
				moves.add(0, entry.getKey());
			}
		}
		
		
/*		
		int i = 0;
		while(max>i){
			list_of_moves.values().removeAll(Collections.singleton(i));
			i++;
		}
		
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
			if(entry.getValue() == Collections.max(list_of_moves.values())){
				moves.add(0, entry.getKey());
			}
				
			else{
				moves.add(entry.getKey());
			}
		}*/
	}
	
}
