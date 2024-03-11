package OberegEngine.Pieces;

import OberegEngine.Alliance;
import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Move.MajorAttackMove;
import OberegEngine.Board.Move.MajorMove;
import OberegEngine.Board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -1, 1, 9};
    private static final int MAXDISTANCE = 3;

    public King(final Alliance pieceAlliance, final int piecePosition) {

        super(PieceType.KING, piecePosition, pieceAlliance);
    }
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            for(int i =0; i<MAXDISTANCE; i++){
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                            isNinthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                        break;
                    }
                    candidateDestinationCoordinate += candidateCoordinateOffset;
                    if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                        final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                        if(!candidateDestinationTile.isTileOccupied()){         // если плитка не занята
                            legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                        } else {
                            final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                            final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                            if(this.pieceAlliance != pieceAlliance){            // если плитка занята фигурой противника
                                legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }
    private static boolean isNinthColumnExclusion(final  int currentPosition, final int candidateOffset){
        return BoardUtils.NINTH_COLUMN[currentPosition]  && (candidateOffset == - 7|| candidateOffset == 1 ||
                candidateOffset == 9);
    }
}
