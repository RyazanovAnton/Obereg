package OberegGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

public class TutorialWindow extends JFrame {
    private final JFrame parent;
    JButton jbReturnBack;
    JButton jbPrevPage;
    JButton jbNextPage;

    private Rectangle frameSize = new Rectangle(200,100,1294,755);
    public TutorialWindow(JFrame parent){
        this.parent = parent;
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

        DescriptionOfRules1 centralArea1 = new DescriptionOfRules1();
        this.getContentPane().add(centralArea1);
        DescriptionOfRules2 centralArea2 = new DescriptionOfRules2();
        this.getContentPane().add(centralArea2);
        DescriptionOfRules3 centralArea3 = new DescriptionOfRules3();
        this.getContentPane().add(centralArea3);

        ImageIcon tutorialWindowBG = new ImageIcon("./images/tutorialwindowbg2.jpg");
        JLabel jlMainBG = new JLabel(tutorialWindowBG);
        jlMainBG.setBounds(0,0,1280,720);

        this.getContentPane().add(jlMainBG);


        jbPrevPage = new JButton();
        jbPrevPage.setBounds(570,640,50,50);
        this.getContentPane().add(jbPrevPage);
        jbPrevPage.setOpaque(false);
        jbPrevPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(centralArea2.isVisible()){
                    centralArea2.setVisible(false);
                    centralArea1.setVisible(true);
                }
                if(centralArea3.isVisible()){
                    centralArea3.setVisible(false);
                    centralArea2.setVisible(true);

                }
            }
        });

        jbNextPage = new JButton();
        jbNextPage.setBounds(660,640,50,50);
        jbNextPage.setOpaque(false);
        jbNextPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(centralArea2.isVisible()){
                    centralArea2.setVisible(false);
                    centralArea3.setVisible(true);

                }
                if(centralArea1.isVisible()){
                    centralArea1.setVisible(false);
                    centralArea2.setVisible(true);
                }
            }
        });
        this.getContentPane().add(jbNextPage);

        jbReturnBack = new JButton();
        jbReturnBack.setBounds(25,65,240,60);
        jbReturnBack.setOpaque(false);
        this.getContentPane().add(jbReturnBack);
        jbReturnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton1ActionPerformed(e);
            }
        });
    }


        Font font = new Font("Arial", NORMAL, 18);
    private class DescriptionOfRules1 extends JPanel{
        Rectangle frameSize = new Rectangle(290,45,740,650);
        DescriptionOfRules1(){
            this.setLayout(null);
            this.setBounds(new Rectangle());
            this.setBounds(frameSize);
            this.setOpaque(false);
            //this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
            this.setVisible(true);
            this.setFont(font);
            this.setForeground(Color.BLACK);

            String text = "<html>To start the game, one player will have to take the " +
                    "side of the Slavs (king and defenders), and the other – Vikings (attackers).<br>" +
                    "In order for the Slavs to win, the king must reach one of the four exits " +
                    "located in the corner of the game board. The Vikings must capture the king " +
                    "in order to win. The players take turns making moves, with the Vikings " +
                    "going first.<br>" +
                    "On his turn, the player must move any one of his chips to an arbitrary " +
                    "number of free squares horizontally or vertically (the rook also moves in chess)." +
                    " It is impossible to \"jump over\" the figures standing in the way.<br>" +
                    "The king, unlike all other pieces, cannot be moved more than three squares. " +
                    "But only he can get on the throne and on the exits – all other chips cannot " +
                    "be placed on these cells (you can transfer chips through the throne).</html>";
            JLabel htmlLabel = new JLabel();
            htmlLabel.setBounds(0,0,740,530);
            //htmlLabel.setVerticalAlignment(SwingConstants.TOP);
            htmlLabel.setText(text);
            htmlLabel.setFont(font);
            htmlLabel.setBorder(BorderFactory.createTitledBorder("Rules. Page 1 of 3"));
            this.add(htmlLabel);

            }
    }

    private class DescriptionOfRules2 extends JPanel {
        private  ImageIcon rules1 = new ImageIcon("./art/rules1.png");
        Rectangle frameSize = new Rectangle(290, 45, 740, 650);

        DescriptionOfRules2() {
            this.setLayout(null);
            this.setBounds(new Rectangle());
            this.setBounds(frameSize);
            this.setOpaque(false);
            //this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
            this.setVisible(true);
            this.setFont(font);
            this.setForeground(Color.BLACK);

            String text = "<html>A piece is considered captured and removed from " +
                    "the playing field if as a result of the opponent's move " +
                    "(but not as a result of the move of the owner of the piece!) " +
                    "it turned out to be:<br>" +
                    "a) Captured between two enemy pieces;<br>" +
                    "b) Trapped between the enemy piece and the exit cell;<br>" +
                    "c) The attacker is sandwiched between the prince or defender and the throne;<br>" +
                    "d) The defender is trapped between the attacker and the empty throne</html>";
            JLabel htmlLabel = new JLabel();
            htmlLabel.setBounds(0, 0, 740, 530);
            htmlLabel.setVerticalAlignment(SwingConstants.TOP);
            htmlLabel.setText(text);
            htmlLabel.setFont(font);
            htmlLabel.setBorder(BorderFactory.createTitledBorder("Rules. Page 2 of 3"));
            this.add(htmlLabel);

            JLabel jlRules1 = new JLabel(rules1);
            jlRules1.setBounds(60,180, 600, 344);
            this.add(jlRules1);
            this.setVisible(false);
        }
    }
    private class DescriptionOfRules3 extends JPanel {
        private  ImageIcon rules2 = new ImageIcon("./art/rules2.png");
        private  ImageIcon rules3 = new ImageIcon("./art/rules3.png");
        Rectangle frameSize = new Rectangle(290, 45, 740, 650);

        DescriptionOfRules3() {
            this.setLayout(null);
            this.setBounds(new Rectangle());
            this.setBounds(frameSize);
            this.setOpaque(false);
            this.setVisible(true);
            this.setFont(font);
            this.setForeground(Color.BLACK);

            String text = "<html>The figure must be clamped strictly horizontally or " +
                    "vertically – it is impossible to clamp with an \"angle\", as shown " +
                    "in the figure:<br><br><br><br><br><br><br>" +
                    "The prince can be captured in the same way as all other pieces, " +
                    "except in the following cases:<br>" +
                    "a) The King is on the throne. In this case, the attackers must occupy all<br>" +
                    "four squares around the throne, blocking all escape routes for the prince;<br>" +
                    "b) The King is in the cage next to the throne horizontally or vertically. " +
                    "In this case, the attackers must occupy all three squares around the prince, " +
                    "pinning him to the throne;<br><br><br><br><br><br><br><br>" +
                    "In the field, the prince participates in the battle as well as other chips. " +
                    "If the opponent cannot make any move (all chips are blocked), he is recognized " +
                    "as the loser.</html>";
            JLabel htmlLabel = new JLabel();
            htmlLabel.setBounds(0, 0, 740, 530);
            htmlLabel.setVerticalAlignment(SwingConstants.TOP);
            htmlLabel.setText(text);
            htmlLabel.setFont(font);
            htmlLabel.setBorder(BorderFactory.createTitledBorder("Rules. Page 3 of 3"));
            this.add(htmlLabel);

            JLabel jlRules1 = new JLabel(rules2);
            jlRules1.setBounds(200,65, 270, 130);
            this.add(jlRules1);

            JLabel jlRules2 = new JLabel(rules3);
            jlRules2.setBounds(100,335, 479, 140);
            this.add(jlRules2);
            this.setVisible(false);
        }
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.parent.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }
}
