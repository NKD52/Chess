import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class endGame extends JPanel {
    Board board;
    Piece piece;
    GamePanel gp;

    boolean white = false;  // Flag to determine the winner (true if white wins, false if black wins)

    public endGame(Board board, Piece piece, GamePanel gp) { 
        this.board = board;
        this.piece = piece;
        this.gp = gp;

        this.setPreferredSize(new Dimension(gp.screenWidth, gp.screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);  // Improves rendering performance
    }

    // Rendering the endgame screen and results
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // // Call to render the board
        // makeBoa(g2d);

        // Set text color and font for the endgame message
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 40));

        // Check and display the winner or draw
        if (board.checkMate) {
            if (board.whiteToMove) {
                white=true;
            }
            if (white) {
                g2d.drawString("Checkmate! White wins!", gp.screenWidth / 4, gp.screenHeight / 2);
            } else {
                g2d.drawString("Checkmate! Black wins!", gp.screenWidth / 4, gp.screenHeight / 2);
            }
        } else if (board.stalemate) {
            g2d.drawString("Stalemate! It's a draw!", gp.screenWidth / 4, gp.screenHeight / 2);
        }
    }

    // Render the board (optional, based on your use case)
    public void makeBoa(Graphics2D g2d) {
        for (int row = 0; row < gp.maxVerticalRow; row++) {
            for (int col = 0; col < gp.maxHorizontalCol; col++) {
                if (white) {
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(new Color(196, 164, 132));
                }
                g2d.fillRect(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);
            }
        }
    }
}
