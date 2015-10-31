import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class EvaluateBoard {

	static int counter = 0;
	int sum = 0;
	int color_value, mover;

	public EvaluateBoard( HashMap<Point, Integer> inputBoard, int color_value, int mover) {

		this.color_value = color_value;
		this.mover = mover;

		for (Map.Entry<Point, Integer> entry : inputBoard.entrySet()) {

			if(entry.getValue()==color_value ){
				sum++;
				int isCapt = 0;
				if(color_value != mover){
					isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				}
				
				sum-=100*isCapt;
				if(isCapt==0){
					int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
					//System.out.println("multiplied value: "+ capt);
					sum+=50*capt;
				}
					sum += entry.getKey().y;
			}
			else if (entry.getValue()==color_value+10){
				int isCapt = 0;
				if(color_value != mover){
					isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				}
				sum-=100*isCapt;
				if(isCapt == 0){
					int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
					//System.out.println("multiplied value: "+ capt);
					sum+=50*capt;
				}
				sum +=50;

			} else if (entry.getValue()== -1*color_value){
				sum -=1;
				int isCapt = 0;
				if(color_value == mover){
					isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				}
				
				sum+=100*isCapt;
				if(isCapt==0){
					int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
					//System.out.println("multiplied value: "+ capt);
					sum-=50*capt;
				}
					sum -= 7-entry.getKey().y;
			
				
			} else if (entry.getValue()== -1*color_value+10){
				sum -=50;
				int isCapt = 0;
				if(color_value == mover){
//					isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				}
				
				sum+=100*isCapt;
				if(isCapt==0){
					int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
					//System.out.println("multiplied value: "+ capt);
					sum-=50*capt;
				}
			}			
		}	
		//	sum = (int)(Math.random()*1000);
		//	sum = -1*counter++;
		//	System.out.println(sum);	
	}

	public int checkIfCaptures(Point entryPosition, HashMap<Point, Integer> inputBoard, int val) {

		int x = entryPosition.x;
		int y = entryPosition.y; 
		//System.out.println("val is now:"+ val + "; x: " + x +" y: "+y);
		//for pawn only - make condition	
		if(x <7 && x>0 ){ 

			if(inputBoard.containsKey(new Point(x+2, y+2)) && inputBoard.get(new Point(x+1, y+1))==-1*color_value){
				if (inputBoard.get(new Point(x+2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x+2, y+2" );
					val++;
					//System.out.println(val);

					val = checkIfCaptures(new Point(x+2, y+2), inputBoard, val );	    
				}
			};
			if(inputBoard.containsKey(new Point(x-2, y+2)) && inputBoard.get(new Point(x-1, y+1))==-1*color_value){
				if (inputBoard.get(new Point(x-2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x-2, y+2" );
					//System.out.println(val );
					val++;
					//System.out.println(val );
					val = checkIfCaptures(new Point(x-2, y+2), inputBoard, val);	
				}
			};
		} else if (x==7){
			if(inputBoard.containsKey(new Point(x-2, y+2)) && inputBoard.get(new Point(x-1, y+1))==-1*color_value){
				if (inputBoard.get(new Point(x-2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x-2, y+2" );
					val++;
					//System.out.println(val );
					val = checkIfCaptures(new Point(x-2, y+2), inputBoard,  val);	
				}
			}; 
		} else if(x==0) {
			if(inputBoard.containsKey(new Point(x+2, y+2)) && inputBoard.get(new Point(x+1, y+1))==-1*color_value){
				if (inputBoard.get(new Point(x+2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x+2, y+2" );
					val++; 
					//System.out.println(val );
					val = checkIfCaptures(new Point(x+2, y+2), inputBoard,  val); 
				}
			} 
		} 
		//System.out.println("outta function:"+ val);
		return val;

	}

	public int CanBeCaptured(Point entryPosition, HashMap<Point, Integer> inputBoard, int val){

		//		System.out.println("Doing board evaluation "+counter);
		int x = entryPosition.x;
		int y = entryPosition.y; 
		if( x !=7 && x!=0  &&  y!=0 && y!=7){ 
			if(color_value == 1){
				if(inputBoard.get(new Point(x+1, y+1))==-1*color_value && inputBoard.get(new Point(x-1, y-1))==99){
//					val++;
					//				if(inputBoard.containsKey(new Point(x-2, y-2))){val = CanBeCaptured(new Point(x-2, y-2), inputBoard,  val);}
				}
				if (inputBoard.get(new Point(x-1, y+1))==-1*color_value && inputBoard.get(new Point(x+1, y-1))==99) {
//					val++;
					//				if(inputBoard.containsKey(new Point(x+2, y-2))){val = CanBeCaptured(new Point(x+2, y-2), inputBoard,  val);}	
				}
			}
			if(color_value == 1){
				if(inputBoard.get(new Point(x+1, y+1))==-1*color_value && inputBoard.get(new Point(x-1, y-1))==99){
//					val++;
					//				if(inputBoard.containsKey(new Point(x-2, y-2))){val = CanBeCaptured(new Point(x-2, y-2), inputBoard,  val);}
				}
				if (inputBoard.get(new Point(x-1, y+1))==-1*color_value && inputBoard.get(new Point(x+1, y-1))==99) {
//					val++;
					//				if(inputBoard.containsKey(new Point(x+2, y-2))){val = CanBeCaptured(new Point(x+2, y-2), inputBoard,  val);}	
				}
			}
			if (inputBoard.get(new Point(x-1, y-1))==-1*color_value+10 && inputBoard.get(new Point(x+1, y+1))==99) {
				val++;
				//				if(inputBoard.containsKey(new Point(x+2, y+2))){val = CanBeCaptured(new Point(x+2, y+2), inputBoard,  val);}	
			}
			if (inputBoard.get(new Point(x+1, y-1))==-1*color_value+10 && inputBoard.get(new Point(x-1, y+1))==99) {
				val++;
				//				if(inputBoard.containsKey(new Point(x-2, y+2))){val = CanBeCaptured(new Point(x-2, y+2), inputBoard,  val);}	
			}
			if (inputBoard.get(new Point(x-1, y+1))==-1*color_value+10 && inputBoard.get(new Point(x+1, y-1))==99) {
				val++;
				//				if(inputBoard.containsKey(new Point(x+2, y+2))){val = CanBeCaptured(new Point(x+2, y+2), inputBoard,  val);}	
			}
			if (inputBoard.get(new Point(x+1, y+1))==-1*color_value+10 && inputBoard.get(new Point(x-1, y-1))==99) {
				val++;
				//				if(inputBoard.containsKey(new Point(x-2, y+2))){val = CanBeCaptured(new Point(x-2, y+2), inputBoard,  val);}	
			}
		}

		return val;
	} 

	public int isDefended(Point entryPosition, HashMap<Point, Integer> inputBoard, int piece){
		int x = entryPosition.x;
		int y = entryPosition.y; 
		int val =0;
		if(piece==1){

		}else {

		}
		return val;
	}


}
