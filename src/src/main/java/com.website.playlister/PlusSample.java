package com.website.playlister;


public class PlusSample {

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