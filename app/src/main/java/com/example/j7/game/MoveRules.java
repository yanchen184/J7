package com.example.j7.game;

import android.content.Context;

import com.example.j7.GameMainActivity;

public class MoveRules {

    private GameMainActivity activity;
    private Context context;

    public MoveRules(Context context) {
        this.context = context;
        this.activity = (GameMainActivity) context;

    }


    public void moveCommon(int x, int y, String txt) {
        /**邊界限制*/
        if (activity.locationXSelf + x < 0) {
            activity.locationXSelf = 0;
        } else if (activity.locationXSelf + x > 4) {
            activity.locationXSelf = 4;
        } else {
            activity.locationXSelf = activity.locationXSelf + x;
        }
        if (activity.locationYSelf + y < 0) {
            activity.locationYSelf = 0;
        } else if (activity.locationYSelf + y > 2) {
            activity.locationYSelf = 2;
        } else {
            activity.locationYSelf = activity.locationYSelf + y;
        }

        activity.sendMessageMove();
        activity.includeMoveInvisible();
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
        activity.locationXCom = x;
        activity.locationYCom = y;
        activity.imageCom.layout(activity.locationX[x].getLeft() + 30 + 50 , activity.locationY[y].getTop() - 200, activity.locationX[x].getLeft() + 100 + 30 + 50 , activity.locationY[y].getBottom());
    }

    public void moveJudgmentSelf(int x, int y) {
        /**重新繪製位置*/
        activity.locationXSelf = x;
        activity.locationYSelf = y;
        activity.imagePlayer.layout(activity.locationX[x].getLeft() + 30, activity.locationY[y].getTop() - 200, activity.locationX[x].getLeft() + 100 + 30, activity.locationY[y].getBottom());
    }


}