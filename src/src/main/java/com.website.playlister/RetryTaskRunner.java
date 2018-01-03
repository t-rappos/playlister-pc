package com.website.playlister;

import java.util.ArrayList;

/**
 * Created by Thomas Rappos (6336361) on 1/3/2018.
 */

abstract class IRetryTaskContext<DataType>{
    private volatile ArrayList<DataType> results = new ArrayList<DataType>();
    ArrayList<DataType> getResults(){return results;}


    private int progress = 0 ;

    void _addDataAndProgress(DataType data) {
        results.add(data);
        skipCurrentIndex();
    }

    private int getProgressIndex(){return progress;}
    private void skipCurrentIndex(){progress++;}
    void _setComplete(){progress = -1;}
    public abstract Runnable _getNewTask(int progressIndex);
    public abstract void _onCompletion();
    private int errorCount = 0;

    private void loop(){
        System.out.println("new loop");
        Thread t = new Thread(_getNewTask(getProgressIndex()));
        t.start();
        long startTime = System.nanoTime();
        int lastCount = -1;
        while (t.isAlive()) {

            if (getProgressIndex() != lastCount) {
                lastCount = getProgressIndex(); //updated since last time
                startTime = System.nanoTime();
            }
            long dt = System.nanoTime() - startTime;
            if ((float) dt / 1000000f > 200) {
                System.out.println("Assuming thread is dead");
                skipCurrentIndex();
                errorCount++;
                try {
                    t.stop();
                    t.destroy();
                    t.interrupt();
                    System.out.println("killed thread");
                } catch(java.lang.NoSuchMethodError e){

                }
            }
        }
        System.out.println("loop ended");
    }

    void Run(){
        while(getProgressIndex() >= 0){
            loop();
        }
        System.out.println("IRetryTaskContext : completed : with " + errorCount + " retries");
        _onCompletion();
    }
}