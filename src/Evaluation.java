import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


public class Evaluation {

	int sum = 0;
	int color_value, mover;

	public Evaluation(HashMap<Point, Integer> inputBoard, int color_value1, int mover){
		this.color_value = color_value1;
		this.mover = mover;
		for (Map.Entry<Point, Integer> entry : inputBoard.entrySet()) {
			if(entry.getValue()==color_value){
				sum++;
			}
			else if(entry.getValue() == color_value+10){
				sum+=1000;
			}else if(entry.getValue() == -1*color_value){
				sum--;
			}
			else if(entry.getValue() == -1*color_value+10){
				sum-=1000;
			}
		}
	}
}
/*			color_value = 1;
			if(entry.getValue()==11){
				sum+=300;
			}
			if(entry.getValue() == 1) { //!= 99){ // == color_value || entry.getValue() == color_value +10){}
				sum+=  1000;  //entry.getValue();
				
				if(entry.getValue()==9|| entry.getValue()== -1){
					sum-=500;
				}
			}
	
			if((entry.getValue()== color_value || entry.getValue()== color_value+10)){ // && mover != color_value){
				color_value = 1;
				int isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				sum-= 8000*isCapt;
//				System.out.println("27 isCapt: "+ isCapt+" point "+entry.getKey());
				if(color_value == mover && isCapt>0){
//					sum+= 10*color_value;
//					System.out.println("3 isCapt: "+ isCapt+" point "+entry.getKey());
				}
			}
			else if((entry.getValue()== -1*color_value || entry.getValue()== -1*color_value+10) && mover != -1){
				color_value = -1;
				int isCapt = CanBeCaptured(entry.getKey(), inputBoard, 0);
				sum+= 20*isCapt;
//				System.out.println("35 isCapt: "+ isCapt+" point "+entry.getKey());
				if(-1*color_value == mover && isCapt>0){
					sum-= 10*color_value;
//					System.out.println("38 isCapt: "+ isCapt+" point "+entry.getKey());
				}
			}

/*

		int capt = checkIfCaptures(entry.getKey(), inputBoard, 0);
			sum+= 20*capt*color_value*mover;
			if((isCapt==0 || color_value == mover) && capt>0){
				sum+= 10*color_value*mover;
			}
		}
	}

	public int CanBeCaptured(Point entryPosition, HashMap<Point, Integer> inputBoard, int val){
		if(inputBoard.containsKey(new Point(entryPosition.x+color_value, entryPosition.y+color_value)) && inputBoard.containsKey(new Point(entryPosition.x-color_value, entryPosition.y-color_value)) && (inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value)) == -1*color_value || inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y-color_value))==99){
			val++;
//			System.out.println("57 val: "+ val+" point "+entryPosition+" point in front "+(new Point(entryPosition.x+color_value,entryPosition.y+color_value))+" point value in front "+inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value)));
		}
		if(inputBoard.containsKey(new Point(entryPosition.x-color_value, entryPosition.y+color_value)) && inputBoard.containsKey(new Point(entryPosition.x+color_value, entryPosition.y-color_value)) && (inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y+color_value))==-1*color_value || inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y+color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y-color_value))==99){
			val++;
//			System.out.println("61 val: "+ val+" point "+entryPosition);
		}
		if(inputBoard.containsKey(new Point(entryPosition.x+color_value, entryPosition.y-color_value)) && inputBoard.containsKey(new Point(entryPosition.x-color_value, entryPosition.y+color_value)) && inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y-color_value))==-1*color_value+10  && inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y+color_value))==99){
			val++;
//			System.out.println("65 val: "+ val+" point "+entryPosition);
		}
		if(inputBoard.containsKey(new Point(entryPosition.x-color_value, entryPosition.y-color_value)) && inputBoard.containsKey(new Point(entryPosition.x+color_value, entryPosition.y+color_value)) && inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y-color_value))==-1*color_value+10  && inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value))==99){
			val++;
//			System.out.println("69 val: "+ val+" point "+entryPosition);
		}
		if(val>0){
			val = 1;
		}
		return val;
	}
	
	
	public int checkIfCaptures(Point entryPosition, HashMap<Point, Integer> inputBoard, int val) {
		if(inputBoard.containsKey(new Point(entryPosition.x+2*color_value, entryPosition.y+2*color_value)) && (inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value))==-1*color_value || inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y+color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x+2*color_value, entryPosition.y+2*color_value))==99){
			val++;
		}
		if(inputBoard.containsKey(new Point(entryPosition.x-2*color_value, entryPosition.y+2*color_value)) && (inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y+color_value))==-1*color_value || inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y+color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x-2*color_value, entryPosition.y+2*color_value))==99){
			val++;
		}
		if(inputBoard.containsKey(new Point(entryPosition.x-2*color_value, entryPosition.y-2*color_value)) && inputBoard.get(new Point(entryPosition.x, entryPosition.y))==-1*color_value+10 && (inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y-color_value))==-1*color_value || inputBoard.get(new Point(entryPosition.x-color_value, entryPosition.y-color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x-2*color_value, entryPosition.y-2*color_value))==99){
			val++;
		}
		if(inputBoard.containsKey(new Point(entryPosition.x+2*color_value, entryPosition.y-2*color_value)) && inputBoard.get(new Point(entryPosition.x, entryPosition.y))==-1*color_value+10 && (inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y-color_value))==-1*color_value || inputBoard.get(new Point(entryPosition.x+color_value, entryPosition.y-color_value))==-1*color_value+10)  && inputBoard.get(new Point(entryPosition.x+2*color_value, entryPosition.y-2*color_value))==99){
			val++;
		}
		return val;
	}
}
*/