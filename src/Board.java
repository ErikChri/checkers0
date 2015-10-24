import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;


public class Board {

	/*
	 * 1 = black piece
	 * 2 = white piece
	 * 11 = black king
	 * 9 = white king
	 * 99 = vacant field
	 */
	HashMap<Point, Integer> configuration;
	int evaluation_value;

	public Board(){
		configuration = new HashMap<Point, Integer>();
		int value = 1;
		for(int i=0; i<8; i++){
			if(i<3){
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
			}
			if(i>2 && i<5){
				value = 99;
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
			}
			if(i>4){
				value = 2;
				if(i%2==0){
					for(int j=0; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
				else{
					for(int j=1; j<8; j+=2){
						configuration.put(new Point(j,i), value);
					}
				}
			}

		}

	}
}
