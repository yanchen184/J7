package com.example.j7.game;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.j7.GameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Boss1 {

    private GameActivity activity;
    private Context context;

    public Boss1(Context context) {
        this.context = context;
        this.activity = (GameActivity) context;

    }


    public void moveAndAtk(int boss1AtkNum) {

        double yc = Math.random();
        double yc1 = (double) Integer.parseInt(activity.binding.include.txtComMp.getText().toString()) / (double) activity.variable.getCMP();

        int yc1H = Integer.parseInt(activity.binding.include.txtComHp.getText().toString());
        int yc1HS = Integer.parseInt(activity.binding.include.txtSelfHp.getText().toString());

        Log.d("回復魔力", "隨機值 : " + String.valueOf(yc));
        Log.d("回復魔力", "剩下的魔力比例 : " + String.valueOf(yc1));
        if (yc1HS > 4 && yc1H < 4) {
            /**回復血量*/
            activity.fullRoom.child(activity.variable.getOtherPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /**提取自己的血量跟魔量*/
                    long HP = (long) snapshot.child("HP").getValue();
                    /**如果有需要回血或回魔力的話 就會在這邊增加*/
                    Log.d("機器人第一戰鬥階段", "回復血量 : " + activity.parameter.HPUPBoss1);
                    if ((int) activity.parameter.HPUPBoss1 + (int) HP > activity.variable.getCHP()) {
                        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("HP").setValue(activity.variable.getCHP());
                    } else {
                        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("HP").setValue((int) activity.parameter.HPUPBoss1 + (int) HP);
                    }
                    /**回血量跟魔量後 將Next歸零*/
                    activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("HPUP").setValue(0);
                    activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("MPUP").setValue(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else if (yc > yc1) {
            /**回復魔力*/
            activity.fullRoom.child(activity.variable.getOtherPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /**提取自己的血量跟魔量*/
                    long MP = (long) snapshot.child("MP").getValue();
                    /**如果有需要回血或回魔力的話 就會在這邊增加*/
                    Log.d("機器人第一戰鬥階段", "回復魔力 : " + activity.parameter.MPUPBoss1);
                    if ((int) activity.parameter.MPUPBoss1 + (int) MP > activity.variable.getCMP()) {
                        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("MP").setValue(activity.variable.getCMP());
                    } else {
                        activity.fullRoom.child(activity.variable.getOtherPlayer()).child("MP").setValue((int) activity.parameter.MPUPBoss1 + (int) MP);
                    }
                    /**回血量跟魔量後 將Next歸零*/
                    activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("HPUP").setValue(0);
                    activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("MPUP").setValue(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            if (activity.variable.getOtherPlayerName().equals("Boss1")) {
                Log.d("機器人移動 - 追擊狀態", String.valueOf(activity.variable.getJg()));
//            activity.moveRules.computer(-1, 0);
                if (activity.variable.getJg()) {

                    /**追擊模式*/
                    int agmd = (int) (Math.pow(activity.parameter.getLocationXC() - activity.parameter.getLocationXS(), 2) +
                            Math.pow(activity.parameter.getLocationYC() - activity.parameter.getLocationYS(), 2));

                    Log.d("agmd", String.valueOf(agmd));
                    int x = 0;
                    int y = 0;
                    int xy = 0;

                    if (activity.variable.getMatch() < 15) {
                        switch (agmd) {
                            case 0:
                            case 1:
                            case 4:
                            case 9:
                                x = 0;
                                y = 0;
                                xy = (int) (Math.random() * 2 + 1);

                                switch (xy) {
                                    case 1:
                                        int[] xx = {-1, 1};
                                        x = xx[(int) (Math.random() * 2)];
                                        break;
                                    case 2:
                                        int[] yy = {-1, 1};
                                        y = yy[(int) (Math.random() * 2)];
                                        break;
                                }
                                break;
                            default:
                                do {
                                    x = 0;
                                    y = 0;
                                    xy = (int) (Math.random() * 2 + 1);

                                    switch (xy) {
                                        case 1:
                                            int[] xx = {-1, 1};
                                            x = xx[(int) (Math.random() * 2)];
                                            break;
                                        case 2:
                                            int[] yy = {-1, 1};
                                            y = yy[(int) (Math.random() * 2)];
                                            break;
                                    }
                                } while (agmd < (Math.pow(activity.parameter.getLocationXC() + x - activity.parameter.getLocationXS(), 2) + Math.pow(activity.parameter.getLocationYC() + y - activity.parameter.getLocationYS(), 2)));
                                Log.d("機器人移動 - 追擊狀態 - 最終判斷", String.valueOf(x));
                                Log.d("機器人移動 - 追擊狀態 - 最終判斷", String.valueOf(y));
                                break;
                        }
                    } else {
                        do {
                            x = 0;
                            y = 0;
                            xy = (int) (Math.random() * 2 + 1);

                            switch (xy) {
                                case 1:
                                    int[] xx = {-1, 1};
                                    x = xx[(int) (Math.random() * 2)];
                                    break;
                                case 2:
                                    int[] yy = {-1, 1};
                                    y = yy[(int) (Math.random() * 2)];
                                    break;
                            }
                        } while (agmd < (Math.pow(activity.parameter.getLocationXC() + x - activity.parameter.getLocationXS(), 2) + Math.pow(activity.parameter.getLocationYC() + y - activity.parameter.getLocationYS(), 2)));
                        Log.d("機器人移動 - 追擊狀態 - 最終判斷", String.valueOf(x));
                        Log.d("機器人移動 - 追擊狀態 - 最終判斷", String.valueOf(y));
                    }

                    activity.moveRules.computer(x * -1, y);
                }
            }


            activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("atkHP").setValue(activity.variable.getFinalHPC().get(boss1AtkNum - 1));
            activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("atkMP").setValue(activity.variable.getFinalMPC().get(boss1AtkNum - 1));
            activity.fullRoom.child(activity.variable.getOtherPlayer()).child("Next").child("atkR").setValue(activity.variable.getFinalAtlRC().get(boss1AtkNum - 1));
        }
    }
}
