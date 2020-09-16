package com.example.j7;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.j7.databinding.GameActivityMainBinding;
import com.example.j7.game.AtkRules;
import com.example.j7.game.MoveRules;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;
import static com.example.j7.tools.Name.STAND;
import static com.example.j7.tools.Name.UNIQUE;

public class GameActivity extends AppCompatActivity {
    /**
     * 0626開幹
     */
    public MoveRules moveRules = new MoveRules(this);
    public AtkRules atkRules = new AtkRules(this);
    public StartActivity startActivity = new StartActivity();
    public Tools tools = new Tools();
    public Variable variable = new Variable();
    public Parameter parameter = new Parameter();


    public View[] locationX;
    public View[] locationY;
    public View includeAtk, includeMove;

    public DatabaseReference fullRoom;
    /**
     * binding
     */
    public GameActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("主要階段", "遊戲開始");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_main);

        /**
         * 1.綁定 - binding
         * 2.綁定 - includeMove 以及 includeAtk
         * 3.roomKey - player - otherPlayer
         * 4.畫出兩個人的攻擊按鈕 insertAtkS - insertAtkC
         * 5.畫出位置 locationX - locationY
         * 6.
         * */
        parameter.setLocationXS(0);
        parameter.setLocationYS(1);
        parameter.setLocationXC(4);
        parameter.setLocationYC(1);


        /** 1 */
        binding = DataBindingUtil.setContentView(this, R.layout.game_activity_main);
        /** 2 */
        includeMove = findViewById(R.id.includeMove);
        includeAtk = findViewById(R.id.includeAtk);
        /** 3 */
        intent(); //從上一頁告知我 roomKey player otherPlayer


        fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(variable.getRoomKey());//FirebaseDatabase
        /** 5 */
        locationX = new View[]{binding.lineX0, binding.lineX1, binding.lineX2, binding.lineX3, binding.lineX4, binding.lineX5};
        locationY = new View[]{binding.lineY0, binding.lineY1, binding.lineY2};


        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //告訴我兩個人選的角色
                variable.setRoleS(Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("Index").getValue())));
                variable.setRoleC(Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("Index").getValue())));
                //告訴我兩個人的名字
                variable.setPlayerName(String.valueOf(snapshot.child(variable.getPlayer()).child("name").getValue()));
                variable.setOtherPlayerName(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("name").getValue()));
                Log.d("我是", String.valueOf(snapshot.child(variable.getPlayer()).child("name").getValue()));
                //換圖
                binding.imagePlayer.setImageResource(tools.roleChangePicture(variable.getRoleS()));
                binding.imageCom.setImageResource(tools.roleChangePicture(variable.getRoleC()));
                //對手要翻轉
                binding.imageCom.setScaleX(-1);

                //告訴我兩個人的名字
                binding.include.txtSelfName.setText(variable.getPlayerName());
                binding.include.txtComName.setText(variable.getOtherPlayerName());

                //回血回魔 法師跟騎士有稍微調整
                if (tools.roleChange(variable.getRoleS()).equals("b74")) {
                    variable.setUpHPInt(parameter.upHPB74);
                } else {
                    variable.setUpHPInt(parameter.upHPOther);
                }

                if (tools.roleChange(variable.getRoleS()).equals("fs")) {
                    variable.setUpMPInt(parameter.upMPFs);
                } else {
                    variable.setUpMPInt(parameter.upMPOther);
                }
                binding.includeMove.button5.upHP.setText(variable.getUpHPInt() + "");
                binding.includeMove.button6.upMP.setText(variable.getUpMPInt() + "");


                variable.setSHP(Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("HP").getValue())));
                variable.setSMP(Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("MP").getValue())));
                variable.setCHP(Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("HP").getValue())));
                variable.setCMP(Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("MP").getValue())));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /** 4 */
        insertAtkS();
        insertAtkC();

        /**上方的按鈕*/
        binding.includeAtkE.includeAtk1.buttonAtk1.setEnabled(false);
        binding.includeAtkE.includeAtk2.buttonAtk2.setEnabled(false);
        binding.includeAtkE.includeAtk3.buttonAtk3.setEnabled(false);
        binding.includeAtkE.includeAtk4.buttonAtk4.setEnabled(false);
        binding.includeAtkE.includeAtk5.buttonAtk5.setEnabled(false);
        binding.includeAtkE.includeAtk6.buttonAtk6.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        fullRoom.addValueEventListener(upListener);//監聽
        fullRoom.child("fourStatus").addValueEventListener(statusListener);//監聽

//        Log.d("重繪", String.valueOf(parameter.getLocationXS()));
//        Log.d("重繪", String.valueOf(parameter.getLocationYS()));
//        Log.d("重繪", String.valueOf(parameter.getLocationXC()));
//        Log.d("重繪", String.valueOf(parameter.getLocationYC()));
        /**
         * 1.確認地板上的火焰消失
         * 2.攻擊的最後兩招為 - 獨有技能
         * 3.攻擊的最後一招為 - 站著
         * 4.上面的數據做監聽 - upListener
         * 5.準備戰鬥階段監聽 - fourStatus
         * */
        fireVisible();//創建遊戲時取消所有火焰
        openBtnAtk();
        switch (tools.roleChange(variable.getIndex())) {
            case "j4":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("續力");
                break;
            case "fs":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("末日");
                break;
            case "player":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("定點攻擊");
                break;
            case "b74":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("隔檔");
                break;
        }
        binding.includeAtk.includeAtk6.buttonAtk6.setText("站著");


