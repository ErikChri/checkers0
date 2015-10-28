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


public class Play {

	static int counter = 0;
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
	boolean is_leaf_node;
	long t1 = System.currentTimeMillis();
	long t2 = System.currentTimeMillis();
	long time_for_move;
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
	HashMap<HashMap<Point, Integer>, Integer> list_of_potential_moves = new HashMap<HashMap<Point, Integer>,Integer>();
	ArrayList<HashMap<Point, Integer>> choose_next_move = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>>temp_choose_next_move = new ArrayList<HashMap<Point, Integer>>();
	ArrayList<HashMap<Point, Integer>> board_seen_before = new ArrayList<HashMap<Point, Integer>>();

	//Buttons
	JButton new_game = new JButton("New Game");
	JButton add_piece = new JButton("Add Piece");
	JButton start_game = new JButton("Start Game");
	JButton move_on = new JButton("Next Move");
	JLabel time_for_this_move = new JLabel("Time for this move: "+time_for_move+" ms  ");
	JLabel search_depth = new JLabel("Search depth: "+depth);
	JLabel average_branching_fac = new JLabel("Average branching factor: "+branching_factor);
	JPanel buttonPanel = new JPanel();

	public Play(){
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
		if(!board.containsValue(-1) && !board.containsValue(9)){
			System.out.println("Black wins the game");
		}
		if(!board.containsValue(1) && !board.containsValue(11)){
			System.out.println("White wins the game");
		}

		list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		list_of_best_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		color_value = 1;
		for (Map.Entry<Point, Integer> entry : board.entrySet()) {
			if(entry.getValue()==color_value || entry.getValue()==color_value+10){
				Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
				generate_move(board, point_to_move);  
				//				System.out.println("103 list of moves size "+list_of_moves.size()+"  board "+board);
			}
		}

		for (Map.Entry<HashMap<Point, Integer>,Integer> entry : list_of_moves.entrySet()) {
//			System.out.println(entry.getKey().containsValue(11));
		}
		
		
		
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

		//System.out.println("118 list of moves "+list_of_moves.size());
		list_of_potential_moves = new HashMap<HashMap<Point, Integer>,Integer>();
		list_of_potential_moves.putAll(list_of_moves);
		choose_next_move = sort_list_of_moves(list_of_potential_moves);

		//		System.out.println("123 chose move size "+choose_next_move.size());
		out_of_time = false;
		iteration();


		board = choose_next_move.get(0);
		/*		boolean move_chosen = false;
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_best_moves.entrySet()) {

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
		}*/
		show(board);

	}


	void iteration(){

		while(!out_of_time){
			if(depthLimit%2==1){
				temp_choose_next_move = new ArrayList<HashMap<Point, Integer>>();
				temp_choose_next_move = choose_next_move;
				search_dept = depthLimit;
				//The number of branches include the number of leaf nodes
				average_branching_factor = (number_of_branches)/(double)(number_of_branches-number_of_leaf_nodes+1);
				average_branching_factor = Math.pow(number_of_leaf_nodes, 1/(double)search_dept);
				DecimalFormat df = new DecimalFormat("#.00"); 
				branching_factor = df.format(average_branching_factor);
//				if(number_of_leaf_nodes != 0)
//					System.out.println("171  number_of_branches "+number_of_branches+" number_of_leaf_nodes "+number_of_leaf_nodes+" average_branching_factor "+average_branching_factor+" search_dept "+search_dept);

			}
			number_of_leaf_nodes = 0;
			number_of_branches = 0;
			board_seen_before = new ArrayList<HashMap<Point, Integer>>();
			depthLimit++;
//						System.out.println(" 179 chose next move size "+choose_next_move.size()+" depthlimit "+depthLimit);
			list_of_best_moves = new HashMap<HashMap<Point, Integer>,Integer>();

			int alpha = -100000;
			int beta = 100000;
			for (int i=0; i<choose_next_move.size(); i++) {
				depth = 0;
				int v = alpha_beta(choose_next_move.get(i), alpha, beta, depth);
				//								System.out.println("162  eval "+v+" depthlimit "+depthLimit+" chose next move size "+choose_next_move.size());
				list_of_best_moves.put(choose_next_move.get(i), v);
				if(v >alpha){
					alpha = v;
				}
			}

			choose_next_move = sort_list_of_moves(list_of_best_moves);
			number_of_branches = number_of_branches +choose_next_move.size();
			//			System.out.println("167 chose next move size "+choose_next_move.size()+" depth limit "+depthLimit);
		}

		choose_next_move = temp_choose_next_move;

	}

	private ArrayList<HashMap<Point, Integer>> sort_list_of_moves(HashMap<HashMap<Point, Integer>, Integer> list_to_sort){
		Object[][] move_queu = new Object[list_to_sort.size()][2];
		int i =0;
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_to_sort.entrySet()) {
			move_queu[i][0] = entry.getValue();
			move_queu[i][1] = entry.getKey();
			i++;
		}

