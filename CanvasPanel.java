
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;


/**
 * This class contains code for the JPanel which is where the user draws points for which the 
 * convex hull will be computed.
 */
public class CanvasPanel extends JPanel {

    ConvexHullGui parent = null;
    LinkedList vertices = null; //A linked list containing the vertices 

    Color currentColor = Color.yellow;  //the initial set color for the points
    
    ArrayList<Point> convexHull = null; //an arraylist with the points that are on the convex hull
    
    boolean wasCalc =  false; //this boolean represents if the convex hull was calculated
    
    /**
    *This method is a constructor for the canvas panel
    */
    public CanvasPanel(ConvexHullGui _parent) {
        super();
        parent = _parent;
        vertices = parent.vertices;
    }

    /**
    *This method is responsible for updating the graphics component inside the canvas
    *
    */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(currentColor); //sets the intial color

        ListIterator iterator = vertices.listIterator(0); //creates an iterator to traverse through 

        Point currentVertex = null;

        //This plots the points which are in the array called vertices
        for (int i=0; i < vertices.size(); ++i) {
            currentVertex = (Point) iterator.next();
            g.fillOval(currentVertex.x - parent.NODE_RADIUS,
                currentVertex.y - parent.NODE_RADIUS,
                2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
        }

        //Ploting the convex hull on the canvas
        //Only plots if the user pressed the button and the convexHull array is not null
        if( wasCalc && convexHull != null) {
            //change color to red
            g.setColor(Color.RED);//I Like Red

            ArrayList<Point> points = convexHull; 
            //Use two staring points
            Point a ;
            Point b;
            for ( int i = 0 ; i < points.size() -1; i++)
            {
                //like i said, two starting points
                a= points.get(i); 
                b= points.get(i+1);
                g.drawLine( a.x , a.y,b.x,b.y ); //connect em
            }
        }
    }
    
    //This method is just sitting here.
    //Only changes the color and stuff
    public void changeColor() {
        if (currentColor.equals(Color.red)) {//nothing too big here
            currentColor = Color.yellow;
        } else {
            currentColor = Color.red;
        }
    }

   
}


