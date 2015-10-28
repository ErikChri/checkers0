import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class EvaluateBoard {
	
	static int counter = 0;
	int sum = 0;
 
	public EvaluateBoard( HashMap<Point, Integer> inputBoard, int color_value ) {
 
		
/*		for (Map.Entry<Point, Integer> entry : inputBoard.entrySet()) {
			
			if(entry.getValue()==color_value ){
				sum++;
				int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
				//System.out.println("multiplied value: "+ capt);
				sum+=50*capt;
				int isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				sum-=60*isCapt;
				sum += entry.getKey().y;
			}
			else if (entry.getValue()==color_value+10){
				int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
				//System.out.println("multiplied value: "+ capt);
				sum+=50*capt;
				sum +=10;
				int isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				sum-=100*isCapt;
			} else if (entry.getValue()== -1){
				sum -=1;
			} else if (entry.getValue()== -1+10){
				sum -=10;
			}			
		}	*/
		
	sum = (int)(Math.random()*1000);
//	sum = -1*counter++;
//	System.out.println(sum);	
	}
	
	public int checkIfCaptures(Point entryPosition, HashMap<Point, Integer> inputBoard, int val) {
		
		int x = entryPosition.x;
		int y = entryPosition.y; 
		//System.out.println("val is now:"+ val + "; x: " + x +" y: "+y);
		//for pawn only - make condition	
		if(x <7 && x>0 ){ 
		 
			if(inputBoard.containsKey(new Point(x+2, y+2)) && inputBoard.get(new Point(x+1, y+1))==-1){
				if (inputBoard.get(new Point(x+2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x+2, y+2" );
					val++;
					//System.out.println(val);
					 
					val = checkIfCaptures(new Point(x+2, y+2), inputBoard, val );	    
				}
			};
			if(inputBoard.containsKey(new Point(x-2, y+2)) && inputBoard.get(new Point(x-1, y+1))==-1){
				if (inputBoard.get(new Point(x-2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x-2, y+2" );
					//System.out.println(val );
					val++;
					//System.out.println(val );
					val = checkIfCaptures(new Point(x-2, y+2), inputBoard, val);	
				}
			};
		} else if (x==7){
			if(inputBoard.containsKey(new Point(x-2, y+2)) && inputBoard.get(new Point(x-1, y+1))==-1){
				if (inputBoard.get(new Point(x-2, y+2))==99){
					//System.out.println("x:"+x + ", y:"+y + " to ->x-2, y+2" );
					val++;
					//System.out.println(val );
					val = checkIfCaptures(new Point(x-2, y+2), inputBoard,  val);	
				}
			}; 
		} else if(x==0) {
			if(inputBoard.containsKey(new Point(x+2, y+2)) && inputBoard.get(new Point(x+1, y+1))==-1){
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
		counter++;
//		System.out.println("Doing board evaluation "+counter);
		int x = entryPosition.x;
		int y = entryPosition.y; 
		if( x !=7 && x!=0  &&  y!=0 && y!=7){ 
			if(inputBoard.get(new Point(x+1, y+1))==-1 && inputBoard.get(new Point(x-1, y-1))==99){
				val++;
				if(inputBoard.containsKey(new Point(x-2, y-2))){val = CanBeCaptured(new Point(x-2, y-2), inputBoard,  val);}
				
			}
			if (inputBoard.get(new Point(x-1, y+1))==-1 && inputBoard.get(new Point(x+1, y-1))==99) {
				val++;
				if(inputBoard.containsKey(new Point(x+2, y-2))){val = CanBeCaptured(new Point(x+2, y-2), inputBoard,  val);}	
			}
			if (inputBoard.get(new Point(x-1, y-1))==9 && inputBoard.get(new Point(x+1, y+1))==99) {
				val++;
				if(inputBoard.containsKey(new Point(x+2, y+2))){val = CanBeCaptured(new Point(x+2, y+2), inputBoard,  val);}	
			}
			if (inputBoard.get(new Point(x+1, y-1))==9 && inputBoard.get(new Point(x-1, y+1))==99) {
				val++;
				if(inputBoard.containsKey(new Point(x-2, y+2))){val = CanBeCaptured(new Point(x-2, y+2), inputBoard,  val);}	
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
