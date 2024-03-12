package OberegGUI;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.Piece;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;
    //    private final GameHistoryPanel gameHistoryPanel;
//    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;

    //    private final GameSetup gameSetup;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    //private Move computerMove;
    private boolean highLightLegalMoves;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(450, 450);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(50, 50);
    private static String defaultPieceImagesPath = "art/pieces/plain/";
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private static final Table INSTANCE = new Table();

    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        //final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
//        this.gameHistoryPanel = new GameHistoryPanel();
//        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();

//        this.addObserver(new TableGameObserver());
//        this.gameSetup = new GameSetup(this.gameFrame, true);
//        this.boardDirection = BoardDirection.NORMAL;
        this.highLightLegalMoves = true;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
//        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
//        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
       // this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void show() {
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    public static Table get() {
        // TODO INSTANCE == SINGLETON???
        return INSTANCE;
    }
    private Board getGameBoard() {
        return this.chessBoard;
    }
    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(9, 9));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

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


    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTilePieceIcon(chessBoard);
            assignTileColor();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                        // System.out.println("RightClick!");


                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {                             //First click to choose Piece
                            // System.out.println("First Left click ");
                            System.out.println("Tile id: " + BoardUtils.getPositionAtCoordinate(tileId));
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            //Second click to choose destination
                            // System.out.println("Second Left Click");
                            destinationTile = chessBoard.getTile((tileId));
                            final Move move = Move.MoveFactory.createMove(chessBoard,
                                    sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                //moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
//                                gameHistoryPanel.redo(chessBoard, moveLog);
//                                takenPiecesPanel.redo(moveLog);
//                                if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
//                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
//                                }
                                boardPanel.drawBoard(chessBoard);

                            }
                        });
                    }


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

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                            board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                            board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void highLightLegals(final Board board) {
            if (highLightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }


        private void assignTileColor() {
//            try {
//                final BufferedImage image = ImageIO.read(new File("art/tile1.png"));
//                add(new JLabel(new ImageIcon(image)));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }



            if (BoardUtils.EIGHTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.NINTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIRST_ROW[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
//            }else if (BoardUtils.NINTH_ROW[this.tileId] ||
//                    BoardUtils.SEVENTH_ROW[this.tileId] ||
//                    BoardUtils.FIFTH_ROW[this.tileId] ||
//                    BoardUtils.THIRD_ROW[this.tileId] ||
//                    BoardUtils.FIRST_ROW[this.tileId]){
//                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
//            }
            }

        }

    }
}
