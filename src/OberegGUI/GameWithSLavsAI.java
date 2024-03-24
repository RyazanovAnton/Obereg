package OberegGUI;

import Music.myMusic;
import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.Piece;
import OberegEngine.Player.AI.MiniMax;
import OberegEngine.Player.AI.MoveStrategy;
import OberegEngine.Player.MoveTransition;
import OberegEngine.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
// Игровое поле
public class GameWithSLavsAI extends Observable{

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
//    private final GameSetup gameSetup;
    private Board gameboard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private Move computerMove;
    private boolean highLightLegalMoves = true;
    private final GameInfoPanel takenPiecesPanel;
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(57, 57);
    private static String defaultPieceImagesPath = "art/pieces/plain/";

    // SINGLETON
    private static final GameWithSLavsAI INSTANCE = new GameWithSLavsAI();
    public static boolean gameOver = false;

    // Конструктор для стартового отображения
    // игрового поля с расставленными фигурами
    public GameWithSLavsAI() {
        myMusic startBattle = new myMusic("./audio/startbattle.wav");
        startBattle.musicOn();
        this.gameFrame = new JFrame("Obereg v.1.0");
        try {
            this.gameFrame.setIconImage(ImageIO.read(new File("./images/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.gameFrame.setLayout(null);
        this.gameFrame.setResizable(false);
        this.gameFrame.setBounds(200,100,1294,755);
        this.gameboard = Board.createStandardBoard();
        this.boardPanel = new GameWithSLavsAI.BoardPanel();
        this.takenPiecesPanel = new GameInfoPanel(gameboard);
        this.gameFrame.getContentPane().add(takenPiecesPanel);
        this.gameFrame.getContentPane().add(boardPanel);
        this.gameFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowClosing(WindowEvent event) {
                Object[] options = { "Yes", "No" };
                int exitChoice = JOptionPane.showOptionDialog(
                        event.getWindow(), "Do you really want to get out?",
                        "Confirmation of exiting the game", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);
                if (exitChoice == 0) {
                    event.getWindow().setVisible(false);
                    System.exit(0);
                }
            }
            @Override
            public void windowOpened(WindowEvent event) {}
            @Override
            public void windowClosed(WindowEvent event) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        JButton jbBackToMainMenu = new JButton("Back");
        jbBackToMainMenu.setBounds(50,550,180,90);
        jbBackToMainMenu.setOpaque(false);
        this.gameFrame.getContentPane().add(jbBackToMainMenu);

        jbBackToMainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startBattle.musicOff();
                gameFrame.dispose();
                new MainMenuWindow();
            }
        });

        ImageIcon mainMenuBG = new ImageIcon("./images/gamewindowbg.jpg");
        JLabel jlMainBG = new JLabel(mainMenuBG);
        jlMainBG.setBounds(0,0,1280,720);

        this.gameFrame.getContentPane().add(jlMainBG);
        this.addObserver(new TableGameAIWatcher());
        this.gameFrame.setVisible(true);

    }



    // Служебный класс для создания сетки 9х9, куда будут добавляться тайлы
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        BoardPanel() {
            super(new GridLayout(9, 9));
            this.setBounds(386,105,509,509);
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
                this.setOpaque(false);
            }
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

    public void show(){
        GameWithSLavsAI.get().getBoardPanel().drawBoard(GameWithSLavsAI.get().getGameBoard());
        //GameWithSLavsAI.get().getTakenPiecesPanel().updateCounts(gameboard);
        GameWithSLavsAI.get().getTakenPiecesPanel().checkWinCondition(gameboard);
        //GameWithSLavsAI.get().getTakenPiecesPanel().currentMove(gameboard);
    }
    public static GameWithSLavsAI get(){
        return INSTANCE;
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
    private GameInfoPanel getTakenPiecesPanel(){
        return this.takenPiecesPanel;
    }
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    private void moveMadeUpdate(final Player player){
        boardPanel.drawBoard(gameboard);
        takenPiecesPanel.updateCounts(gameboard);
        takenPiecesPanel.checkWinCondition(gameboard);
        takenPiecesPanel.currentMove(gameboard);
        setChanged();
        notifyObservers(player);
    }
    private static class TableGameAIWatcher implements Observer{
        @Override
        public void update(Observable o, Object arg) {
            if (GameWithSLavsAI.get().getGameBoard().currentPlayer().getAlliance().isSlavs()){
                //TODO create an AI thread
                // execute AI work
                final AIBrains thinkTank = new AIBrains();
                thinkTank.execute();
            }
        }
        private static class AIBrains extends SwingWorker<Move, String>{
            //у этого класса есть возможность деалать перемещения и корректировать строки
            private AIBrains(){
            }
            @Override
            protected Move doInBackground() throws Exception {
                //для более глубокого поиска нужно альфа-бетта отсечение!
                final MoveStrategy miniMax = new MiniMax(3);
                final Move bestMove = miniMax.execute(GameWithSLavsAI.get().getGameBoard());
                return bestMove;
            }
            @Override
            public void done(){
                //когда поток SwingWorker'a завершен - проводится работа по очистке в этом методе
                try {
                    final Move bestMove = get();
                    GameWithSLavsAI.get().updateComputerMove(bestMove);
                    GameWithSLavsAI.get().updateGameBoard(GameWithSLavsAI.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                    GameWithSLavsAI.get().moveMadeUpdate(GameWithSLavsAI.get().getGameBoard().currentPlayer().getOpponent());

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    // Класс, на котором могут размещаться фигуры
    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final GameWithSLavsAI.BoardPanel boardPanel,
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
                                humanMovedPiece.calculateLegalMoves(gameboard);
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
                                if(gameboard.currentPlayer().getAlliance().isSlavs()){
                                    GameWithSLavsAI.get().moveMadeUpdate(GameWithSLavsAI.get().getGameBoard().currentPlayer().getOpponent());
                                }

                            }
                        }
                    });

                        // Обновляем счетчики и проверяем условия победы,
                        // после чего рисуем новую доску на экране
                    //takenPiecesPanel.updateCounts(gameboard);
                    takenPiecesPanel.checkWinCondition(gameboard);
                    //takenPiecesPanel.currentMove(gameboard);
                    boardPanel.drawBoard(gameboard);
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
                if(board.getTile(this.tileId).isTileOccupied()){
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
        // Ставим свойства тайла (прозрачность)
        private void assignTileColor() {
            this.setOpaque(false);}
    }
}