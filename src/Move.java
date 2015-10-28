import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Move {

	// Attributes
	Point point_moved_to_left;
	Point point_moved_to_right;
	Point point_moved_to_back_left;
	Point point_moved_to_back_right;
	Point point_to_move;
	Point captured_piece;
	ArrayList<Point> captured_pieces = new ArrayList<Point>();
	int move_value;
	boolean second_move, left, right,  back_left, back_right, is_king, back_left_capture,back_right_capture, left_capture, right_capture;
	int color_value;
	int move_direction; // = -1;
	


	HashMap<Point, Integer> configuration;
	HashMap<Point, Integer> configuration_left; // = new HashMap<Point, Integer>();
	HashMap<Point, Integer> configuration_right;
	HashMap<Point, Integer> configuration_back_left; // = new HashMap<Point, Integer>();
	HashMap<Point, Integer> configuration_back_right;
	HashMap<Point, Integer> configuration_back_up;


	public Move(HashMap<Point, Integer> configuration1, int color_value, Point point_to_move, boolean second_move){
		this.point_to_move = point_to_move; 
		this.color_value = color_value;
		this.move_direction = move_direction;
		this.second_move = second_move;

		configuration_back_up = new HashMap<Point, Integer>();
		configuration_back_up.putAll(configuration1);
		//		System.out.println("poitn to move "+configuration_back_up.get(point_to_move));
		if(configuration_back_up.get(point_to_move) >1 && configuration_back_up.get(point_to_move)<99){
			is_king = true;
//			System.out.println("king 1 ");
		}
		configuration = new HashMap<Point, Integer>();
		configuration.putAll(configuration1);
		for (Map.Entry<Point, Integer> entry : configuration.entrySet()) {
			if(entry.getValue()>1 && entry.getValue()<99){
				entry.setValue(entry.getValue()-10);
			}
		}
		configuration_left = new HashMap<Point, Integer>();
		configuration_left.putAll(configuration);
		configuration_right = new HashMap<Point, Integer>();
		configuration_right.putAll(configuration);
		configuration_back_left = new HashMap<Point, Integer>();
		configuration_back_left.putAll(configuration);
		configuration_back_right = new HashMap<Point, Integer>();
		configuration_back_right.putAll(configuration);

		move(point_to_move);


	}

	void move(Point point_to_move){
		point_moved_to_left = new Point(point_to_move.x-1, point_to_move.y+color_value);  
		point_moved_to_right = new Point(point_to_move.x+1, point_to_move.y+color_value);
		point_moved_to_back_left = new Point(point_to_move.x-1, point_to_move.y-color_value);
		point_moved_to_back_right = new Point(point_to_move.x+1, point_to_move.y-color_value);

		if(configuration_left.containsKey(point_moved_to_left) && configuration_left.get(point_moved_to_left)== 99 && !second_move){
			configuration_left.put(point_to_move, 99);
			configuration_left.put(point_moved_to_left, color_value);
			left = true;
			
		}
		else if(configuration_left.containsKey(point_moved_to_left) && configuration_left.get(point_moved_to_left)==-1*color_value){

			point_moved_to_left = new Point(point_moved_to_left.x-1, point_moved_to_left.y+color_value);
			if(configuration_left.containsKey(point_moved_to_left) && configuration_left.get(point_moved_to_left)==99){
				captured_piece = new Point(point_to_move.x-1, point_to_move.y+color_value);
				captured_pieces.add(captured_piece);
				left_capture = true;
				left = false;
				move_value++;
				configuration_left.put(captured_piece, 99);
				configuration_left.put(point_to_move, 99);
				configuration_left.put(point_moved_to_left, color_value);
//				point_to_move = point_moved_to_left;

				//				System.out.println("restore after "+configuration_left.get(point_moved_to_left)+" point "+point_moved_to_left);
			}

		}
	
		if(configuration_right.containsKey(point_moved_to_right) && configuration_right.get(point_moved_to_right)== 99 && !second_move){   
			configuration_right.put(point_to_move, 99);
			configuration_right.put(point_moved_to_right, color_value);
			if(move_value>0){
				right = false;
			}
			else{
				right = true;  
			}
			
			
		}
		else if(configuration_right.containsKey(point_moved_to_right) && configuration_right.get(point_moved_to_right)==-1*color_value){

			point_moved_to_right = new Point(point_moved_to_right.x+1, point_moved_to_right.y+color_value);
			if(configuration_right.containsKey(point_moved_to_right) && configuration_right.get(point_moved_to_right)==99){
				captured_piece = new Point(point_to_move.x+1, point_to_move.y+color_value);
				captured_pieces.add(captured_piece);
				if(move_value == 0){
					
				}
				left = right = false;
				right_capture = true;
				move_value++;
				configuration_right.put(captured_piece, 99);
				configuration_right.put(point_to_move, 99);
				configuration_right.put(point_moved_to_right, color_value);
//				point_to_move = point_moved_to_right;

			}
			
		}

		if(is_king){
//			System.out.println("king ");
			if(configuration_back_left.containsKey(point_moved_to_back_left) && configuration_back_left.get(point_moved_to_back_left)== 99 && !second_move){
				configuration_back_left.put(point_to_move, 99);
				configuration_back_left.put(point_moved_to_back_left, color_value);
				if(move_value>0){
					back_left = false;
				}
				else{
					back_left = true;
				}
				
			}
			else if(configuration_back_left.containsKey(point_moved_to_back_left) && configuration_back_left.get(point_moved_to_back_left)==-1*color_value){

				point_moved_to_back_left = new Point(point_moved_to_back_left.x-1, point_moved_to_back_left.y-color_value);
				if(configuration_back_left.containsKey(point_moved_to_back_left) && configuration_back_left.get(point_moved_to_back_left)==99){
					captured_piece = new Point(point_to_move.x-1, point_to_move.y-color_value);
					captured_pieces.add(captured_piece);
					if(move_value>0){
						right = left = back_left = false;
					}
					back_left_capture = true;
					move_value++;
					configuration_back_left.put(captured_piece, 99);
					configuration_back_left.put(point_to_move, 99);
					configuration_back_left.put(point_moved_to_back_left, color_value);
//					point_to_move = point_moved_to_back_left;

				}
				
			}
			if(configuration_back_right.containsKey(point_moved_to_back_right) && configuration_back_right.get(point_moved_to_back_right)== 99 && !second_move){
				configuration_back_right.put(point_to_move, 99);
				configuration_back_right.put(point_moved_to_back_right, color_value);
				if(move_value>0){
					back_right = right = left = back_left = false;
				}
				else{
					back_right = true;
				}
				
			}
			else if(configuration_back_right.containsKey(point_moved_to_back_right) && configuration_back_right.get(point_moved_to_back_right)==-1*color_value){

				point_moved_to_back_right = new Point(point_moved_to_back_right.x+1, point_moved_to_back_right.y-color_value);
				if(configuration_back_right.containsKey(point_moved_to_back_right) && configuration_back_right.get(point_moved_to_back_right)==99){
					captured_piece = new Point(point_to_move.x+1, point_to_move.y-color_value);
					captured_pieces.add(captured_piece);
				/*	if(move_value == 0 && !back_left_king){
						right = left = back_left = false;
					}
					else{
						right = left = false;
					}*/
					back_right = right = left = back_left = false;
					back_right_capture = true;
					move_value++;
					configuration_back_right.put(captured_piece, 99);
					configuration_back_right.put(point_to_move, 99);
					configuration_back_right.put(point_moved_to_back_right, color_value);
//					point_to_move = point_moved_to_back_right;

				}
				
			}

		}
		restore_kings_to_board(configuration_left, point_moved_to_left);
		restore_kings_to_board(configuration_right, point_moved_to_right);
		restore_kings_to_board(configuration_back_left, point_moved_to_back_left);
		restore_kings_to_board(configuration_back_right, point_moved_to_back_right);		
	}


	void restore_kings_to_board(HashMap<Point, Integer> configuration_restore, Point moved_to){
		for (Map.Entry<Point, Integer> entry : configuration_back_up.entrySet()) {
			if(entry.getValue()>1 && entry.getValue()<99 && configuration_restore.get(entry.getKey()) != 99 ){     //System.out.println("restore "+entry.getKey()+",  "+ entry.getValue()+", restore "+configuration_restore.get(entry.getKey()));
				configuration_restore.put(entry.getKey(), entry.getValue()); 
				
			}
		}
		if(configuration_restore.containsKey(moved_to) && configuration_restore.get(moved_to) == 1 && moved_to.y == 7){
			configuration_restore.put(moved_to, 1+10);
				}
		if(is_king){
//						System.out.println("restore pre "+configuration_restore.get(moved_to)+" point "+moved_to+",   point to move "+point_to_move);
			configuration_restore.put(moved_to, color_value+10); 
//			System.out.println("restore kings "+configuration_back_right);
		}
		
	}
}