		Arrays.sort(move_queu, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] entry1, Object[] entry2) {
				Integer move1 = (Integer) entry1[0];
				Integer move2 = (Integer) entry2[0];
				return move1.compareTo(move2);
			}
		}); 

		choose_next_move.removeAll(choose_next_move);
		for(int j=0; j<move_queu.length;  j++){
			choose_next_move.add(0, (HashMap<Point, Integer>) move_queu[j][1]);
		}
		return choose_next_move;

	}

	void show(HashMap<Point, Integer> board){
		t2 = System.currentTimeMillis();
		time_for_move = t2-t1;
		//		time_for_move = 100;
//				System.out.println("233 Time   1: "+((System.currentTimeMillis())-t1)+" 233  Time for move "+time_for_move+" t1 "+t1+" t2 "+t2);
		buttonPanel.remove(time_for_this_move);
		buttonPanel.remove(search_depth);
		buttonPanel.remove(average_branching_fac);
		mainFrame.remove(plainBoard);
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

	//Min-max and alpha-beta algorithm
	private int alpha_beta(HashMap<Point, Integer> testBoard, int alpha, int beta, int depth){
		if(System.currentTimeMillis()-t1<15000){
			
			counter++;
			depth++;
			//			System.out.println("222 max "+max+ " depthlimit "+depthLimit);
//											System.out.println("265 Doing the alpha-beta "+counter);
			//					System.out.println("depth  "+depth);		
			if(depth%2==0){
				max=true;
			}
			else{
				max = false;
			}
			//			System.out.println("230 max "+max+ " depth "+depth);
			if(depth == depthLimit){
				number_of_leaf_nodes++;
				EvaluateBoard ev = new EvaluateBoard(testBoard, -1*color_value);
				int evaluate_board = ev.sum;
//											System.out.println("278 number_of_leaf_nodes  "+number_of_leaf_nodes);
				//				int ev = (int)(Math.random()*1000);
//									System.out.println("274 ev "+ev.sum+" counter "+counter+" depth "+depth);
				return evaluate_board;

			}

			else if(max){  // Maximazing level
				// Generate list of moves

				color_value = 1;
				list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
				for (Map.Entry<Point, Integer> entry : testBoard.entrySet()) {
					if(entry.getValue()==color_value || entry.getValue()==color_value+10){
						Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
						generate_move(testBoard, point_to_move);   
					}
				}
				//				System.out.println("251 size list of moves "+list_of_moves.size());
				int max =0;
				if(list_of_moves.size()==0){
					number_of_leaf_nodes++;
					EvaluateBoard ev = new EvaluateBoard(testBoard, color_value);
					return -10000;
				}
				else if(list_of_moves.size()>0){
					max = Collections.max(list_of_moves.values());
				}

				if(max>0){
										list_of_moves.values().removeAll(Collections.singleton(0));
				}
				//				System.out.println("260 size list of moves "+list_of_moves.size());
				ArrayList<HashMap<Point, Integer>> list_of_next_moves = new ArrayList<HashMap<Point, Integer>>();
				for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
					if(entry.getValue() == Collections.max(list_of_moves.values()))
						list_of_next_moves.add(0, entry.getKey());
					else{
						list_of_next_moves.add(entry.getKey());
					}
				}
							
				int number_of_moves = list_of_next_moves.size();
				int branching_factor_alpha = 0;
				while(alpha<beta && list_of_next_moves.size()>0){
//					System.out.println("312  size of list of moves "+list_of_next_moves.size()+" depth "+depth+ " depth limit "+depthLimit);
					if(!board_seen_before.contains(list_of_next_moves.get(0))){
//						System.out.println("314  size of list of moves "+list_of_next_moves.size()+" depth "+depth+ " depth limit "+depthLimit);
						board_seen_before.add(list_of_next_moves.get(0));
						show(list_of_next_moves.get(0));
						board_value_next = alpha_beta(list_of_next_moves.get(0), alpha, beta, depth);
						branching_factor_alpha++;	
						number_of_branches++;
						if(board_value_next >alpha){
							alpha = board_value_next;
						}
					}
					list_of_next_moves.remove(0);

				}
				//				number_of_branches = +(number_of_moves-list_of_next_moves.size());
				//							System.out.println("284 branching_factor_alpha  "+branching_factor_alpha+" depth "+depth+" number of moves "+number_of_moves+" depth limit "+depthLimit);
				return alpha;


			}
			else if(!max){ //Minimizing level
				// Generate list of moves
				//				moves = new ArrayList<Move>();
				//				System.out.println("291 size list of moves "+list_of_moves.size());
				list_of_moves = new HashMap<HashMap<Point, Integer>,Integer>();
				color_value = -1;
				for (Map.Entry<Point, Integer> entry : testBoard.entrySet()) {
					if(entry.getValue()==color_value || entry.getValue()==color_value+10){
						Point point_to_move = new Point(entry.getKey().x, entry.getKey().y);
						generate_move(testBoard, point_to_move);   
					}
				}

				int max =0;
				if(list_of_moves.size()==0){
					number_of_leaf_nodes++;
					EvaluateBoard ev = new EvaluateBoard(testBoard, color_value);
					return 10000;
				}
				else if(list_of_moves.size()>0){
					max = Collections.max(list_of_moves.values());
				}

				if(max>0){
										list_of_moves.values().removeAll(Collections.singleton(0));
				}
				//				System.out.println("302 size list of moves "+list_of_moves.size());
				ArrayList<HashMap<Point, Integer>> list_of_next_moves = new ArrayList<HashMap<Point, Integer>>();
				for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
					if(entry.getValue() == Collections.max(list_of_moves.values()))
						list_of_next_moves.add(0, entry.getKey());
					else{
						list_of_next_moves.add(entry.getKey());
					}
				}
				//				System.out.println("318 board_value_next "+board_value_next+" beta  "+beta+" list of moves "+list_of_moves.size()+" depth "+depth);
				int number_of_moves = list_of_next_moves.size();
				int branching_factor_beta = 0;
				while(alpha<beta && list_of_next_moves.size()>0){
					if(!board_seen_before.contains(list_of_next_moves.get(0))){
						show(list_of_next_moves.get(0));
						board_seen_before.add(list_of_next_moves.get(0));
						board_value_next = alpha_beta(list_of_next_moves.get(0), alpha, beta, depth);
						number_of_branches++;
						//				System.out.println("board_value_next "+board_value_next+" beta  "+beta+" depth "+depth);
						if(board_value_next < beta){
							beta = board_value_next;
						}
					}
					//					branching_factor_beta++;
					//					System.out.println("320 max "+max+ " depth "+depth);

					list_of_next_moves.remove(0);

				}
				//				System.out.println("330 branching_factor_beta  "+branching_factor_beta+" depth "+depth+" number of moves "+number_of_moves+" depth limit "+depthLimit );
				//				number_of_branches = +(number_of_moves-list_of_next_moves.size());
				return beta;
			}
		}

		else{
			out_of_time = true;
		}
