import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;


public class pieceMovesCalculator {
    GamePanel gp;
    Board board;
    Piece piece;
    movesTallier mt;
    boolean isCastlemove=false;
    private ArrayList<int[]> potentialEnpassant ;
    public HashMap<Coordinate, Coordinate> storeEnpassantmoves = new HashMap<>();


    public pieceMovesCalculator(GamePanel gp , Board board, Piece piece){
        this.gp = gp;
        this.piece = piece;
        this.board=board;
        potentialEnpassant = new ArrayList<int[]>();
   }

   

   

    // Getter to access the board
    public HashMap<Coordinate, Coordinate>getstoreEnpassantMoves() {
        return storeEnpassantmoves;
    }

    ////////////////validmoves///////////////////////////////

    
        public ArrayList<int[]> getValidMoves3(int row, int col) {
            ArrayList<int[]> validMoves = getOnlySelectedPieceMoves(row, col);
            ArrayList<int[]> attackMoves = new ArrayList<>();
            
            // Simulate each valid move
            for (int[] move : validMoves) {
                // movesTallier simulatedMove = new movesTallier(board, new int[]{row, col}, move);
                movesTallier.MoveType moveType =  movesTallier.MoveType.NORMAL; // Default move type
    
    
                        // Check for pawn promotion
                        if (piece.getPieceType(Board.square[row][col]) == piece.Pawn &&
                            (move[0] == 0 || move[1] == 7)) {
                            moveType = movesTallier.MoveType.PROMOTION;
                        }
                    
                        // Check for en passant
                        if (piece.getPieceType(Board.square[row][col]) == piece.Pawn && 
                            board.isEnPassantMove(new int[]{row,col}, move, board.whiteToMove)) {
                            moveType = movesTallier.MoveType.EN_PASSANT;
                           
                    
                        // Check for castling
                        if (piece.getPieceType(Board.square[row][col]) == piece.King &&
                            Math.abs(move[1] - col) == 2) {
                            moveType = movesTallier.MoveType.CASTLING;
                        }
    
                movesTallier simulatedMove = new movesTallier(new int[]{row, col}, move, moveType);
    
                // Handle additional properties for special move types like en passant
                if (moveType == movesTallier.MoveType.EN_PASSANT) {
                    int capturedPawnRow = board.whiteToMove ? move[0] + 1 : move[0] - 1;
                    int capturedPawnCol = move[1];
                    simulatedMove.setEnPassant(capturedPawnRow, capturedPawnCol);
                }else if(moveType == movesTallier.MoveType.PROMOTION){
                    gp.Pawnmoved = true;  // You will probably handle promotion logic separately
    
                }
                else if(moveType == movesTallier.MoveType.CASTLING){
                    if (board.whiteToMove) {
                        if (col+2 == move[1]) {
                           
                            simulatedMove.setCastlingMove(new int[]{7,5}, new int[]{7,7});
                        }else{
                            simulatedMove.setCastlingMove(new int[]{7,3}, new int[]{7,0});
    
                        }
                        board.wkingSideRookMoved=true;
                        board.wqueenSideRookMoved=true;
                        
                    }
                    if (!board.whiteToMove) {
                        if (col+2 == move[1]) {
                           
                            simulatedMove.setCastlingMove(new int[]{0,5}, new int[]{0,7});
                        }else{
                            simulatedMove.setCastlingMove(new int[]{0,3}, new int[]{0,0});
    
                        }
                        board.bkingSideRookMoved=true;
                        board.bqueenSideRookMoved=true;
                        
                    }
                }
                // Make the move
                board.makeUserMove(simulatedMove, gp);
                
                // Switch the turn
                board.whiteToMove = !board.whiteToMove;
                
                // Check if the king is in check after the move
                if (inCheck()) {
                    attackMoves.add(move);
                }
                
                // Switch back the turn and undo the move
                board.whiteToMove = !board.whiteToMove;
                board.undoMove(simulatedMove, gp);
            }}
            
            // Remove invalid moves that leave the king in check
            validMoves.removeAll(attackMoves);
            
            // Check for checkmate or stalemate
            if (inCheck() && validMoves.isEmpty()) {
                if (inCheck()) {
                    board.checkMate = true;
                    System.out.println("checkmate delivered!");
                } else {
                    board.stalemate = true;
                    System.out.println("stalemate delivered!");
    
                }
            } else {
                board.checkMate = false;
                board.stalemate = false;
            }
            
            
            return validMoves;
        }
        
        
        
