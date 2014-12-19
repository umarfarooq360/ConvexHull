import java.awt.Point;
import java.util.*;
/**
 * This program calculates the Convex Hull of a given set of points. Given set of N points in the Euclidean plane,
 *the minimum area convex region that contains every point is the Convex Hull.Imagine the points are nails perpendicular
 *to plane, stretch an elastic rubber bound around all points; it will minimize length. The class implements the Graham Scan 
 *algorithm to find the convex hull. 
 * 
 * @author (Omar Farooq) 
 * @version (25 Jan 2014)
 */

public class GrahamScan {    

    /**
     * Returns the convex hull of the points created from xs and ys which are arrays
     * containing respective coordinates. Note that the first and last point in the returned
     * List are the same point.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @return   the convex hull of the points created from xs and ys.
     *
     */
    public  List<Point> getConvexHull(int[] xs, int[] ys) {

        if(xs.length != ys.length) {
            System.out.println("ERROR: x and y coordinate arrays don't have same length");
        }

        //Make a list of points from the two arrays of x and y coordinates
        List<Point> points = new ArrayList<Point>(); 
        for(int i = 0; i < xs.length; i++) {
            points.add(new Point(xs[i], ys[i]));
        }

        //returns the convex hull using the other convex Hull method
        return getConvexHull(points);        
    }

    /**
     * Returns the convex hull of the points created from the list points. Note that the 
     * first and last point in the returned List are the same point.
     *
     * @param points the list of points.
     * @return       the convex hull of the points created from the list points.
     * 
     */
    public  ArrayList<Point> getConvexHull(List<Point> points)  {
        if(points == null) {
            return null;
        }

        //Sort all the points radially
        List<Point> sortedPoints = new ArrayList<Point>(getSortedPoints(points));

        if(sortedPoints.size() < 3) {
            return new ArrayList<Point>(points);
        }

        //Create a stack to be used with the Graham Scan algorithm
        Stack<Point> stack = new Stack<Point>();

        //Add the lowest and the next point onto the stack
        stack.push(sortedPoints.get(0));
        stack.push(sortedPoints.get(1));

        //PURE AWESOMENESS AHEAD!!
        for (int i = 2; i < sortedPoints.size(); i++) {
            //This is the head of the points in the chain
            Point head = sortedPoints.get(i);
            //This is the middle point in the chain
            Point middle = stack.pop();
            //This is the tail of the chain of three points
            Point tail = stack.peek();

            //This gets the turn direction of the three points starting from the tail. 
            Turn turn = getTurn(tail, middle, head);

            if(turn.equals("COUNTER_CLOCKWISE")) {
                stack.push(middle); //If there is a left turn ,push the points onto the stack
                stack.push(head);  
            }
            else if(turn.equals("CLOCKWISE") ){
                i--;   //go back, there are dragons ahead!!!
            }
            else if ( turn.equals("COLLINEAR") ){
                stack.push(head);   //push the head!!
            }

        }
        // close the hull
        stack.push(sortedPoints.get(0));  //Add a copy of the lowest point at the end

        return new ArrayList<Point>(stack); //return the stack as an arrayList
    }

    /**
     * Returns the points with the lowest y coordinate. In case more than 1 such
     * point exists, the one with the lowest x coordinate is returned.
     *
     * @param points the list of points.
     * @return       the points with the lowest y coordinate. In case more than
     *               1 such point exists, the one with the lowest x coordinate
     *               is returned.
     */
    private static Point getLowestPoint( List<Point> points) {
        if (points == null) return null;

        Point lowest = points.get(0); //the first point

        //iterates through and finds the lowest point.
        for(int i = 1; i < points.size(); i++) {
            Point temp = points.get(i); //the point to compare
            if(temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;  //the new lowest point
            }
        }

        return lowest; //returns  the lowest
    }

    /**
     * This method returns a radially sorted set of points from the listed points. The set of points are 
     * sorted in increasing order of the angle they and the lowest point P make with the x-axis. If two or more points
     * form the same angle, then the one closest to the lowest point comes first.
     *
     * @param points the list of points to sort.
     * @return a radially sorted set of points from the list points.
     */
    public Set<Point> getSortedPoints(List<Point> points) {
        if ( points == null) return null;
        final Point lowest = getLowestPoint(points);

        //This is a tree set that keeps all Points sorted upon creation depending on the Comparator created below
        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() 
                {
                    @Override

                    /**
                     * This method is the compare method for the comparator used in the tree map. Point
                     */
                    public int compare(Point a, Point b) {

                        if(a == b || a.equals(b)) {
                            return 0;
                        }

                        double thetaA = 0   ;
                        double thetaB =  getTurnValue(lowest,a,b );

                        if(thetaA < thetaB) {  //if left turn
                            return -1;
                        }
                        else if(thetaA > thetaB) { //if there is a right turn
                            return 1;
                        }
                        else {
                            // collinear with the 'lowest' point, let the point closest to it come first

                            //compute distance to both points
                            double distanceA = (((long)lowest.x - a.x) * ((long)lowest.x - a.x)) +
                                (((long)lowest.y - a.y) * ((long)lowest.y - a.y));
                            double distanceB = (((long)lowest.x - b.x) * ((long)lowest.x - b.x)) +
                                (((long)lowest.y - b.y) * ((long)lowest.y - b.y));

                            //return depending on the one which is closer
                            if(distanceA < distanceB) {
                                return -1;
                            }
                            else {
                                return 1;
                            }
                        }
                    }
                });

