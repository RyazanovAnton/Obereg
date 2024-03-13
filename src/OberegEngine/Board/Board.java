package OberegEngine.Board;

import OberegEngine.Alliance;
import OberegEngine.Pieces.King;
import OberegEngine.Pieces.Piece;
import OberegEngine.Pieces.Rook;
import OberegEngine.Player.Player;
import OberegEngine.Player.SlavPlayer;
import OberegEngine.Player.VikingPlayer;

import java.util.*;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> slavPieces;
    private final Collection<Piece> vikingPieces;
    private final SlavPlayer slavPlayer;
    private final VikingPlayer vikingPlayer;
    private final Player currentPlayer;
    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.slavPieces = calculateActivePieces(this.gameBoard, Alliance.SLAVS);
        this.vikingPieces = calculateActivePieces(this.gameBoard, Alliance.VIKINGS);
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.slavPieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.vikingPieces);

        this.slavPlayer = new SlavPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.vikingPlayer = new VikingPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.slavPlayer, this.vikingPlayer);
        //this.currentPlayer = null;
    }
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (int i =0; i<BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
    public Player slavPlayer(){
        return this.slavPlayer;
    }
    public Player vikingPlayer(){
        return this.vikingPlayer;
    }
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    public Collection<Piece> getBlackPieces(){
        return this.vikingPieces;
    }
    public Collection<Piece> getWhitePieces(){
        return this.slavPieces;
    }
    private static String prettyPrint(final Tile tile){
        return tile.toString();
    }

    public boolean isLeftAttackPosition(Tile tile){
        if(this.getTile(tile.tileCoordinate-1).isTileOccupied() &&
                this.getTile(tile.tileCoordinate-1).getPiece().getPieceAlliance() !=
                        this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
            //System.out.println();
            return true;
        }
        return false;
    }
    public boolean isRightAttackPosition(Tile tile){
        if(this.getTile(tile.tileCoordinate+1).isTileOccupied() &&
                this.getTile(tile.tileCoordinate+1).getPiece().getPieceAlliance() !=
                        this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
            //System.out.println();
            return true;
        }
        return false;
    }


    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final Builder builder){
        final List<Tile> tiles2 = new ArrayList<>();
        for(int i =0; i< BoardUtils.NUM_TILES; i++) {
            tiles2.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return Collections.unmodifiableList(tiles2);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //black layout
        builder.setPiece(new Rook(Alliance.VIKINGS, 3));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 4));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 5));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 13));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 27));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 36));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 37));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 45));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 35));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 43));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 44));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 53));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 67));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 75));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 76));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 77));
       // builder.setPiece(new King(Alliance.VIKINGS, 77));
//        builder.setPiece(new Rook(Alliance.VIKINGS, 7));
        // White Layout
        builder.setPiece(new King(Alliance.SLAVS, 40));
        builder.setPiece(new Rook(Alliance.SLAVS, 22));
//        builder.setPiece(new Rook(Alliance.SLAVS, 31));
//        builder.setPiece(new Rook(Alliance.SLAVS, 49));
//        builder.setPiece(new Rook(Alliance.SLAVS, 58));
//        builder.setPiece(new Rook(Alliance.SLAVS, 38));
//        builder.setPiece(new Rook(Alliance.SLAVS, 39));
//        builder.setPiece(new Rook(Alliance.SLAVS, 41));
//        builder.setPiece(new Rook(Alliance.SLAVS, 42));

        // WHITE is the first!
        builder.setMoveMaker(Alliance.SLAVS);
        return builder.build();
    }

    // Maybe I need to implement Guava Iterables ??
    public Collection<Move> getAllLegalMoves(){
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.slavPlayer.getLegalMoves());
        allLegalMoves.addAll(this.vikingPlayer.getLegalMoves());
        //return allLegalMoves;
        return Collections.unmodifiableList(allLegalMoves);
    }

    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        Move transitionMove;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }


    }
}