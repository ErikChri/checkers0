import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class Play {

	JFrame mainFrame = new JFrame("Checkers");
	PlainBoard plainBoard;
	//	Board board;

	//Iterative deepening variables
	
	// Alpha-beta variables
	int depth =0;
	int depthLimit =2;
	int alpha, beta;
	boolean second_move, black, max = true;

	int sizeVar = 5;
	int move_value = 0;
	int capture_value = 0;
	int color_value =1;
	int move_direction = -1;
	Move next_move;
	ArrayList<Move> moves;

	HashMap<Point, Integer> board;
	HashMap<Point, Point> move;
	HashMap<HashMap<Point, Integer>, Integer> list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();

	//Buttons
	JButton new_game = new JButton("New Game");
	JButton add_piece = new JButton("Add Piece");
	JButton start_game = new JButton("Start Game");

	public Play(){
		start_position_board();

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//	mainFrame.setPreferredSize(new Dimension(820,820));
		mainFrame.setMinimumSize(new Dimension(102*sizeVar, 86*sizeVar));
		mainFrame.setLayout(new BorderLayout());
		//		board = new Board();
		plainBoard = new PlainBoard(board);
		plainBoard.setMinimumSize(new Dimension(80*sizeVar,80*sizeVar));
		mainFrame.add(plainBoard, BorderLayout.CENTER);
		mainFrame.setVisible(true);

		addButtons();
		start_position_board();
		alpha_beta();
		
		EvaluateBoard evaluatedBoard = new EvaluateBoard( board) ;
		int evaluate_board = evaluatedBoard.result;
		System.out.println(evaluate_board);
		
	}

	//Min-max and alpha-beta algorithm
	int alpha_beta(){

		
		if(depth == depthLimit){
			//evaluate leaf node boards
			EvaluateBoard evaluatedBoard = new EvaluateBoard( board) ;
			int evaluate_board = evaluatedBoard.result;
		}
		else if(max){  
			// Generate list of moves
			moves = new ArrayList<Move>();
			for (Map.Entry<Point, Integer> entry : board.entrySet()) {
				if(entry.getValue()==color_value || entry.getValue()==color_value+10){
					Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
					generate_move(board, point_to_move);   
				}
			}
			int max =0;
			if(list_of_moves.size()>0){
				max = Collections.max(list_of_moves.values());
				System.out.println("max "+Collections.max(list_of_moves.values()));
			}
			
			if(max>0){
				list_of_moves.values().removeAll(Collections.singleton(0));
				System.out.println("min "+Collections.min(list_of_moves.values()));
			}
//			System.out.println("size "+list_of_moves.size());

			for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
				JFrame mainFrame = new JFrame("Point "+entry.getKey());
				mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				//	mainFrame.setPreferredSize(new Dimension(820,820));
				mainFrame.setMinimumSize(new Dimension(102*sizeVar, 86*sizeVar));
				mainFrame.setLayout(new BorderLayout());
//				System.out.println("entry.getKey()  "+entry.getKey());
				board = entry.getKey();
				
				plainBoard = new PlainBoard(board);
				plainBoard.setMinimumSize(new Dimension(80*sizeVar,80*sizeVar));
				mainFrame.add(plainBoard, BorderLayout.CENTER);
				mainFrame.setVisible(true);

			}
			mainFrame.remove(plainBoard);
			plainBoard = new PlainBoard(board);
			mainFrame.add(plainBoard, BorderLayout.CENTER);
			mainFrame.repaint();
			mainFrame.revalidate();
			return alpha;
		}
		else if(!max){
			// Generate list of moves
			//	generate_list_of_moves(board, color_value);

			return beta;
		}
		return 0;
	}

	void generate_move(HashMap<Point, Integer> testBoard, Point point_to_move){

		next_move = new Move(testBoard, color_value, point_to_move, second_move);
		if(next_move.move_value>0){
			second_move=true;
		}

		
		boolean[] move_direction = {next_move.left, next_move.right, next_move.back_left, next_move.back_right};
		Point[] point_moved_to = {next_move.point_moved_to_left, next_move.point_moved_to_right, next_move.point_moved_to_back_left, next_move.point_moved_to_back_right};
		HashMap[] configuration = {next_move.configuration_left, next_move.configuration_right, next_move.configuration_back_left, next_move.configuration_back_right};
		
		for(int i=0; i<4; i++){
			if(move_direction[i] && next_move.move_value == 0 && !second_move){
				list_of_moves.put(configuration[i], next_move.move_value);
				second_move=false;
			}
			else if(!next_move.left && !next_move.right && !next_move.back_left && !next_move.back_right && second_move){
//				System.out.println("put to list i  "+i+",   "+configuration[i]);
				list_of_moves.put(testBoard, move_value);
				move_value = 0;
				second_move = false;
			}
			else if(move_direction[i] && next_move.move_value>0){
				move_value++;
				second_move=true;
				testBoard =new HashMap<Point, Integer>();
				testBoard.putAll(configuration[i]);
				
//				System.out.println("test board "+testBoard);
				generate_move(testBoard, point_moved_to[i]);
			}
		}
		
		
		
/*		if(next_move.left && next_move.move_value == 0){
			list_of_moves.put(next_move.configuration_left, next_move.move_value);
//			System.out.println("list_of_moves  "+list_of_moves);
			second_move=false;
		}
		else if(!next_move.left && !next_move.right && second_move){
			list_of_moves.put(next_move.configuration_left, move_value);
			move_value = 0;
			second_move = false;
		}
		else if(next_move.left && next_move.move_value>0){
			move_value++;
			testBoard =new HashMap<Point, Integer>();
			testBoard.putAll(next_move.configuration_left);
			generate_move(testBoard, next_move.point_moved_to_left);
		}
		if(next_move.right && next_move.move_value == 0){
			list_of_moves.put(next_move.configuration_right, next_move.move_value);
			second_move=false;
		}
		else if(next_move.right && next_move.move_value>0){
			move_value++;
			testBoard =new HashMap<Point, Integer>();
			testBoard.putAll(next_move.configuration_right);
			//			list_of_moves.put(next_move.configuration_right, next_move.move_value);
			generate_move(testBoard, next_move.point_moved_to_right);
		}
		else if(!next_move.right && !next_move.left && second_move){
			list_of_moves.put(next_move.configuration_right, move_value);
			move_value = 0;
			second_move = false;
		}*/
//		System.out.println("testboard   "+testBoard);

	}

	void start_position_board(){
		board = new HashMap<Point, Integer>();
		int value = 1;
		for(int i=0; i<8; i++){
			if(i<3){
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
			}

			if(i>2 && i<5){
				value = 99;
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
			}
			if(i==3){
				value = -1;

			}
			if(i>4){
				value = -1;
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						board.put(new Point(j,i), value);
					}
				}
			}
		}
		// board.put(new Point(2,2), 1+10);
		// board.put(new Point(1,3), -1);
		//board.put(new Point(0,6), 99);
		//board.put(new Point(2,6), 99);
		//board.put(new Point(3,3), -1);
		//board.put(new Point(1,5), 99);
		
	}

	void addButtons(){

		add_piece.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {

				String s = (String)JOptionPane.showInputDialog(
						null,
						"State color and position (separate by comma)" ,
						"Add a Piece to the Board",
						JOptionPane.PLAIN_MESSAGE,
						null,
						null,
						null);
				System.out.println(s);
			}
		});


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(new_game);
		buttonPanel.add(add_piece);
		buttonPanel.add(start_game);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
	}


	public static void main(String[] args) {
		Play play = new Play();

	}

}