//        moveRules.moveJudgmentSelf(2, 2);
//        moveRules.moveJudgmentCom(2, 2);


    }


    private ValueEventListener upListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;

            /**自己被扣血*/
            if (Integer.parseInt(binding.include.txtSelfHp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getPlayer()).child("HP").getValue().toString()) > 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(600);
                am.setRepeatCount(2);
                binding.imagePlayer.startAnimation(am);
            }

            /**自己被+血*/
            if (Integer.parseInt(binding.include.txtSelfHp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getPlayer()).child("HP").getValue().toString()) < 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(200);
                am.setRepeatCount(2);
                binding.include.role1hp.startAnimation(am);
            }

            /**對手被扣血*/
            if (Integer.parseInt(binding.include.txtComHp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString()) > 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(600);
                am.setRepeatCount(2);
                binding.imageCom.startAnimation(am);
            }

            /**對手被+血*/
            if (Integer.parseInt(binding.include.txtComHp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString()) < 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(200);
                am.setRepeatCount(2);
                binding.include.role2hp.startAnimation(am);
            }

            /**自己被+魔*/
            if (Integer.parseInt(binding.include.txtSelfMp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getPlayer()).child("MP").getValue().toString()) < 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(200);
                am.setRepeatCount(2);
                binding.include.role1mp.startAnimation(am);
            }

            /**對手被+魔*/
            if (Integer.parseInt(binding.include.txtComMp.getText().toString()) - Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("MP").getValue().toString()) < 0) {
                Animation am = new AlphaAnimation(1.0f, 0.0f);
                am.setDuration(200);
                am.setRepeatCount(2);
                binding.include.role2mp.startAnimation(am);
            }


            if (Integer.parseInt(snapshot.child(variable.getPlayer()).child("HP").getValue().toString()) > variable.getSHP()) {
                fullRoom.child(variable.getPlayer()).child("HP").setValue(variable.getSHP());
            }

            if (Integer.parseInt(snapshot.child(variable.getPlayer()).child("MP").getValue().toString()) > variable.getSMP()) {
                fullRoom.child(variable.getPlayer()).child("MP").setValue(variable.getSMP());
            }

            if (Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString()) > variable.getCHP()) {
                fullRoom.child(variable.getOtherPlayerName()).child("HP").setValue(variable.getCHP());
            }

            if (Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("MP").getValue().toString()) > variable.getCMP()) {
                fullRoom.child(variable.getOtherPlayerName()).child("MP").setValue(variable.getCMP());
            }
//
            /**滿血的話就是粗體*/
//            if (Integer.parseInt(snapshot.child(variable.getPlayer()).child("HP").getValue().toString()) == variable.getSHP()) {
//                binding.include.txtSelfHp.setTypeface(Typeface.DEFAULT_BOLD);
////                binding.include.txtSelfHp.setTextColor(Color.parseColor("#4D0000"));
//            } else {
//                binding.include.txtSelfHp.setTypeface(Typeface.MONOSPACE);
////                binding.include.txtSelfHp.setTextColor(Color.parseColor("#FF0000"));
////                binding.include.txtSelfHp.setTextColor(Color.parseColor("#000000"));
//            }
////
//            if (Integer.parseInt(snapshot.child(variable.getPlayer()).child("MP").getValue().toString()) == variable.getSMP()) {
//                binding.include.txtSelfMp.setTypeface(Typeface.DEFAULT_BOLD);
////                binding.include.txtSelfMp.setTextColor(Color.parseColor("#000093"));
//            } else {
//                binding.include.txtSelfMp.setTypeface(Typeface.MONOSPACE);
////                binding.include.txtSelfMp.setTextColor(Color.parseColor("#000000"));
//            }
//
//            if (Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString()) == variable.getCHP()) {
//                binding.include.txtComHp.setTypeface(Typeface.DEFAULT_BOLD);
////                binding.include.txtComHp.setTextColor(Color.parseColor("#4D0000"));
//            } else {
//                binding.include.txtComHp.setTypeface(Typeface.MONOSPACE);
////                binding.include.txtComHp.setTextColor(Color.parseColor("#000000"));
//            }
//
//            if (Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("MP").getValue().toString()) == variable.getCMP()) {
//                binding.include.txtComMp.setTypeface(Typeface.DEFAULT_BOLD);
////                binding.include.txtComMp.setTextColor(Color.parseColor("#000093"));
//            } else {
//                binding.include.txtComMp.setTypeface(Typeface.MONOSPACE);
////                binding.include.txtComMp.setTextColor(Color.parseColor("#000000"));
//            }

