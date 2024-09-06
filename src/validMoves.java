// import java.util.ArrayList;


// public class validMoves {
//     Piece piece;
//     GamePanel gp;
//     Board board;
//     private pieceMovesCalculator pmc;


//     public validMoves(GamePanel gp, Piece piece, Board board){
//         this.gp = gp;
//         this.piece = piece;
//         this.board=board;
//         pmc = new pieceMovesCalculator(gp, board, piece);


//     }


//     public ArrayList<int[]> getValidMoves3(int row, int col) {
//         ArrayList<int[]> validMoves = getOnlySelectedPieceMoves(row, col);
//         ArrayList<int[]> attackMoves = new ArrayList<>();
        
//         // Simulate each valid move
//         for (int[] move : validMoves) {
//             // movesTallier simulatedMove = new movesTallier(board, new int[]{row, col}, move);
//             movesTallier.MoveType moveType =  movesTallier.MoveType.NORMAL; // Default move type


//                     // Check for pawn promotion
//                     if (piece.getPieceType(Board.square[row][col]) == piece.Pawn &&
//                         (move[0] == 0 || move[1] == 7)) {
//                         moveType = movesTallier.MoveType.PROMOTION;
//                     }
                
//                     // Check for en passant
//                     if (piece.getPieceType(Board.square[row][col]) == piece.Pawn && 
//                         board.isEnPassantMove(new int[]{row,col}, move, board.whiteToMove)) {
//                         moveType = movesTallier.MoveType.EN_PASSANT;
                       
                
//                     // Check for castling
//                     if (piece.getPieceType(Board.square[row][col]) == piece.King &&
//                         Math.abs(move[1] - col) == 2) {
//                         moveType = movesTallier.MoveType.CASTLING;
//                     }

//             movesTallier simulatedMove = new movesTallier(new int[]{row, col}, move, moveType);

//             // Handle additional properties for special move types like en passant
//             if (moveType == movesTallier.MoveType.EN_PASSANT) {
//                 int capturedPawnRow = board.whiteToMove ? move[0] + 1 : move[0] - 1;
//                 int capturedPawnCol = move[1];
//                 simulatedMove.setEnPassant(capturedPawnRow, capturedPawnCol);
//             }else if(moveType == movesTallier.MoveType.PROMOTION){
//                 gp.Pawnmoved = true;  // You will probably handle promotion logic separately

//             }
//             else if(moveType == movesTallier.MoveType.CASTLING){
//                 if (board.whiteToMove) {
//                     if (col+2 == move[1]) {
                       
//                         simulatedMove.setCastlingMove(new int[]{7,5}, new int[]{7,7});
//                     }else{
//                         simulatedMove.setCastlingMove(new int[]{7,3}, new int[]{7,0});

//                     }
//                     board.wkingSideRookMoved=true;
//                     board.wqueenSideRookMoved=true;
                    
//                 }
//                 if (!board.whiteToMove) {
//                     if (col+2 == move[1]) {
                       
//                         simulatedMove.setCastlingMove(new int[]{0,5}, new int[]{0,7});
//                     }else{
//                         simulatedMove.setCastlingMove(new int[]{0,3}, new int[]{0,0});

//                     }
//                     board.bkingSideRookMoved=true;
//                     board.bqueenSideRookMoved=true;
                    
//                 }
//             }
//             // Make the move
//             board.makeUserMove(simulatedMove, gp);
            
//             // Switch the turn
//             board.whiteToMove = !board.whiteToMove;
            
//             // Check if the king is in check after the move
//             if (inCheck()) {
//                 attackMoves.add(move);
//             }
            
//             // Switch back the turn and undo the move
//             board.whiteToMove = !board.whiteToMove;
//             board.undoMove(simulatedMove, gp);
//         }}
        
//         // Remove invalid moves that leave the king in check
//         validMoves.removeAll(attackMoves);
        
//         // Check for checkmate or stalemate
//         if (inCheck() && validMoves.isEmpty()) {
//             if (inCheck()) {
//                 board.checkMate = true;
//                 System.out.println("checkmate delivered!");
//             } else {
//                 board.stalemate = true;
//                 System.out.println("stalemate delivered!");

//             }
//         } else {
//             board.checkMate = false;
//             board.stalemate = false;
//         }
        
        
//         return validMoves;
//     }
    
    
    
//     // // Helper method to check if the king is under attack
//     public boolean inCheck() {
//         ArrayList<int[]> enemyMoves = new ArrayList<int[]>();
//         if (board.whiteToMove) {
//             return squareUnderAttack(board.whiteKingLoc[0],board.whiteKingLoc[1],enemyMoves);
//         }else{
//             return squareUnderAttack(board.blackKingLoc[0],board.blackKingLoc[1],enemyMoves);

