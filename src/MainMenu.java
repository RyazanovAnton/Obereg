import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {
    public MainMenu(){
        this.setBounds(50,50,1280,720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./resources/mainmenubg.jpg")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.pack();
        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./resources/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        jlMainMenuLabels = new JLabel[arrMainMenuLabels.length];
        for (int i=0; i<arrMainMenuLabels.length; i++){
            jlMainMenuLabels[i] = new JLabel(arrMainMenuLabels[i]);
            this.add(jlMainMenuLabels[i]);
            jlMainMenuLabels[i].setBounds(550,220+(i*75),400,70);
            jlMainMenuLabels[i].setFont(mainFont);
            jlMainMenuLabels[i].setForeground(mainMenuColor);
            jlMainMenuLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

    }

    String[] arrMainMenuLabels = new String[]{"New Game", "Tutorial", "Options", "Quit"};
    JLabel[] jlMainMenuLabels;
    Font mainFont = new Font("Celtic", Font.BOLD, 70);
    Color mainMenuColor = new Color(161, 10, 10);
}
