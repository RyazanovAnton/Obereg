import Board.*;
import GamePieces.Color;
import GamePieces.Coordinates;
import GamePieces.File;
import GamePieces.Piece;

public class BoardConsoleRenderer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_PIECE_COLOR = "\u001B[97m";
    public static final String ANSI_BLACK_PIECE_COLOR = "\u001B[30m";
    public static final String ANSI_WHITE_SQUARE_BG = "\u001B[47m";
    public static final String ANSI_BLACK_SQUARE_BG = "\u001B[0;100m";




    public void render(Board board){
        for (int rank = 8; rank >= 1; rank--){
            String line = " ";
            for (File file : File.values()){
                Coordinates coordinates = new Coordinates(file, rank);
                if (board.isSquareEmpty(coordinates)) {
                    line += getSpriteForEmprySquare(new Coordinates(file, rank));
                } else {
                    line += getPieceSprite(board.getPiece(coordinates));
                }
            }
            line += ANSI_RESET;
            System.out.println(line);
        }
    }



    private String colorizeSquare(String sprite, Color pieceColor, boolean isSquareDark){
        String result = sprite;
        if(pieceColor == Color.BLUE){
            result =ANSI_WHITE_PIECE_COLOR + result;
        } else {
            result = ANSI_BLACK_PIECE_COLOR + result;
        }
        if (isSquareDark){
            result =ANSI_BLACK_SQUARE_BG + result;
        }else {
            result = ANSI_WHITE_SQUARE_BG + result;
        }
        return result;
    }

    private String getSpriteForEmprySquare(Coordinates coordinates){

        return colorizeSquare("   ", Color.BLUE, Board.isSquareDark(coordinates));
    }

    private String getPieceSprite(Piece piece) {
        return colorizeSquare(" A ", piece.color, Board.isSquareDark(piece.coordinates));
    }
}
