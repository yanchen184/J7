package com.example.j7;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.j7.databinding.GameActivityMainBinding;
import com.example.j7.game.AtkDecide;
import com.example.j7.game.AtkRules;
import com.example.j7.game.ConnectManager;
import com.example.j7.game.MoveRules;
import com.example.j7.game.UpRules;
import com.example.j7.tools.Tools;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.example.j7.LoginActivity.TSUserId;
import static com.example.j7.LoginActivity.userId;

public class GameActivity extends AppCompatActivity {
    /**0626讓冠宇看懂的連線版本 */
    /**
     * 0627 :
     * 1.增加按鈕可以重新連線 initConnect
     * 0704 :
     * 1.分類
     */
    private TextView txt_self_name, txt_com_name;
    private Button btnInitStart, btnInitConnect;
    public ImageView imagePlayer, imageCom;
    private EditText roomEditText;
    public int step;
    //    ConnectManager connectManager = new ConnectManager(this);
    public MoveRules moveRules = new MoveRules(this);
    public AtkRules atkRules = new AtkRules(this);
    public UpRules upRules = new UpRules(this);
    public AtkDecide atkDecide = new AtkDecide();
    Tools tools = new Tools();
    public StartActivity startActivity = new StartActivity();


    public int locationXSelf = 0;
    public int locationYSelf = 1;
    public int locationXCom = 4;
    public int locationYCom = 1;
    private View lineX0, lineX1, lineX2, lineX3, lineX4, lineY0, lineY1, lineY2, lineY3, lineY4;
    public View atkKJ00, atkKJ10, atkKJ20, atkKJ30, atkKJ40;
    public View atkKJ01, atkKJ11, atkKJ21, atkKJ31, atkKJ41;
    public View atkKJ02, atkKJ12, atkKJ22, atkKJ32, atkKJ42;
    public Button initGame;
    public TextView txt_self_hp, txt_self_mp, txt_com_hp, txt_com_mp;
    public View button, button2, button3, button4, button5, button6;
    public Button buttonAtk1, buttonAtk2, buttonAtk3, buttonAtk4, buttonAtk5, buttonAtk6;
    public Button buttonTools1, buttonTools2;

    public View[] locationX;
    public View[] locationY;
    public View includeAtk, includeMove;
    public View includeAtk1, includeAtk2, includeAtk3, includeAtk4, includeAtk5, includeAtk6;
    public View line11, line12, line13, line14, line15, line16, line17, line18, line19;
    public View line21, line22, line23, line24, line25, line26, line27, line28, line29;
    public View line31, line32, line33, line34, line35, line36, line37, line38, line39;
    public View line41, line42, line43, line44, line45, line46, line47, line48, line49;
    public View line51, line52, line53, line54, line55, line56, line57, line58, line59;
    public TextView HP1, HP2, HP3, HP4, HP5, HP6;
    public TextView MP1, MP2, MP3, MP4, MP5, MP6;
    public String roomKey;
    String player;
    String otherPlayer;

    String playerName;
    String otherPlayerName;
    int indexE;
    public String userName = "";
    public DatabaseReference fullRoom;
    int upHPInt;
    int upMPInt;


