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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class MyGameTable {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private boolean highLightLegalMoves = true;


    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(535, 557);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(535, 537);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(57, 57);
    private static String defaultPieceImagesPath = "art/pieces/plain/";
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private static final MyGameTable INSTANCE = new MyGameTable();


    public MyGameTable() {
        this.gameFrame = new JFrame("Obereg");
        this.gameFrame.setLayout(new BorderLayout());
        //this.gameFrame.setLayout(null);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new MyGameTable.BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.gameFrame.add(this.boardPanel);




    }
//    public void show() {
//        MyGameTable.get().getBoardPanel().drawBoard(MyGameTable.get().getGameBoard());
//    }
//    public static MyGameTable get() {
//        // TODO INSTANCE == SINGLETON???
//        return INSTANCE;
//    }
//    private BoardPanel getBoardPanel() {
//        return this.boardPanel;
//    }
//    private Board getGameBoard() {
//        return this.chessBoard;
//    }







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

        TilePanel(final MyGameTable.BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTilePieceIcon(chessBoard);
            assignTileColor();
//            addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(final MouseEvent e) {
//                    if (isRightMouseButton(e)) {
//                        sourceTile = null;
//                        destinationTile = null;
//                        humanMovedPiece = null;
//                        // System.out.println("RightClick!");
//
//
//                    } else if (isLeftMouseButton(e)) {
//                        if (sourceTile == null) {                             //First click to choose Piece
//                            // System.out.println("First Left click ");
//                            System.out.println("Tile id: " + BoardUtils.getPositionAtCoordinate(tileId));
//                            sourceTile = chessBoard.getTile(tileId);
//                            humanMovedPiece = sourceTile.getPiece();
//                            if (humanMovedPiece == null) {
//                                sourceTile = null;
//                            }
//                        } else {
//                            //Second click to choose destination
//                            // System.out.println("Second Left Click");
//                            destinationTile = chessBoard.getTile((tileId));
//                            final Move move = Move.MoveFactory.createMove(chessBoard,
//                                    sourceTile.getTileCoordinate(),
//                                    destinationTile.getTileCoordinate());
//                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
//                            if (transition.getMoveStatus().isDone()) {
//                                chessBoard = transition.getTransitionBoard();
//                                //moveLog.addMove(move);
//                            }
//                            sourceTile = null;
//                            destinationTile = null;
//                            humanMovedPiece = null;
//                        }
//                        boardPanel.drawBoard(chessBoard);
////                        SwingUtilities.invokeLater(new Runnable() {
////                            @Override
////                            public void run() {
//////                                gameHistoryPanel.redo(chessBoard, moveLog);
//////                                takenPiecesPanel.redo(moveLog);
//////                                if(gameSetup.isAIPlayer(chessBoard.currentPlayer())){
//////                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
//////                                }
////                                boardPanel.drawBoard(chessBoard);
////
////                            }
////                        });
//                    }
//
//
//                }
//
//                @Override
//                public void mousePressed(final MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseReleased(final MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseEntered(final MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseExited(final MouseEvent e) {
//
//                }
//            });
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
                    final BufferedImage image = ImageIO.read(new File("art/pieces/plain/slavs.png"));
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
//            ImageIcon image = new ImageIcon("art/tiles/tile" + this.tileId + ".png");
//            this.add(new JLabel(image));
//            //this.setOpaque(true);
//            //this.setBackground(Color.DARK_GRAY);
//            this.setBorder(BorderFactory.createLineBorder(Color.black,1));

           this.setBackground(lightTileColor);
           this.setBorder(BorderFactory.createLineBorder(Color.black, 1));

//            ImageIcon icon = new ImageIcon("art/tiles/tile" + this.tileId + ".png");
//            JLabel label= new JLabel() {
//                public void paintComponent(Graphics g) {
//                    g.drawImage(icon.getImage(), 0, 0, null);
//                    super.paintComponent(g);
//                }
//            };
//
//            label.setOpaque(false);
//            add( label );
//

//
//            ImageIcon image = new ImageIcon("art/tiles/tile" + this.tileId + ".png");
//            this.add(new JLabel(image, JLabel.CENTER));
//
//
//            this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            }

        }

    }