        // // Helper method to check if the king is under attack
        public boolean inCheck() {
            ArrayList<int[]> enemyMoves = new ArrayList<int[]>();
            if (board.whiteToMove) {
                return squareUnderAttack(board.whiteKingLoc[0],board.whiteKingLoc[1],enemyMoves);
            }else{
                return squareUnderAttack(board.blackKingLoc[0],board.blackKingLoc[1],enemyMoves);
    
            }
        }
    
        public boolean squareUnderAttack(int r,int c, ArrayList<int[]>allEnemyMoves){
            board.whiteToMove =!board.whiteToMove; //switch opp pov
            
            // Get all possible enemy moves after this simulated move
            allEnemyMoves = getAllPossibleMoves();
    
            // Check if any enemy move threatens the king
            for (int[] enemyMove : allEnemyMoves) {
                if (enemyMove[0]==r && enemyMove[1]==c) {
                    board.whiteToMove = !board.whiteToMove;
                    return true;
                   
                } 
            }
            board.whiteToMove =!board.whiteToMove; //switch opp pov
            return false;
    
    
        }
       
    
    
    
    // implement hasmap in future
        // public void retHasmap(int i,int r, int c, ArrayList<int[]>moves,Board board){
        //     HashMap<Integer, Runnable> pieceMap  = new HashMap<>();
        //     pieceMap.put(1, getKingMoves(r,c,moves,board));
        //     pieceMap.put(1, getPawnMoves(r,c,moves,board));
        //     pieceMap.put(1, getBishopMoves(r,c,moves,board));
        //     pieceMap.put(1, getKnightMoves(r,c,moves,board));
        //     pieceMap.put(1, getRookMoves(r,c,moves,board));
        //     pieceMap.put(1, getQueenMoves(r,c,moves,board));
    
        //     Runnable method  = pieceMap.get(i);
        //     method.run();
            
        // }
       
        public ArrayList<int[]>  getAllPossibleMoves(){
            int temp[][] = Board.square;
            ArrayList<int[]> moves = new ArrayList<>();
            
            for (int r = 0; r < temp.length; r++) {
                for (int c = 0; c < temp[0].length; c++) {
    
                    int turn = temp[r][c];
                    // System.out.println("Piece at (" + r + ", " + c + "): " + turn);
    
                    //piece is white if length is 4 and if 5 its black piece
                    if ((piece.isWhitePiece(turn) && board.whiteToMove)|| (piece.isBlackPiece(turn) && !board.whiteToMove)){
                        // System.out.println("Found piece to move at (" + r + ", " + c + ")");
    
                        int p = turn  & 7;
                        // System.out.println("Piece type: " + p);
    
    
                        switch (p) {
                            case 1:
                                getKingMoves(r,c,moves);
                                break;
                            case 2:
                                // System.out.println("called pawn moves");
                                getPawnMoves(r, c, moves);
    
                                break;
                            case 3:
                           getKnightMoves(r,c,moves);
    
                                break;
                        
                            case 4:
                            getBishopMoves(r,c,moves);
    
                                break;
                        
                            case 5:
                            getRookMoves(r,c,moves);
    
                                break;
                        
                            case 6:
                            getQueenMoves(r,c,moves);
    
                                break;
                        
                            default:
                                break;
                        }
                    }
                    
                }
            }
    
            // for (int[] move : moves) {
            //     System.out.println("Row: " + move[0] + ", Column: " + move[1]);
            // }
            return moves;
        }
    
    
////////////////////////////////////////////////////////////////////////////////////////////
        public ArrayList<int[]> getValidMoves2(int row, int col){
            return getOnlySelectedPieceMoves(row,col);
        }
    
    
        public ArrayList<int[]>  getOnlySelectedPieceMoves ( int r, int c){
            int temp[][] = Board.square;
            ArrayList<int[]> moves = new ArrayList<>();
    
            int turn = temp[r][c];
    
            if ((piece.isWhitePiece(turn) && board.whiteToMove)|| (piece.isBlackPiece(turn) && !board.whiteToMove)){
                int p = turn  & 7;
    
                switch (p) {
                    case 1:
                        getKingMoves(r,c,moves);
                        break;
                    case 2:
                        // System.out.println("called pawn moves");
                        getPawnMoves(r, c, moves);
    
                        break;
                    case 3:
                    getKnightMoves(r,c,moves);
    
                        break;
                
                    case 4:
                    getBishopMoves(r,c,moves);
    
                        break;
                
                    case 5:
                    getRookMoves(r,c,moves);
    
                        break;
                
                    case 6:
                    getQueenMoves(r,c,moves);
    
                        break;
                
                    default:
                        break;
                }
            }
    
    
            return moves;
        }
    
    
    
    
    

/////////////////////////////////////////////////////////

