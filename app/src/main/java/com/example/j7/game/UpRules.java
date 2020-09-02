package com.example.j7.game;

import android.content.Context;

import com.example.j7.GameActivity;

public class UpRules {
    private GameActivity activity;
    private Context context;

    public UpRules(Context context) {
        this.context = context;
        this.activity = (GameActivity) context;

    }



//    public void upJudgmentSelf(int l, String pp) {
//        if (pp.equals("hp")) {
//            activity.controlMPHP(activity.txt_self_hp, l);
//        } else if (pp.equals("mp")) {
//            activity.controlMPHP(activity.txt_self_mp, l);
//        }
//
//    }
//
//    public void upJudgmentCom(int l, String pp) {
//        if (pp.equals("hp")) {
//            activity.controlMPHP(activity.txt_com_hp, l);
//        } else if (pp.equals("mp")) {
//            activity.controlMPHP(activity.txt_com_mp, l);
//        }
//    }
}