/*
 * East panel for tetris
 */
package view;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import model.MovableTetrisPiece;
import model.Point;

/**
 * Construct east panel.
 * 
 * @author richardyang
 * @version 11.30
 */
public class EastPanel extends JPanel implements Observer, PropertyChangeListener {

    /** A generated version ID for Serialization. */
    private static final long serialVersionUID = 3457243357781833162L;

    /** A default panel size. */
    private static final Dimension DEFAULT_SIZE = new Dimension(180, 410);

    /** A default width. */
    private static final int DEFAULT_WIDTH = 180;

    /** A default height for preview and score panel. */
    private static final int DEFAULT_HEIGHT = 140;

    /** A default height for control panel. */
    private static final int DEFAULT_HEIGHT1 = 120;
    
    /** A display panel. */
    private final DisplayPanel myDisplayPanel;
    
    /** Tostring output of the moveabletetris piece. */
    private String myBlock;

    /** The score for score panel. */
    private int myScore;
    
    /** The scoreLabel which will be updated everytime. */
    private final JLabel myScoreLabel = new JLabel("Your score is 0");
    
    /** The levelLabel which shows current level. */
    private final JLabel myLevel = new JLabel("Level : 0");
    
    /** The reminding lines needs to clear to next level. */
    private final JLabel myLine = new JLabel("Lines to level up : 3");

    /** The end game button. */
    private final JButton myEndButton;
    
    /** The start game button. */
    private final JButton myStartButton;

    /**
     * Construct the east panel.
     * 
     * @param theDisplayPanel 
     */
    public EastPanel(final DisplayPanel theDisplayPanel) {
        super();
        myEndButton = new JButton("End Game");
        myStartButton = new JButton("Start Game");
        myDisplayPanel = theDisplayPanel;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMinimumSize(DEFAULT_SIZE);
        setBackground(Color.WHITE);
        
        setupPreview();
        setupScore();
        setupFunctionDetail();
        
    }
    
    /**
     * Set up preview panel.
     */
    private void setupPreview() {
        
        final JPanel previewPanel = new ReviewPanel();
        
        previewPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        previewPanel.setBackground(Color.WHITE);
        final TitledBorder title1 = BorderFactory.createTitledBorder(null,
                                      "Preview", TitledBorder.LEFT, TitledBorder.TOP);
        previewPanel.setBorder(title1);
        add(previewPanel);
        
    }
    
    /**
     * Set up score panel.
     */
    private void setupScore() {
        final JPanel scorePanel = new JPanel();
        scorePanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        scorePanel.setBackground(Color.WHITE);
        final TitledBorder title2 = BorderFactory.createTitledBorder(null,
                                      "Score", TitledBorder.LEFT, TitledBorder.TOP);
        scorePanel.setBorder(title2);
        
        final Box text = Box.createVerticalBox();
        text.add(myLevel);
        text.add(myLine);
        text.add(myScoreLabel);
       
        scorePanel.add(text);
        
        add(scorePanel);
    }
    
