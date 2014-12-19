
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Arrays;

/**
 * This class contains the code for all the GUI stuff in the application that 
 * computes the convex hull.
 */
public class ConvexHullGui extends JFrame 
implements ActionListener, MouseListener {

    // The radius in pixels of the circles drawn in graph_panel

    final int NODE_RADIUS = 3;

    final Integer ZERO = new Integer( 0);
    // GUI stuff
    CanvasPanel canvas = null;

    JPanel buttonPanel = null;
    
    //This is gui stuff for the top Panel i.e. the Szajda Panel
    JLabel szajdaPanel = null;
    boolean szajdaSpoke =  false;
    //Sample messages to say
    final String[] szajdaSays = { "Beautiful", "dat convex hull", "OMG", "it behooves me" };
    
    //RAndom generator for generating messages
    Random generator = new Random (441 );
    JButton drawButton, clearButton;  //the all mighty buttons

    ImageIcon szajdaImage = new ImageIcon("doug.jpg");

    // Data Structures for the Points

    /*This holds the set of vertices, all
     * represented as type Point.
     */
    LinkedList<Point> vertices = null;

    //an instance of the Graham Scan class
    GrahamScan gs = new GrahamScan();

    //This is the arrayList containing points on the convex hull
    ArrayList<Point>  convexHull =  null;
    // Event handling stuff
    Dimension panelDim = null;

    public ConvexHullGui() {
        super("Convex Hull Application");
        setSize(new Dimension(700,775));

        // Initialize main data structures
        initializeDataStructures();

        //The content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, 
                BoxLayout.Y_AXIS));

        //Create the drawing area
        canvas = new CanvasPanel(this);
        canvas.addMouseListener(this);

        Dimension canvasSize = new Dimension(700,500);
        canvas.setMinimumSize(canvasSize);
        canvas.setPreferredSize(canvasSize);
        canvas.setMaximumSize(canvasSize);
        canvas.setBackground(Color.black);

        // Create buttonPanel and fill it
        buttonPanel = new JPanel();
        Dimension panelSize = new Dimension(700,75);
        buttonPanel.setMinimumSize(panelSize);
        buttonPanel.setPreferredSize(panelSize);
        buttonPanel.setMaximumSize(panelSize);
        buttonPanel.setLayout(new BoxLayout(buttonPanel,
                BoxLayout.X_AXIS));
        buttonPanel.
        setBorder(BorderFactory.
            createCompoundBorder(BorderFactory.
                createLineBorder(Color.red),
                buttonPanel.getBorder()));

        //making the convex hull button
        Dimension buttonSize = new Dimension(175,50);
        drawButton = new JButton("Compute Convex Hull");
        drawButton.setMinimumSize(buttonSize);
        drawButton.setPreferredSize(buttonSize);
        drawButton.setMaximumSize(buttonSize);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawButton.setActionCommand("computeConvexHull");
        drawButton.addActionListener(this);
        drawButton.
        setBorder(BorderFactory.
            createCompoundBorder(BorderFactory.
                createLineBorder(Color.green),
                drawButton.getBorder()));

        //making the clear button        
        clearButton = new JButton("Clear");
        clearButton.setMinimumSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);
        clearButton.setMaximumSize(buttonSize);
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.setActionCommand("clearDiagram");
        clearButton.addActionListener(this);
        clearButton.
        setBorder(BorderFactory.
            createCompoundBorder(BorderFactory.
                createLineBorder(Color.blue),
                clearButton.getBorder()));

        //putting the buttons together
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(drawButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        //EXTRA CODE GOES HERE
        Dimension cPanelSize = new Dimension(szajdaImage.getIconWidth(),szajdaImage.getIconHeight());
        coolPanel coolPanel =  new coolPanel();
        
        //sets up all the cool panel stuff
        coolPanel.setMinimumSize(cPanelSize);
        coolPanel.setPreferredSize(new Dimension(3*szajdaImage.getIconWidth(),szajdaImage.getIconHeight()));
        coolPanel.setMaximumSize(new Dimension(3*szajdaImage.getIconWidth(),szajdaImage.getIconHeight()));
        coolPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        //coolPanel.setLayout(new BoxLayout(coolPanel,
        //  BoxLayout.Y_AXIS));

        szajdaPanel = new JLabel( szajdaImage );
        szajdaPanel.setPreferredSize(cPanelSize);
        szajdaPanel.setAlignmentX((Component.CENTER_ALIGNMENT));
        szajdaPanel.setAlignmentY((Component.BOTTOM_ALIGNMENT));
        szajdaPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

        
        szajdaPanel.setMinimumSize(cPanelSize);
        szajdaPanel.setPreferredSize(cPanelSize);
        szajdaPanel.setMaximumSize(cPanelSize);
        coolPanel.add(szajdaPanel);

        //adds humor and functionalty to the convex hull application.
        
        contentPane.add(canvas);
        contentPane.add(buttonPanel);
        contentPane.add(coolPanel);
    }

    public static void main(String[] args) {

        ConvexHullGui project = new ConvexHullGui();  //create a swing shell
        project.addWindowListener(new WindowAdapter() {
                public void 
                windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
        );
        project.pack();  //pack the project and make it visible
        project.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String buttonIdentifier = e.getActionCommand(); //the action command of the method

        if (buttonIdentifier.equals("computeConvexHull")) {
            //compute the convex hull and display
            if ( vertices != null && (!vertices.isEmpty()) ){
                //                 canvas.changeColor();
                //                 canvas.repaint();

                //gets the convex hull
                convexHull = gs.getConvexHull(new ArrayList<Point>(vertices));

                //System.out.print( convexHull); 
                //sets booelean to true as the convex hull button was pressed
                canvas.wasCalc = true;
                //sets the convex hull in the other class to the array
                canvas.convexHull = convexHull;
                
                szajdaSpoke = true; //enable the comments!
                
                repaint();        //repaint the canvas
            }

        } else if (buttonIdentifier.equals("clearDiagram")) {
            vertices.clear();
            convexHull.clear();  //Clear both the arrays

            canvas.wasCalc = false;
            szajdaSpoke = false;
            
            canvas.convexHull.clear();  //clear this too

            repaint(); //repaint
        } 
    }

    public void mouseClicked(MouseEvent e) {
        Point click_point = e.getPoint();
        vertices.add(click_point);
        canvas.repaint();
    }

    public void initializeDataStructures() {
        vertices = new LinkedList<Point>();
    }
    
    //Unimplememted methods
    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}
    
    
    /**
     * This is a really cool JPanel that does awesome stuff
     */
    class coolPanel extends JPanel {

        coolPanel() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(3*szajdaImage.getIconWidth(),szajdaImage.getIconHeight()));
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (szajdaSpoke ) { //if ye speaks
               
                String msg = szajdaSays[generator.nextInt(4) ];// generate a random message
                g.setColor(Color.RED);
                g.fillOval(290, 65, 110, 90); //make oval
                
                g.setColor(Color.BLACK); //draws the string
                g.drawString(msg, 300, 100);
                 
            }
        }
    }

    
}