//         }
//     }

//     public boolean squareUnderAttack(int r,int c, ArrayList<int[]>allEnemyMoves){
//         board.whiteToMove =!board.whiteToMove; //switch opp pov
        
//         // Get all possible enemy moves after this simulated move
//         allEnemyMoves = getAllPossibleMoves();

//         // Check if any enemy move threatens the king
//         for (int[] enemyMove : allEnemyMoves) {
//             if (enemyMove[0]==r && enemyMove[1]==c) {
//                 board.whiteToMove = !board.whiteToMove;
//                 return true;
               
//             } 
//         }
//         board.whiteToMove =!board.whiteToMove; //switch opp pov
//         return false;


//     }
   



// // implement hasmap in future
//     // public void retHasmap(int i,int r, int c, ArrayList<int[]>moves,Board board){
//     //     HashMap<Integer, Runnable> pieceMap  = new HashMap<>();
//     //     pieceMap.put(1, getKingMoves(r,c,moves,board));
//     //     pieceMap.put(1, getPawnMoves(r,c,moves,board));
//     //     pieceMap.put(1, getBishopMoves(r,c,moves,board));
//     //     pieceMap.put(1, getKnightMoves(r,c,moves,board));
//     //     pieceMap.put(1, getRookMoves(r,c,moves,board));
//     //     pieceMap.put(1, getQueenMoves(r,c,moves,board));

//     //     Runnable method  = pieceMap.get(i);
//     //     method.run();
        
//     // }
   
//     public ArrayList<int[]>  getAllPossibleMoves(){
//         int temp[][] = Board.square;
//         ArrayList<int[]> moves = new ArrayList<>();
        
//         for (int r = 0; r < temp.length; r++) {
//             for (int c = 0; c < temp[0].length; c++) {

//                 int turn = temp[r][c];
//                 // System.out.println("Piece at (" + r + ", " + c + "): " + turn);

//                 //piece is white if length is 4 and if 5 its black piece
//                 if ((piece.isWhitePiece(turn) && board.whiteToMove)|| (piece.isBlackPiece(turn) && !board.whiteToMove)){
//                     // System.out.println("Found piece to move at (" + r + ", " + c + ")");

//                     int p = turn  & 7;
//                     // System.out.println("Piece type: " + p);


//                     switch (p) {
//                         case 1:
//                             pmc.getKingMoves(r,c,moves);
//                             break;
//                         case 2:
//                             // System.out.println("called pawn moves");
//                             pmc.getPawnMoves(r, c, moves);

//                             break;
//                         case 3:
//                         pmc.getKnightMoves(r,c,moves);

//                             break;
                    
//                         case 4:
//                         pmc.getBishopMoves(r,c,moves);

//                             break;
                    
//                         case 5:
//                         pmc.getRookMoves(r,c,moves);

//                             break;
                    
//                         case 6:
//                         pmc.getQueenMoves(r,c,moves);

//                             break;
                    
//                         default:
//                             break;
//                     }
//                 }
                
//             }
//         }

//         // for (int[] move : moves) {
//         //     System.out.println("Row: " + move[0] + ", Column: " + move[1]);
//         // }
//         return moves;
//     }


//     ////////////////////////////////////////////////////////////////////////////////////////////
//     public ArrayList<int[]> getValidMoves2(int row, int col){
//         return getOnlySelectedPieceMoves(row,col);
//     }


//     public ArrayList<int[]>  getOnlySelectedPieceMoves ( int r, int c){
//         int temp[][] = Board.square;
//         ArrayList<int[]> moves = new ArrayList<>();

//         int turn = temp[r][c];

//         if ((piece.isWhitePiece(turn) && board.whiteToMove)|| (piece.isBlackPiece(turn) && !board.whiteToMove)){
//             int p = turn  & 7;

//             switch (p) {
//                 case 1:
//                     pmc.getKingMoves(r,c,moves);
//                     break;
//                 case 2:
//                     // System.out.println("called pawn moves");
//                     pmc.getPawnMoves(r, c, moves);

//                     break;
//                 case 3:
//                 pmc.getKnightMoves(r,c,moves);

//                     break;
            
//                 case 4:
//                 pmc.getBishopMoves(r,c,moves);

//                     break;
            
//                 case 5:
//                 pmc.getRookMoves(r,c,moves);

//                     break;
            
//                 case 6:
//                 pmc.getQueenMoves(r,c,moves);

//                     break;
            
//                 default:
//                     break;
//             }
//         }


//         return moves;
//     }
// /////////////////////////////////////////////



// }
