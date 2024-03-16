package OberegGUI;

import OberegEngine.Board.Board;
import OberegEngine.Board.BoardUtils;
import OberegEngine.Board.Move;
import OberegEngine.Board.Tile;
import OberegEngine.Pieces.Piece;
import OberegEngine.Player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
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
    private Board gameboard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private boolean highLightLegalMoves = true;
    private final TakenPiecesPanel takenPiecesPanel;


    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(835, 557);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(535, 537);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(57, 57);
    private static String defaultPieceImagesPath = "art/pieces/plain/";
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");


    private GameBoard gb1;

    // private static final MyGameTable INSTANCE = new MyGameTable();

    public MyGameTable() {
        this.gameFrame = new JFrame("Obereg");
        this.gameFrame.setLayout(new BorderLayout());
        //this.gameFrame.setLayout(null);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameboard = Board.createStandardBoard();
        this.boardPanel = new MyGameTable.BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.takenPiecesPanel = new TakenPiecesPanel(gameboard);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.EAST);
    }
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
            assignTilePieceIcon(gameboard);
            assignTileColor();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {                             //First click to choose Piece
                            sourceTile = gameboard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = gameboard.getTile((tileId));
                            final Move move = Move.MoveFactory.createMove(gameboard,
                                    sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = gameboard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                gameboard = transition.getTransitionBoard();
                                gameboard.searchEnemies();
                                gameboard = transition.deleteCapturedEnemies(gameboard);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        takenPiecesPanel.updateCounts(gameboard);
                        takenPiecesPanel.checkWinCondition(gameboard);
                        boardPanel.drawBoard(gameboard);
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
        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }
        private void assignTileColor() {
            this.setBackground(darkTileColor);
            this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }

    }
}





