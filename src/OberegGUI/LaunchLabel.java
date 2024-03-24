package OberegGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class LaunchLabel extends JFrame {
    private Rectangle frameSize = new Rectangle(550,300,400,250);
    private File labelImage = new File("./images/launchlabel.jpg");
    public LaunchLabel(){
        this.setBounds(frameSize);
        this.setUndecorated(true);
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(labelImage))));
        } catch (IOException ex1) {
            throw new RuntimeException(ex1);
        }
        this.pack();
        this.setVisible(true);
        try {
            this.setIconImage(ImageIO.read(new File("./images/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
      try {
            TimeUnit.SECONDS.sleep(3);
            this.setVisible(false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        MainMenuWindow main =  new MainMenuWindow();
    }
}
