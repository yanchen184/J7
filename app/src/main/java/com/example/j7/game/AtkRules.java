package com.example.j7.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.j7.GameActivity;
import com.example.j7.Parameter;
import com.example.j7.R;
import com.example.j7.Variable;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.DESedeKeySpec;
import javax.xml.parsers.FactoryConfigurationError;

import static com.example.j7.tools.Name.ERROR;
import static com.example.j7.tools.Name.STAND;
import static com.example.j7.tools.Name.UNIQUE;


public class AtkRules {
    private GameActivity activity;
    private Context context;
    Tools tools = new Tools();

//    Parameter parameter = new Parameter();
//    Variable variable = new Variable();

    public AtkRules(Context context) {
        this.context = context;
        this.activity = (GameActivity) context;
    }

//    public int[][] atkRangeSelf; // 自己的攻擊範圍(根據座標重繪後的)
//    public int[][] atkRangeCom; //對手的攻擊範圍(根據座標重繪後的)
//    public int[][] atkRangeDD; //重疊的範圍(根據座標重繪後的)
//    public AtkDecide atkDecide = new AtkDecide();


    /**
     * 攻擊範圍(根據座標重繪後)
     * 1.[2,2]
     * 2.[1,2,3]
     */
    public int[][] getAtkRange(String who, ArrayList<Integer> atkRange) {
        /**會記錄玩家攻擊的位置方便後續計算*/
        int[] XY = null;
        int pink;
        switch (who) {
            case "self":
                XY = new int[]{activity.parameter.getLocationXS(), activity.parameter.getLocationYS()};
                pink = activity.parameter.getAtkNumS();
                break;
            case "com":
                XY = new int[]{activity.parameter.getLocationXC(), activity.parameter.getLocationYC()};
                pink = activity.parameter.getAtkNumC();
                break;
            default:
                pink = ERROR;
                break;
        }
        int[][] point15 = new int[0][];
        switch (pink) {//點擊第幾個 1-4 開始算攻擊位置
            case STAND:
            case UNIQUE:
                break;
            default:
                point15 = new int[atkRange.size()][2];
                for (int i = 0; i < atkRange.size(); i++) {
                    switch (Integer.parseInt(String.valueOf(atkRange.get(i)))) {
                        case 1:
                            point15[i][0] = XY[0] - 1;
                            point15[i][1] = XY[1] + 1;
                            break;
                        case 2:
                            point15[i][0] = XY[0];
                            point15[i][1] = XY[1] + 1;
                            break;
                        case 3:
                            point15[i][0] = XY[0] + 1;
                            point15[i][1] = XY[1] + 1;
                            break;
                        case 4:
                            point15[i][0] = XY[0] - 1;
                            point15[i][1] = XY[1];
                            break;
                        case 5:
                            point15[i][0] = XY[0];
                            point15[i][1] = XY[1];
                            break;
                        case 6:
                            point15[i][0] = XY[0] + 1;
                            point15[i][1] = XY[1];
                            break;
                        case 7:
                            point15[i][0] = XY[0] - 1;
                            point15[i][1] = XY[1] - 1;
                            break;
                        case 8:
                            point15[i][0] = XY[0];
                            point15[i][1] = XY[1] - 1;
                            break;
                        case 9:
                            point15[i][0] = XY[0] + 1;
                            point15[i][1] = XY[1] - 1;
                            break;
                    }
                }
                switch (who) {
                    case "self":
                        activity.parameter.setAtkRangeSelf(point15);
                        break;
                    case "com":
                        activity.parameter.setAtkRangeCom(point15);
                        break;
                }
                break;
        }
        return point15;
    }


