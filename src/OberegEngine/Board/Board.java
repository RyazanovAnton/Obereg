package OberegEngine.Board;

import OberegEngine.Player.Alliance;
import OberegEngine.Pieces.King;
import OberegEngine.Pieces.Piece;
import OberegEngine.Pieces.Warrior;
import OberegEngine.Player.Player;
import OberegEngine.Player.SlavPlayer;
import OberegEngine.Player.VikingPlayer;

import javax.swing.*;
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
        final Collection<Move> slavsStandardLegalMoves = calculateLegalMoves(this.slavPieces);
        final Collection<Move> vikingStandardLegalMoves = calculateLegalMoves(this.vikingPieces);
        this.slavPlayer = new SlavPlayer(this, slavsStandardLegalMoves);
        this.vikingPlayer = new VikingPlayer(this, vikingStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.slavPlayer, this.vikingPlayer);
    }
    // Получение доступа к фигурам Варягов
    public Player slavPlayer(){
        return this.slavPlayer;
    }
    // Получение доступа к фигиурам Викингов
    public Player vikingPlayer(){
        return this.vikingPlayer;
    }
    // Получение доступа к фигурам текущего игрока
    public Player currentPlayer(){
        return this.currentPlayer;
    }
    // Получение коллекции викингов
    public Collection<Piece> getVikingPieces(){
        return this.vikingPieces;
    }
    // Получение коллекции варягов
    public Collection<Piece> getSlavPieces(){
        return this.slavPieces;
    }
    // Проврка наличия врагов слева
    public boolean isEnemyOnTheLeft(Tile tile){
        if(!BoardUtils.FIRST_COLUMN[tile.tileCoordinate]){
            if(this.getTile(tile.tileCoordinate-BoardUtils.NEXT_ON_RAW).isTileOccupied() &&
                    this.getTile(tile.tileCoordinate-BoardUtils.NEXT_ON_RAW).getPiece().getPieceAlliance() !=
                            this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
                return true;
            }
        }
        return false;
    }
    // Проврка наличия врагов справа
    public boolean isEnemyOnTheRight(Tile tile){
        if(!BoardUtils.NINTH_COLUMN[tile.tileCoordinate]){
            if(this.getTile(tile.tileCoordinate+BoardUtils.NEXT_ON_RAW).isTileOccupied() &&
                    this.getTile(tile.tileCoordinate+BoardUtils.NEXT_ON_RAW).getPiece().getPieceAlliance() !=
                            this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
                return true;
            }
        }
        return false;
    }
    // Проврка наличия врагов сверху
    public boolean isEnemyOnTheTop(Tile tile) {
        if(this.getTile(tile.tileCoordinate-BoardUtils.NEXT_ON_COLUMN).isTileOccupied() &&
                this.getTile(tile.tileCoordinate-BoardUtils.NEXT_ON_COLUMN).getPiece().getPieceAlliance() !=
                        this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
            //System.out.println();
            return true;
        }
        return false;
    }
    // Проврка наличия врагов снизу
    public boolean isEnemyOnTheBottom(Tile tile) {
        if(this.getTile(tile.tileCoordinate+BoardUtils.NEXT_ON_COLUMN).isTileOccupied() &&
                this.getTile(tile.tileCoordinate+BoardUtils.NEXT_ON_COLUMN).getPiece().getPieceAlliance() !=
                        this.getTile(tile.tileCoordinate).getPiece().getPieceAlliance()){
            //System.out.println();
            return true;
        }
        return false;
    }
    // Проверка короля
    public boolean kingIsAlive() {
        for(Piece piece : this.slavPlayer().getActivePieces()){
            if(piece.getPieceType().isKing()){
                return true;
            }
        }
        return false;
    }
    // Создание коллекции допустимых ходов для альянса
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }
    // Инкапсуляция - получение доступа к возможным ходам обоих альянсов
    public Collection<Move> getAllLegalMoves(){
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.slavPlayer.getLegalMoves());
        allLegalMoves.addAll(this.vikingPlayer.getLegalMoves());
        return Collections.unmodifiableList(allLegalMoves);
    }
    // Создание коллекции активных войнов на доске
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
    // Получение доступа к содержимому Тайла по его порядковому номеру
    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }
    // Создание игрового поля из NUM_TILES (81) элемента
    private static List<Tile> createGameBoard(final Builder builder){
        final List<Tile> tiles = new ArrayList<>();
        for(int i =0; i< BoardUtils.NUM_TILES; i++) {
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return Collections.unmodifiableList(tiles);
    }
    // Исходная расстановка + установка первого хода за викингами
    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        // Команда викингов
        builder.setPiece(new Warrior(Alliance.VIKINGS, 3));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 4));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 5));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 13));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 27));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 36));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 37));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 45));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 35));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 43));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 44));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 53));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 67));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 75));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 76));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 77));
        builder.setPiece(new Warrior(Alliance.VIKINGS, 77));
        // Команда варягов
        builder.setPiece(new King(Alliance.SLAVS, 40));
        builder.setPiece(new Warrior(Alliance.SLAVS, 22));
        builder.setPiece(new Warrior(Alliance.SLAVS, 31));
        builder.setPiece(new Warrior(Alliance.SLAVS, 49));
        builder.setPiece(new Warrior(Alliance.SLAVS, 58));
        builder.setPiece(new Warrior(Alliance.SLAVS, 38));
        builder.setPiece(new Warrior(Alliance.SLAVS, 39));
        builder.setPiece(new Warrior(Alliance.SLAVS, 41));
        builder.setPiece(new Warrior(Alliance.SLAVS, 42));
        // Викинги ходят первыми
        builder.setMoveMaker(Alliance.VIKINGS);
        return builder.build();
    }
    // Поиск врагов вокруг воина
    public void searchEnemies() {
        for(Piece piece : this.currentPlayer().getActivePieces()){
            // Если князь расположена на троне, то захватить его могут только 4 врага
            if (piece.getPieceType().isKing() && piece.getPiecePosition() == 40){
                if(this.isEnemyOnTheRight(this.getTile(40)) &&
                        this.isEnemyOnTheLeft(this.getTile(40)) &&
                        this.isEnemyOnTheTop(this.getTile(40)) &&
                        this.isEnemyOnTheBottom(this.getTile(40))
                ){
                    piece.setEnemies();
                }
            }
            // Если князь расположен рядом с троном, то захватить его могут только 3 врага
            if (piece.getPieceType().isKing() && piece.getPiecePosition() != 40){
                if(     (piece.getPiecePosition() == 39 &&
                                this.isEnemyOnTheLeft(this.getTile(39)) &&
                                this.isEnemyOnTheTop(this.getTile(39)) &&
                                this.isEnemyOnTheBottom(this.getTile(39))) ||
                        (piece.getPiecePosition() == 31 &&
                                this.isEnemyOnTheLeft(this.getTile(31)) &&
                                this.isEnemyOnTheTop(this.getTile(31)) &&
                                this.isEnemyOnTheRight(this.getTile(31))) ||
                        (piece.getPiecePosition() == 41 &&
                                this.isEnemyOnTheRight(this.getTile(41)) &&
                                this.isEnemyOnTheTop(this.getTile(41)) &&
                                this.isEnemyOnTheBottom(this.getTile(41))) ||
                        (piece.getPiecePosition() == 49 &&
                                this.isEnemyOnTheLeft(this.getTile(49)) &&
                                this.isEnemyOnTheRight(this.getTile(49)) &&
                                this.isEnemyOnTheBottom(this.getTile(49)))
                ){
                    piece.setEnemies();
                }
            }
            if( piece.getPieceType().isKing() &&
                            (piece.getPiecePosition() != 40 &&
                            piece.getPiecePosition() != 31 &&
                            piece.getPiecePosition() != 39 &&
                            piece.getPiecePosition() != 41 &&
                            piece.getPiecePosition() != 49)
                    ){
                // Проверка не зажат ли Князь на поле между двумя врагами по горизонтали
                if (BoardUtils.isValidTileCoordinate(piece.getPiecePosition() - BoardUtils.NEXT_ON_RAW) &&
                        BoardUtils.isValidTileCoordinate(piece.getPiecePosition() + BoardUtils.NEXT_ON_RAW)) {
                    if (this.isEnemyOnTheLeft(this.getTile(piece.getPiecePosition())) &&
                            this.isEnemyOnTheRight(this.getTile(piece.getPiecePosition()))) {
                        piece.setEnemies();
                    }
                }
                // Проверка не зажат ли Князь на поле двумя врагами по вертикали
                if (BoardUtils.isValidTileCoordinate(piece.getPiecePosition() - BoardUtils.NEXT_ON_COLUMN) &&
                        BoardUtils.isValidTileCoordinate(piece.getPiecePosition() + BoardUtils.NEXT_ON_COLUMN)) {
                    if (this.isEnemyOnTheTop(this.getTile(piece.getPiecePosition())) &&
                            this.isEnemyOnTheBottom(this.getTile(piece.getPiecePosition()))) {
                        piece.setEnemies();
                    }
                }
            }
            if(piece.getPiecePosition() != 40 && (!piece.getPieceType().isKing())){
                // Проверка не зажат ли Воин на поле между двумя врагами по горизонтали
                if (BoardUtils.isValidTileCoordinate(piece.getPiecePosition() - BoardUtils.NEXT_ON_RAW) &&
                        BoardUtils.isValidTileCoordinate(piece.getPiecePosition() + BoardUtils.NEXT_ON_RAW)) {
                    if (this.isEnemyOnTheLeft(this.getTile(piece.getPiecePosition())) &&
                            this.isEnemyOnTheRight(this.getTile(piece.getPiecePosition()))) {
                        piece.setEnemies();
                        System.out.println("find horiz enemies");
                    }
                }
                // Проверка не зажат ли Воин на поле двумя врагами по вертикали
                if (BoardUtils.isValidTileCoordinate(piece.getPiecePosition() - BoardUtils.NEXT_ON_COLUMN) &&
                        BoardUtils.isValidTileCoordinate(piece.getPiecePosition() + BoardUtils.NEXT_ON_COLUMN)) {
                    if (this.isEnemyOnTheTop(this.getTile(piece.getPiecePosition())) &&
                            this.isEnemyOnTheBottom(this.getTile(piece.getPiecePosition()))) {
                        piece.setEnemies();
                        System.out.println("find vert enemies");
                    }
                }
            }
            // Проверка не зажата ли фишка между точкой побега и одним врагом
            if(
                    (piece.getPiecePosition() == 1 && this.isEnemyOnTheRight(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 9 && this.isEnemyOnTheBottom(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 7 && this.isEnemyOnTheLeft(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 17 && this.isEnemyOnTheBottom(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 63 && this.isEnemyOnTheTop(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 73 && this.isEnemyOnTheRight(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 71 && this.isEnemyOnTheTop(this.getTile(piece.getPiecePosition()))) ||
                    (piece.getPiecePosition() == 79 && this.isEnemyOnTheLeft(this.getTile(piece.getPiecePosition())))
            ){
                piece.setEnemies();
            }
            // Проверка не зажата ли фишка между пустым троном и одним врагом
            if((!piece.getPieceType().isKing())){
                if(!this.getTile(40).isTileOccupied()){
                    if((piece.getPiecePosition() == 31 && this.isEnemyOnTheTop(this.getTile(piece.getPiecePosition()))) ||
                            (piece.getPiecePosition() == 39 && this.isEnemyOnTheLeft(this.getTile(piece.getPiecePosition()))) ||
                            (piece.getPiecePosition() == 41 && this.isEnemyOnTheRight(this.getTile(piece.getPiecePosition()))) ||
                            (piece.getPiecePosition() == 49 && this.isEnemyOnTheBottom(this.getTile(piece.getPiecePosition()))) ){
                        piece.setEnemies();
                    }
                }
            }
        }
    }
    // Проверка условий завершения игры - победа Варягов
    public boolean checkSlavWinConditions(){
        // Все воины варягов захвачены
        if (this.vikingPlayer().getActivePieces().size() == 0) {
            return true;
            }
        // Князю удалось достичь клетки победы
        else if ((this.getTile(0).isTileOccupied() &&
                this.getTile(0).getPiece().getPieceType().isKing()) ||
                (this.getTile(8).isTileOccupied() &&
                        this.getTile(8).getPiece().getPieceType().isKing()) ||
                (this.getTile(72).isTileOccupied() &&
                        this.getTile(72).getPiece().getPieceType().isKing()) ||
                (this.getTile(80).isTileOccupied() &&
                        this.getTile(80).getPiece().getPieceType().isKing())) {
            return true;
        }
        else if (this.vikingPlayer().getLegalMoves().isEmpty()){
            // Нет свободных ходов у викингов
            return true;
        }
        else return false;
    }
    // Проверка условий завершения игры - победа Викингов
    public boolean checkVikingWinConditions(){
        //Все воины варягов захвачены
        if (this.slavPlayer().getActivePieces().size() == 0) {
            return true;
        }
        else if (!this.kingIsAlive()) {
            // Князь захвачен
            return true;
        }
        else if (this.slavPlayer().getLegalMoves().isEmpty()){
            // Нет свободных ходов у варягов
            return true;
        }
        else return false;
    }
    public Board deleteCapturedEnemies(Board board) {
        for(Piece piece : board.currentPlayer().getActivePieces()){
            if (piece.getEnemies()) {
                //System.out.println("draw new board");
                Board.Builder builder = new Board.Builder();
                for (final Piece piece2 : board.currentPlayer().getActivePieces()) {
                    if(!piece2.getEnemies()){
                        builder.setPiece(piece2);
                        //System.out.println("set 1st");
                    }
                }
                for (final Piece piece2 : board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece2);
                    //System.out.println("set 2nd");
                }
                //builder.delPiece(piece.getPiecePosition());
                builder.setMoveMaker(board.currentPlayer().getAlliance());
                board = builder.build();
                return board;
            }
        }
        return board;
    }
    public Board deleteCapturedEnemies2(Board board) {
        for(Piece piece : board.currentPlayer().getActivePieces()){
            if (piece.getEnemies()) {
                //System.out.println("draw new board");
                Board.Builder builder = new Board.Builder();
                for (final Piece piece2 : board.currentPlayer().getActivePieces()) {
                    if(!piece2.getEnemies()){
                        builder.setPiece(piece2);
                        //System.out.println("set 1st");
                    }
                }
                for (final Piece piece2 : board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece2);
                    //System.out.println("set 2nd");
                }
                //builder.delPiece(piece.getPiecePosition());
                builder.setMoveMaker(board.currentPlayer().getAlliance());
                board = builder.build();
                return board;
            }
        }
        return board;
    }



    // Вспомогательный класс Builder, который расставляет и удаляет фигуры с игровой доски
    public static class Builder{
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        // TODO Почему Хэшмапа?
        public Builder(){
            this.boardConfig = new HashMap<>();
        }
        // Расстановка фигур на доске
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        // Удаление фигур с доски
        public Builder delPiece(int key){
            this.boardConfig.remove(key);
            return this;
        }
        // Установка перехода хода
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        // Создание нового экземпляра класса Board (новая доска)
        public Board build(){
            return new Board(this);
        }
    }
}