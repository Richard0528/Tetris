/*
 * Tetris display panel
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Board;
import model.Point;

/**
 * The display panel for Tetris.
 * 
 * @author richardyang
 * @version 11.28
 */
public class DisplayPanel extends JPanel implements Observer {
    
    /** The default row number for Tetris board. */
    public static final int ROW = 10;
    
    /** The default column number for Tetris board. */
    public static final int COLUMN = 20;
    
    /** The default delay (in milliseconds) for the move timer. */
    public static final int MOVE_DELAY = 250;
    
    /** The preferred window size. */
    public static final Dimension PANEL_DEFAULT_SIZE = new Dimension(210, 410);
    
    /** Generated serial ID. */
    private static final long serialVersionUID = 1L;

    /** The font size for "pause". */
    private static final int FONT_SIZE = 30;

    /** The focus listener. */
    private final FListener myFocusListener;
    
    /** The key listener. */
    private final KeyListener myKeyListener;
    
    /** Timer. */
    private final Timer myMoveTimer;
    
    /** The board to play with. */
    private final Board myBoard;

    /** The boolean checks the pause status. */
    private final JFrame myFrame;
    
    /** The glass pane for "pause". */
    private final GlassPane myGlassPane;
    
    /** The width for game board. */
    private final int myWidthBlock;
    
    /** The height for game board. */
    private final int myHeightBlock;
    
    /** The String to display when pausing. */
    private String myText;
    
    /**
     * Constructs a display panel.
     * 
     * @param theFrame the whole frame
     * @param theBoard the game board
     */
    public DisplayPanel(final JFrame theFrame, final Board theBoard) {
        super();
        myGlassPane = new GlassPane();
        myFocusListener = new FListener();
        myKeyListener = new KeyListener();
        myMoveTimer = new Timer(MOVE_DELAY, new MoveListener());
        myBoard = theBoard;
        myWidthBlock = myBoard.getWidth();
        myHeightBlock = myBoard.getHeight();
        myFrame = theFrame;
        setUpPanel();
    }

    /**
     * Sets up the panel.
     */
    private void setUpPanel() {
        myMoveTimer.setInitialDelay(0);
        myFrame.setGlassPane(myGlassPane);
        myGlassPane.setVisible(false);
        addMouseListener(new MouseListener());

        setMinimumSize(PANEL_DEFAULT_SIZE);
        setBackground(Color.WHITE);
        
    }

    /**
     * @param theMoveDelay the myMoveDelay to set
     */
    public void setMyMoveDelay(final int theMoveDelay) {
        myMoveTimer.setDelay(theMoveDelay);
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setPaint(Color.BLACK);
        
        setBackground(Color.WHITE);
        g2d.setPaint(Color.BLACK);
        final int blockSize = getBlockSize();
        /////////////////////////////////////////////////////////////////////////
        //Grid
        //        final int gridHSpace = 5;
        //        final int blockSize = (getSize().height - 2 * gridHSpace) / 20;
        //        final int gridWSpace = (getSize().width - WIDTH_BLOCK * blockSize) / 2;

        //        for (int i = 0; i < HEIGHT_BLOCK + 1; i++) {
        //            final int y = gridHSpace + i * blockSize;
        //            g2d.drawLine(gridWSpace, y, getSize().width - gridWSpace, y);
        //        }
        //        for (int j = 0; j < WIDTH_BLOCK + 1; j++) {
        //            final int x = gridWSpace + j * blockSize;
        //            g2d.drawLine(x, gridHSpace, x, getSize().height - gridHSpace);
        //        }

        //////////////////////////////////////////////////////////////////////////
        //Piece
        final List<Point> startP = getPoint();

        // locate the start point by locating the mid point first.
        final Point start = new Point(getWidth() / 2 - myWidthBlock / 2 * blockSize, 
                                      getHeight() / 2 - myHeightBlock / 2 * blockSize);

        // draw the rectangle boundary
        g2d.draw(new Rectangle2D.Double(start.getX(), 
                                        start.getY(), 
                                        myWidthBlock * blockSize, myHeightBlock * blockSize));

        // loop through all the pieces
        final int corner = 10;
        for (final Point a : startP) {
            g2d.setPaint(Color.BLACK);
            g2d.fill(new RoundRectangle2D.Double(start.getX() + blockSize * a.getX(),
                start.getY() + blockSize * a.getY(), blockSize, blockSize, corner, corner));
            
            g2d.setPaint(Color.GRAY);
            g2d.draw(new RoundRectangle2D.Double(start.getX() + blockSize * a.getX(),
                start.getY() + blockSize * a.getY(), blockSize, blockSize, corner, corner));
            
        }
        
    }
    
