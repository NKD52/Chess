public class enPassAndPawnPro {
    
    Board board;
    Piece piece;

   boolean whiteTurn;

    public enPassAndPawnPro(Board board, Piece piece){
        this.board = board;
        this.piece = piece;
        whiteTurn = board.whiteToMove;
    }

    public void pawnPromotion(){
        
        int row = whiteTurn? 0:7;
        int col = 7;

        for(int i = 0; i<=col; i++){
            if (board.square[row][col]==10) {
                System.out.println("please enter the following keys inputs for promotion. 1)B 2)R 3)N 4)Q");
                
            }
        }

    }
}
