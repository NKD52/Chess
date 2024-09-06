import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.*;


public class GamePanel extends JPanel implements Runnable {
  public int counter = 0;
    // screen size
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 4;
    final int tileSize = scale * originalTileSize;

    final int maxHorizontalCol = 8;
    final int maxVerticalRow = 8;
    final int screenWidth = tileSize * maxHorizontalCol;
    final int screenHeight = tileSize * maxVerticalRow;

    Piece piece;
    Board board;
    Thread gameThread;

    public boolean Pawnmoved = false;
    boolean promotionState = false;
    boolean white=true;
    int r = -1, col = -1;  // Track row and column for promotion

    public static boolean flag = false;
    public ArrayList<int[]> yessir = new ArrayList<>();

    // Constructor: Initializes with Board, Piece, and ValidMoves
    public GamePanel(Board board) { 
        this.board = board;
        this.piece = new Piece(this);  

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);  // Improves rendering performance

        this.setFocusable(true);
        this.requestFocusInWindow();

        // Add key listener once in the constructor
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (promotionState) {
                    if (r != -1 && col != -1) {  // Ensure promotion state is active
                        int value = board.whiteToMove ? 16 : 8;  // 8 for White, 16 for Black
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_Q:
                                Board.square[r][col] = 6+ value;  // Promote to Queen
                                System.out.println("Pawn promoted to Queen.");
                                break;
                            case KeyEvent.VK_N:
                                Board.square[r][col] = 3+ value;  // Promote to Knight
                                System.out.println("Pawn promoted to Knight.");
                                break;
                            case KeyEvent.VK_B:
                                Board.square[r][col] = 4+ value;  // Promote to Bishop
                                System.out.println("Pawn promoted to Bishop.");
                                break;
                            case KeyEvent.VK_R:
                                Board.square[r][col] = 5+ value;  // Promote to Rook
                                System.out.println("Pawn promoted to Rook.");
                                break;
                        }
                        promotionState = false;
                        r = -1;  // Reset promotion state after promotion
                        col = -1;
                        value = 0;
                    }
                }
                
            }
        });
    }

    // Start game loop
    public void gameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            if (Pawnmoved) {
                promotionState = true;
                update();  
            }
            Pawnmoved = false;
            repaint();

            try {
                Thread.sleep(1000 / 60); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Game logic updates
    public void update() {
        boolean whiteTurn = board.whiteToMove;
        int row = whiteTurn ? 7 : 0;

        for (int i = 0; i <= 7; i++) {
            if (piece.getPieceType(board.square[row][i]) == (piece.Pawn) ) {  
                System.out.println("Please enter the following keys for promotion: 1) B 2) R 3) N 4) Q");
                r = row;  
                col = i;
                break;  
            }
        }
        
    }
    

    // Rendering the board, pieces, and highlights
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        

            // Draw the chess board
            makeBoard(g2d);

            // Highlight valid moves (if any)
            makeHighlights(g2d);

            if (board.whiteToMove) {
            g2d.setColor(new Color(145, 133, 0, 160)); // Transparent red
            g2d.fillRect(4 * tileSize, 7 * tileSize, tileSize, tileSize);
            }

            // Draw chess pieces
            piece.drawpieces(g2d);
            counter++;

            // if (board.checkMate) {
            //     g2d.setColor(Color.RED);
            //     g2d.setFont(new Font("Arial", Font.BOLD, 40));
            //     if (board.whiteToMove) {
            //         white=false;
            //     }
            //     if (white) {
            //         g2d.drawString("Checkmate! White wins!", screenWidth / 2, screenHeight / 2);
            //     } else {
            //         g2d.drawString("Checkmate! Black wins!", screenWidth / 2, screenHeight / 2);
            //     }
            // } else if (board.stalemate) {
            //     g2d.drawString("Stalemate! It's a draw!", screenWidth / 2, screenHeight / 2);
            // }
           
            g2d.dispose();
 
       
    }

    // Method to draw the chess board
    public void makeBoard(Graphics2D g2d) {
        for (int row = 0; row < maxVerticalRow; row++) {
            for (int col = 0; col < maxHorizontalCol; col++) {
                if ((row + col) % 2 == 0) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(new Color(196, 164, 132));
                }
                g2d.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    public void makeHighlights(Graphics2D g2d){
      if (flag) {
        g2d.setColor(new Color(255, 0, 0, 128)); // Transparent red
        for (int[] move : yessir) {
              int row = move[0];
              int col = move[1];
              g2d.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
          }
      }
    }

    // Set and highlight valid moves
    public void setcol(ArrayList<int[]> list) {
        yessir = list;
        flag = true;
    }
}
