package TODOLater;

import OberegEngine.Board.BoardUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class GameBoard extends JFrame {
    private JLabel[] jlTiles;
    private final Color darkTileColor = Color.decode("#593E1A");
    public GameBoard(){
        this.setBounds(200,100,620,620);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jlTiles = new JLabel[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            jlTiles[i] = new JLabel();
            this.add(jlTiles[i]);
            jlTiles[i].setBounds(0+((i)%9)*57, 0+57*((i)/9), 57, 57);
            jlTiles[i].setOpaque(true);
            ImageIcon icon = new ImageIcon("art/tiles/tile1.png");
            this.add(new JLabel(icon, JLabel.CENTER));

            jlTiles[i].setBackground(darkTileColor);
            jlTiles[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
    }



}
