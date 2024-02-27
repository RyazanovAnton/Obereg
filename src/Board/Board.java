package Board;

import GamePieces.*;


import java.util.HashMap;

public class Board {
    HashMap<Coordinates, Piece> pieces = new HashMap<>();

    public void setPiece(Coordinates coordinates, Piece piece){
        piece.coordinates = coordinates;
        pieces.put(coordinates, piece);
    }

    public boolean isSquareEmpty(Coordinates coordinates){
        return !pieces.containsKey(coordinates);
    }

    public Piece getPiece(Coordinates coordinates){
        return pieces.get(coordinates);
    }

    public void setupDefaultPositions(){
        for (File file : File.values()){
            setPiece(new Coordinates(file, 2), new Warior(Color.BLUE, new Coordinates(file, 2)));
            setPiece(new Coordinates(file, 7), new Warior(Color.RED, new Coordinates(file, 7)));

        }

    }

    public static boolean isSquareDark(Coordinates coordinates){
        return (((coordinates.file.ordinal()+1) + coordinates.rank)% 2) == 0;
    }
}
