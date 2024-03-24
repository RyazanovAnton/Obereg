package OberegGUI;

import Music.myMusic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainMenuWindow extends JFrame  {


   private String[] arrMainMenuLabels = new String[]{"New Game", "Tutorial", "Options", "Quit"};
    private JLabel[] jlMainMenuLabels;
    private JLabel jlMainBG;
    private Font mainFont = new Font("Blackburr", Font.PLAIN, 70);
    private Color mainMenuColor = new Color(161, 10, 10);
    private Rectangle frameSize = new Rectangle(200,100,1294,755);

    public MainMenuWindow(){
        this.setBounds(frameSize);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        ImageIcon mainMenuBG = new ImageIcon("./images/mainmenubg2.jpg");
        myMusic mainTheme = new myMusic("./audio/main.wav");
        mainTheme.musicOn();
        jlMainBG = new JLabel(mainMenuBG);
        jlMainBG.setBounds(0,0,1280,720);
        jlMainMenuLabels = new JLabel[arrMainMenuLabels.length];
        for (int i=0; i<arrMainMenuLabels.length; i++){
            jlMainMenuLabels[i] = new JLabel(arrMainMenuLabels[i]);
            getContentPane().add(jlMainMenuLabels[i]);
            jlMainMenuLabels[i].setBounds(50,75+(i*75),400,70);
            jlMainMenuLabels[i].setFont(mainFont);
            jlMainMenuLabels[i].setForeground(mainMenuColor);
            jlMainMenuLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
        }






        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./images/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.getContentPane().add(jlMainBG);
        jlMainMenuLabels[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                mainTheme.musicOff();
                new GamePropertyWindow();
            }

        });

        jlMainMenuLabels[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectTutorial(e);
            }

        });

        jlMainMenuLabels[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
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
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    private void selectTutorial(java.awt.event.MouseEvent evt) {
        TutorialWindow tutorialWindow = new TutorialWindow(this);
        tutorialWindow.setVisible(true);
        this.setVisible(false);
    }
}


