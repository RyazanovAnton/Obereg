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

public class Rook extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -1, 1, 9};

    public Rook(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.ROOK, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isNinthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {         // если плитка не занята
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {

//                        if (board.getTile(candidateDestinationCoordinate + 1).isTileOccupied() &&
//                                board.getTile(candidateDestinationCoordinate + 1).getPiece().getPieceAlliance() != this.pieceAlliance &&
//                                board.getTile(candidateDestinationCoordinate + 2).isTileOccupied() &&
//                                board.getTile(candidateDestinationCoordinate + 2).getPiece().getPieceAlliance() == this.pieceAlliance) {
//
//                            legalMoves.add(new MajorAttackMove(board,
//                                    this, candidateDestinationCoordinate,
//                                    board.getTile(candidateDestinationCoordinate + 1).getPiece()));
//                            //board.getTile(candidateDestinationCoordinate)=new Tile.EmptyTile();
//                        }
                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().pieceAlliance, move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isNinthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.NINTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }
}