    /**
     * Set up control panel.
     */
    private void setupFunctionDetail() {
        
        final JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT1));
        controlPanel.setBackground(Color.WHITE);
        final TitledBorder title3 = BorderFactory.createTitledBorder(null,
                            "Control Panel", TitledBorder.LEFT, TitledBorder.TOP);
        controlPanel.setBorder(title3);
        
        final AudioClip clip = Applet.newAudioClip(getClass().getResource("/Tetris.wav"));
        
        final JButton help = new JButton("Help");
        final JButton music = new JButton("Music");
        final JButton mute = new JButton("Mute");
        myEndButton.setEnabled(false);
        mute.setVisible(false);
        
        
        myStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theA) {
                
                myDisplayPanel.start();
                myDisplayPanel.requestFocus();
                myEndButton.setEnabled(true);
                myStartButton.setEnabled(false);
                
            }
        });
        
        myEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theA) {
                myDisplayPanel.end();
                myEndButton.setEnabled(false);
                myStartButton.setEnabled(true);
            }
        });
        
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theB) {
                JOptionPane.showMessageDialog(null, "Control"
                                + "\nPress \u25C0' or 'a' or 'A' to move left."
                                + "\nPress '\u25B6 or 'd' or 'D' to move right."
                                + "\nPress '\u25BC' or 's' or 'S' to move down."
                                + "\nPress '\u25B2' or 'w' or 'W' to rotate."
                                + "\nPress 'space' to drop."
                                + "\nClick outside to pause, click on board to continue"
                                + "\nTo adjust the grid size, you need to close the game "
                                + "\nthen restart again to set a new size"
                                + "\n "
                                + "\nStatics"
                                + "\nOne line for 100 points "
                                + "\nLevel up when you clear 3 lines", 
                                null, JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
        
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theB) {
                clip.play();
                music.setVisible(false);
                mute.setVisible(true);
            }
            
        });
        
        
        mute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theB) {
                clip.stop();
                music.setVisible(true);
                mute.setVisible(false);
            }
            
        });

        controlPanel.add(myStartButton);
        controlPanel.add(myEndButton);
        controlPanel.add(help);
        controlPanel.add(music);
        controlPanel.add(mute);
        
        add(controlPanel);
    }

    /**
     * A method which gets all the start points.
     * 
     * @return a arraylist containing start points
     */
    private List<Point> getPoint() {
        
        final List<Point> startP = new ArrayList<>();
        
        if (myBlock != null) {
            final String[] block = myBlock.split("\n");
            

            for (int row = 0; row < block.length; row++) {
                final String line = block[row];
                for (int col = 0; col < block.length; col++) {
                    if (line.charAt(col) != ' ') {
                        startP.add(new Point(col, row));
                    }
                }
            }
            
        }
        
        return startP;
        
    }
    
    @Override
    public void update(final Observable theObj, final Object theArg) {
        if (theArg instanceof MovableTetrisPiece) {
            myBlock = ((MovableTetrisPiece) theArg).toString();
        } else if (theArg instanceof Integer[]) {
            final int addP = ((Integer[]) theArg).length;
            myScore += addP;
            final int score1 = 100;
            final int levelup = 3;
            myScoreLabel.setText("You score is " + myScore * score1);
            
            // level up label
            myLevel.setText("Level : " + (int) myScore / levelup);
            
            // set move delay
            final int moveDelay = 250 - (int) myScore / levelup * 50;
            myDisplayPanel.setMyMoveDelay(moveDelay);
            myLine.setText("Lines to level up : " + (levelup - myScore % levelup));
        }
        
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("end".equals(theEvent.getPropertyName())) {

            myEndButton.setEnabled((boolean) theEvent.getNewValue());
            myStartButton.setEnabled(!(boolean) theEvent.getNewValue());
        } 
        
    }
    
    ////////////////////////////////////////////////////////////////////////
    // Inner class
    
    /**
     * Construct review panel.
     * 
     * @author richardyang
     */
    private class ReviewPanel extends JPanel {
        
        /** Generated serial ID. */
        private static final long serialVersionUID = 1603020774924136330L;
        
        @Override
        public void paintComponent(final Graphics theGraphics) {
            final Graphics2D g2d = (Graphics2D) theGraphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(Color.BLACK);
            
            final int blockSize = 20;
            final List<Point> startP = getPoint();
            final Point start = new Point(getWidth() / 2 - 2 * blockSize, 
                                          getHeight() / 2 - 2 * blockSize);
            
            final int edge = 10;
            for (final Point a : startP) {
                
                g2d.setPaint(Color.BLACK);
                g2d.fill(new RoundRectangle2D.Double(start.getX() + blockSize * a.getX(),
                    start.getY() + blockSize * a.getY(), blockSize, blockSize, edge, edge));
                
                g2d.setPaint(Color.GRAY);
                g2d.draw(new RoundRectangle2D.Double(start.getX() + blockSize * a.getX(),
                    start.getY() + blockSize * a.getY(), blockSize, blockSize, edge, edge));
                
            }
        }
    }

  
    
}
