package OberegGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class GameProperty extends JFrame {
    public GameProperty(){
        this.setBounds(50,50,1280,720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./images/chooiceofteam.jpg")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.pack();
        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./images/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
