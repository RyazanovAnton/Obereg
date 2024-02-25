import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class MainMenuWindow extends JFrame  {
    public MainMenuWindow(){
        this.setBounds(200,100,1280,720);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setVisible(true);
        ImageIcon mainMenuBG = new ImageIcon("./resources/mainmenubg2.jpg");
        jlMainBG = new JLabel(mainMenuBG);
        jlMainBG.setBounds(0,0,1280,720);
        //this.add(jlMainBG);

        jlMainMenuLabels = new JLabel[arrMainMenuLabels.length];
        for (int i=0; i<arrMainMenuLabels.length; i++){
            jlMainMenuLabels[i] = new JLabel(arrMainMenuLabels[i]);
            getContentPane().add(jlMainMenuLabels[i]);
            jlMainMenuLabels[i].setBounds(50,75+(i*75),400,70);
            //jlMainMenuLabels[i].setBounds(550,220+(i*75),400,70);
            jlMainMenuLabels[i].setFont(mainFont);
            jlMainMenuLabels[i].setForeground(mainMenuColor);
            jlMainMenuLabels[i].setHorizontalAlignment(SwingConstants.LEFT);
        }

        this.setResizable(false);
        this.setTitle("Obereg v1.0");
        try {
            this.setIconImage(ImageIO.read(new File("./resources/icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.getContentPane().add(jlMainBG);




        myMusic mainTheme = new myMusic("./audio/main.wav");
        mainTheme.musicOn();

        jlMainMenuLabels[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                mainTheme.musicOff();
                new GamePropertyWindow();


            }

        });

        jlMainMenuLabels[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click");
                System.exit(0);


            }
        });

    }

    String[] arrMainMenuLabels = new String[]{"New Game", "Tutorial", "Options", "Quit"};
    JLabel[] jlMainMenuLabels;

    JLabel jlMainBG;
    Font mainFont = new Font("Blackburr", Font.PLAIN, 70);
    Color mainMenuColor = new Color(161, 10, 10);

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
//    public static void playMusic(String location){
//        try{
//            File musicPath = new File(location);
//
//            if(musicPath.exists()){
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
//                Clip myClip =AudioSystem.getClip();
//                myClip.open(audioInputStream);
//                myClip.start();
//            }
//            else{
//                System.out.println("Can't find the file!");
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//    }

}


