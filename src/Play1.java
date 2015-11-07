import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class Play1 {

	static int mov_no = 0;
	JFrame mainFrame = new JFrame("Checkers    Present board");
	PlainBoard plainBoard;
	int no_simple_moves = 0;
	int piece_color = 1;
	int fifty_moves_counter = 0;
	int present_depth = 0;
	String msg = "";

	String alphaVal = "";
	//	Board board;

	// Alpha-beta variables
	int depth =0;
	int depthLimit = 0;
	double average_branching_factor;
	int number_of_leaf_nodes = 0;
	int number_of_branches = 0;
	String branching_factor;
	int search_dept;
	//	int alpha = -100000;
	//	int beta = 100000;
	int board_value_next = 0;
	boolean second_move, black;
	boolean max = true;
	boolean out_of_time;
	boolean no_changes;
	boolean is_leaf_node;
	long t1 = System.currentTimeMillis();
	long t2 = System.currentTimeMillis();
	long time_for_move;
	int sizeVar = 5;
	int move_value = 0;
	int capture_value = 0;
	int color_value = 1;
	int mover;
	int move_direction = -1;
	Move next_move;
	ArrayList<Move> moves;

	HashMap<Point, Integer> board;
	//	HashMap<Point, Point> move;
	HashMap<HashMap<Point, Integer>, Integer> list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
	//	HashHashMap<Point, Integer>, Integer> list_of_best_moves = new HashMap<HashMap<Point, Integer>,Integer>();
	HashMap<Point, Integer> preliminary_board = new HashMap<Point, Integer>();
	ArrayList<HashMap<Point, Integer>> choose_next_move = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>>temp_choose_next_move = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> board_seen_before = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> next_moves = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> board_played_before = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> previous_boards = new ArrayList<HashMap<Point, Integer>>();
	//	HashSet<HashMap<Point, Integer>> board_played_before = new HashSet<HashMap<Point, Integer>>();

	//Buttons
	JButton change_side = new JButton("change side");
	JButton add_piece = new JButton("Add Piece");
	JButton start_game = new JButton("Start Game");
	JButton move_on = new JButton("Start game/Next Move");
	JLabel time_for_this_move = new JLabel("Time for this move: "+time_for_move+" ms  ");
	JLabel search_depth = new JLabel("Search depth: "+1);
	JLabel average_branching_fac = new JLabel("Average branching factor: "+branching_factor);
	JPanel buttonPanel = new JPanel();


	public Play1(){
		start_position_board();

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(125*sizeVar, 88*sizeVar));
		mainFrame.setLayout(new BorderLayout());
		plainBoard = new PlainBoard(board, piece_color, msg);
		plainBoard.setMinimumSize(new Dimension(80*sizeVar,80*sizeVar));
		mainFrame.add(plainBoard, BorderLayout.CENTER);
		mainFrame.setVisible(true);

		addButtons();
		start_position_board();
	}

	void next_move(){
		out_of_time = false;
		Object[][]  move_queue = new Object[0][0];
		present_depth = 0;
		color_value = 1;

		if(!board.containsValue(1) && !board.containsValue(11)){
			msg = "Opponent wins the game";
			show(board, 0, 0, false);
			return;
		}

		next_moves.removeAll(next_moves);
		MoveGenerator next_move = new MoveGenerator(board, color_value);

		next_moves.addAll(next_move.moves);
		
		move_queue = new Object[next_moves.size()][2];
		for(int i=0; i<move_queue.length; i++){
			move_queue[i][0] = next_moves.get(i);
			move_queue[i][1] = -1000000000;
		}
 int alpha = 0;
		while(!out_of_time){
			
			
			Arrays.sort(move_queue, new Comparator<Object[]>() {
				public int compare(Object[] O1, Object[] O2) {
					Integer first = (Integer)O1[1];
					Integer second = (Integer)O2[1];
					return -first.compareTo(second);
				}
			});
			preliminary_board = (HashMap<Point, Integer>) move_queue[0][0];
			
//			System.out.println("140  alpha "+alpha+",  depth limit "+depthLimit);
			search_dept = depthLimit;
			average_branching_factor = Math.pow(number_of_leaf_nodes, 1/(double)search_dept);
			DecimalFormat df = new DecimalFormat("#.00"); 
			branching_factor = df.format(average_branching_factor);
			number_of_leaf_nodes = 0;
			if( present_depth<depthLimit){
				//				out_of_time = true;
			}
			else{

			}
			depthLimit++;
			color_value = 1;

			alpha = -100000;
			int beta = 100000;
			for (int i=0; i<move_queue.length; i++) {

				int v = alpha_beta((HashMap<Point, Integer>) move_queue[i][0], alpha, beta, false, 1);
				System.out.println("164 v     "+v+",  depth limit "+depthLimit);
				
				
				if(v >alpha){
					alpha = v;
				}
//				System.out.println("161  alpha "+alpha+",  depth limit "+depthLimit);
				move_queue[i][0] = move_queue[i][0];
				move_queue[i][1] = alpha;
			}
/*
			Arrays.sort(move_queue, new Comparator<Object[]>() {
				public int compare(Object[] O1, Object[] O2) {
					Integer first = (Integer)O1[1];
					Integer second = (Integer)O2[1];
					return -first.compareTo(second);
				}
			});
			*/
			if(alpha == 100000){
				out_of_time = true;
			}
			else if(alpha == -100000){
				out_of_time = true;
			}
			
			

		}

		if(move_queue.length==0){
			msg = "Opponent wins the game";
			show(previous_boards.get(0),  0, 0, false);
		}
		if( (Integer)move_queue[0][1] != 100000){
			board = preliminary_board;
		}
		else {
			board = (HashMap<Point, Integer>) move_queue[0][0];
		}

		previous_boards.add(0, board);


		if(!board.containsValue(-1) && !board.containsValue(9)){
			msg = "Black wins the game";
			show(board, 0, 0, false);
		}
		show(board, 0, 0, false);


		if(previous_boards.size()>1){
			show(previous_boards.get(1), 700, 100, true);
		}

		fifty_moves_check();

	}

	int fifty_moves_check(){
		int number_of_blanks = 0;
		int number_of_blanks_prev = 0;
		if(previous_boards.size()>1){
			for (Map.Entry<Point, Integer> entry : board.entrySet()) {
				if((entry.getValue() == 1 || entry.getValue() == -1) && entry.getValue() == previous_boards.get(1).get(entry.getKey())){
					return fifty_moves_counter = 0;
				}
				if(entry.getValue() == 99){
					number_of_blanks++;
				}
				if(previous_boards.get(1).get(entry.getKey()) == 99){
					number_of_blanks_prev++;
				}
			}
		}
		if(number_of_blanks != number_of_blanks_prev){
			return fifty_moves_counter = 0;
		}
		else{
			fifty_moves_counter++;
			if(fifty_moves_counter == 50){
				msg = "The game is a draw!";
				show(board, 0, 0, false);
			}
			return fifty_moves_counter;
		}
	}

	void show(HashMap<Point, Integer> board, int x, int y, boolean new_frame){
		t2 = System.currentTimeMillis();
		time_for_move = t2-t1;
		if(new_frame){
			JFrame previousMove = new JFrame("Move no. "+ (previous_boards.size()-1));
			previousMove.setLocation(x, y);
			PlainBoard plainBoard_p = new PlainBoard(board, piece_color, msg);
			previousMove.add(plainBoard_p, BorderLayout.CENTER);
			previousMove.setMinimumSize(new Dimension(125*sizeVar, 88*sizeVar));
			previousMove.setVisible(true);
		}
		else{
			buttonPanel.remove(time_for_this_move);
			buttonPanel.remove(search_depth);
			buttonPanel.remove(average_branching_fac);
			mainFrame.remove(plainBoard);

			mainFrame.setLocation(x, y);

			time_for_this_move = new JLabel("Time for this move: "+time_for_move+" ms  ");
			search_depth = new JLabel("Search depth: "+search_dept);
			average_branching_fac = new JLabel("Average branching factor: "+branching_factor+"  ");
			buttonPanel.add(time_for_this_move);
			buttonPanel.add(search_depth);
			buttonPanel.add(average_branching_fac);

			plainBoard = new PlainBoard(board, piece_color, msg);
			mainFrame.add(plainBoard, BorderLayout.CENTER);
			mainFrame.repaint();
			mainFrame.revalidate();
			if(!msg.equals("")){
				return;
			}
		}
	}

	//Min-max and alpha-beta algorithm
	private int alpha_beta(HashMap<Point, Integer> testBoard, int alpha, int beta, boolean is_alpha_node, int depth){
		
		board_seen_before.removeAll(board_seen_before);
		present_depth = depth;
		if(System.currentTimeMillis()-t1>14998){
			out_of_time=true;
		}
		if(!out_of_time){

			if(depth == depthLimit){

				number_of_leaf_nodes++;
				Evaluation eval = new Evaluation(testBoard, 1, mover);
				int evaluate_board = eval.sum;
				if(evaluate_board>90000){
					evaluate_board = evaluate_board-depth*100;
				}
				return evaluate_board;
			}

			else if(is_alpha_node){  

				color_value = 1;
				MoveGenerator next_move = new MoveGenerator(testBoard, color_value);
				//				show(testBoard, 400, 400, true);
				if(!(testBoard.containsValue(color_value) || testBoard.containsValue(color_value+10))){
					number_of_leaf_nodes++;
					return -100000;
				}
				else if(!(testBoard.containsValue(-1*color_value) || testBoard.containsValue(-1*color_value+10))){
					number_of_leaf_nodes++;
					return 100000-depth*100;
				}
				else if(next_move.moves.size() == 0){
					number_of_leaf_nodes++;
					return -100000;
				}
				while(alpha<beta && next_move.moves.size()>0){ 
					if(!board_seen_before.contains(next_move.moves.get(0))){

						board_seen_before.add(next_move.moves.get(0));
						HashMap<Point, Integer> next_testboard = new HashMap<Point, Integer>();
						next_testboard.putAll(next_move.moves.get(0));
						board_value_next = alpha_beta(next_testboard, alpha, beta, false, depth+1);
						number_of_branches++;
						if(board_value_next >alpha){
							alpha = board_value_next;
						}
					}
					next_move.moves.remove(0);
				}
				return alpha;
			}
			else if(!is_alpha_node){ 
				color_value = -1;

				MoveGenerator next_move = new MoveGenerator(testBoard, color_value);

				if(!(testBoard.containsValue(color_value) || testBoard.containsValue(color_value+10))){
					number_of_leaf_nodes++;
					return 100000-depth*100;
				}
				else if(!(testBoard.containsValue(-1*color_value) || testBoard.containsValue(-1*color_value+10))){
					number_of_leaf_nodes++;

					//					show(testBoard, 700, 400, true);
					return -100000;
				}
				else if(next_move.moves.size() == 0){
					number_of_leaf_nodes++;
					return 100000-depth*100;
				}

				while(alpha<beta && next_move.moves.size()>0){ 
					if(!board_seen_before.contains(next_move.moves.get(0))){
						board_seen_before.add(next_move.moves.get(0));
						HashMap<Point, Integer> next_testboard = new HashMap<Point, Integer>();
						next_testboard.putAll(next_move.moves.get(0));
						board_value_next = alpha_beta(next_testboard, alpha, beta, true, depth+1);
						number_of_branches++;
						if(board_value_next < beta){
							beta= board_value_next;
						}
					}
					next_move.moves.remove(0);
				}
				return beta;


			}
		}
		if(is_alpha_node){
			return alpha;
		}
		else {
			return beta;
		}

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
	}

	void addButtons(){

		start_game.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				next_move();
			}
		});

		move_on.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				t1 = System.currentTimeMillis();
				board = plainBoard.configuration;
				depthLimit=0;
				next_move();
			}
		});

		change_side.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {

				piece_color = -1*piece_color;
				start_position_board();
				buttonPanel.remove(time_for_this_move);
				buttonPanel.remove(search_depth);
				buttonPanel.remove(average_branching_fac);
				mainFrame.remove(plainBoard);
				plainBoard = new PlainBoard(board, piece_color, msg);
				mainFrame.add(plainBoard, BorderLayout.CENTER);
				mainFrame.repaint();
				mainFrame.revalidate();

			}
		});

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(change_side);
		//		buttonPanel.add(start_game);
		buttonPanel.add(move_on);
		buttonPanel.add(time_for_this_move);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
	}


	public static void main(String[] args) {
		Play1 play1 = new Play1();

	}

}
