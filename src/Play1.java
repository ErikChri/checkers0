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
	JFrame mainFrame = new JFrame("Checkers");
	PlainBoard plainBoard;
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
	int color_value =1;
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
	ArrayList<HashMap<Point, Integer>> next_moves2 = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> previous_boards = new ArrayList<HashMap<Point, Integer>>();

	//Buttons
	JButton new_game = new JButton("New Game");
	JButton add_piece = new JButton("Add Piece");
	JButton start_game = new JButton("Start Game");
	JButton move_on = new JButton("Next Move");
	JLabel time_for_this_move = new JLabel("Time for this move: "+time_for_move+" ms  ");
	JLabel search_depth = new JLabel("Search depth: "+1);
	JLabel average_branching_fac = new JLabel("Average branching factor: "+branching_factor);
	JPanel buttonPanel = new JPanel();


	public Play1(){
		start_position_board();

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//	mainFrame.setPreferredSize(new Dimension(820,820));
		mainFrame.setMinimumSize(new Dimension(125*sizeVar, 88*sizeVar));
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
		out_of_time = false;
		Object[][]  move_queue = new Object[0][0];


		if(!board.containsValue(-1) && !board.containsValue(9)){
			System.out.println("Black wins the game");
		}
		if(!board.containsValue(1) && !board.containsValue(11)){
			System.out.println("White wins the game");
		}

		while(!out_of_time){
			if(move_queue.length>0){
				preliminary_board = (HashMap<Point, Integer>) move_queue[0][0];
			}
			search_dept = depthLimit;
			average_branching_factor = Math.pow(number_of_leaf_nodes, 1/(double)search_dept);
			DecimalFormat df = new DecimalFormat("#.00"); 
			branching_factor = df.format(average_branching_factor);
			number_of_leaf_nodes = 0;
			depthLimit++;
			color_value = 1;
			MoveGenerator next_move = new MoveGenerator(board, color_value);
			//		System.out.println("116 moves size "+next_move.moves.size());
			move_queue = new Object[next_move.moves.size()][2];
			for(int i=0; i<move_queue.length; i++){
				move_queue[i][1] = -1000000;
			}
			int alpha = -100000;
			int beta = 100000;
			int j_inverse = move_queue.length-1;
			for (int i=0; i<next_move.moves.size(); i++) {
				int v = alpha_beta(next_move.moves.get(i), alpha, beta, false, 1);
				System.out.println("124 alpha "+v);
				if(v >alpha){
					alpha = v;
					int j = 0;
					while((int)move_queue[j][1]>alpha){  // && j<move_queue.length){
						j++;
					}
					move_queue[j][0] = next_move.moves.get(i);
					move_queue[j][1] = alpha;
				}
				else{
					move_queue[j_inverse][0] = next_move.moves.get(i);
					move_queue[j_inverse][1] = alpha;
					j_inverse--;
				}
			}
		}
		board = preliminary_board;
		previous_boards.add(0, board);
		show(board, 0, 0, false);
		if(previous_boards.size()>1){
			show(previous_boards.get(1), 700, 100, true);
		}


	}



	void show(HashMap<Point, Integer> board, int x, int y, boolean new_frame){
		t2 = System.currentTimeMillis();
		time_for_move = t2-t1;
		if(new_frame){
			JFrame previousMove = new JFrame("Previous move");
			previousMove.setLocation(x, y);
			PlainBoard plainBoard_p = new PlainBoard(board);
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

			plainBoard = new PlainBoard(board);
			mainFrame.add(plainBoard, BorderLayout.CENTER);
			mainFrame.repaint();
			mainFrame.revalidate();
			//				System.out.println("249 Time: "+((System.currentTimeMillis())-t1));
		}
	}

	//Min-max and alpha-beta algorithm
	private int alpha_beta(HashMap<Point, Integer> testBoard, int alpha, int beta, boolean is_alpha_node, int depth){
		board_seen_before.removeAll(board_seen_before);
		if(System.currentTimeMillis()-t1>14999){
			out_of_time=true;
		}
		if(!out_of_time){
			if(depth == depthLimit){
				number_of_leaf_nodes++;
				Evaluation eval = new Evaluation(testBoard, 1, mover);
				int evaluate_board = eval.sum;
				return evaluate_board;
			}

			else if(is_alpha_node){  
				color_value = 1;
				MoveGenerator next_move = new MoveGenerator(testBoard, color_value);
				System.out.println("183 moves size "+next_move.moves.size());
				if(!(testBoard.containsValue(color_value) || testBoard.containsValue(color_value+10))){
					number_of_leaf_nodes++;
					return -100000;
				}
				else if(!(testBoard.containsValue(-1*color_value) || testBoard.containsValue(-1*color_value+10))){
					number_of_leaf_nodes++;
					return 100000;
				}
				else if(next_move.moves.size() == 0){
					number_of_leaf_nodes++;
					return -100000;
				}

				while(alpha<beta && next_move.moves.size()>0){ // && System.currentTimeMillis()-t1<15000){
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
				System.out.println("212 moves size "+next_move.moves.size());
				if(!(testBoard.containsValue(color_value) || testBoard.containsValue(color_value+10))){
					number_of_leaf_nodes++;
					return 100000;
				}
				else if(!(testBoard.containsValue(-1*color_value) || testBoard.containsValue(-1*color_value+10))){
					number_of_leaf_nodes++;
					return -100000;
				}
				else if(next_move.moves.size() == 0){
					number_of_leaf_nodes++;
					return 100000;
				}

				while(alpha<beta && next_move.moves.size()>0){ // && System.currentTimeMillis()-t1<15000){
					System.out.println("219 alpha beta "+alpha+ ", "+beta);
					if(!board_seen_before.contains(next_move.moves.get(0))){
						board_seen_before.add(next_move.moves.get(0));
						HashMap<Point, Integer> next_testboard = new HashMap<Point, Integer>();
						next_testboard.putAll(next_move.moves.get(0));
						board_value_next = alpha_beta(next_testboard, alpha, beta, true, depth+1);
						System.out.println("230 board_value_next "+board_value_next);
						number_of_branches++;
						if(board_value_next < beta){
							beta= board_value_next;
						}
					}
					next_move.moves.remove(0);
				}
				System.out.println("233 return beta "+beta+", depth "+depth);
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
				System.out.println("Quiet!!   I am thinking......");
				t1 = System.currentTimeMillis();
				board = plainBoard.configuration;
				depthLimit=0;
				next_move();
			}
		});



		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		//		buttonPanel.add(new_game);
		//		buttonPanel.add(add_piece);
		//		buttonPanel.add(start_game);
		buttonPanel.add(move_on);
		buttonPanel.add(time_for_this_move);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
	}


	public static void main(String[] args) {
		Play1 play1 = new Play1();

	}

}
