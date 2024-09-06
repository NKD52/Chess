    import java.awt.Graphics2D;
    import java.awt.image.BufferedImage;
    import java.io.IOException;

    import javax.imageio.ImageIO;

    public class Piece {
        
        public final  int None=0;
        public final  int King = 1;
        public final  int Pawn=2;
        public final  int Knight =3;
        public final  int Bishop = 4;
        public final   int Rook = 5;
        public final  int Queen = 6;

        public final  int White = 8;
        public final   int Black = 16;

        GamePanel gp;
        Board ce;



        public Piece(GamePanel gp){
            this.gp=gp;
            getPlayerImage();
        }
        // public Piece( ChessEngine ce){
        //     this.ce =ce;
        // }
        public Piece(){
            getPlayerImage();
        }

        public BufferedImage pieces[] = new BufferedImage[12];

        public  String  piecesArr1 [] = {"bK","bP","bN","bB","bR","bQ","wK","wP","wN","wB","wR","wQ"};

        // public int piecesArr2[] = {
        //     Black | King,  // Black King
        //     Black | Pawn,  // Black Pawn
        //     Black | Knight, // Black Knight
        //     Black | Bishop, // Black Bishop
        //     Black | Rook,  // Black Rook
        //     Black | Queen, // Black Queen
        //     White | King,  // White King
        //     White | Pawn,  // White Pawn
        //     White | Knight, // White Knight
        //     White | Bishop, // White Bishop
        //     White | Rook,  // White Rook
        //     White | Queen  // White Queen
        // };


        public void getPlayerImage(){

            // bB  =ImageIO.read(getClass().getResourceAsStream("/pics/bB.png"));
            // bK  =ImageIO.read(getClass().getResourceAsStream("/pics/bK.png"));
            // bN  =ImageIO.read(getClass().getResourceAsStream("/pics/bN.png"));
            // bP  =ImageIO.read(getClass().getResourceAsStream("/pics/bP.png"));
            // bQ  =ImageIO.read(getClass().getResourceAsStream("/pics/bQ.png"));
            // bR  =ImageIO.read(getClass().getResourceAsStream("/pics/bR.png"));
            // wB  =ImageIO.read(getClass().getResourceAsStream("/pics/wB.png"));
            // wK  =ImageIO.read(getClass().getResourceAsStream("/pics/wK.png"));
            // wN  =ImageIO.read(getClass().getResourceAsStream("/pics/wN.png"));
            // wP  =ImageIO.read(getClass().getResourceAsStream("/pics/wP.png"));
            // wQ  =ImageIO.read(getClass().getResourceAsStream("/pics/wQ.png"));
            // wR  =ImageIO.read(getClass().getResourceAsStream("/pics/wR.png"));
            
            // for (int piece = 0; piece < piecesArr.length; piece++) {
            //     pieces[piece] = ImageIO.read(getClass().getResourceAsStream("pics/"+piecesArr[piece]+ ".png"));   
            // }

            for (int i = 0; i < piecesArr1.length; i++) {
                // Use the index to store the BufferedImage in the pieces array
                try {
                    pieces[i] = ImageIO.read(getClass().getResourceAsStream("/pics/" + piecesArr1[i] + ".png"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    
        public static void update(){

        }

        public void drawpieces(Graphics2D g2) {
            int[][] tempArr = Board.square;
            BufferedImage image = null;
        
            for (int row = 0; row < tempArr.length; row++) {
                for (int col = 0; col < tempArr[0].length; col++) {
                    int piece = tempArr[row][col];
        
                    if (piece != None) {
                        // Determine the index in the 'pieces' array based on the piece value
                        int index=0;
        
                        if ((piece & White) == White) {
                            // White pieces are indexed from 6 to 11 in the 'pieces' array
                            index = (piece & 7) + 5; // e.g., White King (9 & 7 = 1) + 6 = 7th index
                        } else if ((piece & Black) == Black) {
                            // Black pieces are indexed from 0 to 5 in the 'pieces' array
                            index = (piece & 7)-1; // e.g., Black King (17 & 7 = 1) = 1st index
                        }
        
                        if (index < pieces.length) {
                            image = pieces[index];
                            if (image != null) { // Check if the image is loaded properly
                                // Draw the piece image on the board
                                g2.drawImage(image, col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize, null);
                            }
                        }else{
                            System.out.println("Wrong index");
                        }
                    }
                }
            }
        }
        
        
        public boolean isWhitePiece(int piece) {
            return (piece & White) == White;
        }
        
        public boolean isBlackPiece(int piece) {
            return (piece & Black) == Black;
        }

        public int getPieceType(int piece) {
            return piece & 7; // Mask out the color bits
        }
        
        public  boolean isOpponentPiece(int pieceValue, boolean isWhitePawn) {
            return (isWhitePawn && isBlackPiece(pieceValue)) ||
                (!isWhitePawn && isWhitePiece(pieceValue));
        }
        
        public boolean isMyPiece(int pieceValue, boolean isWhitePlayer) {
            if (isWhitePlayer) {
                return isWhitePiece(pieceValue);
            } else {
                return isBlackPiece(pieceValue);
            }
        }
    
    }
