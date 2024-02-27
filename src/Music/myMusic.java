package Music;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class myMusic {

    public Clip myClip;
    //boolean musicPlaying = true;
    public String location;
    public myMusic(String location) {
        this.location=location;
    }



    public void musicOn(){
        try {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicPath);
                myClip = AudioSystem.getClip();
                myClip.open(audioInputStream);
                myClip.start();
                myClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Can't find the file!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        if (bool){
//
//
//
//        }
//        else {
//            myClip.stop();
//            myClip.close();
//        }
    }

    public void musicOff(){
        myClip.stop();
        myClip.close();
    }

}

