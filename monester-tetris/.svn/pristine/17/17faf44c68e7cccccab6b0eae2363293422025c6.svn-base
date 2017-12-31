/*
 * Tetris GUI
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Board;

/**
 * The GUI for Tetris.
 * 
 * @author richardyang
 * @version 11.28
 */
public class TetrisGUI {

    /** A minimum size for the frame. */
    private static final Dimension MIN_SIZE = new Dimension(390, 410);
    
    /** The top level Window for this GUI. */
    private final JFrame myFrame;
    
    /** The board to play with. */
    private Board myBoard;
    
    /** The display panel for game board. */
    private final DisplayPanel myDisplayPanel;

    /** The east panel. */
    private final EastPanel myEastPanel;
    
    /**
     * Construct the GUI.
     */
    public TetrisGUI() {
        
        myBoard = setupBoard();
        
        myFrame = new JFrame("Tetris");
        myFrame.setMinimumSize(MIN_SIZE);
        
        myDisplayPanel = new DisplayPanel(myFrame, myBoard);
        myEastPanel = new EastPanel(myDisplayPanel);
        setup();
        
    }
    
    /**
     * Helper function for setting up game board.
     * 
     * @return myBoard the game board
     */
    public final Board setupBoard() {
        final JTextField width = new JTextField(5);
        final JTextField height = new JTextField(5);
        final JPanel panel = new JPanel();
        panel.add(new JLabel("New Game"));
        panel.add(new JLabel("Width:"));
        panel.add(width);
        panel.add(new JLabel("Height:"));
        panel.add(height);
        final int result = JOptionPane.showConfirmDialog(null, panel, 
                         "Enter Width and Height(Default is 10*20)", JOptionPane.OK_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            
            if (width.getText() == null || height.getText() == null) {
                final int widt = Integer.parseInt(width.getText());
                final int heigh = Integer.parseInt(height.getText());
                myBoard = new Board(widt, heigh);
            } else {
                myBoard = new Board();
            }
            
        } else {
            myBoard = new Board();
        }
        
        return myBoard;
    }
    
    /**
     * Sets up the GUI.
     */
    public final void setup() {
        
        myBoard.addObserver(myEastPanel);
        myBoard.addObserver(myDisplayPanel);
        myDisplayPanel.addPropertyChangeListener(myEastPanel);
        myFrame.add(myDisplayPanel, BorderLayout.CENTER);
        myFrame.add(myEastPanel, BorderLayout.EAST);
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
        myDisplayPanel.requestFocus();
        
    }

}