    public GameActivityMainBinding binding;
    Variable variable = new Variable();
    Parameter parameter = new Parameter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("---------遊戲開始---------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.game_activity_main);

        findView(); //findView()
        intent(); //從上一頁告知我 roomKey player otherPlayer

        fullRoom = FirebaseDatabase.getInstance().getReference("rooms").child(variable.getRoomKey());//FirebaseDatabase

        locationX = new View[]{binding.lineX0, binding.lineX1, binding.lineX2, binding.lineX3, binding.lineX4};
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


                //換圖
                imagePlayer.setImageResource(tools.roleChangePicture(variable.getRoleS()));
                imageCom.setImageResource(tools.roleChangePicture(variable.getRoleC()));
                //對手要翻轉
                imageCom.setScaleX(-1);

                //告訴我兩個人的名字
                binding.include.txtSelfName.setText(variable.getPlayerName());
                binding.include.txtComName.setText(variable.getOtherPlayerName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //回血回魔 法師跟騎士有稍微調整
        if (tools.roleChange(variable.getRoleS()).equals("b74")) {
            binding.includeMove.button5.upHP.setText(parameter.upHPB74 + "");
            variable.setUpHPInt(parameter.upHPB74);
        } else {
            binding.includeMove.button5.upHP.setText(parameter.upHPOther + "");
            variable.setUpHPInt(parameter.upHPOther);
        }

        if (tools.roleChange(variable.getRoleS()).equals("fs")) {
            binding.includeMove.button5.upHP.setText(parameter.upMPFs + "");
            variable.setUpHPInt(parameter.upMPFs);
        } else {
            binding.includeMove.button5.upHP.setText(parameter.upMPOther + "");
            variable.setUpHPInt(parameter.upMPOther);
        }

        /**上方的按鈕*/
        binding.includeAtkE.includeAtk1.buttonAtk1.setEnabled(false);
        binding.includeAtkE.includeAtk2.buttonAtk2.setEnabled(false);
        binding.includeAtkE.includeAtk3.buttonAtk3.setEnabled(false);
        binding.includeAtkE.includeAtk4.buttonAtk4.setEnabled(false);
        binding.includeAtkE.includeAtk5.buttonAtk5.setEnabled(false);
        binding.includeAtkE.includeAtk6.buttonAtk6.setEnabled(false);

        //繪製圖片
//        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
//        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());
    }


    public void MPUse() {
        fullRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long MP1 = (long) snapshot.child("player1").child("MP").getValue();
                long MPUse1 = (long) snapshot.child("player1").child("Next").child("atkMP").getValue();

                long MP2 = (long) snapshot.child("player2").child("MP").getValue();
                long MPUse2 = (long) snapshot.child("player2").child("Next").child("atkMP").getValue();

//                System.out.println("player1" + " 魔力 " + MP1 + " - " + MPUse1 + " = " + ((int) MP1 - (int) MPUse1));
//                System.out.println("player2" + " 魔力 " + MP2 + " - " + MPUse2 + " = " + ((int) MP2 - (int) MPUse2));
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


    private ValueEventListener upListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;


            binding.include.txtSelfHp.setText(snapshot.child(variable.getPlayer()).child("HP").getValue().toString());
            binding.include.txtSelfMp.setText(snapshot.child(variable.getPlayer()).child("MP").getValue().toString());
            binding.include.txtSelfHp.setText(snapshot.child(variable.getOtherPlayer()).child("HP").getValue().toString());
            binding.include.txtSelfHp.setText(snapshot.child(variable.getOtherPlayer()).child("MP").getValue().toString());


            long x1 = (long) snapshot.child(variable.getPlayer()).child("X").getValue();
            long y1 = (long) snapshot.child(variable.getPlayer()).child("Y").getValue();
            long x2 = (long) snapshot.child(variable.getOtherPlayer()).child("X").getValue();
            long y2 = (long) snapshot.child(variable.getOtherPlayer()).child("Y").getValue();

            moveRules.moveJudgmentSelf((int) x1, (int) y1);
            moveRules.moveJudgmentCom((int) x2, (int) y2);


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

            if (x11 && x21 && x12 != 0 && x22 != 0) {
                fullRoom.child("fourStatus").child("player11").setValue(false);
                fullRoom.child("fourStatus").child("player21").setValue(false);
                fullRoom.child("fourStatus").child("player12").setValue(0);
                fullRoom.child("fourStatus").child("player22").setValue(0);
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
            lockBtn();
        }
        if (comHP <= 0) {
            binding.initGame.setVisibility(View.VISIBLE);
            binding.initGame.setText("獲得勝利！");
            lockBtn();
            getGain(10);

        }
        if (selfHP <= 0 && comHP <= 0) {
            binding.initGame.setVisibility(View.VISIBLE);
            binding.initGame.setText("平手");
            lockBtn();
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
                level.setValue((int) snapshot.getValue() + gain);
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

    @Override
    protected void onStart() {
        super.onStart();

        binding.includeAtk.includeAtk6.buttonAtk6.setText("站著");

        openBtnAtk();

        switch (tools.roleChange(variable.getIndex())) {
            case "j4":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("續力");
                break;
            case "fs":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("末日");
                break;
            case "player":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("尚未開發");
                break;
            case "b74":
                binding.includeAtk.includeAtk5.buttonAtk5.setText("隔檔");
                break;
        }

        fullRoom.addValueEventListener(upListener);//監聽
        fullRoom.child("fourStatus").addValueEventListener(statusListener);//監聽
    }

    int index;

    private void intent() {
        Intent it = getIntent();
        variable.setRoomKey(it.getStringExtra("roomKey"));
        variable.setIndex(it.getIntExtra("index", 0));

        /**正上方的房間號碼*/
        TextView roomNum = findViewById(R.id.roomNum);//TODO Binding
        roomNum.setText(variable.getRoomKey());

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
        insertAtkS();
        insertAtkC();
    }

    private void insertAtkC() {
        /**填入對手的招式*/ //TODO 改成相反的形狀
        FirebaseDatabase.getInstance().getReference("rooms").child(variable.getRoomKey()).child(variable.getOtherPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variable.setOtherPlayerName((String) snapshot.child("name").getValue());
                variable.setIndexE(Integer.parseInt(String.valueOf(snapshot.child("Index").getValue())));

                Log.d("TAG", "敵人名稱為 : " + otherPlayerName);
                Log.d("TAG", "敵人角色為 : " + tools.roleChange(indexE));

                FirebaseDatabase.getInstance().getReference("users").child(variable.getOtherPlayerName()).child("role").child(tools.roleChange(variable.getIndexE())).child("atkR").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", String.valueOf((ArrayList<ArrayList<Integer>>) snapshot.getValue()));
                        binding.includeAtkE.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#00000000"));
                        binding.includeAtkE.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#00000000"));
                        startActivity.atkDraw((ArrayList<ArrayList<Integer>>) snapshot.getValue(), binding.includeAtkE.includeAtk1.line11, binding.includeAtkE.includeAtk1.line12, binding.includeAtkE.includeAtk1.line13, binding.includeAtkE.includeAtk1.line14, binding.includeAtkE.includeAtk1.line15, binding.includeAtkE.includeAtk1.line16, binding.includeAtkE.includeAtk1.line17, binding.includeAtkE.includeAtk1.line18, binding.includeAtkE.includeAtk1.line19
                                , binding.includeAtkE.includeAtk2.line21, binding.includeAtkE.includeAtk2.line22, binding.includeAtkE.includeAtk2.line23, binding.includeAtkE.includeAtk2.line24, binding.includeAtkE.includeAtk2.line25, binding.includeAtkE.includeAtk2.line26, binding.includeAtkE.includeAtk2.line27, binding.includeAtkE.includeAtk2.line28, binding.includeAtkE.includeAtk2.line29
                                , binding.includeAtkE.includeAtk3.line31, binding.includeAtkE.includeAtk3.line32, binding.includeAtkE.includeAtk3.line33, binding.includeAtkE.includeAtk3.line34, binding.includeAtkE.includeAtk3.line35, binding.includeAtkE.includeAtk3.line36, binding.includeAtkE.includeAtk3.line37, binding.includeAtkE.includeAtk3.line38, binding.includeAtkE.includeAtk3.line39
                                , binding.includeAtkE.includeAtk4.line41, binding.includeAtkE.includeAtk4.line42, binding.includeAtkE.includeAtk4.line43, binding.includeAtkE.includeAtk4.line44, binding.includeAtkE.includeAtk4.line45, binding.includeAtkE.includeAtk4.line46, binding.includeAtkE.includeAtk4.line47, binding.includeAtkE.includeAtk4.line48, binding.includeAtkE.includeAtk4.line49);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                FirebaseDatabase.getInstance().getReference("users").child(variable.getOtherPlayerName()).child("role").child(tools.roleChange(variable.getIndexE())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", String.valueOf(snapshot.child("HP").getValue()));
                        startActivity.atkDrawHPMP((ArrayList<Integer>) snapshot.child("HP").getValue(), (ArrayList<Integer>) snapshot.child("MP").getValue()
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
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(variable.getIndex())).child("atkR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variable.setFinalAtlR((ArrayList<ArrayList<Integer>>) snapshot.getValue());

                startActivity.atkDraw((ArrayList<ArrayList<Integer>>) snapshot.getValue(), binding.includeAtk.includeAtk1.line11, binding.includeAtk.includeAtk1.line12, binding.includeAtk.includeAtk1.line13, binding.includeAtk.includeAtk1.line14, binding.includeAtk.includeAtk1.line15, binding.includeAtk.includeAtk1.line16, binding.includeAtk.includeAtk1.line17, binding.includeAtk.includeAtk1.line18, binding.includeAtk.includeAtk1.line19
                        , binding.includeAtk.includeAtk2.line21, binding.includeAtk.includeAtk2.line22, binding.includeAtk.includeAtk2.line23, binding.includeAtk.includeAtk2.line24, binding.includeAtk.includeAtk2.line25, binding.includeAtk.includeAtk2.line26, binding.includeAtk.includeAtk2.line27, binding.includeAtk.includeAtk2.line28, binding.includeAtk.includeAtk2.line29
                        , binding.includeAtk.includeAtk3.line31, binding.includeAtk.includeAtk3.line32, binding.includeAtk.includeAtk3.line33, binding.includeAtk.includeAtk3.line34, binding.includeAtk.includeAtk3.line35, binding.includeAtk.includeAtk3.line36, binding.includeAtk.includeAtk3.line37, binding.includeAtk.includeAtk3.line38, binding.includeAtk.includeAtk3.line39
                        , binding.includeAtk.includeAtk4.line41, binding.includeAtk.includeAtk4.line42, binding.includeAtk.includeAtk4.line43, binding.includeAtk.includeAtk4.line44, binding.includeAtk.includeAtk4.line45, binding.includeAtk.includeAtk4.line46, binding.includeAtk.includeAtk4.line47, binding.includeAtk.includeAtk4.line48, binding.includeAtk.includeAtk4.line49);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("role").child(tools.roleChange(index)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                variable.setFinalHP((ArrayList<Integer>) snapshot.child("HP").getValue());
                variable.setFinalMP((ArrayList<Integer>) snapshot.child("MP").getValue());

                startActivity.atkDrawHPMP((ArrayList<Integer>) snapshot.child("HP").getValue(), (ArrayList<Integer>) snapshot.child("MP").getValue()
                        , binding.includeAtk.HP1, binding.includeAtk.HP2, binding.includeAtk.HP3, binding.includeAtk.HP4, binding.includeAtk.HP5
                        , binding.includeAtk.MP1, binding.includeAtk.MP2, binding.includeAtk.MP3, binding.includeAtk.MP4, binding.includeAtk.MP5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        /**填入完畢**/
    }

    private void findView() {

        roomEditText = findViewById(R.id.roomEditText);
        imageCom = findViewById(R.id.image_com);
        imagePlayer = findViewById(R.id.image_player);


        buttonTools1 = findViewById(R.id.buttonTools1);
        buttonTools2 = findViewById(R.id.buttonTools2);


        lineX0 = findViewById(R.id.lineX0);
        lineX1 = findViewById(R.id.lineX1);
        lineX2 = findViewById(R.id.lineX2);
        lineX3 = findViewById(R.id.lineX3);
        lineX4 = findViewById(R.id.lineX4);
        lineY0 = findViewById(R.id.lineY0);
        lineY1 = findViewById(R.id.lineY1);
        lineY2 = findViewById(R.id.lineY2);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        buttonAtk1 = findViewById(R.id.buttonAtk1);
        buttonAtk2 = findViewById(R.id.buttonAtk2);
        buttonAtk3 = findViewById(R.id.buttonAtk3);
        buttonAtk4 = findViewById(R.id.buttonAtk4);
        buttonAtk5 = findViewById(R.id.buttonAtk5);
        buttonAtk6 = findViewById(R.id.buttonAtk6);

        txt_self_name = findViewById(R.id.txt_self_name);
        txt_com_name = findViewById(R.id.txt_com_name);
        txt_self_hp = findViewById(R.id.txt_self_hp);
        txt_self_mp = findViewById(R.id.txt_self_mp);
        txt_com_hp = findViewById(R.id.txt_com_hp);
        txt_com_mp = findViewById(R.id.txt_com_mp);
        atkKJ00 = findViewById(R.id.atkKJ00);
        atkKJ10 = findViewById(R.id.atkKJ10);
        atkKJ20 = findViewById(R.id.atkKJ20);
        atkKJ30 = findViewById(R.id.atkKJ30);
        atkKJ40 = findViewById(R.id.atkKJ40);
        atkKJ01 = findViewById(R.id.atkKJ01);
        atkKJ11 = findViewById(R.id.atkKJ11);
        atkKJ21 = findViewById(R.id.atkKJ21);
        atkKJ31 = findViewById(R.id.atkKJ31);
        atkKJ41 = findViewById(R.id.atkKJ41);
        atkKJ02 = findViewById(R.id.atkKJ02);
        atkKJ12 = findViewById(R.id.atkKJ12);
        atkKJ22 = findViewById(R.id.atkKJ22);
        atkKJ32 = findViewById(R.id.atkKJ32);
        atkKJ42 = findViewById(R.id.atkKJ42);
        initGame = findViewById(R.id.initGame);
        initGame.setVisibility(View.INVISIBLE);

        fireVisible();//創建遊戲

        includeMove = findViewById(R.id.includeMove);
        includeAtk = findViewById(R.id.includeAtk);
        includeAtk.setVisibility(View.INVISIBLE);


        includeAtk1 = findViewById(R.id.includeAtk1);
        includeAtk2 = findViewById(R.id.includeAtk2);
        includeAtk3 = findViewById(R.id.includeAtk3);
        includeAtk4 = findViewById(R.id.includeAtk4);
        includeAtk5 = findViewById(R.id.includeAtk5);
        includeAtk6 = findViewById(R.id.includeAtk6);

        line11 = findViewById(R.id.line11);
        line12 = findViewById(R.id.line12);
        line13 = findViewById(R.id.line13);
        line14 = findViewById(R.id.line14);
        line15 = findViewById(R.id.line15);
        line16 = findViewById(R.id.line16);
        line17 = findViewById(R.id.line17);
        line18 = findViewById(R.id.line18);
        line19 = findViewById(R.id.line19);

        line21 = findViewById(R.id.line21);
        line22 = findViewById(R.id.line22);
        line23 = findViewById(R.id.line23);
        line24 = findViewById(R.id.line24);
        line25 = findViewById(R.id.line25);
        line26 = findViewById(R.id.line26);
        line27 = findViewById(R.id.line27);
        line28 = findViewById(R.id.line28);
        line29 = findViewById(R.id.line29);

        line31 = findViewById(R.id.line31);
        line32 = findViewById(R.id.line32);
        line33 = findViewById(R.id.line33);
        line34 = findViewById(R.id.line34);
        line35 = findViewById(R.id.line35);
        line36 = findViewById(R.id.line36);
        line37 = findViewById(R.id.line37);
        line38 = findViewById(R.id.line38);
        line39 = findViewById(R.id.line39);

        line41 = findViewById(R.id.line41);
        line42 = findViewById(R.id.line42);
        line43 = findViewById(R.id.line43);
        line44 = findViewById(R.id.line44);
        line45 = findViewById(R.id.line45);
        line46 = findViewById(R.id.line46);
        line47 = findViewById(R.id.line47);
        line48 = findViewById(R.id.line48);
        line49 = findViewById(R.id.line49);

//        line51 = findViewById(R.id.line51);
//        line52 = findViewById(R.id.line52);
//        line53 = findViewById(R.id.line53);
//        line54 = findViewById(R.id.line54);
//        line55 = findViewById(R.id.line55);
//        line56 = findViewById(R.id.line56);
//        line57 = findViewById(R.id.line57);
//        line58 = findViewById(R.id.line58);
//        line59 = findViewById(R.id.line59);

        HP1 = findViewById(R.id.HP1);
        HP2 = findViewById(R.id.HP2);
        HP3 = findViewById(R.id.HP3);
        HP4 = findViewById(R.id.HP4);
        HP5 = findViewById(R.id.HP5);
        HP6 = findViewById(R.id.HP6);
        MP1 = findViewById(R.id.MP1);
        MP2 = findViewById(R.id.MP2);
        MP3 = findViewById(R.id.MP3);
        MP4 = findViewById(R.id.MP4);
        MP5 = findViewById(R.id.MP5);
        MP6 = findViewById(R.id.MP6);

    }


    public void atkVisible() {
        includeMove.setVisibility(View.INVISIBLE);
        includeAtk.setVisibility(View.VISIBLE);
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

    public void lockBtn() {
        includeMove.setEnabled(false);


//        button.setEnabled(false);
//        button2.setEnabled(false);
//        button3.setEnabled(false);
//        button4.setEnabled(false);
//        button5.setEnabled(false);
//        button6.setEnabled(false);
//        buttonTools1.setEnabled(false);
//        buttonTools2.setEnabled(false);
        binding.includeMove.button.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeMove.button2.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeMove.button3.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeMove.button4.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeMove.button5.setBackgroundColor(Color.parseColor("#e0000000"));
//        binding.includeMove.button6.setBackgroundColor(Color.parseColor("#e0000000"));
    }

    public void openBtn() {
        includeMove.setEnabled(true);


//        binding.includeMove.button.setEnabled(true);
//        binding.includeMove.button2.setEnabled(true);
//        binding.includeMove.button3.setEnabled(true);
//        binding.includeMove.button4.setEnabled(true);
//        binding.includeMove.button5.setEnabled(true);
//        binding.includeMove.button6.setEnabled(true);
//        binding.includeMove.buttonTools1.setEnabled(true);
//        binding.includeMove.buttonTools2.setEnabled(true);
        binding.includeMove.button.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeMove.button2.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeMove.button3.setBackgroundColor(Color.parseColor("#00000000"));
        binding.includeMove.button4.setBackgroundColor(Color.parseColor("#00000000"));
//        binding.includeMove.button5.setBackgroundColor(Color.parseColor("#00000000"));
//        binding.includeMove.button6.setBackgroundColor(Color.parseColor("#00000000"));
    }

    public void lockBtnAtk() {
        binding.includeAtk.includeAtk1.buttonAtk1.setEnabled(false);
        binding.includeAtk.includeAtk2.buttonAtk2.setEnabled(false);
        binding.includeAtk.includeAtk3.buttonAtk3.setEnabled(false);
        binding.includeAtk.includeAtk4.buttonAtk4.setEnabled(false);
        binding.includeAtk.includeAtk5.buttonAtk5.setEnabled(false);
        binding.includeAtk.includeAtk6.buttonAtk6.setEnabled(false);
        binding.includeAtk.includeAtk1.buttonAtk1.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeAtk.includeAtk2.buttonAtk2.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeAtk.includeAtk3.buttonAtk3.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeAtk.includeAtk4.buttonAtk4.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeAtk.includeAtk5.buttonAtk5.setBackgroundColor(Color.parseColor("#e0000000"));
        binding.includeAtk.includeAtk6.buttonAtk6.setBackgroundColor(Color.parseColor("#e0000000"));

    }

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

//    public void controlMPHP(TextView PP, int add) {
//        int MpBefore = Integer.parseInt(PP.getText().toString());
//        int MpAfter = MpBefore + add;
//        PP.setText(MpAfter + "");
//        if (MpAfter >= 10) {
//            PP.setText(10 + "");
//        }
//    }


    public void statusUp(String whichPlayer) {
        if (whichPlayer.equals("player1")) {
            fullRoom.child("fourStatus").child("player11").setValue(true);
        } else if (whichPlayer.equals("player2")) {
            fullRoom.child("fourStatus").child("player21").setValue(true);
        }

    }

    public void statusUp(String whichPlayer, int atkNum) {
        if (whichPlayer.equals("player2")) {
            fullRoom.child("fourStatus").child("player12").setValue(atkNum);
        } else if (whichPlayer.equals("player2")) {
            fullRoom.child("fourStatus").child("player22").setValue(atkNum);
        }

    }

    /**
     * 回合結束觸發
     * 1.增加雙發完家的MP以及HP
     * 2.判斷攻擊技能是否可以使用
     * 3.判斷遊戲是否結束
     * 4.把攻擊範圍初始化
     */
    public void init() {
        openBtn();
        openBtnAtk();
//        gameEnd();
        atkRules.initAtkRange();
    }


//    public void initGame1() {
//        initGame.setVisibility(View.INVISIBLE);
//        txt_self_hp.setText("10");
//        txt_com_hp.setText("10");
//        txt_self_mp.setText("10");
//        txt_com_mp.setText("10");
//        locationXSelf = 0;
//        locationYSelf = 1;
//        locationXCom = 4;
//        locationYCom = 1;
//        moveVisible();
//        fireVisibile();//重開新局 消滅地圖火焰
//
//        openBtn();
//        openBtnAtk();
//        imagePlayer.layout(locationX[locationXSelf].getLeft() + 30, locationY[locationYSelf].getTop() - 200, locationX[locationXSelf].getLeft() + 100 + 30, locationY[locationYSelf].getBottom());
//        imageCom.layout(locationX[locationXCom].getLeft() + 30, locationY[locationYCom].getTop() - 200, locationX[locationXCom].getLeft() + 100 + 30, locationY[locationYCom].getBottom());
//
//    }

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
        fullRoom.child(variable.getPlayer()).child("Next").child("locationXSelf").setValue(locationXSelf);
        fullRoom.child(variable.getPlayer()).child("Next").child("locationYSelf").setValue(locationYSelf);
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
        fullRoom.child(player).child("Next").child("atkR").setValue(variable.getFinalAtlR().get(atkNum));
        fullRoom.child(player).child("Next").child("atkHP").setValue(Integer.parseInt(String.valueOf(variable.getFinalHP().get(atkNum))));
        fullRoom.child(player).child("Next").child("atkMP").setValue(Integer.parseInt(String.valueOf(variable.getFinalMP().get(atkNum))));
        statusUp(variable.getPlayer(), atkNum);
    }


    public void atk1(View v) {
        /**
         * 1.把攻擊按鈕鎖起來
         * 2.顯示移動按鈕(?)
         * 3.
         * */
        lockBtnAtk();
        moveVisible();
        lockBtn();
        sendMessageMoveAtk(1);

    }

    public void atk2(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        sendMessageMoveAtk(2);
    }

    public void atk3(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        sendMessageMoveAtk(3);
    }

    public void atk4(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        sendMessageMoveAtk(4);
    }

    public void atk5(View v) {//變成特殊技能
        lockBtnAtk();
        moveVisible();
        lockBtn();
        fullRoom.child(player).child("unique").setValue(true); //特殊技能啟動

        ArrayList<Integer> x = new ArrayList<>();
        x.add(-1);

        /**法師末日*/
//        if (tools.roleChange(variable.getIndex()).equals("fs")) {
//            ArrayList<Integer> y = new ArrayList<>();
//            for (int i = 1; i <= 9; i++) {
//                y.add(i);
//            }
//            fullRoom.child(player).child("unique").setValue(false);
//            sendMessageMoveAtk(y, 2, 5);
//        } else {
//            sendMessageMoveAtk(x, 0, 0);
//        }

    }


    public void atk6(View v) {
        lockBtnAtk();
        moveVisible();
        lockBtn();
        sendMessageMoveAtk(6);
    }

    int turn = 4;

    /**
     * 執行動畫的部分
     */
    private void receiveMessage() {
        /**
         * 1.初始化地板
         * 2.
         * */

        fireVisible();//收到後 地板火焰先取消

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
                case 1:
                    handler.sendMessageDelayed(msg, 1050);
                    break;
                case 2:
                    handler.sendMessageDelayed(msg, 2000);
                    break;
                case 3:
                    handler.sendMessageDelayed(msg, 2050);
                    break;
            }

        }
    }


    private MyHandler handler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int start = msg.getData().getInt("NUM");

            Log.d("開始動作", String.valueOf(start));

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

                            /**提取自己應該要增加的血量跟魔量 - Next*/
                            long HPUP = (long) snapshot.child("Next").child("HPUP").getValue();
                            long MPUP = (long) snapshot.child("Next").child("MPUP").getValue();
                            /**提取自己的血量跟魔量*/
                            long HP = (long) snapshot.child("HP").getValue();
                            long MP = (long) snapshot.child("MP").getValue();
                            /**如果有需要回血或回魔力的話 就會在這邊增加*/
                            if ((int) HPUP > 0) {
                                fullRoom.child(variable.getPlayer()).child("HP").setValue((int) HPUP + (int) HP);
                            }
                            if ((int) MPUP > 0) {
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
                    break;
                case 2:
                    fullRoom.child(variable.getPlayer()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            /**假如player自己是b74 則隔檔本次傷害*/

                            int index = variable.getIndex();
//                            Boolean aBoolean = (Boolean) snapshot.child("unique").getValue();
                            Boolean aBoolean = variable.getUnique();


                            /** 如果你是騎士且你有開啟獨有技能 對手的Next - atkHP 直接歸零*/
                            if (tools.roleChange((int) variable.getIndex()).equals("b74") && variable.getUnique()) {
                                /**傷害歸0*/
                                fullRoom.child(variable.getOtherPlayer()).child("Next").child("atkHP").setValue(0);
                                /**消耗獨有技能*/
                                variable.setUnique(false);
                                fullRoom.child(variable.getPlayer()).child("unique").setValue(false);
                            }


//                            /** 如果你是J4且你有開啟獨有技能 對手的Next - atkHP 直接歸零*/
//                            if (tools.roleChange((int) variable.getIndex()).equals("j4") && variable.getUnique()) {
//                                /** 傷害 * 2 */
//                                long j4atk = (long) snapshot.child("Next").child("atkHP").getValue();
//                                fullRoom.child(player).child("Next").child("atkHP").setValue((int) j4atk * 2);
//                                /**消耗獨有技能*/
//                                variable.setUnique(false);
//                                fullRoom.child(variable.getPlayer()).child("unique").setValue(false);
//                            }

                            long HP = (long) snapshot.child("Next").child("atkHP").getValue();
                            long MP = (long) snapshot.child("Next").child("atkMP").getValue();
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();

                            atkRules.atkJudgmentSelf(atkR, (int) HP, (int) MP, variable.getPlayer());

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
                            long HP = (long) snapshot.child("Next").child("atkHP").getValue();
                            long MP = (long) snapshot.child("Next").child("atkMP").getValue();
                            ArrayList<Integer> atkR = (ArrayList<Integer>) snapshot.child("Next").child("atkR").getValue();
                            atkRules.atkJudgmentCom(atkR, (int) HP, (int) MP);
                            settlement();
                            moveVisible();
                            init();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Log.d("戰鬥", "結束");
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
//
//            }
//        });
//    }

    public void settlement() {
        MPUse();
    }


}