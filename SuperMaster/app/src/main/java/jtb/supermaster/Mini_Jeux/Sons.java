package jtb.supermaster.Mini_Jeux;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by blanc on 22/04/2017.
 */

public class Sons {
    private MediaPlayer mp1;
    public static boolean sonOn = true;
    public static boolean sonMusicOn = true;
    public static boolean vibrationsOn = true;
    private static ArrayList<Sons> listeSons = new ArrayList<Sons>();
    private int audio;
    private Context context;
    private String style;

    public Sons(int audio, Context context, String s){
        this.audio = audio;
        this.context = context;
        this.style = s;
    }

    public MediaPlayer getSon(){return mp1;}
    public ArrayList<Sons> getListeSons(){return listeSons;}


    public MediaPlayer play() {
        mp1 = MediaPlayer.create(this.context, this.audio);
        if ((sonMusicOn && style.equals("music"))||(sonOn && style.equals("son"))) {
            try {
                mp1.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mp1.start();
            mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();

                }
            });
        }

        return mp1;

    }

    public void stop(){
        if ((sonMusicOn && style.equals("music"))||(sonOn && style.equals("son"))) {
            this.mp1.stop();
        }
    }

}
