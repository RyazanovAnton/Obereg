package OberegEngine.Player.AI;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.King;
import OberegEngine.Pieces.Piece;
import OberegEngine.Pieces.Warrior;
import OberegEngine.Player.Player;
import OberegEngine.Player.SlavPlayer;

public final class StandardBoardEvaluator implements BoardEvaluator {

//    @Override
//    public int evaluate(final Board board, final int depth) {
//        return scorePlayer(board, board.slavPlayer(), depth) -
//                scorePlayer(board, board.vikingPlayer(), depth);
//    }
//@Override
//public int evaluate(final Board board, final int depth) {
//    int pieceValueScore = 0;
//    for(int i=0; i< BoardUtils.NUM_TILES; ++i){
//        if(board.getTile(i).isTileOccupied()){
//            if(board.getTile(i).getPiece() instanceof King){
//                pieceValueScore += 2;
//            } else if (board.getTile(i).getPiece() instanceof Warrior){
//                if(board.getTile(i).getPiece().getPieceAlliance().isSlavs()){
//                    pieceValueScore += 1;
//                } else {
//                    pieceValueScore += -1;
//                }
//
//            }
//        }
//    }
//    System.out.println(pieceValueScore);
//    return pieceValueScore;
//}

    @Override
    public int evaluate(final Board board, final Player player, final int depth) {
       int boardValue;
       boardValue = mobility(player);
        System.out.println(boardValue);
       return boardValue;
    }

//    private int scorePlayer(final Board board,
//                            final Player player,
//                            final int depth) {
//        //System.out.println(pieceValue(player)+mobility(player) + captureEnemies(player));
//
//
//
//        return pieceValue(player) + mobility(player) + captureEnemies(player);
//    }

    private int scorePlayer(final Board board,
                            final Player player) {
        //System.out.println(pieceValue(player)+mobility(player) + captureEnemies(player));



        return pieceValue(board) + captureEnemies(board);
    }

    private int mobility(Player player) {
        int mobility = 0;
        if(player.getAlliance().isSlavs()){
            mobility += player.getLegalMoves().size();
        } else if(player.getAlliance().isVikings()){
            mobility += - player.getLegalMoves().size();
        }
        return mobility;
    }

    private int captureEnemies(Board board){
        int pieceEnemiesScore = 0;

        for(int i=0; i< BoardUtils.NUM_TILES; ++i){
            if(board.getTile(i).isTileOccupied()){
                if(board.getTile(i).getPiece().getPieceAlliance().isSlavs()){
                    //System.out.println("find slavs tile");
                    if(board.getTile(i).getPiece().getEnemies())
                    {
                        pieceEnemiesScore += -10;
                    }
                } else if (board.getTile(i).getPiece().getPieceAlliance().isVikings() && board.getTile(i).getPiece().getEnemies()){
                        pieceEnemiesScore += 10;
                    }
                }
            }
        //System.out.println(pieceValueScore);
        return pieceEnemiesScore;
    }

//    private static int pieceValue(final Player player){
//        int pieceValueScore = 0;
//        for(final Piece piece : player.getActivePieces()){
//            pieceValueScore += piece.getPieceValue();
//        }
//        return pieceValueScore;
//    }

    private static int pieceValue(final Board board){
        int pieceValueScore = 0;
        for(int i=0; i< BoardUtils.NUM_TILES; ++i){
            if(board.getTile(i).isTileOccupied()){
                if(board.getTile(i).getPiece() instanceof King){
                    pieceValueScore += 2;
                } else if (board.getTile(i).getPiece() instanceof Warrior){
                    if(board.getTile(i).getPiece().getPieceAlliance().isSlavs()){
                        pieceValueScore += 1;
                    } else {
                        pieceValueScore += -1;
                    }

                }
            }
        }
        //System.out.println(pieceValueScore);
        return pieceValueScore;
    }
}
