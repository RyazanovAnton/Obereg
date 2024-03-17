package OberegEngine.Board;

import OberegEngine.Pieces.Piece;

import static OberegEngine.Board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
//    protected final boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();


    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
//        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;

    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }if (!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    public Board getBoard(){
        return this.board;
    }
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }
    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }
    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()){
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        //перемещение фигуры
        builder.setPiece(this.movedPiece.movePiece(this));
        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            if(!piece.getEnemies()){
                builder.setPiece(piece);
            }
//
//            if(BoardUtils.isValidTileCoordinate(piece.getPiecePosition()-BoardUtils.NEXT_ON_RAW) &&
//            BoardUtils.isValidTileCoordinate(piece.getPiecePosition()+BoardUtils.NEXT_ON_RAW)){
//                System.out.println("true");
//                if(this.board.isEnemyOnTheLeft(this.board.getTile(piece.getPiecePosition())) &&
//                        this.board.isEnemyOnTheRight(this.board.getTile(piece.getPiecePosition()))){
//                    System.out.println("2nd true");
//                    piece.setHorizontalEnemies();
//                    System.out.println(piece.getPiecePosition());
//                    System.out.println("Find horizontal enemies!!!" +
//                            this.board.getTile(piece.getPiecePosition()).getPiece().setHorizontalEnemies());
//                }
//            }
//            if(BoardUtils.isValidTileCoordinate(piece.getPiecePosition()-BoardUtils.NEXT_ON_COLUMN) &&
//                    BoardUtils.isValidTileCoordinate(piece.getPiecePosition()+BoardUtils.NEXT_ON_COLUMN)){
//                if(this.board.isEnemyOnTheTop(this.board.getTile(piece.getPiecePosition())) &&
//                        this.board.isEnemyOnTheBottom(this.board.getTile(piece.getPiecePosition()))){
//                    piece.setVerticalEnemies();
//                    System.out.println(piece.getPiecePosition());
//                    System.out.println("Find horizontal enemies!!! " +
//                            this.board.getTile(piece.getPiecePosition()).getPiece().setVerticalEnemies());
//                }
//
//            }

        }

        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

//
//    public Board deliteAttackedPiece(){
//        final Builder builder = new Builder();
//        for(final Piece piece :this.currentPlayer().getActivePieces()){
//            if(!(piece.getVerticalEnemies() || piece.getHorizontalEnemies())){
//                builder.setPiece(piece);
//            }
//        }
//        for (final Piece piece : this.currentPlayer().getOpponent().getActivePieces()){
//            builder.setPiece(piece);
//        }
//        builder.setMoveMaker(this.currentPlayer().getOpponent().getAlliance());
//        return builder.build();
//    }










    public static class MajorAttackMove extends AttackMove{
        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final int destinationCoordinate,
                               final Piece pieceAttacked){
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        @Override
        public String toString(){

            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class MajorMove extends Move{
        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            String moveResult = movedPiece.getPieceType().toString() +": " +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
            System.out.println(moveResult);
            return moveResult;

        }
    }
    public static class AttackMove extends Move{

        final Piece attackedPiece;
        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }
    }




    public static final class NullMove extends Move{
        public NullMove() {
            super(null, 93);  // неверное мустоназначение
        }
        @Override
        public Board execute(){
            throw new RuntimeException("Can't execute the NULL move");
        }
        @Override
        public int getCurrentCoordinate(){
            return -1; // Возвращаем несуществующую координату для любой фигуры
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Not instantiable");
        }
        public static Move createMove(final Board board,
                                      final  int currentCoordinate,
                                      final  int destinationCoordinate){
            for (final Move move : board.getAllLegalMoves()){
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}