    /**
     * A method to decide block size adapting the window size.
     * 
     * @return the block size
     */
    private int getBlockSize() {
        final int gridSpace = 5;
        final int blockSize;
        final int rate = myHeightBlock / myWidthBlock;
        if (rate * getWidth() >= getHeight()) {
            blockSize = (getSize().height - rate * gridSpace) / myHeightBlock;
        } else {
            blockSize = (getSize().width - rate * gridSpace) / myWidthBlock;
        }
        return blockSize;
    }

    /**
     * Help function for painting pieces.
     * 
     * @return piece rectangle location
     */
    private List<Point> getPoint() {
        
        final String[] gameBoard = myBoard.toString().split("\n");
        final ArrayList<Point> startP = new ArrayList<>();

        //omit the first 5 rows.
        final int omitRow = 5;
        for (int row = omitRow; row < gameBoard.length; row++) {
            final String line = gameBoard[row];
            for (int col = 1; col < line.length() - 1; col++) {
                if (line.charAt(col) != '-' && line.charAt(col) != '|' 
                                && line.charAt(col) != ' ') {
                    startP.add(new Point(col - 1, row - omitRow));
                }
            }
        }
        return startP;
        
    }

    /** Starts a new game. */
    public void start() {
        myBoard.newGame();
        myMoveTimer.start();
        myGlassPane.setVisible(false);
        myGlassPane.repaint();
        setFocusable(true);
        addKeyListener(myKeyListener);
        addFocusListener(myFocusListener);
    }
    
    /** Resume the game. */
    public void resume() {
        myText = "";
        myMoveTimer.start();
        myGlassPane.setVisible(false);
        myGlassPane.repaint();
    }
    
    /** Stop the timer. */
    public void stop() {
        
        myText = "Pause";
        myMoveTimer.stop();
        myGlassPane.setVisible(true);
        myGlassPane.repaint();
    }
    
    /** Completely stop the timer. */
    public void end() {
        myText = "Game Over";
        myMoveTimer.stop();
        
        myGlassPane.setVisible(true);
        myGlassPane.repaint();
        removeFocusListener(myFocusListener);
        removeKeyListener(myKeyListener);

    }
    
    @Override
    public void update(final Observable theObj, final Object theArg) {
        
        if (theArg instanceof Boolean) {
            end();
            firePropertyChange("end", null, false);
        }
        
        repaint();
    }
    
    // *********** Inner Class Listener   *********************************

    /**
     * A class that listens for timer events and moves the shape, checking for
     * the window boundaries and changing direction as appropriate.
     */
    private class MoveListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.down();

            
//            System.out.println(myBoard.toString());

        }
       
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Inner class
    
    /**
     * Focus listener for detecting focus status.
     * 
     * @author richardyang
     */
    private class FListener implements FocusListener {

        @Override
        public void focusGained(final FocusEvent theA) {
            resume();
        }

        @Override
        public void focusLost(final FocusEvent theB) {
            stop();
        }

    }
    
    /**
     * Keyboard listener for game control.
     * 
     * @author richardyang
     */
    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (theEvent.getKeyCode() == KeyEvent.VK_A
                            || theEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                myBoard.left();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_S
                            || theEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                myBoard.down();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_D
                            || theEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                myBoard.right();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_W
                            || theEvent.getKeyCode() == KeyEvent.VK_UP) {
                myBoard.rotate();
            } else if (theEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                myBoard.drop();
            }
        }
    }

    /**
     * Mouse listener for focusing on game board.
     * 
     * @author richardyang
     */
    private class MouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(final MouseEvent theC) {
            requestFocus();
            
        }
    }
    
    /**
     * Glass pane for pausing.
     * 
     * @author richardyang
     */
    private class GlassPane extends JComponent {
        
        /** Generated serial ID. */
        private static final long serialVersionUID = -1130387819079316668L;
        
        /**
         * Construct the glass pane.
         */
        GlassPane() {
            super();
            setOpaque(false);
        }
        
        @Override
        public void paintComponent(final Graphics theGraphics) {
//            super.paintComponent(theGraphics);
            final Graphics2D g2d = (Graphics2D) theGraphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            
            
            final Font font = new Font(null, Font.PLAIN, FONT_SIZE);
            g2d.setFont(font);
            g2d.setPaint(Color.GRAY);

            final FontRenderContext renderContext = g2d.getFontRenderContext();
            final GlyphVector glyphVector = font.createGlyphVector(renderContext, myText);
            final Rectangle2D visualBounds = glyphVector.getVisualBounds().getBounds();

            final int x =
                (int) ((getWidth() / 2 - visualBounds.getWidth()) / 2 - visualBounds.getX());
            final int y =
                (int) ((getHeight() - visualBounds.getHeight()) / 2 - visualBounds.getY());

            // display the text visually centered in the JPanel
            g2d.drawString(myText, x, y);
            
            
        }
    }


    
}
