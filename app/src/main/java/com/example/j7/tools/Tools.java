package com.example.j7.tools;

import com.example.j7.R;

public class Tools {

    public String random4Number(){
        int n = (int) (Math.random() * 9998 + 1);
//        int n = (int) (Math.random() * 3 + 1);
        String nn = "";
        if (Integer.toString(n).length() < 4) {
            for (int i = 0; i < (4 - Integer.toString(n).length()); i++) {
                nn = nn + "0";
            }
        }
        nn = nn + Integer.toString(n);
        return nn;
    }

    public String roleChange(int index){
        String je = null;
        switch (index) {
            case 0:
                je = "j4";
                break;
            case 1:
                je = "fs";
                break;
            case 2:
                je = "player";
                break;
            case 3:
                je = "b74";
                break;
        }
        return je;
    }

    public int roleChangePicture(int index){
        int je = 0;
        switch (index) {
            case 0:
                je = R.drawable.j4;
                break;
            case 1:
                je = R.drawable.fs;
                break;
            case 2:
                je = R.drawable.player;
                break;
            case 3:
                je = R.drawable.b74;
                break;
        }
        return je;
    }


}
