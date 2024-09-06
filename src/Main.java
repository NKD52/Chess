import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Main {
    
    public static int userClicks = 0;
    public int whatToMove[] = new int[2]; //store x and y coords aka row and col
    public int whereToMove[] = new int[2]; //store value of where to move
    public boolean moveMade = false;
    public ArrayList<int[]> moves;


    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Chess");

        // Create instances of your main objects
        Main mainInstance = new Main();
        Piece piece = new Piece();  
        Board board = new Board(piece);
        GamePanel gp = new GamePanel(board);  // Pass Board to GamePanel
        // validMoves vm = new validMoves(gp, piece, board);  // Pass GamePanel, Piece, and Board to validMoves
        endGame eg = new endGame(board, piece, gp);
        pieceMovesCalculator pcm = new pieceMovesCalculator(gp, board, piece);
        

        // Mouse listener to capture clicks
        gp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!gp.promotionState){
                int x = e.getX();
                int y = e.getY();
                int col = x / gp.tileSize;
                int row = y / gp.tileSize;

                mainInstance.moveCalculator(row, col, gp, board, piece,eg,pcm);
                }
                
            }
        });

        // Key listener for undoing moves
        gp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    if (!board.moveHistory.isEmpty()) {
                        movesTallier lastMove = board.moveHistory.pop();
                        board.undoMove(lastMove, gp);
                        mainInstance.moveMade = true;
                        userClicks=0;
                        System.out.println("Move undone: " + lastMove);
                    } else {
                        System.out.println("No moves to undo.");
                    }
                }
            }
        });

        gp.setFocusable(true);
        window.add(eg);
        window.add(gp);
      
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.gameThread();  // Start the game loop/thread
    }

    // Method for handling clicks and calculating moves
    public void moveCalculator(int row, int col, GamePanel gp, Board board, Piece piece, endGame eg, pieceMovesCalculator pcm) {
        // moves = vm.getValidMoves();
        
    
        boolean WhiteToMove = board.whiteToMove;
        boolean isWhitePiece = piece.isWhitePiece(Board.square[row][col]);
        int selectedPiece = Board.square[row][col];

        // if (col >= 0 && col < gp.maxHorizontalCol && row >= 0 && row < gp.maxVerticalRow) {
              // Handle first click: Select a piece to move

        if (userClicks == 0) {

            if (selectedPiece != 0 && ((isWhitePiece && WhiteToMove) || (!isWhitePiece && !WhiteToMove))) {
                // Valid piece selected
                whatToMove[0] = row;
                whatToMove[1] = col;
                userClicks++;
                moves = pcm.getValidMoves2(row,col);
                gp.setcol(moves);  // Highlight valid moves
               
                System.out.println("Piece selected at: " + whatToMove[0] + ", " + whatToMove[1]);
            } else {
                System.out.println("Invalid selection. Select a valid piece.");
            }

        // Handle second click: Move the piece or select a different piece
        } else if (userClicks == 1) {

            // If clicking on a different valid piece of the same color, change selection
            if (selectedPiece != 0 && ((isWhitePiece && WhiteToMove) || (!isWhitePiece && !WhiteToMove))) {
                // Change the selected piece
                whatToMove[0] = row;
                whatToMove[1] = col;

                moves = pcm.getValidMoves2(row, col);  // Get valid moves for the newly selected piece
                gp.setcol(moves);  // Highlight valid moves
              
                System.out.println("Changed selection to piece at: " + whatToMove[0] + ", " + whatToMove[1]);

            // If clicking on the same piece, reset selection
            } else if (whatToMove[0] == row && whatToMove[1] == col) {
                System.out.println("Same piece clicked. Resetting.");
                resetMoveTracking();
                gp.flag = false;
                userClicks = 0;

            // If clicking on a valid destination square, make the move
            } else {
                whereToMove[0] = row;
                whereToMove[1] = col;

                boolean isValidMove = false;
                for (int[] move : moves) {
                    if (Arrays.equals(move, whereToMove) ) {
                        isValidMove = true;
                        break;
                    }
                }

                if (isValidMove) {
                    movesTallier.MoveType moveType = movesTallier.MoveType.NORMAL;  // Default move type

                    // Check for pawn promotion
                    if (piece.getPieceType(Board.square[whatToMove[0]][whatToMove[1]]) == piece.Pawn &&
                        (whereToMove[0] == 0 || whereToMove[0] == 7)) {
                        moveType = movesTallier.MoveType.PROMOTION;
                    }
                
                    // Check for en passant
                    if (piece.getPieceType(Board.square[whatToMove[0]][whatToMove[1]]) == piece.Pawn && 
                        board.isEnPassantMove(whatToMove, whereToMove, WhiteToMove)) {
                        moveType = movesTallier.MoveType.EN_PASSANT;
                    }
                
                    // Check for castling
                    if (piece.getPieceType(Board.square[whatToMove[0]][whatToMove[1]]) == piece.King &&
                        Math.abs(whereToMove[1] - whatToMove[1]) == 2) {
                        moveType = movesTallier.MoveType.CASTLING;
                    }
                
                    // Create the movesTallier object based on the determined move type
                    movesTallier currentMove = new movesTallier(whatToMove, whereToMove, moveType);
                    
                    // Handle additional properties for special move types like en passant
                    if (moveType == movesTallier.MoveType.EN_PASSANT) {
                        int capturedPawnRow = board.whiteToMove ? whereToMove[0] + 1 : whereToMove[0] - 1;
                        int capturedPawnCol = whereToMove[1];
                        currentMove.setEnPassant(capturedPawnRow, capturedPawnCol);
                    }else if(moveType == movesTallier.MoveType.PROMOTION){
                        gp.Pawnmoved = true;  // You will probably handle promotion logic separately

                    }else if(moveType == movesTallier.MoveType.CASTLING){
                        if (board.whiteToMove) {
                            if (whatToMove[1]+2 == whereToMove[1]) {
                               
                                currentMove.setCastlingMove(new int[]{7,5}, new int[]{7,7});
                            }else{
                                currentMove.setCastlingMove(new int[]{7,3}, new int[]{7,0});

                            }
                            board.wkingSideRookMoved=true;
                            board.wqueenSideRookMoved=true;
                            
                        }
                        if (!board.whiteToMove) {
                            if (whatToMove[1]+2 == whereToMove[1]) {
                               
                                currentMove.setCastlingMove(new int[]{0,5}, new int[]{0,7});
                            }else{
                                currentMove.setCastlingMove(new int[]{0,3}, new int[]{0,0});

                            }
                            board.bkingSideRookMoved=true;
                            board.bqueenSideRookMoved=true;
                            
                        }
                    }
                
                    // Now make the move
                    board.makeUserMove(currentMove, gp);
                    System.out.println("Move made from [" + whatToMove[0] + ", " + whatToMove[1] + "] to [" + whereToMove[0] + ", " + whereToMove[1] + "]");
                
                    // Save the move to the history stack
                    board.moveHistory.push(currentMove);
                
                    resetMoveTracking();
                    userClicks = 0;
                    moveMade = true;
                } else {
                    System.out.println("Invalid move attempted. Try again.");
                }
            }
        }
            

        // } else {
        //     System.out.println("Clicked outside the board.");
        //     userClicks = 0;
        // }

        if (moveMade) {
            // moves = vm.getValidMoves();
            
            moves.clear();
            moveMade = false;

            // if (board.checkMate) {
            //     eg.repaint();
            // }
        }
    }

    private void resetMoveTracking() {
        whatToMove[0] = -1;
        whatToMove[1] = -1;
        whereToMove[0] = -1;
        whereToMove[1] = -1;
    }

