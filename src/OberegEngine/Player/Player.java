package OberegEngine.Player;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.Piece;
import OberegEngine.Pieces.PieceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

// У класса 2 наследника SlavPlayer / VikingPlayer
public abstract class Player {
    protected final Board board;
    protected ArrayList<Move> legalMoves;
    Player(final Board board,
           final ArrayList<Move> playerLegals) {
        this.board = board;
        this.legalMoves = playerLegals;
    }
    // Коллекция доступных ходов для игрока
    public ArrayList<Move> getLegalMoves(){
        return this.legalMoves;
    }
    // Проверка доступности действия
    public boolean isLegalMove(final Move move){
        return this.legalMoves.contains(move);
    }
    // Расстановка фигур на доске по результатам хода
    public MoveTransition makeMove(final Move move){
        if(!isLegalMove(move)){
            System.out.println("Player.makeMove Illegal");
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transmitonBoard = move.execute();
        return new MoveTransition(transmitonBoard, move, MoveStatus.DONE);
    }
    public abstract ArrayList<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    public boolean isKingEscaped() {
        for(Piece piece : board.getSlavPieces()){
            if(piece.getPieceType().isKing() &&
                            (piece.getPiecePosition() == 0 ||
                            piece.getPiecePosition() == 8 ||
                            piece.getPiecePosition() == 72 ||
                            piece.getPiecePosition() == 80 )){
                return true;
            }
        }
        return false;
    }

    public boolean isKingCaptured() {
        if(board.currentPlayer().getAlliance().isSlavs()){
            for(int i =0; i< BoardUtils.NUM_TILES; i++){
                if(board.getTile(i).isTileOccupied() &&
                        board.getTile(i).getPiece().getPieceType()== PieceType.KING){
                    board.searchKingEnemies();
                    if(board.getTile(i).getPiece().getEnemies()){
                        return true;
                    } else {
                        return false;
                    }

                }

            }
            return true;
        }
        if(board.currentPlayer().getAlliance().isVikings()){
            for(Piece piece : board.getSlavPieces()){
                if (!piece.getPieceType().isKing()){
                    continue;
                }
                if(piece.getPieceType().isKing()){
                    board.searchKingEnemies();
                    if(piece.getEnemies()){
                        return true;
                    }
                }
            }
            board.resetEnem();
        }
        return false;
    }
}


