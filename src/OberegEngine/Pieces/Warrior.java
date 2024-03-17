package OberegEngine.Pieces;

import OberegEngine.Player.Alliance;
import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Warrior extends Piece {
    // Вектора для перемещения на 4 стороны
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -1, 1, 9};
    // Перемещение ограничено размером поля
    private static final int MAXDISTANCE = 9;

    public Warrior(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.WARRIOR, piecePosition, pieceAlliance);
    }
    // Переопределение метода базового класса
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            for(int i =0; i<MAXDISTANCE; i++) {
                // Если доошли до первого или последнего столбца,
                // то дальше не перемещаемся в этом же направлении, чтобы не попасть на другой ряд
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                            isNinthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                        break;
                    }
                    candidateDestinationCoordinate += candidateCoordinateOffset;
                    // Если наткнулись на клетку, занятую другой фигурой, то через нее не идем
                    if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate) &&
                            board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        break;
                    }
                    // Запрет на поставновку на трон и точки побега, но перепрыгнуть через трон можно
                    if(     candidateDestinationCoordinate == 40 ||
                            candidateDestinationCoordinate == 0 ||
                            candidateDestinationCoordinate == 8 ||
                            candidateDestinationCoordinate == 72 ||
                            candidateDestinationCoordinate ==80){
                        continue;
                    }
                    if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                        if (!candidateDestinationTile.isTileOccupied()) {         // если плитка не занята
                            legalMoves.add(new Move(board, this, candidateDestinationCoordinate));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Warrior movePiece(Move move) {
        return new Warrior(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }
    @Override
    public String toString() {
        return PieceType.WARRIOR.toString();
    }
    // Исключение в перемещении для первого столбца
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }
    // Исключение в перемещении для второго столбца
    private static boolean isNinthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.NINTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}

