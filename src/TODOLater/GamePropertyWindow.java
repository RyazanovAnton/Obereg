package TODOLater;

import Music.myMusic;
import OberegGUI.GameTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GamePropertyWindow extends JFrame {
    public GamePropertyWindow(){
        this.setBounds(200,100,1280,720);
        //this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
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

        jlGamePropertyBG = new JLabel();
        this.add(jlGamePropertyBG);
        jlGamePropertyBG.setBounds(0,0,1280,720);
        ImageIcon withoutChoiceBG = new ImageIcon("./images/chooiceofteam3.jpg");
        ImageIcon rusTeamChoice = new ImageIcon("./images/rusteam5.jpg");
        ImageIcon vikTeamChoice = new ImageIcon("./images/vikteam5.jpg");
        //jbReturnBack = new JButton("Return Back");
//        this.getContentPane().add(jbReturnBack);

        //jbReturnBack.setBounds(50,670,100,50);

        jlGamePropertyBG.setIcon(withoutChoiceBG);
        this.setContentPane(jlGamePropertyBG);
        //this.getContentPane().add(jbReturnBack);

        jbRusTeam = new JButton();
        jbRusTeam.setBounds(390,650,180,50);

        ImageIcon startGame = new ImageIcon("./images/table3.jpg");
        ImageIcon startGame2 = new ImageIcon("./images/table4.jpg");


        jbRusTeam.setIcon(startGame);

        jbVikTeam = new JButton();
        jbVikTeam.setBounds(720,650,180,50);
        jbVikTeam.setIcon(startGame2);

        this.pack();




        myMusic rusTheme = new myMusic("./audio/wings.wav");
        myMusic vikTheme = new myMusic("./audio/valhalla.wav");
        jlGamePropertyBG.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < 540){
                    jlGamePropertyBG.setIcon(rusTeamChoice);
                    getContentPane().add(jbRusTeam);
                    getContentPane().remove(jbVikTeam);

                    if (vikTheme.myClip==null){
                        rusTheme.musicOn();
                    }
                    else {
                        vikTheme.musicOff();
                        rusTheme.musicOn();
                    }
                }
                else if (e.getX()>740){
                    jlGamePropertyBG.setIcon(vikTeamChoice);
                    getContentPane().add(jbVikTeam);
                    getContentPane().remove(jbRusTeam);
                    if (rusTheme.myClip==null){
                        vikTheme.musicOn();
                    }
                    else{
                        rusTheme.musicOff();
                        vikTheme.musicOn();
                    }
                }
                else {
                    jlGamePropertyBG.setIcon(withoutChoiceBG);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jbVikTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vikTheme.musicOff();
                dispose();
                GameTable.get().show();
            }
        });


        jbRusTeam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rusTheme.musicOff();
                dispose();
//                GameSetup vikingAI = new GameSetup();
//
//                GameTable.PlayerType vikingPlayerType = GameTable.PlayerType.COMPUTER;
//
//


                GameWindow gw1 = new GameWindow();
            }
        });


        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./images/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
    JLabel jlGamePropertyBG, jlReturnBack;
    JButton jbRusTeam, jbVikTeam, jbReturnBack;
}
