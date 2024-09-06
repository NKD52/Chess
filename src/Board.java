import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;



public class Board {

    public static final int EMPTY = 0;
    public static int[][] square;
    public boolean whiteToMove = true;
    private Stack<String> stack = new Stack<>();
    public Stack<movesTallier> moveHistory; 
    int whiteKingLoc[]= {7,4};
    int blackKingLoc[]= {0,4};
    boolean checkMate =false;
    boolean stalemate = false;
    boolean wkingSideRookMoved = false;
    boolean wqueenSideRookMoved = false;
    boolean bkingSideRookMoved = false;
    boolean bqueenSideRookMoved = false;
    pieceMovesCalculator pcm;
    boolean enpass = false;

    Main maininstance;
    private Piece piece;

    public Board(Piece piece) {
        this.piece = piece;
        moveHistory = new Stack<>(); 
        square = new int[8][8];
        generateFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        
        // square[7][4] = piece.King | 8;  // white King
        // square[0][4] = piece.King | 16; // blck King

        // square[3][5] = piece.Rook|8;
        // square[4][7] = piece.Rook|8;

        // square[4][4] = piece.Pawn|16; //black
        // square[6][3]= piece.Pawn|8;
        // square[4][2]= piece.Pawn|8;
        // square[4][5]= piece.Pawn|8;
        // square[1][4]= piece.Pawn|16;
        // square[4][3]= piece.Pawn|8;
        
    }

    // public Board(Main maininstance){
    //    this.maininstance = maininstance;
    // }

    public void generateFEN(String fen) {
        String[] parts = fen.split(" ", 2);
        HashMap<Character, Integer> pieceMap = new HashMap<>();
        pieceMap.put('k', piece.King);
        pieceMap.put('q', piece.Queen);
        pieceMap.put('n', piece.Knight);
        pieceMap.put('b', piece.Bishop);
        pieceMap.put('r', piece.Rook);
        pieceMap.put('p', piece.Pawn);

        int c = 0;
        int r = 0;
        String firstPart = parts[0];

        for (int i = 0; i < firstPart.length(); i++) {
            char ch = firstPart.charAt(i);

            if (ch == '/') {
                c = 0;
                r++;
            } else {
                if (Character.isDigit(ch)) {
                    c += (ch - '0');
                } else {
                    int temp = pieceMap.get(Character.toLowerCase(ch));
                    temp += Character.isLowerCase(ch) ? piece.Black : piece.White;
                    square[r][c] = temp;
                    c++;
                }
            }
        }

        // Board.square[0][0]= piece.None;
        // Board.square[1][0]= piece.White | piece.Pawn;
        // Board.square[7][0]= piece.None;
        // Board.square[6][0]= piece.Black | piece.Pawn;
        // Board.square[0][0]= piece.None;


    }

    public void makeUserMove(movesTallier currentMove, GamePanel gp) {

        // Handle en passant
        if (currentMove.moveType == movesTallier.MoveType.EN_PASSANT) {
            // Move the capturing pawn to the destination square
            square[currentMove.endRow][currentMove.endCol] = currentMove.pieceMoved;
            
            // Clear the start square
            square[currentMove.startRow][currentMove.startCol] = piece.None;
            
            // Remove the opponent's pawn captured by en passant
            square[currentMove.enPassantCapturedPawnRow][currentMove.endCol] = piece.None;
        } 
        // Handle castling
        else if (currentMove.moveType == movesTallier.MoveType.CASTLING) {
            // Move the king to the destination square
            square[currentMove.endRow][currentMove.endCol] = currentMove.pieceMoved;
            
            // Clear the starting square of the king
            square[currentMove.startRow][currentMove.startCol] = piece.None;
            
            // Handle king-side castling
            square[currentMove.newrookloc[0]][currentMove.newrookloc[1]]= square[currentMove.oldrookloc[0]][currentMove.oldrookloc[1]];
            square[currentMove.oldrookloc[0]][currentMove.oldrookloc[1]] = piece.None;
            
            // Update castling rights (if needed)
            if (whiteToMove) {
                wkingSideRookMoved = true;
                wqueenSideRookMoved = true;
            } else {
               bkingSideRookMoved = true;
                bqueenSideRookMoved = true;
            }
        } 
        // Handle normal moves
        else {
            // Move the piece to its new location
            square[currentMove.endRow][currentMove.endCol] = currentMove.pieceMoved;
            
            // Clear the old square
            square[currentMove.startRow][currentMove.startCol] = piece.None;
        }
    
        // Update the king's location if it was moved
        if (currentMove.pieceMoved == (piece.King | piece.White)) {
            whiteKingLoc[0] = currentMove.endRow;
            whiteKingLoc[1] = currentMove.endCol;
        } else if (currentMove.pieceMoved == (piece.King | piece.Black)) {
            blackKingLoc[0] = currentMove.endRow;
            blackKingLoc[1] = currentMove.endCol;
        }
    
        // Push the move to the history stack
    
        // Toggle the turn
        whiteToMove = !whiteToMove;
    
        // Repaint the game panel
        gp.repaint();
    }
    





