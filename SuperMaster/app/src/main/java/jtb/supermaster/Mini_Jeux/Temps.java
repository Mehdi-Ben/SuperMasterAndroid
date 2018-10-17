package jtb.supermaster.Mini_Jeux;

import android.os.Handler;

/**
 * Created by Maxime on 04/04/2017 (22:55).
 */

public abstract class Temps
{
    private int time;
    public int realTime;
    private int deltaTime;
    private int tick;

    private int minute;
    private int seconde;
    private int milliseconde;

    private int startTime;

    public boolean isRunning;

    private Handler timerHandler = new Handler();
    Runnable timerRunnable;

    public Temps(final int tickTime) {
        startTime = (int)System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        isRunning = true;
        timerRunnable = new Runnable()
        {
            @Override
            public void run() {
                    int nextTime = (int) (System.currentTimeMillis() - startTime);
                    tick += 1;
                    deltaTime = nextTime - realTime;
                    realTime = nextTime;
                    time = time + deltaTime * ((isRunning)? 1 : 0);
                    seconde = time / 1000;
                    seconde = seconde % 60;
                    milliseconde = time - (seconde * 1000);
                    minute = seconde / 60;

                    if (isRunning) {
                        update();
                    }
                    if (timerHandler != null) {
                        timerHandler.postDelayed(this, tickTime);
                    }
            }
        };

    }


    public abstract void update();

    public void stop(){

        if (timerHandler != null) {
            //timerHandler.removeCallbacks(timerRunnable);
            isRunning = false;
        }
    }
    public void resume(){

        timerHandler.postDelayed(timerRunnable, 0);
        isRunning = true;
    }

    public void destroy()
    {
        timerRunnable = null;
        timerHandler = null;


    }

    public void reset()
    {
        this.startTime = (int) System.currentTimeMillis();
        this.realTime = 0;
        this.time = 0;
        this.tick = 0 ;
        isRunning = true;
    }

    public int getDeltaTime(){
        return this.deltaTime;
    }
    public float getDeltaTimeSeconde(){ return (this.deltaTime/ 1000f);}
    public int getTime() { return this.time; }
    public int getTick() { return this.tick; }
    public int getTimeSeconde() { return (this.time / 1000); }
    public int getMinute() { return this.minute; }
    public int getSeconde() { return this.seconde; }
    public int getMilliseconde() { return this.milliseconde; }
}
