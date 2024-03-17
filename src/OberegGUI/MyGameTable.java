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
// Игровое поле
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
    private final Color darkTileColor = Color.decode("#593E1A");
    // Конструктор для стартового отображения
    // игрового поля с расставленными фигурами
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
                                gameboard = transition.deleteCapturedEnemies(gameboard);
                            }
                            // Снимаем все опции выбора
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        // Обновляем счетчики и проверяем условия победы,
                        // после чего рисуем новую доску на экране
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