package com.website.playlister;
import PlaylisterMain2.My2;
import PlaylisterMain2.My3;

public class PlusSample {
    My2 m = new My2();
    My3 m2 = new My3();
    public static void main(String[] args) {
        try{
            TrayApp tray = new TrayApp();
            return;
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(1);
    }

}