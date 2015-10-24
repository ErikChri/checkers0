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
import java.util.List;
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

	// Alpha-beta variables
	int depth =0;
	int depthLimit = 3;
	int alpha = -100000;
	int beta = 100000;
	int board_value_next = 0;
	boolean second_move, black;
	boolean max = true;

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
	HashMap<HashMap<Point, Integer>, Integer> list_of_best_moves = new HashMap<HashMap<Point, Integer>,Integer>();

	//Buttons
	JButton new_game = new JButton("New Game");
	JButton add_piece = new JButton("Add Piece");
	JButton start_game = new JButton("Start Game");
	JButton move_on = new JButton("Next Move");

	public Play(){
		start_position_board();

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//	mainFrame.setPreferredSize(new Dimension(820,820));
		mainFrame.setMinimumSize(new Dimension(102*sizeVar, 88*sizeVar));
		mainFrame.setLayout(new BorderLayout());
		//		board = new Board();
		plainBoard = new PlainBoard(board);
		plainBoard.setMinimumSize(new Dimension(80*sizeVar,80*sizeVar));
		mainFrame.add(plainBoard, BorderLayout.CENTER);
		mainFrame.setVisible(true);

		addButtons();
		start_position_board();
		next_move();


	}
	
	void next_move(){
		if(!board.containsValue(-1) && !board.containsValue(9)){
			System.out.println("Black wins the game");
		}
		if(!board.containsValue(1) && !board.containsValue(11)){
			System.out.println("White wins the game");
		}
		list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		list_of_best_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		for (Map.Entry<Point, Integer> entry : board.entrySet()) {
			if(entry.getValue()==color_value || entry.getValue()==color_value+10){
				Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
				generate_move(board, point_to_move);   
			}

		}
		HashMap<HashMap<Point, Integer>, Integer> list_of_potential_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		int max =0;
		if(list_of_moves.size()>0){
			max = Collections.max(list_of_moves.values());
		}

		if(max>0){
			list_of_moves.values().removeAll(Collections.singleton(0));
		}
		if(list_of_moves.size()==0){
			System.out.println("Opponent wins the game");
		}
		System.out.println("list of moves size "+list_of_moves.size());
		list_of_potential_moves.putAll(list_of_moves);
		alpha = -100000;
		beta = 100000;
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_potential_moves.entrySet()) {
//			show(entry.getKey());
			depth = 0;
			list_of_best_moves.put(entry.getKey(), alpha_beta(entry.getKey(), alpha, beta, depth));

		}
		ArrayList<HashMap<Point, Integer>> choose_next_move = new ArrayList<HashMap<Point, Integer>>();
		boolean move_chosen = false;
		System.out.println(move_chosen);
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_best_moves.entrySet()) {
						System.out.println("alpha or beta  "+entry.getValue());
			
			if(entry.getValue() == Collections.max(list_of_best_moves.values())){
				board = entry.getKey();
				move_chosen = true;
			}
			else{
				choose_next_move.add(entry.getKey());
			}
		}
		if(!move_chosen){
			board = choose_next_move.get(0);
		}
		System.out.println(move_chosen);
		show(board);

	}
	
	void show(HashMap<Point, Integer> board){
		mainFrame.remove(plainBoard);
		plainBoard = new PlainBoard(board);
		mainFrame.add(plainBoard, BorderLayout.CENTER);
		
		mainFrame.repaint();
		mainFrame.revalidate();
	}

	//Min-max and alpha-beta algorithm
	int alpha_beta(HashMap<Point, Integer> testBoard, int alpha, int beta, int depth){
		
				depth++;
		System.out.println("depth  "+depth);		
		if(depth%2==0){
			max=true;
		}
		else{
			max = false;
		}
		if(depth == depthLimit){
 
			EvaluateBoard ev = new EvaluateBoard(testBoard, 1);
			int evaluate_board = ev.sum;
			return evaluate_board;
 
		}
		else if(max){  // Maximazing level
			// Generate list of moves
			//			moves = new ArrayList<Move>();
			System.out.println(" maximazing  ");
			list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
			for (Map.Entry<Point, Integer> entry : board.entrySet()) {
				if(entry.getValue()==color_value || entry.getValue()==color_value+10){
					Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
					generate_move(testBoard, point_to_move);   
				}
			}
			int max =0;
			if(list_of_moves.size()>0){
				max = Collections.max(list_of_moves.values());
			}

			if(max>0){
				list_of_moves.values().removeAll(Collections.singleton(0));
			}

			ArrayList<HashMap<Point, Integer>> list_of_next_moves = new ArrayList<HashMap<Point, Integer>>();
			for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
				if(entry.getValue() == Collections.max(list_of_moves.values()))
					list_of_next_moves.add(0, entry.getKey());
				else{
					list_of_next_moves.add(entry.getKey());
				}
			}
			while(alpha<beta && list_of_next_moves.size()>0){
				board_value_next = alpha_beta(list_of_next_moves.get(0), alpha, beta, depth);
				list_of_next_moves.remove(0);
				if(board_value_next >alpha){
					alpha = board_value_next;
				}
			}
			System.out.println(" alpha  "+alpha);
			return alpha;



			/*			for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
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
			 */
		}
		else if(!max){ //Minimizing level
			// Generate list of moves
			moves = new ArrayList<Move>();
			for (Map.Entry<Point, Integer> entry : board.entrySet()) {
				if(entry.getValue()==color_value || entry.getValue()==color_value+10){
					Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
					generate_move(testBoard, point_to_move);   
				}
			}
			int max =0;
			if(list_of_moves.size()>0){
				max = Collections.max(list_of_moves.values());
			}

			if(max>0){
				list_of_moves.values().removeAll(Collections.singleton(0));
			}

			ArrayList<HashMap<Point, Integer>> list_of_next_moves = new ArrayList<HashMap<Point, Integer>>();
			for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
				if(entry.getValue() == Collections.max(list_of_moves.values()))
					list_of_next_moves.add(0, entry.getKey());
				else{
					list_of_next_moves.add(entry.getKey());
				}
			}
			while(alpha<beta && list_of_next_moves.size()>0){
				board_value_next = alpha_beta(list_of_next_moves.get(0), alpha, beta, depth);
				list_of_next_moves.remove(0);
				if(board_value_next < beta){
					beta = board_value_next;
				}
				System.out.println(" beta  "+beta);
				return beta;
			}
		}
		System.out.println("end  ");
		if(max){
			return alpha;
		}
		else{
			return beta;
		}
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
	}



	int evaluate_board(HashMap<Point, Integer> board){
		//		System.out.println(board);
		return 0;
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

/*			board.put(new Point(2,2), 1+10);
		board.put(new Point(1,3), -1);
		board.put(new Point(0,6), 99);
		board.put(new Point(2,6), 99);
		board.put(new Point(3,3), -1);
		board.put(new Point(1,5), -1);

		
		board.put(new Point(4,4), -1);
		board.put(new Point(4,2), 99);
		board.put(new Point(2,4), 1);
		board.put(new Point(3,5), 99);


	 board.put(new Point(2,2), 1+10);
		 board.put(new Point(1,3), -1);
		 board.put(new Point(0,6), 99);
		 board.put(new Point(2,6), 99);
		 board.put(new Point(3,3), -1);
		 board.put(new Point(1,5), 99);
		 board.put(new Point(1,5), -1);
		 board.put(new Point(6,4), 1);
		 board.put(new Point(6,2), 99);*/
//		EvaluateBoard evaluatedBoard = new EvaluateBoard( board, color_value) ;

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

		move_on.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {

				board = plainBoard.configuration;
				next_move();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(new_game);
		buttonPanel.add(add_piece);
		buttonPanel.add(start_game);
		buttonPanel.add(move_on);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
	}


	public static void main(String[] args) {
		Play play = new Play();

	}

}
