public class movesTallier {

    public enum MoveType {
        NORMAL, EN_PASSANT, CASTLING, PROMOTION
    }
    
    public int startRow, startCol, endRow, endCol;
    public int pieceMoved, pieceCaptured;
    public MoveType moveType;
    Board board;
    
    // For specific moves
    public boolean wasEnPassant;
    public int enPassantCapturedPawnRow, enPassantCapturedPawnCol;
    public int[] newrookloc, oldrookloc;
    public boolean promotion;
    public int promotedPiece;

    public movesTallier(int[] whatToMove, int[] whereToMove, MoveType moveType) {
        this.startRow = whatToMove[0];
        this.startCol = whatToMove[1];
        this.endRow = whereToMove[0];
        this.endCol = whereToMove[1];
        this.pieceMoved = Board.square[startRow][startCol];
        this.pieceCaptured = Board.square[endRow][endCol];
        this.moveType = moveType;
    }

    // public movesTallier(Board board, int whatToMove[], int whereToMove[], boolean wasEnPassant, int enPassantCapturedPawnRow, int enPassantCapturedPawnCol) {
    //     this.board =board;
    //     this.startRow = whatToMove[0];
    //     this.startCol = whatToMove[1];
    //     this.endRow = whereToMove[0];
    //     this.endCol = whereToMove[1];

    //     // Capture the state of the board for the undo
    //     this.pieceMoved = Board.square[startRow][startCol];
    //     this.pieceCaptured = Board.square[endRow][endCol];
        
    //     this.wasEnPassant = wasEnPassant;
    //     this.enPassantCapturedPawnRow = enPassantCapturedPawnRow;
    //     this.enPassantCapturedPawnCol = enPassantCapturedPawnCol;
    //     this.moveType =moveType;
    // }
    
    public void setEnPassant(int capturedRow, int capturedCol) {
        this.wasEnPassant = true;
        this.enPassantCapturedPawnRow = capturedRow;
        this.enPassantCapturedPawnCol = capturedCol;
    }

    public void setCastlingMove(int []newrookloc, int oldrookloc[]) {
        this.newrookloc = newrookloc;
        this.oldrookloc = oldrookloc;
    }

    public void setPromotion(int promotedPiece) {
        this.promotion = true;
        this.promotedPiece = promotedPiece;
    }
}
