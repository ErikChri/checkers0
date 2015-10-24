import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


public class PlainBoard extends JPanel{
	
	HashMap<Point, Integer> configuration = new HashMap<Point, Integer>();
	
	public PlainBoard(HashMap<Point, Integer> configuration){
		this.configuration = configuration;
//		setMinimumSize(new Dimension(800,800));
		
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
	    		g2.fillRect(100*i, 100*j, 100, 100);
	    	}
	    }
	    
	    // Put pieces on board
	    for (Map.Entry<Point, Integer> entry : configuration.entrySet()) {
	    	if(entry.getValue()==1){
	    		g2.setColor(black);
	    		g2.fillOval(entry.getKey().x*100, entry.getKey().y*100, 100, 100);
	    	}
	    	else if(entry.getValue()==-1){
	    		g2.setColor(white);
	    		g2.fillOval(entry.getKey().x*100, entry.getKey().y*100, 100, 100);
	    	}
	    	else if(entry.getValue()==11){
	    		g2.setColor(black);
	    		g2.fillOval(entry.getKey().x*100, entry.getKey().y*100, 100, 100);
	    		g2.setStroke(new BasicStroke(5));
	    		g2.setColor(gold);
	    		g2.drawOval(entry.getKey().x*100+15, entry.getKey().y*100+15, 70, 70);
	    	}
	    	else if(entry.getValue()==9){
	    		g2.setColor(white);
	    		g2.fillOval(entry.getKey().x*100, entry.getKey().y*100, 100, 100);
	    		g2.setStroke(new BasicStroke(5));
	    		g2.setColor(gold);
	    		g2.drawOval(entry.getKey().x*100+15, entry.getKey().y*100+15, 70, 70);
	    	}
	    }
	}
	
	
}
