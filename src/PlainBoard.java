import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


public class PlainBoard extends JPanel implements MouseListener{
	
	HashMap<Point, Integer> configuration = new HashMap<Point, Integer>();
	int sizeVar = 5;
	Point to_move;
	Point move_to;
	Point captured_piece;
	
	int color_value;
	public PlainBoard(HashMap<Point, Integer> configuration){
		this.configuration = configuration;
		
		addMouseListener(this);
		
	}

	public void paint (Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setRenderingHints(rh);
	    
	    Color gold = new Color(255,165,0);
	    Color black = new Color(0,0,0);
	    Color white = new Color(155,0,180);
	    
	    // Create plain board
	    Color grey = Color.gray;
	    Color red = Color.red;
	    g2.setColor(Color.black); 
	    g2.setStroke(new BasicStroke(2));
	    for(int i=0; i<8; i++){
	    	if(g2.getColor().equals(red)){
	    		g2.setColor(grey);
	    	}
	    	else{
	    		g2.setColor(red);
	    	}
	    	for(int j=0; j<8; j++){
	    		if(g2.getColor().equals(red)){
	    		g2.setColor(grey);
	    	}
	    	else{
	    		g2.setColor(red);
	    	}
	    		g2.fillRect(10*sizeVar*i, 10*sizeVar*j, 10*sizeVar, 10*sizeVar);
	    	}
	    }
	    
	    // Put pieces on board
	    for (Map.Entry<Point, Integer> entry : configuration.entrySet()) {
	    	if(entry.getValue()==1){
	    		g2.setColor(black);
	    		g2.fillOval(entry.getKey().x*10*sizeVar, entry.getKey().y*10*sizeVar, 10*sizeVar, 10*sizeVar);
	    	}
	    	else if(entry.getValue()==-1){
	    		g2.setColor(white);
	    		g2.fillOval(entry.getKey().x*10*sizeVar, entry.getKey().y*10*sizeVar, 10*sizeVar, 10*sizeVar);
	    	}
	    	else if(entry.getValue()==11){
	    		g2.setColor(black);
	    		g2.fillOval(entry.getKey().x*10*sizeVar, entry.getKey().y*10*sizeVar, 10*sizeVar, 10*sizeVar);
	    		g2.setStroke(new BasicStroke(5));
	    		g2.setColor(gold);
	    		g2.drawOval(entry.getKey().x*10*sizeVar+8, entry.getKey().y*10*sizeVar+8, 7*sizeVar, 7*sizeVar);
	    	}
	    	else if(entry.getValue()==9){
	    		g2.setColor(white);
	    		g2.fillOval(entry.getKey().x*10*sizeVar, entry.getKey().y*10*sizeVar, 10*sizeVar, 10*sizeVar);
	    		g2.setStroke(new BasicStroke(5));
	    		g2.setColor(gold);
	    		g2.drawOval(entry.getKey().x*10*sizeVar+8, entry.getKey().y*10*sizeVar+8, 7*sizeVar, 7*sizeVar);
	    	}
	    }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX()/(10*sizeVar);
		int y = e.getY()/(10*sizeVar);
		captured_piece = new Point(x,y);
		configuration.put(captured_piece, 99);
		repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX()/(10*sizeVar);
		int y = e.getY()/(10*sizeVar);
		to_move = new Point(x,y);
		
		color_value = configuration.get(to_move);
		configuration.put(to_move, 99);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()/(10*sizeVar);
		int y = e.getY()/(10*sizeVar);
		move_to = new Point(x,y);
		if(y == 0 || y == 7){
			color_value = color_value +10;
		}
		configuration.put(move_to, color_value);
		repaint();
	}
	
	
}
