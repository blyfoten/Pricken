import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class Pricken extends JApplet 
                            implements MouseMotionListener {
	private Prick kran;
	private Prick krok;
	
	private class Prick extends Thread {
		private double x = 40; 		//muspekarens position
		private double y = 40;
		private double dotx = 40;	//prickens position
		private double doty = 40;
		private double velx=0;		//hastighet
		private double vely=0;
		private double accx=0;		//acceleration
		private double accy=0;
		private double fx=0;  		//kraft
		private double fy=0;  
		private double m=1000; 		//massa         lek med dessa för att få till den önskade effekten
		private double c=30; 		//vindmotståndskoefficient 0=inget vindmotstånd
		private Prick polare = null;
		
		public Prick() {
			super();
		}
		
		public Prick(Prick polare,double doty,double m,double c) {
			super();
			this.m = m;
			this.c = c;
			this.doty = doty;
			this.polare = polare;
		}
		
		public void run() {  // tråden
			while (true) {   // loopar förevigt =) perfa
				if (polare !=null) {
					x = polare.getX();
					y = polare.getY();
				}
				fx = x - dotx;  // räknar ut kraften som skillnaden mellan pricken och muspekaren
				//fy = y - doty;
				accx = (fx -velx*Math.abs(velx)*c)/m;  // accelerationen = kraften/massan - lite vindmotstånd (brukar vara en kraft som är proportionell mot hastigheten i kvadrat) 
				//accy = (fy -vely*Math.abs(vely)*c)/m;
				velx += accx;	// hastigheten är ju summan av accelerationen (lite förenklat)
				//vely += accy;
				dotx += velx;   // positionen är den summan av den accumulerade hastigheten
				//doty += vely;
				repaint();		// rite prickens nya position
				try {
					Thread.sleep(10);  // softa i 10ms
				} catch(Exception e) {
				}
			}
		}
		public void setCoord(double x, double y) {
			this.x = x;
			this.y = y;
		}
		public double getX() {
			return this.dotx;
		}
		public double getY() {
			return this.doty;
		}
		
	}

    public void init() {
        addMouseMotionListener(this);  // lägger till mig själv som en musrörelselyssnare 
        kran = new Prick(null,10,100,10);
        krok = new Prick(kran,500,3000,1);
        kran.start();
        krok.start();
    }



    public void mouseMoved(MouseEvent e) {
       kran.setCoord((double)e.getX(),(double)e.getY());  // när musen rörs spara muspositionen i x och y
    }
    public void mouseDragged(MouseEvent e) {
    }
    
	public void paint(Graphics g) {
	    g.setColor(Color.white);
	    g.fillRect(0,0,getSize().width,getSize().height);
	    g.setColor(Color.black);
	    g.fillOval((int)kran.getX()-4,(int)kran.getY()-4,7,7); // rita en svart 7 pixlars prick på pos (dotx, doty)
	    g.fillOval((int)krok.getX()-4,(int)krok.getY()-4,7,7); // rita en svart 7 pixlars prick på pos (dotx, doty)
	    
	    
	}
		
}
