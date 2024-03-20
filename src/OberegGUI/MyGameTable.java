package OberegGUI;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.Piece;
import OberegEngine.Player.AI.MiniMax;
import OberegEngine.Player.AI.MoveStrategy;
import OberegEngine.Player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
// Игровое поле
public class MyGameTable extends Observable{
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final GameSetup gameSetup;
    private Board gameboard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private Move computerMove;
    private boolean highLightLegalMoves = true;
    private final TakenPiecesPanel takenPiecesPanel;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(835, 557);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(535, 537);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(57, 57);
    private static String defaultPieceImagesPath = "art/pieces/plain/";
    private final Color darkTileColor = Color.decode("#593E1A");
    // SINGLETON
    private static final  MyGameTable INSTANCE = new MyGameTable();
    public static boolean gameOver = false;
    // Конструктор для стартового отображения
    // игрового поля с расставленными фигурами
    public MyGameTable() {
        this.gameFrame = new JFrame("Obereg");
        this.gameFrame.setLayout(new BorderLayout());
        //this.gameFrame.setLayout(null);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameboard = Board.createStandardBoard();
        this.boardPanel = new MyGameTable.BoardPanel();
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.takenPiecesPanel = new TakenPiecesPanel(gameboard);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.EAST);
        this.gameSetup = new GameSetup(gameFrame, true);



