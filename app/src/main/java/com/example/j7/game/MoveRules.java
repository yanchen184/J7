package com.example.j7.game;

import android.content.Context;
import android.util.Log;

import com.example.j7.GameActivity;
import com.example.j7.Parameter;

public class MoveRules {

    private GameActivity activity;
    private Context context;

    public MoveRules(Context context) {
        this.context = context;
        this.activity = (GameActivity) context;

    }


    public void moveCommon(int x, int y) {
        /**邊界限制*/
        if (activity.parameter.getLocationXS() + x < 0) {
            activity.parameter.setLocationXS(0);
        } else if (activity.parameter.getLocationXS() + x > 4) {
            activity.parameter.setLocationXS(4);
        } else {
            activity.parameter.setLocationXS(activity.parameter.getLocationXS() + x);
        }
        if (activity.parameter.getLocationYS() + y < 0) {
            activity.parameter.setLocationYS(0);
        } else if (activity.parameter.getLocationYS() + y > 2) {
            activity.parameter.setLocationYS(2);
        } else {
            activity.parameter.setLocationYS(activity.parameter.getLocationYS() + y);
        }
        activity.sendMessageMove();
    }

    public void computer(int x, int y) {
        x = -x;
        /**邊界限制*/
//        Log.d("s", String.valueOf(activity.parameter.getLocationXC()));
//        Log.d("s", String.valueOf(activity.parameter.getLocationYC()));
        if (activity.parameter.getLocationXC() + x < 0) {
            activity.parameter.setLocationXC(0);
        } else if (activity.parameter.getLocationXC() + x > 4) {
            activity.parameter.setLocationXC(4);
        } else {
            activity.parameter.setLocationXC(activity.parameter.getLocationXC() + x);
        }
        if (activity.parameter.getLocationYC() + y < 0) {
            activity.parameter.setLocationYC(0);
        } else if (activity.parameter.getLocationYC() + y > 2) {
            activity.parameter.setLocationYC(2);
        } else {
            activity.parameter.setLocationYC(activity.parameter.getLocationYC() + y);
        }
//        Log.d("computer", String.valueOf(activity.parameter.getLocationXC()));
//        Log.d("computer", String.valueOf(activity.parameter.getLocationYC()));

//        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("locationXSelf").setValue(activity.parameter.getLocationXC());
//        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("locationYSelf").setValue(activity.parameter.getLocationYC());
        int yc = 0;
        switch (activity.parameter.getLocationXC()) {
            case 0:
                yc = 4;
                break;
            case 1:
                yc = 3;
                break;
            case 2:
                yc = 2;
                break;
            case 3:
                yc = 1;
                break;
            case 4:
                yc = 0;
                break;
        }
        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("X").setValue(yc);
        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Y").setValue(activity.parameter.getLocationYC());
    }

    public void moveJudgmentCom(int x, int y) {
        /**重新繪製位置*/
        switch (x) {
            case 4:
                x = 0;
                break;
            case 3:
                x = 1;
                break;
            case 2:
                x = 2;
                break;
            case 1:
                x = 3;
                break;
            case 0:
                x = 4;
                break;
        }
        activity.parameter.setLocationXC(x);
        activity.parameter.setLocationYC(y);
        activity.binding.imageCom.layout(activity.locationX[x].getLeft() + 60, activity.locationY[y].getTop() - 200, activity.locationX[x + 1].getLeft() + 100, activity.locationY[y].getBottom());
    }

    public void moveJudgmentSelf(int x, int y) {
        /**重新繪製位置*/
        activity.parameter.setLocationXS(x);
        activity.parameter.setLocationYS(y);
        activity.binding.imagePlayer.layout(activity.locationX[x].getLeft() - 100, activity.locationY[y].getTop() - 200, activity.locationX[x + 1].getLeft() - 60, activity.locationY[y].getBottom());
    }


}