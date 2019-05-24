/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Pool.Pool;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author julia
 */
public class CountDown {

    Timer timer;
    Pool miPool;
    int timin;
    byte[] ip;

    public CountDown(int tiempo, byte[] ip, Pool myPool) 
    {
        miPool = myPool;
        timin = tiempo;
        this.ip = ip;
        timer = new Timer();
    }
    
    public void activate()
    {
        timer.schedule(new DisplayCountdown(), 0, 1000);
    }

    class DisplayCountdown extends TimerTask {

        int count = 0;

        @Override
        public void run() {
            if (timin == count) {
                miPool.releaseIP(ip);
                this.cancel();
                //or do something IDK
            } else {
                count++;
            }
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
