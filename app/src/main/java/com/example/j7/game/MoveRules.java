package com.example.j7.game;

import android.content.Context;

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
        activity.binding.imagePlayer.layout(activity.locationX[x].getLeft() -100, activity.locationY[y].getTop() - 200, activity.locationX[x + 1].getLeft()-60, activity.locationY[y].getBottom());
    }


}