//    private static void endg(Board board, endGame eg, Main mainInstance) {
//         if (board.checkMate) {
//             // Display checkmate message
//             if (board.whiteToMove) {
//                 JOptionPane.showMessageDialog(null, "Checkmate! Black wins!");
//             } else {
//                 JOptionPane.showMessageDialog(null, "Checkmate! White wins!");
//             }
//             // resetBoard(board, mainInstance);  // Reset the board
//         } else if (board.stalemate) {
//             // Display stalemate message
//             JOptionPane.showMessageDialog(null, "Stalemate! It's a draw!");
//             // resetBoard(board, mainInstance);  // Reset the board
//         }
//     }
    
    // public static void resetBoard(Board board, Main mainInstance){
    //     Board board
    // }

    // public static void testCheckmate() {
    //     Board board = new Board(new Piece());
    //     Piece piece = new Piece();
    //     GamePanel gp = new GamePanel(board);
    //     validMoves vm = new validMoves(gp, piece, board);
    //     // Set up a known checkmate position on the board
    //     // For example: Fool's Mate
    //     board.square[0][4] = piece.King | 16; // White King
    //     board.square[7][4] = piece.King | 8;  // Black King
    //     board.square[1][5] = piece.Queen | 8; // Black Queen delivers checkmate
        
    //     ArrayList<int[]> moves = vm.getValidMoves3(0, 4);  // Get white's valid moves
        
    //     if (moves.isEmpty() && vm.inCheck()) {
    //         System.out.println("Checkmate detected correctly");
    //     } else {
    //         System.out.println("Checkmate detection failed");
    //     }
    // }
}       