//            float sb = Integer.parseInt(snapshot.child(variable.getPlayer()).child("HP").getValue().toString()) / Integer.parseInt(binding.include.txtSelfHp.getText().toString());
//            Animation sbA = new ScaleAnimation(0.0f, sb, 0.0f, sb);
//            binding.imagePlayer.startAnimation(sbA);
//
//            float cb = Integer.parseInt(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString()) / Integer.parseInt(binding.include.txtComHp.getText().toString());
//            Animation cbA = new ScaleAnimation(0.0f, cb, 0.0f, cb);
//            binding.imagePlayer.startAnimation(cbA);

            binding.include.txtSelfHp.setText(snapshot.child(variable.getPlayer()).child("HP").getValue().toString());
            binding.include.txtSelfMp.setText(snapshot.child(variable.getPlayer()).child("MP").getValue().toString());
            binding.include.txtComHp.setText(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString());
            binding.include.txtComMp.setText(snapshot.child(variable.getOtherPlayer()).child("MP").getValue().toString());


            long x1 = (long) snapshot.child(variable.getPlayer()).child("X").getValue();
            long y1 = (long) snapshot.child(variable.getPlayer()).child("Y").getValue();
            long x2 = (long) snapshot.child(variable.getOtherPlayer()).child("X").getValue();
            long y2 = (long) snapshot.child(variable.getOtherPlayer()).child("Y").getValue();

            moveRules.moveJudgmentSelf((int) x1, (int) y1);
            moveRules.moveJudgmentCom((int) x2, (int) y2);


            //將動畫參數設定到圖片並開始執行動畫


            int selfMP = Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("MP").getValue()));
            MPLimit(selfMP, Integer.parseInt(String.valueOf(variable.getFinalMP().get(0))), binding.includeAtk.includeAtk1.buttonAtk1);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(variable.getFinalMP().get(1))), binding.includeAtk.includeAtk2.buttonAtk2);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(variable.getFinalMP().get(2))), binding.includeAtk.includeAtk3.buttonAtk3);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(variable.getFinalMP().get(3))), binding.includeAtk.includeAtk4.buttonAtk4);
            MPLimit(selfMP, Integer.parseInt(String.valueOf(variable.getFinalMP().get(4))), binding.includeAtk.includeAtk5.buttonAtk5);

            int selfHP = Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("HP").getValue()));
            int comHP = Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("HP").getValue()));

            gameEnd(selfHP, comHP);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };


    public void MPLimit(int selfMP, int atkKindNum, View buttonX) {
        if (selfMP < atkKindNum) {
            buttonX.setBackgroundColor(Color.parseColor("#e0000000"));
            buttonX.setEnabled(false);
        } else {
            buttonX.setBackgroundColor(Color.parseColor("#00000000"));
            buttonX.setEnabled(true);
        }
    }


    private ValueEventListener statusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            Boolean x11 = (Boolean) snapshot.child("player11").getValue();
            Boolean x21 = (Boolean) snapshot.child("player21").getValue();
            int x12 = Integer.parseInt(String.valueOf(snapshot.child("player12").getValue()));
            int x22 = Integer.parseInt(String.valueOf(snapshot.child("player22").getValue()));


            if (variable.getOtherPlayer().equals("player2")) {
                parameter.setAtkNumC(x22);
            } else {
                parameter.setAtkNumC(x12);
            }

            if (x11 && x21 && x12 != 0 && x22 != 0) {
                receiveMessage();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };


    public void gameEnd(int selfHP, int comHP) {
        if (selfHP <= 0) {
            binding.initGame.setVisibility(View.VISIBLE);
            binding.initGame.setText("對手太強");
            lockMoveBtn();
        }
        if (comHP <= 0) {
            binding.initGame.setVisibility(View.VISIBLE);
            binding.initGame.setText("獲得勝利！");
            lockMoveBtn();
            getGain(10);
            Log.d("獎勵", String.valueOf(10));

        }
        if (selfHP <= 0 && comHP <= 0) {
            binding.initGame.setVisibility(View.VISIBLE);
            binding.initGame.setText("平手");
            lockMoveBtn();
        }
        if (selfHP > 0 && comHP > 0) {
            binding.initGame.setVisibility(View.INVISIBLE);
//            openBtn();
        }
    }

    private void getGain(final int gain) {
        /**獲得獎勵*/
        DatabaseReference level = FirebaseDatabase.getInstance().getReference("users").child(TSUserId).child("level");
        level.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference level = FirebaseDatabase.getInstance().getReference("users").child(TSUserId).child("level");
                level.setValue(Integer.parseInt(String.valueOf(snapshot.getValue())) + gain);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


//    public void gameEnd() {
//        if (Integer.parseInt(txt_self_hp.getText().toString()) <= 0 || Integer.parseInt(txt_com_hp.getText().toString()) <= 0) {
//            initGame.setVisibility(View.VISIBLE);
//            lockBtn();
//        }
//    }


    private void intent() {
        Intent it = getIntent();
        variable.setRoomKey(it.getStringExtra("roomKey"));
        variable.setIndex(it.getIntExtra("index", 0));

        /**正上方的房間號碼*/
        binding.include.roomNum.setText(variable.getRoomKey().substring(0, 4));

        /**檢視自己是player1 or player2*/
        variable.setPlayer(it.getStringExtra("player"));
        switch (variable.getPlayer()) {
            case "player1":
                variable.setOtherPlayer("player2");
                break;
            case "player2":
                variable.setOtherPlayer("player1");
                break;
        }
    }

    private void insertAtkC() {
        /**填入對手的招式*/
        FirebaseDatabase.getInstance().getReference("rooms").child(variable.getRoomKey()).child(variable.getOtherPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variable.setOtherPlayerName((String) snapshot.child("name").getValue());
                variable.setIndexE(Integer.parseInt(String.valueOf(snapshot.child("Index").getValue())));

                Log.d("TAG", "敵人名稱為 : " + variable.getOtherPlayerName());
                Log.d("TAG", "敵人角色為 : " + tools.roleChange(variable.getIndexE()));

                fullRoom.child(variable.getPlayer()).child("game").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Log.d("TAG", String.valueOf((ArrayList<ArrayList<Integer>>) snapshot.getValue()));
                        binding.includeAtkE.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
                        startActivity.atkDrawForC((ArrayList<ArrayList<Integer>>) snapshot.child("gameAtkR").getValue(), binding.includeAtkE.includeAtk1.line11, binding.includeAtkE.includeAtk1.line12, binding.includeAtkE.includeAtk1.line13, binding.includeAtkE.includeAtk1.line14, binding.includeAtkE.includeAtk1.line15, binding.includeAtkE.includeAtk1.line16, binding.includeAtkE.includeAtk1.line17, binding.includeAtkE.includeAtk1.line18, binding.includeAtkE.includeAtk1.line19
                                , binding.includeAtkE.includeAtk2.line21, binding.includeAtkE.includeAtk2.line22, binding.includeAtkE.includeAtk2.line23, binding.includeAtkE.includeAtk2.line24, binding.includeAtkE.includeAtk2.line25, binding.includeAtkE.includeAtk2.line26, binding.includeAtkE.includeAtk2.line27, binding.includeAtkE.includeAtk2.line28, binding.includeAtkE.includeAtk2.line29
                                , binding.includeAtkE.includeAtk3.line31, binding.includeAtkE.includeAtk3.line32, binding.includeAtkE.includeAtk3.line33, binding.includeAtkE.includeAtk3.line34, binding.includeAtkE.includeAtk3.line35, binding.includeAtkE.includeAtk3.line36, binding.includeAtkE.includeAtk3.line37, binding.includeAtkE.includeAtk3.line38, binding.includeAtkE.includeAtk3.line39
                                , binding.includeAtkE.includeAtk4.line41, binding.includeAtkE.includeAtk4.line42, binding.includeAtkE.includeAtk4.line43, binding.includeAtkE.includeAtk4.line44, binding.includeAtkE.includeAtk4.line45, binding.includeAtkE.includeAtk4.line46, binding.includeAtkE.includeAtk4.line47, binding.includeAtkE.includeAtk4.line48, binding.includeAtkE.includeAtk4.line49);
                        startActivity.atkDrawHPMP((ArrayList<Integer>) snapshot.child("gameHP").getValue(), (ArrayList<Integer>) snapshot.child("gameMP").getValue()
                                , binding.includeAtkE.HP1, binding.includeAtkE.HP2, binding.includeAtkE.HP3, binding.includeAtkE.HP4, binding.includeAtkE.HP5
                                , binding.includeAtkE.MP1, binding.includeAtkE.MP2, binding.includeAtkE.MP3, binding.includeAtkE.MP4, binding.includeAtkE.MP5);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void insertAtkS() {
        /**填入自己的招式**/
        fullRoom.child(variable.getOtherPlayer()).child("game").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variable.setFinalAtlR((ArrayList<ArrayList<Integer>>) snapshot.child("gameAtkR").getValue());
                variable.setFinalHP((ArrayList<Integer>) snapshot.child("gameHP").getValue());
                variable.setFinalMP((ArrayList<Integer>) snapshot.child("gameMP").getValue());
                startActivity.atkDraw((ArrayList<ArrayList<Integer>>) snapshot.child("gameAtkR").getValue(), binding.includeAtk.includeAtk1.line11, binding.includeAtk.includeAtk1.line12, binding.includeAtk.includeAtk1.line13, binding.includeAtk.includeAtk1.line14, binding.includeAtk.includeAtk1.line15, binding.includeAtk.includeAtk1.line16, binding.includeAtk.includeAtk1.line17, binding.includeAtk.includeAtk1.line18, binding.includeAtk.includeAtk1.line19
                        , binding.includeAtk.includeAtk2.line21, binding.includeAtk.includeAtk2.line22, binding.includeAtk.includeAtk2.line23, binding.includeAtk.includeAtk2.line24, binding.includeAtk.includeAtk2.line25, binding.includeAtk.includeAtk2.line26, binding.includeAtk.includeAtk2.line27, binding.includeAtk.includeAtk2.line28, binding.includeAtk.includeAtk2.line29
                        , binding.includeAtk.includeAtk3.line31, binding.includeAtk.includeAtk3.line32, binding.includeAtk.includeAtk3.line33, binding.includeAtk.includeAtk3.line34, binding.includeAtk.includeAtk3.line35, binding.includeAtk.includeAtk3.line36, binding.includeAtk.includeAtk3.line37, binding.includeAtk.includeAtk3.line38, binding.includeAtk.includeAtk3.line39
                        , binding.includeAtk.includeAtk4.line41, binding.includeAtk.includeAtk4.line42, binding.includeAtk.includeAtk4.line43, binding.includeAtk.includeAtk4.line44, binding.includeAtk.includeAtk4.line45, binding.includeAtk.includeAtk4.line46, binding.includeAtk.includeAtk4.line47, binding.includeAtk.includeAtk4.line48, binding.includeAtk.includeAtk4.line49);
                startActivity.atkDrawHPMP((ArrayList<Integer>) snapshot.child("gameHP").getValue(), (ArrayList<Integer>) snapshot.child("gameMP").getValue()
                        , binding.includeAtk.HP1, binding.includeAtk.HP2, binding.includeAtk.HP3, binding.includeAtk.HP4, binding.includeAtk.HP5
                        , binding.includeAtk.MP1, binding.includeAtk.MP2, binding.includeAtk.MP3, binding.includeAtk.MP4, binding.includeAtk.MP5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void atkVisible() {
        includeMove.setVisibility(View.INVISIBLE);
        includeAtk.setVisibility(View.VISIBLE);
        lockMoveBtn();
    }

    public void moveVisible() {
        includeMove.setVisibility(View.VISIBLE);
        includeAtk.setVisibility(View.INVISIBLE);

    }

    public void fireVisible() {
        binding.atkKJ00.setVisibility(View.INVISIBLE);
        binding.atkKJ10.setVisibility(View.INVISIBLE);
        binding.atkKJ20.setVisibility(View.INVISIBLE);
        binding.atkKJ30.setVisibility(View.INVISIBLE);
        binding.atkKJ40.setVisibility(View.INVISIBLE);
        binding.atkKJ01.setVisibility(View.INVISIBLE);
        binding.atkKJ11.setVisibility(View.INVISIBLE);
        binding.atkKJ21.setVisibility(View.INVISIBLE);
        binding.atkKJ31.setVisibility(View.INVISIBLE);
        binding.atkKJ41.setVisibility(View.INVISIBLE);
        binding.atkKJ02.setVisibility(View.INVISIBLE);
        binding.atkKJ12.setVisibility(View.INVISIBLE);
        binding.atkKJ22.setVisibility(View.INVISIBLE);
        binding.atkKJ32.setVisibility(View.INVISIBLE);
        binding.atkKJ42.setVisibility(View.INVISIBLE);
    }

    public void lockMoveBtn() {
        btnOC(false, Color.parseColor("#e0000000"));
    }

    public void openMoveBtn() {
        btnOC(true, Color.parseColor("#00000000"));
    }

    private void btnOC(Boolean x, int color) {
        binding.includeMove.button.setEnabled(x);
        binding.includeMove.button2.setEnabled(x);
        binding.includeMove.button3.setEnabled(x);
        binding.includeMove.button4.setEnabled(x);
        binding.includeMove.button5.buttonTools1.setEnabled(x);
        binding.includeMove.button6.buttonTools2.setEnabled(x);
        binding.includeMove.button.setBackgroundColor(color);
        binding.includeMove.button2.setBackgroundColor(color);
        binding.includeMove.button3.setBackgroundColor(color);
        binding.includeMove.button4.setBackgroundColor(color);
        binding.includeMove.button5.buttonTools1.setBackgroundColor(color);
        binding.includeMove.button6.buttonTools2.setBackgroundColor(color);
    }


//    public void lockBtnAtk() {
//        binding.includeAtk.includeAtk1.buttonAtk1.setEnabled(false);
//        binding.includeAtk.includeAtk2.buttonAtk2.setEnabled(false);
//        binding.includeAtk.includeAtk3.buttonAtk3.setEnabled(false);
//        binding.includeAtk.includeAtk4.buttonAtk4.setEnabled(false);
//        binding.includeAtk.includeAtk5.buttonAtk5.setEnabled(false);
//        binding.includeAtk.includeAtk6.buttonAtk6.setEnabled(false);
//        binding.includeAtk.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeAtk.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeAtk.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeAtk.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeAtk.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeAtk.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#e0000000"));
//    }

    public void openBtnAtk() {
        binding.includeAtk.includeAtk1.buttonAtk1.setEnabled(true);
        binding.includeAtk.includeAtk2.buttonAtk2.setEnabled(true);
        binding.includeAtk.includeAtk3.buttonAtk3.setEnabled(true);
        binding.includeAtk.includeAtk4.buttonAtk4.setEnabled(true);
        binding.includeAtk.includeAtk5.buttonAtk5.setEnabled(true);
        binding.includeAtk.includeAtk6.buttonAtk6.setEnabled(true);
        binding.includeAtk.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtk.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#00000000"));
    }


    public void moveRight(View view) {
        moveRules.moveCommon(1, 0);
    }

    public void moveLeft(View view) {
        moveRules.moveCommon(-1, 0);
    }

    public void moveTop(View view) {
        moveRules.moveCommon(0, 1);
    }

    public void moveDown(View view) {
        moveRules.moveCommon(0, -1);
    }

    public void tool1(View view) {
        sendMessageUp("hp", variable.getUpHPInt());
    }

    public void tool2(View view) {
        sendMessageUp("mp", variable.getUpMPInt());
    }

    public void statusUp(String whichPlayer) {
        if (whichPlayer.equals("player1")) {
            fullRoom.child("fourStatus").child("player11").setValue(true);
        } else if (whichPlayer.equals("player2")) {
            fullRoom.child("fourStatus").child("player21").setValue(true);
        }
    }

    public void statusUp(String whichPlayer, int atkNum) {
        if (whichPlayer.equals("player1")) {
            fullRoom.child("fourStatus").child("player12").setValue(atkNum);
            Log.d("準備階段", "player1 你使用了第 " + atkNum + " 招");
        } else if (whichPlayer.equals("player2")) {
            fullRoom.child("fourStatus").child("player22").setValue(atkNum);
            Log.d("準備階段", "player2 你使用了第 " + atkNum + " 招");
        }

    }

/**
 * 回合結束觸發
 * 1.增加雙發完家的MP以及HP
 * 2.判斷攻擊技能是否可以使用
 * 3.判斷遊戲是否結束
 * 4.把攻擊範圍初始化
 * <p>
 * 退出遊戲
 */


    /**
     * 退出遊戲
     */
    public void initGame(View v) {
        Intent it = new Intent(this, StartActivity.class);
        startActivity(it);
    }


    public void sendMessageMove() {
        /**
         * 1.紀錄"執行"
         * 2.顯示攻擊按鈕
         * 3.紀錄"內容"
         * */
        statusUp(variable.getPlayer());
        atkVisible();

        fullRoom.child(variable.getPlayer()).child("Next").child("locationXSelf").setValue(parameter.getLocationXS());
        fullRoom.child(variable.getPlayer()).child("Next").child("locationYSelf").setValue(parameter.getLocationYS());
//        Toast.makeText(this, "移動", Toast.LENGTH_SHORT).show();
    }

    public void sendMessageUp(String HPMP, int up) {
        statusUp(variable.getPlayer());
        atkVisible();
        switch (HPMP) {
            case "hp":
                fullRoom.child(variable.getPlayer()).child("Next").child("HPUP").setValue(up);
                break;
            case "mp":
                fullRoom.child(variable.getPlayer()).child("Next").child("MPUP").setValue(up);
                break;
        }
//        Toast.makeText(this, "回復", Toast.LENGTH_SHORT).show();
    }


    public void sendMessageMoveAtk(int atkNum) {
        /**
         * 1.紀錄"內容"
         * 2.紀錄"執行"
         * */
        parameter.setAtkNumS(atkNum);
        moveVisible();
        lockMoveBtn();
        ArrayList yc = new ArrayList();
        switch (atkNum) {
            case STAND:
                /**把傷害跟消耗歸零*/
                yc.add(0);
                fullRoom.child(variable.getPlayer()).child("Next").child("atkR").setValue(yc);
                fullRoom.child(variable.getPlayer()).child("Next").child("atkHP").setValue(0);
                fullRoom.child(variable.getPlayer()).child("Next").child("atkMP").setValue(0);
                break;
            case UNIQUE:
                fullRoom.child(variable.getPlayer()).child("unique").setValue(true); //特殊技能啟動
//                fullRoom.child(variable.getPlayer()).child("Next").child("atkR").setValue(yc);
                fullRoom.child(variable.getPlayer()).child("Next").child("atkHP").setValue(Integer.parseInt(String.valueOf(variable.getFinalHP().get(UNIQUE - 1))));
                fullRoom.child(variable.getPlayer()).child("Next").child("atkMP").setValue(Integer.parseInt(String.valueOf(variable.getFinalMP().get(UNIQUE - 1))));
                yc.add(0);
                fullRoom.child(variable.getPlayer()).child("Next").child("atkR").setValue(yc);
                variable.setUnique(true);
                break;
            default:
                fullRoom.child(variable.getPlayer()).child("Next").child("atkR").setValue(variable.getFinalAtlR().get(atkNum - 1));
                fullRoom.child(variable.getPlayer()).child("Next").child("atkHP").setValue(Integer.parseInt(String.valueOf(variable.getFinalHP().get(atkNum - 1))));
                fullRoom.child(variable.getPlayer()).child("Next").child("atkMP").setValue(Integer.parseInt(String.valueOf(variable.getFinalMP().get(atkNum - 1))));
                break;
        }
        statusUp(variable.getPlayer(), atkNum);
    }


    public void atk1(View v) {
        /**
         * 1.把攻擊按鈕鎖起來
         * 2.顯示移動按鈕(?)
         * 3.
         * */

        sendMessageMoveAtk(1);

    }

    public void atk2(View v) {
        sendMessageMoveAtk(2);
    }

    public void atk3(View v) {
        sendMessageMoveAtk(3);
    }

    public void atk4(View v) {
        sendMessageMoveAtk(4);
    }

    public void atk5(View v) {//變成特殊技能
        sendMessageMoveAtk(UNIQUE);
    }

    public void atk6(View v) {
        sendMessageMoveAtk(STAND);
    }

    int turn = 4;

    public void comUseInit() {
        binding.includeAtkE.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtkE.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtkE.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtkE.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtkE.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeAtkE.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#00000000"));
    }

    public void comUse(int atkNum) {
        switch (atkNum) {
            case 1:
                binding.includeAtkE.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
            case 2:
                binding.includeAtkE.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
            case 3:
                binding.includeAtkE.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
            case 4:
                binding.includeAtkE.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
            case 5:
                binding.includeAtkE.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
            case 6:
                binding.includeAtkE.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#e0000000"));
                break;
        }
    }


    /**
     * 執行動畫的部分
     */
    private void receiveMessage() {
        /**
         * 1.初始化地板
         * 2.
         * */

        fireVisible();//收到後 地板火焰先取消
        comUseInit();
        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long atkNum1 = (long) snapshot.child("fourStatus").child("player12").getValue();
                long atkNum2 = (long) snapshot.child("fourStatus").child("player22").getValue();
                if (variable.getPlayer().equals("player1")) {
                    Log.d("準備階段", "你是玩家一");
                    Log.d("準備階段", "玩家一你使用了第 " + atkNum1 + " 招");
                    Log.d("準備階段", "玩家二他使用了第 " + atkNum2 + " 招");
                    comUse(Integer.parseInt(String.valueOf(atkNum2)));
                    if (atkNum1 == 5) {
                        variable.setUnique(true);
                        Log.d("準備階段", "你使用獨有技能");
                    }
                    if (atkNum2 == 5) {
                        variable.setUniqueC(true);
                        Log.d("準備階段", "對手使用獨有技能");
                    }
                } else {
                    Log.d("準備階段", "你是玩家二");
                    Log.d("準備階段", "玩家二你使用了第 " + atkNum2 + " 招");
                    Log.d("準備階段", "玩家一他使用了第 " + atkNum1 + " 招");
                    comUse(Integer.parseInt(String.valueOf(atkNum1)));
                    if (atkNum1 == 5) {
                        variable.setUniqueC(true);
                        Log.d("準備階段", "對手使用獨有技能");
                    }
                    if (atkNum2 == 5) {
                        variable.setUnique(true);
                        Log.d("準備階段", "你使用獨有技能");
                    }
                }

                int x1 = Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("X").getValue()));
                int y1 = Integer.parseInt(String.valueOf(snapshot.child(variable.getPlayer()).child("Y").getValue()));
                int x2 = Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("X").getValue()));
                int y2 = Integer.parseInt(String.valueOf(snapshot.child(variable.getOtherPlayer()).child("Y").getValue()));
                switch (x2) {
                    case 0:
                        x2 = 4;
                        break;
                    case 1:
                        x2 = 3;
                        break;
                    case 2:
                        x2 = 2;
                        break;
                    case 3:
                        x2 = 1;
                        break;
                    case 4:
                        x2 = 0;
                        break;
                }
                variable.setPlayerUX(x1);
                variable.setPlayerUY(y1);
                variable.setPlayerUXB(x2);
                variable.setPlayerUYB(y2);

                Log.d("TAG", variable.getPlayerUX() + " , " + variable.getPlayerUY());
                Log.d("TAG", variable.getPlayerUXB() + " , " + variable.getPlayerUYB());

                for (int i = 0; i < turn; i++) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("NUM", i);
                    msg.setData(bundle);
                    msg.what = 0;
                    switch (i) {
                        case 0:
                            handler.sendMessageDelayed(msg, 1000);
                            break;
                        case 2:
                            handler.sendMessageDelayed(msg, 3000);
                            break;
                        case 3:
                            handler.sendMessageDelayed(msg, 3150);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int start = msg.getData().getInt("NUM");

            Log.d("戰鬥階段", String.valueOf(start));

            switch (start) {
                case 0:
                    fullRoom.child(variable.getPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            /**提取自己應該要在的位置 - Next*/
                            long xl = (long) snapshot.child("Next").child("locationXSelf").getValue();
                            long yl = (long) snapshot.child("Next").child("locationYSelf").getValue();
                            /**變成自己真正的位置 - XY*/
                            fullRoom.child(variable.getPlayer()).child("X").setValue((int) xl);
                            fullRoom.child(variable.getPlayer()).child("Y").setValue((int) yl);

                            Log.d("第一戰鬥階段", "自己移動到" + xl + " , " + yl);

                            /**提取自己應該要增加的血量跟魔量 - Next*/
                            long HPUP = (long) snapshot.child("Next").child("HPUP").getValue();
                            long MPUP = (long) snapshot.child("Next").child("MPUP").getValue();
                            /**提取自己的血量跟魔量*/
                            long HP = (long) snapshot.child("HP").getValue();
                            long MP = (long) snapshot.child("MP").getValue();
                            /**如果有需要回血或回魔力的話 就會在這邊增加*/
                            if ((int) HPUP > 0) {
                                Log.d("第一戰鬥階段", "回復血量 : " + HPUP);
                                fullRoom.child(variable.getPlayer()).child("HP").setValue((int) HPUP + (int) HP);
                            }
                            if ((int) MPUP > 0) {
                                Log.d("第一戰鬥階段", "回復魔力 : " + MPUP);
                                fullRoom.child(variable.getPlayer()).child("MP").setValue((int) MPUP + (int) MP);
                            }
                            /**回血量跟魔量後 將Next歸零*/
                            fullRoom.child(variable.getPlayer()).child("Next").child("HPUP").setValue(0);
                            fullRoom.child(variable.getPlayer()).child("Next").child("MPUP").setValue(0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    /** 如果你是騎士且你有開啟獨有技能 對手的Next - atkHP 直接歸零*/
                    if (tools.roleChange((int) variable.getIndex()).equals("b74") && variable.getUnique()) {
                        Log.d("獨有技能", "騎士發動");
                        /**傷害歸0*/
                        fullRoom.child(variable.getOtherPlayer()).child("Next").child("atkHP").setValue(0);
                        /**消耗獨有技能*/
                        variable.setUnique(false);
                        fullRoom.child(variable.getPlayer()).child("unique").setValue(false);
                    }

                    break;
                case 2:
                    fullRoom.child(variable.getPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            int HP = Integer.parseInt(String.valueOf(snapshot.child("Next").child("atkHP").getValue()));
                            int MP = Integer.parseInt(String.valueOf(snapshot.child("Next").child("atkMP").getValue()));
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();
                            if (tools.roleChange(variable.getIndexE()).equals("j4")) {
                                Log.d("劍士獨有技能", String.valueOf(variable.getUnique()));
                                Log.d("劍士獨有技能", String.valueOf(HP));
                            }

                            if (tools.roleChange(variable.getIndex()).equals("j4") && variable.getUnique() && HP != 0) {
                                Log.d("獨有技能", "自己劍士發動");
                                HP = HP + 2;
                                fullRoom.child(variable.getPlayer()).child("Next").child("atkHP").setValue(HP);
                                variable.setUnique(false);
                            }

                            int[][] atkRealRange = atkRules.atkRealRange(parameter.getAtkNumS(), atkR, "self");
                            parameter.setAtkRangeSelf(atkRealRange);
                            for (int i = 0; i < atkRealRange.length; i++) {
                                Log.d("自己的攻擊範圍", atkRealRange[i][0] + " , " + atkRealRange[i][1]);
                            }
                            atkRules.atkJudgmentSelf(atkRealRange, (int) HP, (int) MP);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    break;
                case 3:
                    fullRoom.child(variable.getOtherPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int HP = Integer.parseInt(String.valueOf(snapshot.child("Next").child("atkHP").getValue()));
                            int MP = Integer.parseInt(String.valueOf(snapshot.child("Next").child("atkMP").getValue()));
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();

                            Log.d("atkR", String.valueOf(atkR));

                            if (tools.roleChange(variable.getIndexE()).equals("j4")) {
                                Log.d("劍士獨有技能", String.valueOf(variable.getUniqueC()));
                                Log.d("劍士獨有技能", String.valueOf(HP));
                            }

                            int[][] atkRealRange = atkRules.atkRealRange(parameter.getAtkNumC(), atkRules.atk9o(atkR), "com");
                            for (int i = 0; i < atkRealRange.length; i++) {
                                Log.d("對手的攻擊範圍", atkRealRange[i][0] + " , " + atkRealRange[i][1]);
                            }

                            parameter.setAtkRangeCom(atkRealRange);


                            if (tools.roleChange(variable.getIndexE()).equals("j4") && variable.getUniqueC() && HP != 0) {
                                Log.d("獨有技能", "對手劍士發動");
                                HP = HP + 2;
                                fullRoom.child(variable.getOtherPlayer()).child("Next").child("atkHP").setValue(HP);
                                variable.setUnique(false);
                            }

                            atkRules.atkJudgmentCom(atkRealRange, (int) HP, (int) MP);


                            /**初始化*/
                            fullRoom.child("fourStatus").child("player11").setValue(false);
                            fullRoom.child("fourStatus").child("player21").setValue(false);
                            fullRoom.child("fourStatus").child("player12").setValue(0);
                            fullRoom.child("fourStatus").child("player22").setValue(0);
                            MPUse();
                            openMoveBtn();

                            if (tools.roleChange((int) variable.getIndex()).equals("b74") || tools.roleChange((int) variable.getIndex()).equals("fs")) {
                                fullRoom.child(variable.getPlayer()).child("unique").setValue(false);
                                variable.setUnique(false);
                                fullRoom.child(variable.getOtherPlayer()).child("unique").setValue(false);
                                variable.setUniqueC(false);
                            }
                            Log.d("戰鬥階段", "結束");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                    break;
            }
        }

    }

//    public boolean fsUnique = false;
//
//    public void fsUnique() {
//        fullRoom.child(player).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Boolean aBoolean = (Boolean) snapshot.child("unique").getValue();
//                long Index = (long) snapshot.child("Index").getValue();
//                if (aBoolean && tools.roleChange((int)Index).equals("fs")) {
//                    fsUnique = true;
//                    fullRoom.child(player).child("unique").setValue(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    public void MPUse() {
        fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(variable.getRoomKey());//FirebaseDatabase

        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long MP1 = (long) snapshot.child("player1").child("MP").getValue();
                long MPUse1 = (long) snapshot.child("player1").child("Next").child("atkMP").getValue();

                long MP2 = (long) snapshot.child("player2").child("MP").getValue();
                long MPUse2 = (long) snapshot.child("player2").child("Next").child("atkMP").getValue();

                fullRoom.child("player1").child("MP").setValue((int) MP1 - (int) MPUse1);
                fullRoom.child("player2").child("MP").setValue((int) MP2 - (int) MPUse2);
                fullRoom.child("player1").child("Next").child("atkMP").setValue(0);
                fullRoom.child("player2").child("Next").child("atkMP").setValue(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}