        this.addObserver(new TableGameAIWatcher());
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);






    }
    // Служебный класс для создания сетки 9х9, куда будут добавляться тайлы
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel() {
            super(new GridLayout(9, 9));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
        // Метод для отрисовки доски, состоящей из 81 тайла
        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    //TODO
    private JMenuBar createTableMenuBar(){
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }
    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyGameTable.get().getGameSetup().promptUser();
                MyGameTable.get().setupUpdate(MyGameTable.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }
    public void show(){
        MyGameTable.get().getBoardPanel().drawBoard(MyGameTable.get().getGameBoard());
        MyGameTable.get().getTakenPiecesPanel().updateCounts(gameboard);
        MyGameTable.get().getTakenPiecesPanel().checkWinCondition(gameboard);
    }
    public static MyGameTable get(){
        return INSTANCE;
    }
    private GameSetup getGameSetup(){
        return this.gameSetup;
    }
    private void setupUpdate(final GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }
    private Board getGameBoard(){
        return this.gameboard;
    }
    private void updateGameBoard(final Board board){
        this.gameboard = board;
    }
    private void updateComputerMove(final Move move){
        this.computerMove = move;
    }
    // Метод может быть приватным, потому что использован паттерн Синглтон
    private TakenPiecesPanel getTakenPiecesPanel(){
        return this.takenPiecesPanel;
    }
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    private void moveMadeUpdate(final PlayerType playerType){
        gameboard.searchEnemies();
        gameboard = gameboard.deleteCapturedEnemies(gameboard);

        takenPiecesPanel.updateCounts(gameboard);
        takenPiecesPanel.checkWinCondition(gameboard);
//        if (gameboard.checkVikingWinConditions() || gameboard.checkSlavWinConditions()){
//            gameOver = true;
//            System.out.println(gameOver);
//        }
        boardPanel.drawBoard(gameboard);

        //gameboard.deleteCapturedEnemies(gameboard);
        setChanged();
        notifyObservers(playerType);
        //System.out.println("Here!");

    }
    private static class TableGameAIWatcher implements Observer{
        @Override
        public void update(Observable o, Object arg) {
            if (MyGameTable.get().getGameSetup().isAIPlayer(MyGameTable.get().getGameBoard().currentPlayer())){
                //TODO create an AI thread
                // execute AI work
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
        }
        private static class AIThinkTank extends SwingWorker<Move, String>{
            //у этого класса есть возможность деалать перемещения и корректировать строки
            private AIThinkTank(){
            }
            @Override
            protected Move doInBackground() throws Exception {
                //для более глубокого поиска нужно альфа-бетта отсечение!
                final MoveStrategy miniMax = new MiniMax(2);
                final Move bestMove = miniMax.execute(MyGameTable.get().getGameBoard());
                return bestMove;
            }
            @Override
            public void done(){
                //когда поток SwingWorker'a завершен - проводится работа по очистке в этом методе
                try {



                    final Move bestMove = get();
                    MyGameTable.get().updateComputerMove(bestMove);
                    MyGameTable.get().updateGameBoard(MyGameTable.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                    //MyGameTable.get().getBoardPanel().drawBoard(MyGameTable.get().getGameBoard());
                    MyGameTable.get().moveMadeUpdate(PlayerType.COMPUTER);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    enum PlayerType {
        HUMAN,
        COMPUTER
    }


    // Класс, на котором могут размещаться фигуры
    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final MyGameTable.BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTilePieceIcon(gameboard);
            assignTileColor();
            // Отслеживаем действия игрока
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (!gameOver) {
                        // ПКМ - сброс всех опций выбора
                        if (isRightMouseButton(e)) {
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        } else if (isLeftMouseButton(e)) {
                            // Первое нажатие ЛКМ, чтобы выбрать одну фигуру,
                            // после чего станет доступен список её доступных ходов
                            if (sourceTile == null) {
                                sourceTile = gameboard.getTile(tileId);
                                humanMovedPiece = sourceTile.getPiece();
                                if (humanMovedPiece == null) {
                                    // Если первые нажатия ЛКМ происходят в пустую область
                                    sourceTile = null;
                                }
                            } else {
                                // Второе нажатие ЛКМ
                                destinationTile = gameboard.getTile((tileId));
                                final Move move = Move.MoveFactory.createMove(gameboard,
                                        sourceTile.getTileCoordinate(),
                                        destinationTile.getTileCoordinate());


                                final MoveTransition transition = gameboard.currentPlayer().makeMove(move);
                                if (transition.getMoveStatus().isDone()) {
                                    // Если ход был сделан, то делаем перестановку фигуры на новую позицию и переход хода
                                   gameboard = transition.getTransitionBoard();
                                    // Ищем на новой доске захваченных противников

                                    gameboard.searchEnemies();
                                    // Если найдены захваченные фигуры противника, то снимаем их с доски
                                    //gameboard = transition.deleteCapturedEnemies(gameboard);
                                    gameboard = gameboard.deleteCapturedEnemies(gameboard);
                                    boardPanel.drawBoard(gameboard);
                                    //boardPanel.drawBoard(gameboard);
                                    if (gameboard.checkVikingWinConditions() || gameboard.checkSlavWinConditions()) {
                                        gameOver = true;

                                    }
                                }
                                // Снимаем все опции выбора
                                sourceTile = null;
                                destinationTile = null;
                                humanMovedPiece = null;
                            }
                        }
                    }


                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(!gameOver){
                                    if(gameSetup.isAIPlayer(gameboard.currentPlayer())){
                                        //gameboard.searchEnemies();
//                                    gameboard.deleteCapturedEnemies();
                                        MyGameTable.get().moveMadeUpdate(PlayerType.HUMAN);

                                    }
                                }

//
//                                takenPiecesPanel.updateCounts(gameboard);
//                                takenPiecesPanel.checkWinCondition(gameboard);

                                // Обновляем счетчики и проверяем условия победы,
                                // после чего рисуем новую доску на экране
//                                if (gameboard.checkVikingWinConditions() || gameboard.checkSlavWinConditions()){
//                                    gameOver = true;
//                                    System.out.println(gameOver);
//                                }
                                boardPanel.drawBoard(gameboard);


                            }
                        });

//                        // Обновляем счетчики и проверяем условия победы,
//                        // после чего рисуем новую доску на экране
                    takenPiecesPanel.updateCounts(gameboard);
                    takenPiecesPanel.checkWinCondition(gameboard);
                    //boardPanel.drawBoard(gameboard);
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }
                @Override
                public void mouseReleased(final MouseEvent e) {
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                }
                @Override
                public void mouseExited(final MouseEvent e) {
                }
            });


            validate();
        }
        // Отрисовка тайла и всего, что на нем может быть (указатель доступного хода / фигура)
        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
        }
        // Установка спрайтов фигур
        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
                   if (board.getTile(this.tileId).isTileOccupied()) {
                      if (board.getTile(this.tileId).getPiece().getPieceAlliance().isSlavs()){
                          if(board.getTile(this.tileId).getPiece().getPieceType().isKing()){
                              try {
                                  final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                                          "king.png"));
                                  add(new JLabel(new ImageIcon(image)));
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          } else {
                              try {
                                  final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                                          "slav.png"));
                                  add(new JLabel(new ImageIcon(image)));
                              } catch (IOException e) {
                                  throw new RuntimeException(e);
                              }
                          }
                       }
                      if (board.getTile(this.tileId).getPiece().getPieceAlliance().isVikings()) {
                           try {
                               final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                                       "viking.png"));
                               add(new JLabel(new ImageIcon(image)));
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }
                      }
                   }
        }
        // Подсветка доступных ходов для выбранной фигуры
        private void highLightLegals(final Board board) {
            if (highLightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            ImageIcon image = new ImageIcon(ImageIO.read(new File("art/misc/highlighter.png")));
                            add(new JLabel(image, JLabel.CENTER));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        // Определение доступных ходов для фигуры
        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        // Назначение свойств тайла (цвет / граница)
        private void assignTileColor() {
            this.setBackground(darkTileColor);
            this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
    }
}