//						System.out.println("382 end alpha, beta "+alpha+", "+beta);
		if(max){
			return alpha;
		}
		else{
			return beta;
		}

	}

	void generate_move(HashMap<Point, Integer> testBoard, Point point_to_move){

		next_move = new Move(testBoard, color_value, point_to_move, second_move);

//				System.out.println("412 next move values "+next_move.move_value);

		boolean[] move_direction = {next_move.left, next_move.right, next_move.back_left, next_move.back_right};
		boolean[] move_direction_capture = {next_move.left_capture, next_move.right_capture, next_move.back_left_capture, next_move.back_right_capture};
		Point[] point_moved_to = {next_move.point_moved_to_left, next_move.point_moved_to_right, next_move.point_moved_to_back_left, next_move.point_moved_to_back_right};
		HashMap[] configuration = {next_move.configuration_left, next_move.configuration_right, next_move.configuration_back_left, next_move.configuration_back_right};
//		System.out.println("429 capture "+next_move.left_capture +" "+ next_move.right_capture);
		for(int i=0; i<4; i++){
			if(move_direction[i] && !second_move && (configuration[i].containsValue(1) || configuration[i].containsValue(11)) && (configuration[i].containsValue(-1) || configuration[i].containsValue(9))){ //next_move.move_value == 0 && ){
				list_of_moves.put(configuration[i], next_move.move_value);
				second_move=false;
				//				System.out.println("360 size list of moves "+list_of_moves.size());
			}
			else if(move_direction_capture[i]){ // && (configuration[i].containsValue(1) || configuration[i].containsValue(11)) && (configuration[i].containsValue(-1) || configuration[i].containsValue(9))){ /// && second_move){
//								System.out.println("437 put to list i  "+i+",   "+configuration[i]);
				move_value++;
				list_of_moves.put(configuration[i], move_value);
				move_value = 0;
				second_move = false;
				//				System.out.println("368 size list of moves "+list_of_moves.size());
			}
			if(move_direction_capture[i]  && (configuration[i].containsValue(1) || configuration[i].containsValue(11)) && (configuration[i].containsValue(-1) || configuration[i].containsValue(9))){ // && next_move.move_value>0){
				move_value++;
				second_move=true;
				testBoard =new HashMap<Point, Integer>();
				testBoard.putAll(configuration[i]);

				//								System.out.println("test board "+testBoard);
				generate_move(testBoard, point_moved_to[i]);
			}
		}
		second_move=false;
		for (Map.Entry<HashMap<Point, Integer>, Integer> entry : list_of_moves.entrySet()) {
			if(entry.getValue() >0){
				//				System.out.println("move values "+entry.getValue()+" move "+entry.getKey());
			}

		}
		//		System.out.println("386 size list of moves "+list_of_moves.size());
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
		buttonPanel.add(new_game);
		buttonPanel.add(add_piece);
		buttonPanel.add(start_game);
		buttonPanel.add(move_on);
		buttonPanel.add(time_for_this_move);
		mainFrame.add(buttonPanel, BorderLayout.EAST);
	}


	public static void main(String[] args) {
		Play play = new Play();

	}

}