    /**
     * 繪製地圖
     * 1.畫自己的攻擊範圍
     * 2.畫對手的攻擊範圍
     * 3.畫共同的的攻擊範圍
     */
    public void atkJudgmentCommonRange(String x, int[][] range) {
        Resources res = activity.getResources();
        Drawable drawable;
        drawable = res.getDrawable(R.drawable.atk);
        if (x.equals("com")) {
            drawable = res.getDrawable(R.drawable.atkc);
        }
        if (x.equals("DD")) {
            drawable = res.getDrawable(R.drawable.atkt);
        }
        for (int i = 0; i < range.length; i++) {
            if (range[i][0] >= 0 && range[i][1] >= 0 && range[i][0] < 5 && range[i][1] < 3) {
//                System.out.println(x + "燃燒的地方 : " + range[i][0] + "," + range[i][1]);
                switch (range[i][0] * 10 + range[i][1]) {
                    case 0:
                        activity.binding.atkKJ00.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ00.setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        activity.binding.atkKJ10.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ10.setVisibility(View.VISIBLE);
                        break;
                    case 20:
                        activity.binding.atkKJ20.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ20.setVisibility(View.VISIBLE);
                        break;
                    case 30:
                        activity.binding.atkKJ30.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ30.setVisibility(View.VISIBLE);
                        break;
                    case 40:
                        activity.binding.atkKJ40.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ40.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        activity.binding.atkKJ01.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ01.setVisibility(View.VISIBLE);
                        break;
                    case 11:
                        activity.binding.atkKJ11.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ11.setVisibility(View.VISIBLE);
                        break;
                    case 21:
                        activity.binding.atkKJ21.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ21.setVisibility(View.VISIBLE);
                        break;
                    case 31:
                        activity.binding.atkKJ31.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ31.setVisibility(View.VISIBLE);
                        break;
                    case 41:
                        activity.binding.atkKJ41.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ41.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        activity.binding.atkKJ02.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ02.setVisibility(View.VISIBLE);
                        break;
                    case 12:
                        activity.binding.atkKJ12.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ12.setVisibility(View.VISIBLE);
                        break;
                    case 22:
                        activity.binding.atkKJ22.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ22.setVisibility(View.VISIBLE);
                        break;
                    case 32:
                        activity.binding.atkKJ32.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ32.setVisibility(View.VISIBLE);
                        break;
                    case 42:
                        activity.binding.atkKJ42.setBackgroundDrawable(drawable);
                        activity.binding.atkKJ42.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    /**
     * 計算共同的攻擊範圍
     */
    public void rangeSame() {
        List<Integer> listX = new ArrayList<>();
        List<Integer> listY = new ArrayList<>();

        Log.d("計算共同範圍", String.valueOf(activity.parameter.getAtkRangeCom() != null));
        Log.d("計算共同範圍", String.valueOf(activity.parameter.getAtkRangeSelf() != null));

        if (activity.parameter.getAtkRangeCom() != null && activity.parameter.getAtkRangeSelf() != null) {
            for (int i = 0; i < activity.parameter.getAtkRangeCom().length; i++) {
                for (int j = 0; j < activity.parameter.getAtkRangeSelf().length; j++) {
                    if (activity.parameter.getAtkRangeCom()[i][0] * 10 + activity.parameter.getAtkRangeCom()[i][1] == activity.parameter.getAtkRangeSelf()[j][0] * 10 + activity.parameter.getAtkRangeSelf()[j][1]) {
                        listX.add(activity.parameter.getAtkRangeCom()[i][0]);
                        listY.add(activity.parameter.getAtkRangeCom()[i][1]);
                    }
                }
            }

            int[][] atkRangeDD = new int[listX.size()][2];
            for (int i = 0; i < listX.size(); i++) {
                atkRangeDD[i][0] = listX.get(i);
                atkRangeDD[i][1] = listY.get(i);
                Log.d("計算共同範圍", atkRangeDD[i][0] + " , " + atkRangeDD[i][1]);
            }
            activity.parameter.setAtkRangeDD(atkRangeDD);

            if (atkRangeDD != null) {
                atkJudgmentCommonRange("DD", activity.parameter.getAtkRangeDD());
            }
        }
        initAtkRange();
    }

    //    "player1" 打到 "player2"
    public void HPHit(final String playerW) {
//        DatabaseReference fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(activity.roomKey);//FirebaseDatabase
        activity.fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /**
                 * 1.player2的血量
                 * 2.player1的傷害
                 * 3.player2的血量 = player2的血量 - player1的傷害
                 * */
                String playerL = "";
                switch (playerW) {
                    case "player1":
                        playerL = "player2";
                        break;
                    case "player2":
                        playerL = "player1";
                        break;
                }

                long HP = (long) snapshot.child(playerL).child("HP").getValue();
                long HPUse = (long) snapshot.child(playerW).child("Next").child("atkHP").getValue();
                activity.fullRoom.child(playerL).child("HP").setValue((int) HP - (int) HPUse);
                activity.fullRoom.child(playerW).child("Next").child("atkHP").setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * 1.atkRange [1,2,3]
     */
    public void atkJudgmentSelf(int[][] atkRange, int hp, int mp) {

        judgment(activity.parameter.getAtkNumS()
                , atkRange, hp, mp
                , activity.variable.getPlayer()
                , "self");
        /**
         * 1. 9宮格攻擊範圍 - 攻擊傷害跟魔量 atkRange , hp , mp
         * 2. 按的號碼 - 自己按的或別人按的
         * 3. 別人所在的位置 - 用來判斷是否有打到他
         * 4. 你是player1 還是 player2
         * 5. SC
         * */
    }

    public void atkJudgmentCom(int[][] atkRange, int hp, int mp) {
        judgment(activity.parameter.getAtkNumC()
                , atkRange, hp, mp
                , activity.variable.getOtherPlayer()
                , "com");

    }

    public void judgment(int atkNum, int[][] atkRange, final int hp, final int mp, final String name, final String SC) {
        /**
         * 1.扣魔力
         * 2.自己是否攻擊成功
         * */
        Log.d("攻擊內容", "HP : " + hp);
        Log.d("攻擊內容", "MP : " + mp);
        switch (atkNum) {
            case STAND:
                Toast.makeText(context, "自己站著不動", Toast.LENGTH_LONG).show();
                break;
            default:
                if (SC.equals("self")) {
                    atkPD(hp, mp, atkRange, name, SC);
                } else {
                    atkPD(hp, mp, atkRange, name, SC);
                    rangeSame();
                }
                break;
        }


    }

    /*判定**/
    private void atkPD(int hp, int mp, int[][] atkRealRange, String name, String SC) {
        /**是否攻擊成功*/
        boolean success = false;
        /**畫出攻擊範圍*/
        atkJudgmentCommonRange(SC, atkRealRange);
        int lx = 0;
        int ly = 0;
        if (SC.equals("self")) {
            lx = activity.parameter.getLocationXC();
            ly = activity.parameter.getLocationYC();
        } else if (SC.equals("com")) {
            lx = activity.parameter.getLocationXS();
            ly = activity.parameter.getLocationYS();
        }

        /**對手的位置 -包含進- 自己的攻擊位置就判定true */
        for (int[] el : atkRealRange) {
            if (lx == el[0] && ly == el[1]) {
                success = true;
            }
        }
        /**如果成功就進行扣寫判定*/
        if (success) {
            HPHit(name);
            Toast.makeText(context, "攻擊成功!! 扣對方 " + hp + " HP ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "攻擊失敗.. 消耗 " + mp + " MP ", Toast.LENGTH_LONG).show();
        }
    }


    public ArrayList<Integer> atk9o(ArrayList<Integer> atkRange) {
        ArrayList<Integer> newAtkRange = new ArrayList<>();
        int change;
        for (int i = 0; i < atkRange.size(); i++) {
            switch (Integer.parseInt(String.valueOf(atkRange.get(i)))) {
                case 1:
                    change = 3;
                    break;
                case 2:
                    change = 2;
                    break;
                case 3:
                    change = 1;
                    break;
                case 4:
                    change = 6;
                    break;
                case 5:
                    change = 5;
                    break;
                case 6:
                    change = 4;
                    break;
                case 7:
                    change = 9;
                    break;
                case 8:
                    change = 8;
                    break;
                case 9:
                    change = 7;
                    break;
                case 0:
                    change = 0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + Integer.parseInt(String.valueOf(atkRange.get(i))));
            }
            newAtkRange.add(change);
        }
        return newAtkRange;
    }


    public void initAtkRange() {
        activity.parameter.setAtkRangeDD(null);
        activity.parameter.setAtkRangeCom(null);
        activity.parameter.setAtkRangeSelf(null);
    }


    public int[][] atkRealRange(int atkNum, ArrayList<Integer> atkRange, String SC) {
        int[][] allMap = new int[1][2];
        allMap[0][0] = -1;
        allMap[0][1] = -1;
        switch (atkNum) {
            case STAND:
                Toast.makeText(context, "自己站著不動", Toast.LENGTH_LONG).show();
                allMap[0][0] = 100;
                allMap[0][1] = 100;
                return allMap;
            case UNIQUE:
                int ii;
                Boolean b;
                if (SC.equals("self")) {
                    ii = activity.variable.getIndex();
                    b = activity.variable.getUnique();
                } else {
                    ii = activity.variable.getIndexE();
                    b = activity.variable.getUniqueC();
                }
                Log.d("獨有技能判定",  tools.roleChange((int) ii)+ " " +b);
                if (b) {
                    switch (tools.roleChange((int) ii)) {
                        case "fs":
                            /**法師獨有技能*/
                            allMap = new int[15][2];
                            for (int i = 0; i < 3; i++) {
                                allMap[0 + 5 * i][0] = 0;
                                allMap[0 + 5 * i][1] = i;
                                allMap[1 + 5 * i][0] = 1;
                                allMap[1 + 5 * i][1] = i;
                                allMap[2 + 5 * i][0] = 2;
                                allMap[2 + 5 * i][1] = i;
                                allMap[3 + 5 * i][0] = 3;
                                allMap[3 + 5 * i][1] = i;
                                allMap[4 + 5 * i][0] = 4;
                                allMap[4 + 5 * i][1] = i;
                            }
                            return allMap;
                        case "player":
                            /**普通人獨有技能*/
                            allMap = new int[1][2];
                            if(SC.equals("self")) {
                                allMap[0][0] = activity.variable.getPlayerUXB();
                                allMap[0][1] = activity.variable.getPlayerUYB();
                            }else{
                                allMap[0][0] = activity.variable.getPlayerUX();
                                allMap[0][1] = activity.variable.getPlayerUY();
                            }
                            return allMap;
                        default:
                            allMap[0][0] = 101;
                            allMap[0][1] = 101;
                            return allMap;
                    }
                }
                allMap[0][0] = 102;
                allMap[0][1] = 102;
                return allMap;
            default:
                return getAtkRange(SC, atkRange);
        }
    }


}
