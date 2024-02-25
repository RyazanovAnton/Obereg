import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame {

    public GameWindow(){
        this.setTitle("Obereg v.1.0");
        this.setLayout(null);
        this.setBounds(200,100,1280,720);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./resources/gamewindowbg.jpg")))));
        } catch (IOException ex1) {
            throw new RuntimeException(ex1);
        }

        this.pack();
        this.setVisible(true);
        try {
            this.setIconImage(ImageIO.read(new File("./resources/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.addWindowListener(new WindowListener() {

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
            public void windowOpened(WindowEvent event) {
            }
            @Override
            public void windowClosed(WindowEvent event) {
            }
            @Override
            public void windowIconified(WindowEvent e) {
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            @Override
            public void windowActivated(WindowEvent e) {
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

//    public static void main(String[] args) {
//        GameWindow gw1 = new GameWindow();
//    }
}