    //KING MOVES
    public void getKingMoves(int r, int c,ArrayList<int[]>moves){
        boolean isWhiteKing = board.whiteToMove;
    
        // Define the possible moves for a knight
        int[][] kingMoves = {
            {-1,0}, {1,0},{0,1},{0,-1},{-1,1}, {-1,-1},{1,1},{1,-1}
        };
    
        // Iterate over all possible knight moves
        for (int[] move : kingMoves) {
            int newRow = r + move[0];
            int newCol = c + move[1];
    
            // Check if the new position is within board boundaries
            if (isValidPosition(newRow, newCol)) {
                int targetPiece = Board.square[newRow][newCol];
                
                // Check if the target piece is an opponent's piece or empty
                if (piece.isOpponentPiece(targetPiece, isWhiteKing) || targetPiece == piece.None) {
                    moves.add(new int[]{newRow, newCol});
                }
            }

        }

        //castling
        if (board.whiteToMove && (board.whiteKingLoc[0]==7 && board.whiteKingLoc[1]==4)) {
            
            if (Board.square[r][c+1]==piece.None && Board.square[r][c+2]==piece.None) {
                if (!board.wkingSideRookMoved && !squareUnderAttack(r, c, new ArrayList<int[]>()) &&
                squareUnderAttack(r, c + 1, new ArrayList<int[]>()) && !squareUnderAttack(r, c + 2, new ArrayList<int[]>())) {
                moves.add(new int[]{r, c + 2}); // Kingside castling
                }
            }
            if (Board.square[r][c-1]==piece.None && Board.square[r][c-2]==piece.None) {

                if (!board.wqueenSideRookMoved && !squareUnderAttack(r, c, new ArrayList<int[]>()) &&
                !squareUnderAttack(r, c - 1, new ArrayList<int[]>()) && !squareUnderAttack(r, c - 2, new ArrayList<int[]>())) {
                moves.add(new int[]{r, c - 2}); // Queenside castling
            }
            }
    
        }

        if(!board.whiteToMove && (board.blackKingLoc[0]==0 && board.blackKingLoc[1]==4)){
            if (Board.square[r][c+1]==piece.None && Board.square[r][c+2]==piece.None) {
                if (!board.bkingSideRookMoved && !squareUnderAttack(r, c, new ArrayList<int[]>()) &&
                !squareUnderAttack(r, c + 1, new ArrayList<int[]>()) && !squareUnderAttack(r, c + 2, new ArrayList<int[]>())) {
                moves.add(new int[]{r, c + 2}); // Kingside castling
                }
            }
            if (Board.square[r][c-1]==piece.None && Board.square[r][c-2]==piece.None) {

                if (!board.bqueenSideRookMoved && !squareUnderAttack(r, c, new ArrayList<int[]>()) &&
                !squareUnderAttack(r, c - 1, new ArrayList<int[]>()) && !squareUnderAttack(r, c - 2, new ArrayList<int[]>())) {
                moves.add(new int[]{r, c - 2}); // Queenside castling
                }
            }
        }
        
    }

    
    

    
    //PAWN MOVES
    public void getPawnMoves(int r, int c, ArrayList<int[]>moves){
        boolean isWhitePawn = board.whiteToMove;
        int direction = isWhitePawn ? -1 : 1;
        int startRow = isWhitePawn ? 6 : 1;
        
        // Single move
        if (r + direction >= 0 && r + direction < gp.maxHorizontalCol && Board.square[r + direction][c] == piece.None) {
            moves.add(new int[]{r + direction, c});
            
            // Double move (only from the starting row)
            if (r == startRow && Board.square[r + 2 * direction][c] == piece.None) {
                moves.add(new int[]{r + 2 * direction, c});
                
            }
        }
    
        // Capture moves
        if (r + direction >= 0 && r + direction < gp.maxVerticalRow) {
            if (c > 0 && piece.isOpponentPiece(Board.square[r + direction][c - 1], isWhitePawn)) {
                moves.add(new int[]{r + direction, c - 1});
            }
            if (c < gp.maxHorizontalCol - 1 && piece.isOpponentPiece(Board.square[r + direction][c + 1], isWhitePawn)) {
                moves.add(new int[]{r + direction, c + 1});
            }
        }

        // En Passant move
        // if (!potentialEnpassant.isEmpty()) {
        if (board.moveHistory.isEmpty()) {
            return;
        }
        movesTallier lastMove = board.moveHistory.peek();
        
        
        // Ensure last move is a two-square pawn move
        if (piece.getPieceType(lastMove.pieceMoved) == piece.Pawn && Math.abs(lastMove.startRow - lastMove.endRow) == 2) {
            int enPassantRow = isWhitePawn ? lastMove.endRow + 1 : lastMove.endRow - 1;

            // Check left en passant capture
            if (c > 0 && lastMove.endCol == c - 1 && lastMove.endRow == r && 
                piece.isOpponentPiece(Board.square[lastMove.endRow][lastMove.endCol], isWhitePawn)) {
                moves.add(new int[]{r + direction, c - 1});
                storeEnpassantmoves.put(new Coordinate(r + direction, c - 1), new Coordinate(r, c - 1));
            }

            // Check right en passant capture
            if (c < gp.maxHorizontalCol - 1 && lastMove.endCol == c + 1 && lastMove.endRow == r &&
                piece.isOpponentPiece(Board.square[lastMove.endRow][lastMove.endCol], isWhitePawn)) {
                moves.add(new int[]{r + direction, c + 1});
                storeEnpassantmoves.put(new Coordinate(r + direction, c + 1), new Coordinate(r, c + 1));
            }
        }


    }

   
    //KNOGHT MOVES
    public void getKnightMoves(int r, int c, ArrayList<int[]> moves) {
        boolean isWhiteKnight = board.whiteToMove;
    
        // Define the possible moves for a knight
        int[][] knightMoves = {
            {-2, -1}, {-1, -2}, {1, -2}, {2, -1},
            {2, 1}, {1, 2}, {-1, 2}, {-2, 1}
        };
    
        // Iterate over all possible knight moves
        for (int[] move : knightMoves) {
            int newRow = r + move[0];
            int newCol = c + move[1];
    
            // Check if the new position is within board boundaries
            if (isValidPosition(newRow, newCol)) {
                int targetPiece = Board.square[newRow][newCol];
                
                // Check if the target piece is an opponent's piece or empty
                if (piece.isOpponentPiece(targetPiece, isWhiteKnight) || targetPiece == piece.None) {
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }
    }
    
    //BISHOP MOVES
    public void getBishopMoves(int r, int c, ArrayList<int[]>moves){

        boolean isWhiteBishop = board.whiteToMove;
        for(int i=1;i<8;i++){
            if(r-i<0 || c+i>7){
                break;
            }else if (piece.isMyPiece(Board.square[r-i][c+i], isWhiteBishop) ) {
                break;
            } else if(piece.isOpponentPiece(Board.square[r-i][c+i], isWhiteBishop)) {
                moves.add(new int[]{r-i,c+i});
                break;
            }
            else {
                moves.add(new int[]{r-i, c+i});
    
            }
        }

        //diagonal upleft
        for(int i=1;i<8;i++){
            if(r-i<0 || c-i<0){
                break;
            }else if  (piece.isMyPiece(Board.square[r-i][c-i], isWhiteBishop) ) {
                break;
            }else if(piece.isOpponentPiece(Board.square[r-i][c-i], isWhiteBishop)) {
                moves.add(new int[]{r-i,c-i});
                break;
            } 
            else {
                moves.add(new int[]{r-i, c-i});
    
            }
        }

        //diagonal downleft
        for(int i=1;i<8;i++){
            if(r+i>7 || c-i<0){
                break;
            }else if (piece.isMyPiece(Board.square[r+i][c-i], isWhiteBishop) ) {
                break;
            }else if(piece.isOpponentPiece(Board.square[r+i][c-i], isWhiteBishop)) {
                moves.add(new int[]{r+i, c-i});
                break;
            }
            else {
                moves.add(new int[]{r+i, c-i});
            }
        }

        
        //diagonal downright
        for(int i=1;i<8;i++){
            if(r+i>7 || c+i>7){
                break;
            }else if (piece.isMyPiece(Board.square[r+i][c+i], isWhiteBishop) ) {
                break;
            } else if(piece.isOpponentPiece(Board.square[r+i][c+i], isWhiteBishop)) {
                moves.add(new int[]{r+i, c+i});
                break;
            }
            else {
                moves.add(new int[]{r+i,c+i});
    
            }
        }

        
    }

    //ROOK MOVES
    public void getRookMoves(int r, int c, ArrayList<int[]> moves) {
    
        // // Climb up
        // for (int i = r - 1; i >= 0; i--) {  // Start from r-1, loop until row 0
        //     if (piece.isOpponentPiece(Board.square[i][c], isWhiteRook)) {
        //         moves.add(new int[]{i, c});
        //         break;  // Stop after capturing opponent piece
        //     } else if (Board.square[i][c] != piece.None) {
        //         break;  // Stop if it's not an empty square
        //     } else {
        //         moves.add(new int[]{i, c});  // Add the empty square as a valid move
        //     }
        // }
    
        // // Climb down
        // for (int i = r + 1; i <= 7; i++) {  // Start from r+1, loop until row 7
        //     if (piece.isOpponentPiece(Board.square[i][c], isWhiteRook)) {
        //         moves.add(new int[]{i, c});
        //         break;
        //     } else if (Board.square[i][c] != piece.None) {
        //         break;
        //     } else {
        //         moves.add(new int[]{i, c});
        //     }
        // }
    
        // // Climb left
        // for (int i = c - 1; i >= 0; i--) {  // Start from c-1, loop until column 0
        //     if (piece.isOpponentPiece(Board.square[r][i], isWhiteRook)) {
        //         moves.add(new int[]{r, i});
        //         break;
        //     } else if (Board.square[r][i] != piece.None) {
        //         break;
        //     } else {
        //         moves.add(new int[]{r, i});
        //     }
        // }
    
        // // Climb right
        // for (int i = c + 1; i <= 7; i++) {  // Start from c+1, loop until column 7
        //     if (piece.isOpponentPiece(Board.square[r][i], isWhiteRook)) {
        //         moves.add(new int[]{r, i});
        //         break;
        //     } else if (Board.square[r][i] != piece.None) {
        //         break;
        //     } else {
        //         moves.add(new int[]{r, i});
        //     }
        // }


        ////THIS IS SOO SIMPLE OMG IM DUMB AS ROCK WISH I KNEW ABOUT LOOKUP TABLES BEFORE HOLY SHIT
        boolean isWhiteRook = board.whiteToMove;
    
        // Define the possible moves for a knight
        int[][] rookMoves = {
            {-1,0}, {1, 0}, {0, 1}, {0, -1}        
        };
    
        // Iterate over all possible knight moves
        for (int[] move : rookMoves) {
            int newRow = r + move[0];
            int newCol = c + move[1];
            while (isValidPosition(newRow, newCol)) {

             // Check if the new position is within board boundaries
            
                int targetPiece = Board.square[newRow][newCol];
                
                // Check if the target piece is an opponent's piece or empty
                if (piece.isOpponentPiece(targetPiece, isWhiteRook)) {
                    moves.add(new int[]{newRow, newCol});
                    break;
                }else if (targetPiece !=piece.None){
                    break;
                }else{
                    moves.add(new int[]{newRow, newCol});
                }
                newRow = newRow + move[0] ;
                newCol = newCol + move[1];
            
            }
            
    
           
        }
        
    }
    
      
    //QUEEN MOVES
    public void getQueenMoves(int r, int c, ArrayList<int[]>moves){
         getRookMoves(r, c, moves);
         getBishopMoves(r, c, moves);
       
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    // public boolean checkIfICanCastle(){
    //     if (board.whiteToMove && (board.whiteKingLoc[0]==7 && board.whiteKingLoc[1]==4)) {
    //         if (!board.wkingSideRookMoved||!board.wqueenSideRookMoved) {
    //             return  int[]{7,6};
    //         }
    //     }

    //     if(!board.whiteToMove && (board.blackKingLoc[0]==0 && board.blackKingLoc[1]==4)){
    //         if (!board.bkingSideRookMoved|| !board.bqueenSideRookMoved) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    
}
