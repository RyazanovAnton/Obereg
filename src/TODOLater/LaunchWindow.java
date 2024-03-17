package TODOLater;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class LaunchWindow extends JFrame {
    public LaunchWindow(){
        this.setBounds(550,300,400,250);
       // this.setLayout(null);
       // this.setVisible(true);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        File f1 = new File("./images/launchlabel.jpg");
        if (f1.exists()){
            
        }

        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./images/launchlabel.jpg")))));
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

//        Timer time = new Timer(1000, null);
//        time.start();


        MainMenuWindow mm1 = new MainMenuWindow();
//        Thread th1 = new Thread(mm1);
//
//        th1.start();

//        try {
//            this.wait(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }



    }
}