        set.addAll(points); // add all the points

        return set;
    }

    /**
     * Check whether three points make a left(counter clockwise turn) or not. More specifically, the cross product between the
     * 3 points (vectors) is calculated:
     * (b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)
     *
     * If C is less than 0, the turn is left or clockwise, if C is more than 0, the turn is right or counter clockwise, else
     * the three points are collinear.
     *
     * @param a the starting point.
     * @param b the second point.
     * @param c the end point.
     * @return the direction of the turn, CLOCKWISE, COUNTER_CLOCKWISE OR COLLINEAR.        
     */
    protected  Turn getTurn(Point a, Point b, Point c) {

        
        double crossProduct = (((double)b.x - a.x) * ((double)c.y - a.y)) -
            (((double)b.y - a.y) * ((double)c.x - a.x));

        if(crossProduct > 0) { //if there is a left trun
            return new Turn("COUNTER_CLOCKWISE" );
        }
        else if(crossProduct < 0) { // if there is a right turn
            return new Turn("CLOCKWISE");
        }
        else {
            return  new Turn("COLLINEAR");
        }
    }

    /**
     * Check whether three points make a left(counter clockwise turn) or not. More specifically, the cross product between the
     * 3 points (vectors) is calculated:
     * (b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)
     *
     * If C is less than 0, the turn is left or clockwise, if C is more than 0, the turn is right or counter clockwise, else
     * the three points are collinear.
     *
     * @param a the starting point.
     * @param b the second point.
     * @param c the end point.
     * @return the cross product or Ca(b,c)  where C is the comparator function that checks for left turns.        
     */    
    protected  double getTurnValue(Point a, Point b, Point c) {

        // use doubles for more accuracy
        double crossProduct = (((double)b.x - a.x) * ((double)c.y - a.y)) -
            (((double)b.y - a.y) * ((double)c.x - a.x));
            
        return crossProduct;
    }

        
    /**
     * This is a tester method.
     */
    public static List<Point> TESTER() {
        Point a =  new Point( 1 , 1  );
        Point a2 =  new Point(0 ,  0 );
        Point b =  new Point( 2 , 3  );
        Point c =  new Point( -3 , 0  );
        Point d =  new Point( 0 , -4  );
        Point e =  new Point( 5 , 2  );
        Point f =  new Point( -4 ,  5 );
        Point g =  new Point( -2 ,  -3 );
        Point h =  new Point( -1 ,  0 );
        Point i =  new Point( 1 ,  -1 );

        ArrayList<Point> array = new ArrayList<Point>();
        array.add(a); array.add(b);array.add(c);array.add(d);array.add(e);array.add(f);array.add(g); array.add(a2);array.add(h);array.add(i); 

        GrahamScan x = new GrahamScan();
        return x.getConvexHull(array);
    }

    /**
     * Returns true if all points in points are collinear. This is a helper method.
     *
     * @param points the list of points.
     * @return       true iff all points in points are collinear.
     */
    protected  boolean areAllCollinear(List<Point> points) {

        if(points.size() < 2) {
            return true;
        }

        final Point a = points.get(0);  //take two points
        final Point b = points.get(1);

        for(int i = 2; i < points.size(); i++) {

            Point c = points.get(i);  //get the next point

            if(! this.getTurn(a, b, c).equals( "COLLINEAR") ){  //if any two are not collinear
                return false;
            }
        }

        return true;
    }

    /**
     * This is a private turn class that I made for clarity. It just gives a direction to the turn.
     * Depending on the value of the comparator, for positive the turn is left that is anticlockwise and vice versa.
     */
    private class Turn { 
        //Turn can take three values CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR.
        private String turnDirection ;

        private Turn (String turn) {

            if ( turn.equalsIgnoreCase("CLOCKWISE")) turnDirection = "CLOCKWISE";
            if ( turn.equalsIgnoreCase("COUNTER_CLOCKWISE")) turnDirection = "COUNTER_CLOCKWISE";
            if ( turn.equalsIgnoreCase("COLLINEAR") )turnDirection = "COLLINEAR"; 

        }

        public boolean equals ( String B) {
            if ( this.turnDirection.equals( B ) ) return true;
            else return false;
        }
    }

  
}
