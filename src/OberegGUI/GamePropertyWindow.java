package OberegGUI;

import Music.myMusic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GamePropertyWindow extends JFrame {
    private  ImageIcon toMenu = new ImageIcon("./art/tomenu.png");
    private ImageIcon startGame = new ImageIcon("./art/slavstart.png");
    private ImageIcon startGame2 = new ImageIcon("./art/vikstart.png");
    private myMusic rusTheme = new myMusic("./audio/wings.wav");
    private myMusic vikTheme = new myMusic("./audio/valhalla.wav");
    private Rectangle frameSize = new Rectangle(200,100,1294,755);
    private JLabel jlGamePropertyBG, jlReturnBack, jlRusTeam, jlVikTeam;
    public GamePropertyWindow(){
        this.setBounds(frameSize);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./images/icon.png")));
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
            public void windowOpened(WindowEvent event) {}
            @Override
            public void windowClosed(WindowEvent event) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        jlGamePropertyBG = new JLabel();
        this.add(jlGamePropertyBG);
        jlGamePropertyBG.setBounds(0,0,1280,720);
        ImageIcon withoutChoiceBG = new ImageIcon("./images/chooiceofteam3.jpg");
        ImageIcon rusTeamChoice = new ImageIcon("./images/rusteam5.jpg");
        ImageIcon vikTeamChoice = new ImageIcon("./images/vikteam5.jpg");


        jlGamePropertyBG.setIcon(withoutChoiceBG);
        this.setContentPane(jlGamePropertyBG);


        jlRusTeam = new JLabel(startGame);
        jlRusTeam.setBounds(380,630,205,70);

        jlVikTeam = new JLabel(startGame2);
        jlVikTeam.setBounds(700, 630,205,70);

        jlReturnBack = new JLabel(toMenu);
        jlReturnBack.setBounds(400,10,176,58);
        this.getContentPane().add(jlReturnBack);


        jlReturnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (vikTheme.myClip!=null){
                    vikTheme.musicOff();
                    vikTheme.myClip = null;
                }
                if(rusTheme.myClip!=null){
                    rusTheme.musicOff();
                    rusTheme.myClip = null;
                }
                setVisible(false);
                dispose();
                new MainMenuWindow();
//
            }
        });
        jlGamePropertyBG.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < 540 && e.getY() > 120){
                    jlGamePropertyBG.setIcon(rusTeamChoice);
                    getContentPane().add(jlRusTeam);
                    getContentPane().remove(jlVikTeam);

                    if (vikTheme.myClip==null && rusTheme.myClip==null){
                        rusTheme.musicOn();
                    }
                    if(vikTheme.myClip != null && vikTheme.myClip.isActive()){
                        vikTheme.musicOff();
                        rusTheme.musicOn();
                    }
                }
                else if (e.getX()>740 && e.getY() > 120){
                    jlGamePropertyBG.setIcon(vikTeamChoice);
                    getContentPane().add(jlVikTeam);
                    getContentPane().remove(jlRusTeam);
                    if (vikTheme.myClip==null && rusTheme.myClip==null){
                        vikTheme.musicOn();
                    }
                    if(rusTheme.myClip != null && rusTheme.myClip.isActive()){
                        rusTheme.musicOff();
                        vikTheme.musicOn();
                    }
                }
                else {
                    jlGamePropertyBG.setIcon(withoutChoiceBG);
                    if(rusTheme.myClip != null){
                        rusTheme.musicOff();
                        rusTheme.myClip = null;
                    }
                    if(vikTheme.myClip != null){
                        vikTheme.musicOff();
                        vikTheme.myClip = null;
                    }
                    getContentPane().remove(jlRusTeam);
                    getContentPane().remove(jlVikTeam);

                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        jlVikTeam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                vikTheme.musicOff();
                dispose();
                GameWithSLavsAI.get().show();
            }
        });
        jlRusTeam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rusTheme.musicOff();
                dispose();
                GameWithVikingsAI.get().show();
            }
        });
    }
}
