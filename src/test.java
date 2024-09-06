// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// public class test {
//     public static void main(String[] args) {
//         // int n = 14;

//         // // String l = Integer.toBinaryString(n);
//         // // System.out.println(l.length());

//         // int k =1;
//         // int q =2;
//         // int white = 8;
//         // int black = 16;
//         // int arr[]= {10,9,18,17};


//         // for (int i = 0; i < arr.length; i++) {
//         //     if ((arr[i]&white)==white) {
//         //         System.out.println( "white");
//         //     }else{
//         //         System.out.println("blavk");
//         //     }

//         //     if ((arr[i]&7) ==1) {
//         //         System.out.println("king");
//         //     }
//         //     if ((arr[i]&7) ==2) {
//         //         System.out.println("quee  ");
//         //     }
//         // }

//         // int what[]={1,2};
//         // ArrayList<int[]> kingMoves = ret();

//         //  for (int[] move : kingMoves) {
//         //             if (Arrays.equals(move, what)) {
//         //                 System.out.println("true!");
//         //             }else{
//         //                 System.out.println(false);
//         //             }
//         //         }    

//     //             String  f = "12121242141";
//     //             System.out.println(f.length());



//     //             System.out.println('8'-'0');
//     // } 
    
//     // public static ArrayList<int[]> ret(){
//     //     ArrayList<int[]> kingMoves = new ArrayList<>();


//     //     kingMoves.add(new int[]{1,2});

//     //     for (int[] is : kingMoves) {
//     //         System.err.print(is);
//     //     }
//     //     return kingMoves;
   

//     //  // Simulate each valid move
//     //  for (int[] move : validMoves) {
//     //     movesTallier simulatedMove = new movesTallier(board, new int[]{row, col}, move);
        
//     //     // Make the move
//     //     board.makeUserMove(simulatedMove, gp);
        
//     //     // Switch the turn
//     //     board.whiteToMove = !board.whiteToMove;
        
//     //     // Check if the king is in check after the move
//     //     if (inCheck()) {
//     //         attackMoves.add(move);
//     //     }
        
//     //     // Switch back the turn and undo the move
//     //     board.whiteToMove = !board.whiteToMove;
//     //     board.undoMove(simulatedMove, gp);
//     // }
    
//     // // Remove invalid moves that leave the king in check
//     // validMoves.removeAll(attackMoves);
    
//     // // Check for checkmate or stalemate
//     // if (inCheck() && validMoves.isEmpty()) {
//     //     if (inCheck()) {
//     //         board.checkMate = true;
//     //         System.out.println("checkmate delivered!");
//     //     } else {
//     //         board.stalemate = true;
//     //         System.out.println("stalemate delivered!");

//     //     }
//     // } else {
//     //     board.checkMate = false;
//     //     board.stalemate = false;
//     // }
    
    
//     // return validMoves;

//     //  }

   
// }