    public void undoMove(movesTallier lastMove, GamePanel gp) {
         if (moveHistory.isEmpty()) return; // No moves to undo
    
        // Pop the last move from the stack
    
        // Handle undo for en passant
        if (lastMove.moveType == movesTallier.MoveType.EN_PASSANT) {
            // Restore the captured pawn on its original position
            square[lastMove.startRow][lastMove.startCol] = lastMove.pieceMoved;
            
            // Remove the capturing pawn from its landing position
            square[lastMove.endRow][lastMove.endCol] = piece.None;
            
            // Revert the moving pawn to its original position
            square[lastMove.enPassantCapturedPawnRow][lastMove.enPassantCapturedPawnCol] = piece.Pawn | (whiteToMove? piece.White : piece.Black);
        } 
        // Handle undo for castling
        else if (lastMove.moveType == movesTallier.MoveType.CASTLING) {
            // Move the king back to its original position
            square[lastMove.startRow][lastMove.startCol] = lastMove.pieceMoved;
            
            // Clear the king's new position
            square[lastMove.endRow][lastMove.endCol] = piece.None;
            
            // Move the rook back to its original position
            square[lastMove.oldrookloc[0]][lastMove.oldrookloc[1]] = square[lastMove.newrookloc[0]][lastMove.newrookloc[1]];
            square[lastMove.newrookloc[0]][lastMove.newrookloc[1]] = piece.None;
            // Update castling rights (if needed)
            if (!whiteToMove) {
                wkingSideRookMoved = false;
                wqueenSideRookMoved = false;
            } else {
               bkingSideRookMoved = false;
                bqueenSideRookMoved = false;
            }
        } 
        // Handle normal moves
        else {
            // Revert the piece to its original position
            square[lastMove.startRow][lastMove.startCol] = lastMove.pieceMoved;
            
            // Restore the captured piece (if any) to its original position
            square[lastMove.endRow][lastMove.endCol] = lastMove.pieceCaptured;
        }
    
        // Restore king location for white and black, if needed
        if (lastMove.pieceMoved == (piece.King | piece.White)) {
            whiteKingLoc[0] = lastMove.startRow;
            whiteKingLoc[1] = lastMove.startCol;
        } else if (lastMove.pieceMoved == (piece.King | piece.Black)) {
            blackKingLoc[0] = lastMove.startRow;
            blackKingLoc[1] = lastMove.startCol;
        }
    
        // Change the turn back to the previous player
        whiteToMove = !whiteToMove;
    
        // Repaint the game panel to reflect the updated board state
        gp.repaint();
    }

   

    

    public boolean isEnPassantMove(int whatToMove[], int []whereToMove, boolean isWhiteToMove) {
        printBoard();

        int startRow = whatToMove[0];
        int startCol = whatToMove[1];
        int endRow = whereToMove[0];
        int endCol = whereToMove[1];
        
    
        // Check if the piece is a pawn
        int pics = square[startRow][startCol];
        if ((pics & 7) != piece.Pawn) return false;
    
        // Ensure it's a diagonal move
        if (Math.abs(startCol - endCol) != 1) return false;
    
        // // Ensure the pawn is moving in the correct direction and only one row forward
        // int direction = isWhiteToMove ? -1 : 1;
        // if (endRow != startRow + direction) return false;
    
        // Check for opponent pawn
        int capturedPiece = square[startRow][endCol];
        if ((capturedPiece & 7) != piece.Pawn || piece.isMyPiece(capturedPiece, isWhiteToMove)) return false;
    
        // Ensure move history is non-empty and last move matches en passant conditions
        if (!moveHistory.isEmpty()) {
            movesTallier lastMove = moveHistory.peek();
            
            // Combine last move checks
            if (piece.getPieceType(lastMove.pieceMoved) == piece.Pawn &&
                Math.abs(lastMove.startRow - lastMove.endRow) == 2 &&
                lastMove.endRow == startRow && lastMove.endCol == endCol) {
                return true;
            }
        }
    
        return false;
    }
    
    

    public void printBoard() {
        for (int row = 0; row < square.length; row++) {
            for (int col = 0; col < square[row].length; col++) {
                System.out.print(getPieceSymbol(square[row][col]) + " "); // Get piece symbol and print it
            }
            System.out.println(); // Move to the next line after printing each row
        }
        System.out.println(); // Add a blank line after the board for clarity
    }
    
    private static final int WHITE_MASK = 8;
    private static final int BLACK_MASK = 16;

private String getPieceSymbol(int pics) {
    switch (pics & 7) {
        case 1:
            return (pics & WHITE_MASK) == WHITE_MASK ? "K" : "k";
        case 6:
            return (pics & WHITE_MASK) == WHITE_MASK ? "Q" : "q";
        case 5:
            return (pics & WHITE_MASK) == WHITE_MASK ? "R" : "r";
        case 4:
            return (pics & WHITE_MASK) == WHITE_MASK ? "B" : "b";
        case 3:
            return (pics & WHITE_MASK) == WHITE_MASK ? "N" : "n";
        case 2:
            return (pics & WHITE_MASK) == WHITE_MASK ? "P" : "p";
        default:
            return "."; // Empty space or unknown piece
    }
}

public void castling(movesTallier currentMove){

    //for king side castle white
    if (currentMove.endRow==currentMove.startCol && currentMove.endCol==currentMove.startCol+2) {
        square[currentMove.endRow][currentMove.endCol] = currentMove.pieceMoved;  // Move piece
        square[currentMove.startRow][currentMove.startCol] = piece.None;  // Clear the old square    
        square[currentMove.startRow][currentMove.startCol+1]= piece.Rook| (whiteToMove? piece.White:piece.Black);
    }
}

    
    
    
}
